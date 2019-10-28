package io.dplusic.stardust.entity;

import io.dplusic.stardust.component.Contaminatable;
import io.dplusic.stardust.component.Movable;
import io.dplusic.stardust.component.Renderable;
import io.dplusic.stardust.mesh.Cube;
import io.dplusic.stardust.mesh.Mesh;

public class Dust extends StardustEntity {

	public static final float ALTITUDE = 85;

	public Dust(Star start, final Star destination) {
		super(start.getOwnerOptional(), new Coordinate(start.coordinate.longitude,
				start.coordinate.latitude, ALTITUDE));

		Mesh mesh = new Cube(5, 5, 5);
		setMesh(mesh);

		Movable movable = new Movable(
				new Coordinate(start.coordinate.longitude,
						start.coordinate.latitude, ALTITUDE), new Coordinate(
						destination.coordinate.longitude,
						destination.coordinate.latitude, ALTITUDE));
		movable.setOnArrival(new Runnable() {
			@Override
			public void run() {
				Contaminatable contaminatable = new Contaminatable(destination);
				contaminatable.setOnFinishContaminating(new Runnable() {
					@Override
					public void run() {
						remove();
					}
				});
				addComponent(contaminatable);
			}
		});

		addComponent(new Renderable());
		addComponent(movable);
	}
}
