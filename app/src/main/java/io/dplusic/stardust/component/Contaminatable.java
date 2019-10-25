package io.dplusic.stardust.component;

import io.dplusic.stardust.entity.Player;
import io.dplusic.stardust.entity.StardustEntity;

public class Contaminatable extends StardustComponent {

	private StardustEntity contaminated;

	private Runnable onFinishContaminating;

	public Contaminatable(StardustEntity contaminated) {
		this.contaminated = contaminated;
	}

	@Override
	public void update() {

		StardustEntity contaminating = getEntity();
		Player ownerOfContaminating = contaminating.getOwner();

		if (contaminating.getInfectivity() >= 1) {
			contaminating.affectInfectivity(ownerOfContaminating, -1);
			contaminated.affectInfectivity(ownerOfContaminating, 1);
		} else {
			if (onFinishContaminating != null) {
				onFinishContaminating.run();
			}
		}
	}

	public void setOnFinishContaminating(Runnable onFinishContaminating) {
		this.onFinishContaminating = onFinishContaminating;
	}

}
