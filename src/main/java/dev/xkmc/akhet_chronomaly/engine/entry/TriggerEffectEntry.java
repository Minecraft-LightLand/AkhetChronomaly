package dev.xkmc.akhet_chronomaly.engine.entry;

import dev.xkmc.akhet_chronomaly.engine.core.effect.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.engine.core.type.IEffectEntry;
import dev.xkmc.akhet_chronomaly.engine.core.type.IUserPredicate;

import java.util.List;
import java.util.Optional;

public record TriggerEffectEntry<R>(
		Optional<IUserPredicate<?>> predicate,
		List<ITriggerEffect<?, R>> listeners
) implements IEffectEntry<TriggerEffectEntry<R>> {

	public static <R> TriggerEffectEntry<R> of(IUserPredicate<?> pred, ITriggerEffect<?, R>... effs) {
		return new TriggerEffectEntry<>(Optional.of(pred), List.of(effs));
	}

	public static <R> TriggerEffectEntry<R> of(ITriggerEffect<?, R>... effs) {
		return new TriggerEffectEntry<>(Optional.empty(), List.of(effs));
	}

}
