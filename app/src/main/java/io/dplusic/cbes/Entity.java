package io.dplusic.cbes;

import java.util.ArrayList;
import java.util.List;

public class Entity {

	private List<Component> components;

	public Entity() {
		components = new ArrayList<Component>();

		EntityManager.getInstance().addEntity(this);
	}

	public void addComponent(Component component) {
		component.setEntity(this);
		components.add(component);
	}

	public void removeComponent(Component component) {
		component.remove();
		components.remove(component);
	}

	public void remove() {
		for (Component component : components) {
			component.remove();
		}
		components.clear();

		EntityManager.getInstance().removeEntity(this);
	}

	public void update() {
		for (Component component : components) {
			component.update();
		}
	}

}
