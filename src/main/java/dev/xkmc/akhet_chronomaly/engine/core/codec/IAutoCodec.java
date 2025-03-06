package dev.xkmc.akhet_chronomaly.engine.core.codec;

import dev.xkmc.l2serial.util.Wrappers;

public interface IAutoCodec<T extends Record & IAutoCodec<T>> {

	default T self() {
		return Wrappers.cast(this);
	}

	default <E extends IAutoCodec<?>> WrappedCodec<E, T> type() {
		return WrappedCodec.rev(Wrappers.cast(getClass()));
	}

	AutoCodecTypeRegistry<?> registry();

}
