package dev.xkmc.akhet_chronomaly.content.core.bonus;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;

public record BonusRef(String type) implements NumberProvider {

	@Override
	public double get(SetEffectContext ctx) {
		return ctx.data().getBonus(type).get();
	}

}
