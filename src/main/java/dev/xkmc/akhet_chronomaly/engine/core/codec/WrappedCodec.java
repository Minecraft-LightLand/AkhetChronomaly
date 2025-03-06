package dev.xkmc.akhet_chronomaly.engine.core.codec;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2serial.util.Wrappers;

import java.util.concurrent.ConcurrentHashMap;

public record WrappedCodec<E extends IAutoCodec<?>, T extends Record & IAutoCodec<T>>(Class<T> cls) {

	private static final ConcurrentHashMap<Class<?>, WrappedCodec<?, ?>> MAP = new ConcurrentHashMap<>();

	public WrappedCodec {
		MAP.put(cls, this);
	}

	public static <E extends IAutoCodec<?>, T extends Record & IAutoCodec<T>> WrappedCodec<E, T> rev(Class<T> cls) {
		return Wrappers.cast(MAP.get(cls));
	}

	public MapCodec<T> mapCodec() {
		return CodecHelper.getAutoCodec(cls);
	}

}
