package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.CommonEffect;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.TriggerType;
import dev.xkmc.akhet_chronomaly.engine.util.EffectRecord;

public record GainEffect<R>(
		EffectRecord component, TriggerType<R> triggerType
) implements CommonEffect<GainEffect<R>, R> {

	@Override
	public void trigger(SetEffectContext ctx) {
		component.trigger(ctx.player(), ctx.player());
	}

}
