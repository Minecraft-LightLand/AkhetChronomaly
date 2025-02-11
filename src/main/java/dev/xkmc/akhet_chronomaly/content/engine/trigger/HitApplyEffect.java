package dev.xkmc.akhet_chronomaly.content.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.EffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.trigger.AfterPlayerHurtTargetEffect;
import dev.xkmc.akhet_chronomaly.content.engine.util.EffectRecord;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;

public record HitApplyEffect(
		EffectRecord component
) implements AfterPlayerHurtTargetEffect<HitApplyEffect> {

	@Override
	public void trigger(EffectContext ctx, DamageData.DefenceMax event) {
		component.trigger(ctx.player(), event.getTarget());
	}

}
