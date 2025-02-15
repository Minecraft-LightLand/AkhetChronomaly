package dev.xkmc.akhet_chronomaly.content.engine.entry;

import dev.xkmc.akhet_chronomaly.content.engine.core.effect.IStatusEffect;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.IEffectEntry;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.IUserPredicate;

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
