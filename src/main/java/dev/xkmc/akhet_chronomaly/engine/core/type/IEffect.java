package dev.xkmc.akhet_chronomaly.engine.core.type;

import dev.xkmc.akhet_chronomaly.engine.core.codec.IAutoCodec;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementsProvider;

public interface IEffect<T extends Record & IEffect<T>> extends IAutoCodec<T>, DescElementsProvider {
}
