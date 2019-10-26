package io.dplusic.stardust;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.dplusic.android.gl.GLHelper;
import io.dplusic.android.gl.MatrixManager;
import io.dplusic.cbes.ComponentManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import io.dplusic.cbes.EntityManager;
import io.dplusic.stardust.component.Renderable;
import io.dplusic.stardust.component.Selectable;
import io.dplusic.stardust.mesh.Background;
import io.dplusic.stardust.mesh.Mesh;

import io.dplusic.android.graphics.spritetext.LabelMaker;
import io.dplusic.android.graphics.spritetext.NumericSprite;

public class StardustRenderer implements Renderer {

	private static final float[] lightAmbient = { 0.4f, 0.4f, 0.4f, 1 };
	private static final float[] lightDiffuse = { 0.6f, 0.6f, 0.6f, 1 };
	private static final float[] lightPosition = { -1, -1, 1, 0 };

	private FloatBuffer lightAmbientBuffer;
	private FloatBuffer lightDiffuseBuffer;
	private FloatBuffer lightPositionBuffer;

	private int surfaceWidth;
	private int surfaceHeight;

	private float cameraAltitude;
	private MatrixManager cameraMatrixManager;
	private float[] cameraPosition;
	private float[] cameraUp;

	private Mesh backgroundMesh;

	private LabelMaker mLabels;
	private Paint mLabelPaint;
	private int mLabelMsPF;
	private NumericSprite mNumericSprite;

	private Selector selector;

	public StardustRenderer(Resources resources) {

		lightAmbientBuffer = GLHelper.fromArrayToBuffer(lightAmbient);
		lightDiffuseBuffer = GLHelper.fromArrayToBuffer(lightDiffuse);
		lightPositionBuffer = GLHelper.fromArrayToBuffer(lightPosition);

		cameraAltitude = 400;
		cameraMatrixManager = new MatrixManager();
		cameraUp = new float[] { 0, 0, 1, 0 };
		cameraPosition = new float[4];
		setCamera();

		backgroundMesh = new Background(resources);

		mLabelPaint = new Paint();
		mLabelPaint.setTextSize(32);
		mLabelPaint.setAntiAlias(true);
		mLabelPaint.setARGB(255, 255, 255, 255);

		selector = Selector.getInstance();
	}

	public void rotateCameraX(float delta) {
		cameraMatrixManager.rotate(-delta, 0, 1, 0);
		setCamera();
	}

	public void rotateCameraY(float delta) {
		cameraMatrixManager.rotate(delta, 1, 0, 0);
		setCamera();
	}

	public void rotateCameraUp(float delta) {
		cameraMatrixManager.rotate(delta, 0, 0, 1);
		setCamera();
	}

	private void setCamera() {
		cameraMatrixManager.multiplyVector(cameraPosition, new float[] { 0, 0,
				cameraAltitude, 1 });
		cameraMatrixManager
				.multiplyVector(cameraUp, new float[] { 0, 1, 0, 1 });
	}

	private void drawBackground(GL10 gl) {

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrthof(0, 1, 0, 1, 0, 1);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glDepthMask(false);

		gl.glColor4f(1, 1, 1, 1);
		backgroundMesh.draw(gl);

		gl.glDepthMask(true);

		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	}

	private void drawMsPF(GL10 gl, float rightMargin) {
		mNumericSprite.setValue(100);
		float numWidth = mNumericSprite.width();
		float x = rightMargin - numWidth;
		mNumericSprite.draw(gl, x, 0, surfaceWidth, surfaceHeight);
	}

	@Override
	public void onDrawFrame(GL10 gl) {

		//
		// set projection

		gl.glMatrixMode(GL10.GL_PROJECTION);

		gl.glLoadIdentity();
		GLU.gluPerspective(gl, (float) Math.toDegrees(0.7854),
				(float) surfaceWidth / (float) surfaceHeight, 0.1f,
				cameraAltitude - 10);
		GLU.gluLookAt(gl, cameraPosition[0], cameraPosition[1],
				cameraPosition[2], 0, 0, 0, cameraUp[0], cameraUp[1],
				cameraUp[2]);

		//

		gl.glMatrixMode(GL10.GL_MODELVIEW);

		//
		// picking

		if (selector.isTouching()) {

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glLoadIdentity();

			gl.glDisable(GL10.GL_LIGHTING);
			gl.glDisable(GL10.GL_DITHER);
			gl.glDisable(GL10.GL_BLEND);

			for (Selectable selectable : EntityManager.getInstance().getComponentManager()
					.getComponents(Selectable.class)) {
				selectable.picking(gl);
			}

			ByteBuffer colorBuffer = ByteBuffer.allocateDirect(4);
			colorBuffer.order(ByteOrder.nativeOrder());
			gl.glReadPixels((int) selector.getX(), surfaceHeight
					- (int) selector.getY(), 1, 1, GL10.GL_RGBA,
					GL10.GL_UNSIGNED_BYTE, colorBuffer);

			Selectable.selectByColor(colorBuffer.get(), colorBuffer.get(),
					colorBuffer.get());
		}

		//
		// draw renderables

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_BLEND);

		drawBackground(gl);

		for (Renderable renderable : EntityManager.getInstance().getComponentManager()
				.getComponents(Renderable.class)) {
			renderable.draw(gl);
		}

		for (Selectable selectable : selector.getSelectedSelectables()) {
			selectable.draw(gl);
		}

		//
		// draw text labels

		// mLabels.beginDrawing(gl, surfaceWidth, surfaceHeight);
		// float msPFX = surfaceWidth - mLabels.getWidth(mLabelMsPF) - 1;
		// mLabels.draw(gl, msPFX, 0, mLabelMsPF);
		// mLabels.endDrawing(gl);

		// drawMsPF(gl, msPFX);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		surfaceWidth = width;
		surfaceHeight = height;

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, (float) Math.toDegrees(0.7854), (float) width
				/ (float) height, 0.1f, cameraAltitude);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		gl.glEnable(GL10.GL_LIGHTING);

		gl.glEnable(GL10.GL_LIGHT0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);

		gl.glEnable(GL10.GL_COLOR_MATERIAL);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		gl.glLoadIdentity();

		//
		// set label

		if (mLabels != null) {
			mLabels.shutdown(gl);
		} else {
			mLabels = new LabelMaker(true, 256, 64);
		}
		mLabels.initialize(gl);
		mLabels.beginAdding(gl);
		mLabelMsPF = mLabels.add(gl, "ms/f", mLabelPaint);
		mLabels.endAdding(gl);

		if (mNumericSprite != null) {
			mNumericSprite.shutdown(gl);
		} else {
			mNumericSprite = new NumericSprite();
		}
		mNumericSprite.initialize(gl, mLabelPaint);
	}

}
