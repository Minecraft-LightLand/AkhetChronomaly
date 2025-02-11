package dev.xkmc.akhet_chronomaly.content.engine.core.type;

import dev.xkmc.akhet_chronomaly.content.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.content.engine.core.codec.IAutoCodec;

public interface IEffectEntry<T extends Record & IEffectEntry<T>> extends IAutoCodec<T> {

	@Override
	default AutoCodecTypeRegistry<?> registry() {
		return AutoReg.ENTRY;
	}

}
