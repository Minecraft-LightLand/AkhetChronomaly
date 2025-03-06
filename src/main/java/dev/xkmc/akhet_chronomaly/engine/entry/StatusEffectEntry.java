package dev.xkmc.akhet_chronomaly.engine.entry;

import dev.xkmc.akhet_chronomaly.engine.core.type.IEffectEntry;
import dev.xkmc.akhet_chronomaly.engine.core.type.IStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.core.type.IUserPredicate;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.Optional;

public record StatusEffectEntry(
		Optional<IUserPredicate<?>> predicate, List<IStatusEffect<?>> listeners
) implements IEffectEntry<StatusEffectEntry> {

	public static StatusEffectEntry of(IUserPredicate<?> pred, IStatusEffect<?>... effs) {
		return new StatusEffectEntry(Optional.of(pred), List.of(effs));
	}

	public static StatusEffectEntry of(IStatusEffect<?>... effs) {
		return new StatusEffectEntry(Optional.empty(), List.of(effs));
	}

}
