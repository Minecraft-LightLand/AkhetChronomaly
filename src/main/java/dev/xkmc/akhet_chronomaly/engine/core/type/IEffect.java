package dev.xkmc.akhet_chronomaly.engine.core.type;

import dev.xkmc.akhet_chronomaly.engine.core.codec.IAutoCodec;

public interface IEffect<T extends Record & IEffect<T>> extends IAutoCodec<T> {
}
