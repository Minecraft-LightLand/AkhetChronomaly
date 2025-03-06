package dev.xkmc.akhet_chronomaly.engine.predicate;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.type.IUserPredicate;

public record TargetHealthPredicate(
		double min, double max
) implements IUserPredicate<TargetHealthPredicate> {

	@Override
	public boolean isValid(SetEffectContext ctx) {
		if (ctx.getAttackTrigger().isEmpty()) return false;
		var e = ctx.getAttackTrigger().get().getTarget();
		var php = e.getHealth() / e.getMaxHealth();
		return (min <= 0 || php >= min) && (max >= 1 || php <= max);
	}


}
