package dev.xkmc.akhet_chronomaly.engine.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.OnTargetHurtEffect;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;

public record HurtTargetFactor(
		float factor
) implements OnTargetHurtEffect<HurtTargetFactor> {

	@Override
	public void trigger(SetEffectContext ctx, DamageData.Offence event) {
		event.addHurtModifier(DamageModifier.multBase(factor, ctx.path()));
	}

}
