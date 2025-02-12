package dev.xkmc.akhet_chronomaly.content.engine.core.effect;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.AutoReg;

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
