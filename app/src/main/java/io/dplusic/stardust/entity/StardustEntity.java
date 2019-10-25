package io.dplusic.stardust.entity;

import io.dplusic.cbes.Entity;
import io.dplusic.stardust.mesh.Mesh;

public class StardustEntity extends Entity {

	public Coordinate coordinate;

	private Mesh mesh;

	private Player owner;

	private int infectivity;

	public StardustEntity(Player owner, Coordinate coordinate) {

		this.owner = owner;
		this.coordinate = coordinate;

		owner.addOwned(this);

		if (owner.getPlayerType() == Player.PLAYER_TYPE_NOBODY) {
			infectivity = 0;
		} else {
			infectivity = 100;
		}
	}

	public void affectInfectivity(Player player, int infectivity) {

		if (player == owner) {
			this.infectivity += infectivity;
		} else {
			switch (owner.getPlayerType()) {
			case Player.PLAYER_TYPE_NOBODY:
				changeOwner(player);
				this.infectivity += infectivity;
				break;
			default:
				this.infectivity -= infectivity;
				break;
			}
		}

		if (this.infectivity > 100) {
			this.infectivity = 100;
		} else if (this.infectivity <= 0) {
			this.infectivity = 0;
			changeOwner(Player.getInstance(Player.PLAYER_TYPE_NOBODY));
		}
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Player getOwner() {
		return owner;
	}

	public int getInfectivity() {
		return infectivity;
	}

	public void setInfectivity(int infectivity) {
		this.infectivity = infectivity;
	}

	private void changeOwner(Player owner) {

		if (this.owner != null) {
			this.owner.removeOwned(this);
		}

		this.owner = owner;

		if (this.owner.getPlayerType() != Player.PLAYER_TYPE_NOBODY) {
			this.owner.addOwned(this);
		}
	}

}
