package io.dplusic.stardust.entity;

public class Coordinate {

	public float longitude;
	public float latitude;
	public float altitude;

	public Coordinate(float longitude, float latitude, float altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}

	public void set(Coordinate coordinate) {
		longitude = coordinate.longitude;
		latitude = coordinate.latitude;
		altitude = coordinate.altitude;
	}

	public static Coordinate calculateDifference(Coordinate aCoordnate,
			Coordinate bCoordnate) {
		return new Coordinate(bCoordnate.longitude - aCoordnate.longitude,
				bCoordnate.latitude - aCoordnate.latitude, bCoordnate.altitude
						- aCoordnate.altitude);
	}

	public static float calculateDistance(Coordinate aCoordinate, Coordinate bCoordinate) {
		Coordinate difference = Coordinate.calculateDifference(aCoordinate,
				bCoordinate);

		if (difference.longitude > 180) {
			difference.longitude -= 360;
		} else if (difference.longitude < -180) {
			difference.longitude += 360;
		}

		float distance = (float) Math.sqrt(difference.longitude
				* difference.longitude + difference.latitude
				* difference.latitude + difference.altitude
				* difference.altitude);

		return distance;
	}
}