package dev.xkmc.akhet_chronomaly.engine.predicate;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.type.IUserPredicate;

public record SelfHealthPredicate(
		double min, double max
) implements IUserPredicate<SelfHealthPredicate> {

	@Override
	public boolean isValid(SetEffectContext ctx) {
		var e = ctx.player();
		var php = e.getHealth() / e.getMaxHealth();
		return (min <= 0 || php >= min) && (max >= 1 || php <= max);
	}


}
