package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.CritTestEffect;
import dev.xkmc.akhet_chronomaly.engine.util.CritTest;

public record SetCritEffect() implements CritTestEffect<SetCritEffect> {

	@Override
	public void trigger(SetEffectContext ctx, CritTest event) {
		event.setCrit(true);
	}

}
