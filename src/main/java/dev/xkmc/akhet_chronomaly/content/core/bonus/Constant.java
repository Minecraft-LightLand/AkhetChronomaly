package dev.xkmc.akhet_chronomaly.content.core.bonus;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;

public record Constant(double val) implements NumberProvider {

	@Override
	public double get(SetEffectContext ctx) {
		return val;
	}

}
