package io.dplusic.stardust.component;

import android.util.FloatMath;
import io.dplusic.stardust.entity.Coordinate;

public class Movable extends StardustComponent {

	private float distancePerClock = 1;

	private Coordinate current;
	private Coordinate destination;

	private Coordinate differencePerClock;
	private float remainedClock;

	private Runnable onArrival;

	public Movable(Coordinate start, Coordinate destination) {

		if (start.latitude == 90 || start.latitude == -90) {
			start.longitude = destination.longitude;
		}
		if (destination.latitude == 90 || destination.latitude == -90) {
			destination.longitude = start.longitude;
		}

		Coordinate difference = Coordinate.calculateDifference(start,
				destination);

		if (difference.longitude > 180) {
			start.longitude += 360;
			difference.longitude -= 360;
		} else if (difference.longitude < -180) {
			start.longitude -= 360;
			difference.longitude += 360;
		}

		this.current = start;
		this.destination = destination;

		float distance = (float) Math.sqrt(difference.longitude
				* difference.longitude + difference.latitude
				* difference.latitude + difference.altitude
				* difference.altitude);

		float totalClock = (float) Math.ceil(distance / distancePerClock);

		differencePerClock = new Coordinate(difference.longitude / totalClock,
				difference.latitude / totalClock, difference.altitude
						/ totalClock);

		remainedClock = totalClock;

	}

	public void setOnArrival(Runnable onArrival) {
		this.onArrival = onArrival;
	}

	@Override
	public void update() {

		if (remainedClock != 0) {

			if (--remainedClock == 0) {
				current = destination;
			} else {
				current.longitude += differencePerClock.longitude;
				current.latitude += differencePerClock.latitude;
				current.altitude += differencePerClock.altitude;
			}

		} else {
			if (onArrival != null) {
				onArrival.run();
			}
			getEntity().removeComponent(this);
		}

		getEntity().coordinate.set(current);

		super.update();
	}
}
