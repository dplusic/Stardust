package io.dplusic.stardust;

import com.google.common.base.Optional;

import java.util.concurrent.ConcurrentLinkedQueue;

import io.dplusic.cbes.EntityManager;
import io.dplusic.stardust.component.Selectable;
import io.dplusic.stardust.entity.Dust;
import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.Star;

public class Selector {

	private OnSelected onSelected;

	private ConcurrentLinkedQueue<Selectable> selectedSelectables;

	private boolean selecting;

	private float x;
	private float y;

	private Selectable lastTouchedSelectable;
	private Selectable lastSelectedSelectable;

	public Selector(OnSelected onSelected) {
		this.onSelected = onSelected;

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

					onSelected.onSelected(selectedSelectables, lastSelectedSelectable);

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
		Optional<Player> ownerOptional = seletatble.getEntity().getOwnerOptional();
		return ownerOptional.isPresent() && ownerOptional.get().getPlayerType() == Player.PLAYER_TYPE_USER;
	}

	public boolean isEmptySpaceTouched(){
		if(selectedSelectables.isEmpty() && lastSelectedSelectable==null)
			return true;
		return false;
	}

	public interface OnSelected {
		void onSelected(Iterable<Selectable> selectablesFrom, Selectable selectableTo);
	}
}
