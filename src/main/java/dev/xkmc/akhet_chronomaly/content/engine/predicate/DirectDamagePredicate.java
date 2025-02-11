package dev.xkmc.akhet_chronomaly.content.engine.predicate;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.EffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.IUserPredicate;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;

public record DirectDamagePredicate(
		boolean checkStrength
) implements IUserPredicate<DirectDamagePredicate> {

	@Override
	public boolean isValid(EffectContext ctx) {
		var source = ctx.getDamageSource();
		var attack = ctx.getAttackTrigger();
		return source.isPresent() && source.get().is(L2DamageTypes.DIRECT) &&
				(!checkStrength || attack.isEmpty() || attack.get().getStrength() > 0.95f);
	}

}
