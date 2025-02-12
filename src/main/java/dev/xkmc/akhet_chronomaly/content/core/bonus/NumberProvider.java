package dev.xkmc.akhet_chronomaly.content.core.bonus;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;

public interface NumberProvider {

	double get(SetEffectContext ctx);

	default int getAsInt(SetEffectContext ctx) {
		return (int) get(ctx);
	}

}
