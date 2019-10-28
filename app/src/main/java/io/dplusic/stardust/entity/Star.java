package io.dplusic.stardust.entity;

import com.google.common.base.Optional;

import javax.microedition.khronos.opengles.GL10;

import io.dplusic.cbes.EntityManager;
import io.dplusic.stardust.Selector;
import io.dplusic.stardust.component.Propagatable;
import io.dplusic.stardust.component.Renderable;
import io.dplusic.stardust.component.Selectable;
import io.dplusic.stardust.mesh.Cube;
import io.dplusic.stardust.mesh.Mesh;

public class Star extends StardustEntity {

	public static final float ALTITUDE = 80;

	public Star(EntityManager entityManager, Optional<Player> ownerOptional, float longitude, float latitude) {
		super(entityManager, ownerOptional, new Coordinate(longitude, latitude, ALTITUDE));

		Mesh mesh = new Cube(10, 20, 20);
		setMesh(mesh);

		Mesh selectingMesh = new Cube(11, 21, 21);
		selectingMesh.setColor(0, 1, 1, 1);
		selectingMesh.setDrawMode(GL10.GL_LINE_LOOP);

		addComponent(new Renderable());
		addComponent(new Selectable(selectingMesh));
		addComponent(new Propagatable());
	}

}
