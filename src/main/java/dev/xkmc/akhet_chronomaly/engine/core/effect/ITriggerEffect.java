package dev.xkmc.akhet_chronomaly.engine.core.effect;

import dev.xkmc.akhet_chronomaly.content.core.data.SetEffectContext;
import dev.xkmc.akhet_chronomaly.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.engine.core.trigger.TriggerType;
import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.engine.entry.TriggerEffectEntry;

public interface ITriggerEffect<T extends Record & ITriggerEffect<T, R>, R> extends IEffect<T> {

	void trigger(SetEffectContext ctx, R event);

	TriggerType<R> triggerType();

	@Override
	default AutoCodecTypeRegistry<?> registry() {
		return AutoReg.TRIGGER_EFFECT;
	}

	default TriggerEffectEntry asEntry() {
		return TriggerEffectEntry.of(this);
	}

}
