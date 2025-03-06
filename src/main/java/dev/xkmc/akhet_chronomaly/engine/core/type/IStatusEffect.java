package dev.xkmc.akhet_chronomaly.engine.core.type;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.codec.AutoCodecTypeRegistry;

public interface IStatusEffect<T extends Record & IStatusEffect<T>> extends IEffect<T> {

	default void start(SetEffectContext ctx) {

	}

	default void tick(SetEffectContext ctx) {

	}

	default void stop(SetEffectContext ctx) {

	}

	@Override
	default AutoCodecTypeRegistry<?> registry() {
		return AutoReg.STATUS_EFFECT;
	}

}
