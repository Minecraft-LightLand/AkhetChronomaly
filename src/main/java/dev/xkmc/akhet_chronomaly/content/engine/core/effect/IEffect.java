package dev.xkmc.akhet_chronomaly.content.engine.core.effect;

import dev.xkmc.akhet_chronomaly.content.engine.core.codec.IAutoCodec;

public interface IEffect<T extends Record & IEffect<T>> extends IAutoCodec<T> {
}
