package dev.xkmc.akhet_chronomaly.content.engine.core.effect;

import dev.xkmc.akhet_chronomaly.content.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.content.engine.core.trigger.TriggerType;
import dev.xkmc.akhet_chronomaly.content.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.content.engine.entry.TriggerEffectEntry;

public interface ITriggerEffect<T extends Record & ITriggerEffect<T, R>, R> extends IEffect<T> {

	void trigger(EffectContext ctx, R event);

	TriggerType<R> triggerType();

	@Override
	default AutoCodecTypeRegistry<?> registry() {
		return AutoReg.TRIGGER_EFFECT;
	}

	default TriggerEffectEntry asEntry() {
		return TriggerEffectEntry.of(this);
	}

}
