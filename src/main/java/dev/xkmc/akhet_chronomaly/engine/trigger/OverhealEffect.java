package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.TriggerType;
import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.engine.core.type.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.engine.util.OverhealTest;

public record OverhealEffect() implements ITriggerEffect<OverhealEffect, OverhealTest> {

	@Override
	public void trigger(SetEffectContext ctx, OverhealTest event) {
		var e = ctx.player();
		var cur = e.getAbsorptionAmount();
		e.setAbsorptionAmount(cur + event.consume());
	}

	@Override
	public TriggerType<OverhealTest> triggerType() {
		return AutoReg.OVERHEAL.get();
	}

}
