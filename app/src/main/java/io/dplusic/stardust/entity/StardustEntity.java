package io.dplusic.stardust.entity;

import com.google.common.base.Optional;

import io.dplusic.cbes.Entity;
import io.dplusic.cbes.EntityManager;
import io.dplusic.stardust.mesh.Mesh;

public class StardustEntity extends Entity {

	public Coordinate coordinate;

	private Mesh mesh;

	private Optional<Player> ownerOptional;
	private Player nobody;

	private int infectivity;

	public StardustEntity(EntityManager entityManager, Optional<Player> ownerOptional, Coordinate coordinate) {
		super(entityManager);

	    this.ownerOptional = ownerOptional;
		this.coordinate = coordinate;

		if (this.ownerOptional.isPresent()) {
            this.ownerOptional.get().addOwned(this);
			infectivity = 100;
		} else {
			infectivity = 0;
		}
	}

	public void affectInfectivity(Player player, int infectivity) {

		if (ownerOptional.isPresent()) {
			Player owner = ownerOptional.get();
			if (owner == player) {
				this.infectivity += infectivity;
			} else {
				this.infectivity -= infectivity;
			}
 		} else {
			changeOwner(Optional.of(player));
			this.infectivity += infectivity;
		}

		if (this.infectivity > 100) {
			this.infectivity = 100;
		} else if (this.infectivity <= 0) {
			this.infectivity = 0;
			changeOwner(Optional.<Player>absent());
		}
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Optional<Player> getOwnerOptional() {
		return ownerOptional;
	}

	public int getInfectivity() {
		return infectivity;
	}

	public void setInfectivity(int infectivity) {
		this.infectivity = infectivity;
	}

	private void changeOwner(Optional<Player> ownerOptional) {

		if (this.ownerOptional.isPresent()) {
			this.ownerOptional.get().removeOwned(this);
		}

		this.ownerOptional = ownerOptional;

		if (this.ownerOptional.isPresent()) {
			this.ownerOptional.get().addOwned(this);
		}
	}

}
