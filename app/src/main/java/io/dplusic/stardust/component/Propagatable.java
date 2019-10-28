package io.dplusic.stardust.component;

import com.google.common.base.Optional;

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
			Optional<Player> ownerOptional = stardust.getOwnerOptional();

			if (ownerOptional.isPresent()) {
				stardust.affectInfectivity(ownerOptional.get(), 1);
			}
			
		}

		super.update();
	}

}
