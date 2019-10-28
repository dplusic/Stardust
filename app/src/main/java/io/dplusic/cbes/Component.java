package io.dplusic.cbes;

public abstract class Component {

	private Entity entity;

	public Entity getEntity() {
		return entity;
	}

	void setEntity(Entity entity) {

		if (this.entity != null) {
			throw new InvalidComponentException(
					"Only one entity for a component is acceptable");
		}
		this.entity = entity;
	}

	public void update() {
	}

}
