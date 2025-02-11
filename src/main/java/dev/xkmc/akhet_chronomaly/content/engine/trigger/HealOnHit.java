package dev.xkmc.akhet_chronomaly.content.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.EffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.trigger.AfterPlayerHurtTargetEffect;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;

public record HealOnHit(
		float healFactor
) implements AfterPlayerHurtTargetEffect<HealOnHit> {

	@Override
	public void trigger(EffectContext ctx, DamageData.DefenceMax event) {
		ctx.player().heal(event.getDamageFinal() * healFactor);
	}

}
