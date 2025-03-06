package dev.xkmc.akhet_chronomaly.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.type.ITriggerEffect;

public interface CommonEffect<T extends Record & CommonEffect<T, R>, R> extends ITriggerEffect<T, R> {

	@Override
	default void trigger(SetEffectContext ctx, R event) {
		trigger(ctx);
	}

	void trigger(SetEffectContext ctx);

}
