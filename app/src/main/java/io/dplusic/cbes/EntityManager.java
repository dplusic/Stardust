package io.dplusic.cbes;

import java.util.concurrent.ConcurrentLinkedQueue;

public class EntityManager {

	private ComponentManager componentManager = new ComponentManager();

	private ConcurrentLinkedQueue<Entity> entities = new ConcurrentLinkedQueue<Entity>();

	public ComponentManager getComponentManager() {
		return componentManager;
	}

	protected void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public void updateAll() {
		for (Entity entity : entities) {
			entity.update();
		}
	}

	//
	// Singleton Implementation

	private static EntityManager instance;

	public static EntityManager getInstance() {
		if (instance == null) {
			instance = new EntityManager();
		}
		return instance;
	}

	public static void reset() {
		instance = null;
	}

	private EntityManager() {

	}
}
