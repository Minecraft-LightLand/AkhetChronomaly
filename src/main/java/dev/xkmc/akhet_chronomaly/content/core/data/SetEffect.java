package dev.xkmc.akhet_chronomaly.content.core.data;

import dev.xkmc.akhet_chronomaly.engine.core.trigger.TriggerType;
import dev.xkmc.akhet_chronomaly.engine.core.type.IEffectEntry;
import dev.xkmc.akhet_chronomaly.engine.entry.StatusEffectEntry;
import dev.xkmc.akhet_chronomaly.engine.entry.TriggerEffectEntry;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SetEffect(int count, List<IEffectEntry<?>> effects) {

	public <T> void trigger(SetEffectContext ctx, TriggerType<T> type, T event) {
		for (int i = 0; i < effects().size(); i++) {
			if (effects().get(i) instanceof TriggerEffectEntry<?> entry) {
				var c0 = ctx.sub("/" + i);
				if (entry.predicate().isEmpty() || entry.predicate().get().isValid(c0)) {
					for (int j = 0; j < entry.listeners().size(); j++) {
						var e = entry.listeners().get(j);
						if (e.triggerType() == type) {
							var c1 = c0.sub("/" + j);
							e.trigger(c1, Wrappers.cast(event));
						}
					}
				}
			}
		}
	}

	public int tick(SetEffectContext ctx, int flag, boolean available) {
		int ans = 0;
		for (int i = 0; i < effects.size(); i++) {
			if (effects().get(i) instanceof StatusEffectEntry entry) {
				var c0 = ctx.sub("/" + i);
				boolean old = (flag & (1 << i)) != 0;
				boolean valid = available && (entry.predicate().isEmpty() || entry.predicate().get().isValid(c0));
				ans |= 1 << i;
				for (int j = 0; j < entry.listeners().size(); j++) {
					var e = entry.listeners().get(j);
					var c1 = c0.sub("/" + j);
					if (!old && valid) {
						e.start(c1);
					}
					if (valid) {
						e.tick(c1);
					}
					if (old && !valid) {
						e.stop(c1);
					}
				}
			}
		}
		return ans;
	}

	public MutableComponent getName(ResourceLocation id) {
		return Component.translatable(Util.makeDescriptionId("set_effect", id));
	}

	public List<MutableComponent> getDetailedDescription() {
		return List.of();//TODO
	}

}
