package dev.xkmc.akhet_chronomaly.content.engine.core.type;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.content.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.content.engine.core.codec.IAutoCodec;

public interface IUserPredicate<T extends Record & IUserPredicate<T>> extends IAutoCodec<T> {

	@Override
	default AutoCodecTypeRegistry<?> registry() {
		return AutoReg.PREDICATE;
	}

	boolean isValid(SetEffectContext ctx);

}
