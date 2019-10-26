package io.dplusic.cbes;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ComponentManager {

	private Map<Class<? extends Component>, Iterable<? extends Component>> componentListMap = new HashMap<Class<? extends Component>, Iterable<? extends Component>>();

	public <T extends Component> Iterable<T> getComponents(
			Class<T> componentClass) {

		@SuppressWarnings("unchecked")
		Iterable<T> componentList = (Iterable<T>) componentListMap
				.get(componentClass);

		if (componentList == null) {
			componentList = new ConcurrentLinkedQueue<T>();
			componentListMap.put(componentClass, componentList);
		}

		return componentList;
	}

	protected <T extends Component> void addComponent(T component) {

		@SuppressWarnings("unchecked")
		Queue<T> componentList = (Queue<T>) getComponents(component.getClass());

		componentList.add(component);
	}

	public <T extends Component> void removeComponent(T component) {

		@SuppressWarnings("unchecked")
		Queue<T> componentList = (Queue<T>) getComponents(component.getClass());

		componentList.remove(component);

	}

}