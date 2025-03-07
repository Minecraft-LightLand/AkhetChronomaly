package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.OnPlayerDamageEffect;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;

public record SelfDamageFactor(
		float factor
) implements OnPlayerDamageEffect<SelfDamageFactor> {

	@Override
	public void trigger(SetEffectContext ctx, DamageData.Defence event) {
		event.addDealtModifier(DamageModifier.multTotal(factor, ctx.path()));
	}

}
