package io.dplusic.stardust.component;

import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.StardustEntity;

public class Propagatable extends StardustComponent {

	private int delayClock = 10;
	private int consumedClock = delayClock;

	@Override
	public void update() {

		if (--consumedClock == 0) {
			consumedClock = delayClock;

			StardustEntity stardust = getEntity();
			Player owner = stardust.getOwner();

			if (owner.getPlayerType() != Player.PLAYER_TYPE_NOBODY) {
				stardust.affectInfectivity(owner, 1);
			}
			
		}

		super.update();
	}

}
