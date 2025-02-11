package dev.xkmc.akhet_chronomaly.content.engine.core.effect;

import dev.xkmc.akhet_chronomaly.content.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.AutoReg;

public interface IStatusEffect<T extends Record & IStatusEffect<T>> extends IEffect<T> {

	default void start(EffectContext ctx) {

	}

	default void tick(EffectContext ctx) {

	}

	default void stop(EffectContext ctx) {

	}

	@Override
	default AutoCodecTypeRegistry<?> registry() {
		return AutoReg.STATUS_EFFECT;
	}

}
