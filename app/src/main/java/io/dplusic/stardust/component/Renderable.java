package io.dplusic.stardust.component;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.Matrix;

import com.google.common.base.Optional;

import io.dplusic.stardust.entity.Coordinate;
import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.StardustEntity;
import io.dplusic.stardust.mesh.Mesh;

public class Renderable extends StardustComponent {

	private static final float[] COLOR_NOBODY = { 0.6f, 0.6f, 0.6f, 1 };

	public void draw(GL10 gl) {
		getEntity().getMesh().draw(gl);
	}

	@Override
	public void update() {

		StardustEntity entity = getEntity();
		Mesh mesh = entity.getMesh();

		updateMeshLocation(mesh, entity.coordinate);

		Optional<Player> ownerOptional = entity.getOwnerOptional();
		if (ownerOptional.isPresent()) {
			mesh.setColor(entity.getOwnerOptional().get().getPlayerIdColor());
		} else {
			mesh.setColor(COLOR_NOBODY);
		}
		mesh.setAlpha((entity.getInfectivity() / 200f + 0.5f));

		super.update();
	}

	protected static void updateMeshLocation(Mesh mesh, Coordinate coordinate) {

		float[] transformationMatrix = new float[16];
		Matrix.setIdentityM(transformationMatrix, 0);

		Matrix.rotateM(transformationMatrix, 0, coordinate.longitude, 0, 0, 1);
		Matrix.rotateM(transformationMatrix, 0, coordinate.latitude, 0, -1, 0);
		Matrix.translateM(transformationMatrix, 0, coordinate.altitude, 0, 0);

		mesh.setTransformationMatrix(transformationMatrix);
	}

}
