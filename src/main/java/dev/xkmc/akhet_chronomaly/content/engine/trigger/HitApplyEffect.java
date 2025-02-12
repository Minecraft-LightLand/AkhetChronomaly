package dev.xkmc.akhet_chronomaly.content.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.trigger.AfterPlayerHurtTargetEffect;
import dev.xkmc.akhet_chronomaly.content.engine.util.EffectRecord;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;

public record HitApplyEffect(
		EffectRecord component
) implements AfterPlayerHurtTargetEffect<HitApplyEffect> {

	@Override
	public void trigger(SetEffectContext ctx, DamageData.DefenceMax event) {
		component.trigger(ctx.player(), event.getTarget());
	}

}
