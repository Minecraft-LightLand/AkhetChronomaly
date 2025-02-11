package dev.xkmc.akhet_chronomaly.content.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.EffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.trigger.OnPlayerDamageEffect;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;

public record SelfDamageFactor(
		float factor
) implements OnPlayerDamageEffect<SelfDamageFactor> {

	@Override
	public void trigger(EffectContext ctx, DamageData.Defence event) {
		event.addDealtModifier(DamageModifier.multTotal(factor, ctx.path()));
	}

}
