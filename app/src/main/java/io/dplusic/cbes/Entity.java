package io.dplusic.cbes;

import java.util.ArrayList;
import java.util.List;

public class Entity {

	private EntityManager entityManager;

	private List<Component> components;

	public Entity(EntityManager entityManager) {
		this.entityManager = entityManager;

		components = new ArrayList<Component>();

		entityManager.addEntity(this);
	}

	public void addComponent(Component component) {
		component.setEntity(this);
		components.add(component);

		entityManager.getComponentManager().addComponent(component);
	}

	public void removeComponent(Component component) {
		entityManager.getComponentManager().removeComponent(component);

		components.remove(component);
	}

	public void remove() {
		for (Component component : components) {
			entityManager.getComponentManager().removeComponent(component);
		}
		components.clear();

		entityManager.removeEntity(this);
	}

	public void update() {
		for (Component component : components) {
			component.update();
		}
	}

}
