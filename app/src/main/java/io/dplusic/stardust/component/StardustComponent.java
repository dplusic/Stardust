package io.dplusic.stardust.component;

import io.dplusic.stardust.entity.StardustEntity;
import io.dplusic.cbes.Component;

public class StardustComponent extends Component {

	@Override
	public StardustEntity getEntity() {
		return (StardustEntity) super.getEntity();
	}

}
