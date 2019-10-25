package io.dplusic.stardust.mesh;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import io.dplusic.android.gl.GLHelper;
import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.opengl.Matrix;

public class Mesh {

	private float[] uniqueColor;
	private float[] flatColor;

	private int numberOfIndices;

	private FloatBuffer verticesBuffer;
	private FloatBuffer normalVectorsBuffer;
	private FloatBuffer vertexColorBuffer;
	private FloatBuffer textureCoordinatesBuffer;
	private ShortBuffer indicesBuffer;

	protected FloatBuffer materialAmbientBuffer;
	protected FloatBuffer materialDiffuseBuffer;
	protected FloatBuffer materialSpecularBuffer;

	protected int textureId;
	protected Bitmap bitmap;

	protected int drawMode;

	protected float[] transformationMatrix;

	public Mesh() {

		uniqueColor = new float[] { 0, 0, 0, 0 };
		flatColor = new float[] { 0.594f, 0.535f, 0.896f, 1 };
		textureId = -1;

		drawMode = GL10.GL_TRIANGLES;

		transformationMatrix = new float[16];
		Matrix.setIdentityM(transformationMatrix, 0);
	}

	public void draw(GL10 gl) {

		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);

		if (normalVectorsBuffer != null) {
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalVectorsBuffer);
		}

		if (materialAmbientBuffer != null) {
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
					materialAmbientBuffer);
		}
		if (materialDiffuseBuffer != null) {
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
					materialDiffuseBuffer);
		}
		if (materialSpecularBuffer != null) {
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
					materialSpecularBuffer);
		}

		if (textureCoordinatesBuffer != null) {
			if (textureId == -1 && bitmap != null) {
				loadGLTexture(gl);
			}
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glTexCoordPointer(3, GL10.GL_FLOAT, 0, textureCoordinatesBuffer);
		} else if (vertexColorBuffer != null) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, vertexColorBuffer);
		} else {
			gl.glColor4f(flatColor[0], flatColor[1], flatColor[2], flatColor[3]);
		}

		gl.glPushMatrix();

		gl.glMultMatrixf(transformationMatrix, 0);

		if (indicesBuffer != null) {
			gl.glDrawElements(drawMode, numberOfIndices,
					GL10.GL_UNSIGNED_SHORT, indicesBuffer);
		} else {
			gl.glDrawArrays(drawMode, 0, 3);
		}

		gl.glPopMatrix();

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

	public void picking(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
		gl.glColor4f(uniqueColor[0], uniqueColor[1], uniqueColor[2],
				uniqueColor[3]);
		gl.glPushMatrix();
		gl.glMultMatrixf(transformationMatrix, 0);
		gl.glDrawElements(GL10.GL_TRIANGLES, numberOfIndices,
				GL10.GL_UNSIGNED_SHORT, indicesBuffer);
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	protected void setVertices(float[] vertices) {
		verticesBuffer = GLHelper.fromArrayToBuffer(vertices);
	}

	protected void setVertices(FloatBuffer verticesBuffer) {
		this.verticesBuffer = verticesBuffer;
	}

	protected void setNormalVectors(float[] normalVectors) {
		normalVectorsBuffer = GLHelper.fromArrayToBuffer(normalVectors);
	}

	protected void setVertexColor(float[] vertexColor) {
		vertexColorBuffer = GLHelper.fromArrayToBuffer(vertexColor);
	}

	public void setColor(float red, float green, float blue, float alpha) {
		flatColor = new float[] { red, green, blue, alpha };
	}

	public void setColor(float[] color) {
		flatColor = color.clone();
	}
	
	public void setAlpha(float alpha) {
		if(alpha!=0.5 && alpha!=1) {
			System.out.println();
		}
		flatColor[3] = alpha;
	}

	public void setUniqueColor(float red, float green, float blue, float alpha) {
		uniqueColor = new float[] { red, green, blue, alpha };
	}

	public void setUniqueColor(float[] color) {
		uniqueColor = color;
	}

	protected void setMaterialAmbient(float[] materialAmbient) {
		materialAmbientBuffer = GLHelper.fromArrayToBuffer(materialAmbient);
	}

	protected void setMaterialDiffuse(float[] materialDiffuse) {
		materialDiffuseBuffer = GLHelper.fromArrayToBuffer(materialDiffuse);
	}

	protected void setMaterialSpecular(float[] materialSpecular) {
		materialSpecularBuffer = GLHelper.fromArrayToBuffer(materialSpecular);
	}

	protected void setTextureCoordinates(float[] textureCoordinates) {
		textureCoordinatesBuffer = GLHelper
				.fromArrayToBuffer(textureCoordinates);
	}

	protected void setIndices(short[] indices) {
		indicesBuffer = GLHelper.fromArrayToBuffer(indices);
		numberOfIndices = indices.length;
	}

	public void setTransformationMatrix(float[] transformationMatrix) {
		this.transformationMatrix = transformationMatrix.clone();
	}
	
	public void setDrawMode(int drawMode) {
		this.drawMode = drawMode;
	}

	public void loadBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	protected void loadGLTexture(GL10 gl) {

		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		textureId = textures[0];

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
	}

}