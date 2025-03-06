package dev.xkmc.akhet_chronomaly.engine.core.type;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.engine.core.codec.IAutoCodec;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementsProvider;

public interface IUserPredicate<T extends Record & IUserPredicate<T>> extends IAutoCodec<T>, DescElementsProvider {

	@Override
	default AutoCodecTypeRegistry<?> registry() {
		return AutoReg.PREDICATE;
	}

	boolean isValid(SetEffectContext ctx);

}
