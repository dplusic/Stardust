package io.dplusic.stardust.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.dplusic.cbes.Entity;
import io.dplusic.cbes.EntityManager;

public class Player extends Entity {

	public static final int PLAYER_TYPE_USER = 0;
	public static final int PLAYER_TYPE_COM = 1;

	public static final float[] PLAYER_ID_COLOR_USER = { 0, 0.5f, 1, 1 };

	private Map<Class<? extends StardustEntity>, List<? extends StardustEntity>> ownedListMap;

	private Random random = new Random();

	private int playerType;

	private float[] playerIdColor;

	public Player(EntityManager entityManager, int playerType) {
		super(entityManager);

		ownedListMap = new HashMap<Class<? extends StardustEntity>, List<? extends StardustEntity>>();

		this.playerType = playerType;

		switch (playerType) {
		case PLAYER_TYPE_USER:
			playerIdColor = PLAYER_ID_COLOR_USER;
			break;
		case PLAYER_TYPE_COM:
			playerIdColor = new float[] { random.nextFloat(),
					random.nextFloat(), 0, 1 };
			break;
		}

	}

	public int getPlayerType() {
		return playerType;
	}

	public float[] getPlayerIdColor() {
		return playerIdColor.clone();
	}

	public <T extends StardustEntity> List<T> getOwnedList(Class<T> classOfOwned) {

		@SuppressWarnings("unchecked")
		List<T> ownedList = (List<T>) ownedListMap.get(classOfOwned);

		if (ownedList == null) {
			ownedList = new ArrayList<T>();
			ownedListMap.put(classOfOwned, ownedList);
		}

		return ownedList;
	}

	<T extends StardustEntity> void addOwned(T owned) {

		@SuppressWarnings("unchecked")
		List<T> ownedList = (List<T>) getOwnedList(owned.getClass());

		ownedList.add(owned);
	}

	<T extends StardustEntity> void removeOwned(T owned) {

		@SuppressWarnings("unchecked")
		List<T> ownedList = (List<T>) getOwnedList(owned.getClass());

		ownedList.remove(owned);
	}
}
