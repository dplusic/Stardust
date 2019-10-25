package io.dplusic.stardust;

import java.util.concurrent.ConcurrentLinkedQueue;

import io.dplusic.stardust.component.Selectable;
import io.dplusic.stardust.entity.Dust;
import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public class Selector {

	private ConcurrentLinkedQueue<Selectable> selectedSelectables;

	private boolean selecting;

	private float x;
	private float y;

	private Selectable lastTouchedSelectable;
	private Selectable lastSelectedSelectable;

	private Selector() {
		selectedSelectables = new ConcurrentLinkedQueue<Selectable>();
	}

	public Iterable<Selectable> getSelectedSelectables() {
		return selectedSelectables;
	}

	public boolean isTouching() {
		return selecting;
	}

	public void startSelecting() {
		selecting = true;
	}

	public void endSelelecting() {

		selecting = false;

		if (lastTouchedSelectable == null) {

			if (lastSelectedSelectable != null) {
				return;
			}

			selectedSelectables.clear();
			lastSelectedSelectable = null;

		} else {

			if (lastSelectedSelectable != null) {
				if (!isOfUserPlayer(lastSelectedSelectable)) {

					selectedSelectables.remove(lastSelectedSelectable);

					for (Selectable selectable : selectedSelectables) {

						// TODO All Selectables are components of Star

						Star start = (Star) selectable.getEntity();
						int halfInfectivity = start.getInfectivity() / 2;

						Dust dust = new Dust(start,
								(Star) lastSelectedSelectable.getEntity());

						start.setInfectivity(halfInfectivity);
						dust.setInfectivity(halfInfectivity);
					}

					selectedSelectables.clear();
					lastSelectedSelectable = null;
				}
			}

		}
	}

	public void setSelectingXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void touchOver(Selectable touchedSelectable) {

		if (touchedSelectable != null) {

			if (!isOfUserPlayer(touchedSelectable)) {

				if (selectedSelectables.isEmpty()) {
					return;
				} else if (lastSelectedSelectable != null) {
					if (!isOfUserPlayer(lastSelectedSelectable)) {
						selectedSelectables.remove(lastSelectedSelectable);
						lastSelectedSelectable = null;
					}
				}

				lastSelectedSelectable = touchedSelectable;
			}

			selectedSelectables.remove(touchedSelectable);
			selectedSelectables.add(touchedSelectable);

		} else {

			if (!selectedSelectables.isEmpty()
					&& lastSelectedSelectable != null) {
				if (!isOfUserPlayer(lastSelectedSelectable)) {
					selectedSelectables.remove(lastSelectedSelectable);
					lastSelectedSelectable = null;
				}
			}

		}

		lastTouchedSelectable = touchedSelectable;
	}

	private static boolean isOfUserPlayer(Selectable seletatble) {
		return seletatble.getEntity().getOwner().getPlayerType() == Player.PLAYER_TYPE_USER;
	}

	// Singleton Implementation

	private static Selector instance;

	public static synchronized Selector getInstance() {
		if (instance == null) {
			instance = new Selector();
		}
		return instance;
	}

	public boolean isEmptySpaceTouched(){
		if(selectedSelectables.isEmpty() && lastSelectedSelectable==null)
			return true;
		return false;
	}
}
