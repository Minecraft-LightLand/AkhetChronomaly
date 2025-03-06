package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementCollector;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.CommonEffect;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.TriggerType;
import dev.xkmc.akhet_chronomaly.engine.util.BonusRecord;

public record GainBonus<R>(
		BonusRecord component, TriggerType<R> triggerType
) implements CommonEffect<GainBonus<R>, R> {

	@Override
	public void trigger(SetEffectContext ctx) {
		component.trigger(ctx);
	}

	@Override
	public void getDescElements(DescElementCollector list) {
		list.cache(component);
	}

}
