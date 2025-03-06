package dev.xkmc.akhet_chronomaly.content.core.data;

import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementCollector;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.TooltipConsumer;
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
import java.util.Optional;

public record SetEffect(int count, List<IEffectEntry<?>> effects, Optional<String> desc) {

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

	public void getDetailedDescription(ResourceLocation id, TooltipConsumer list) {
		if (desc.isPresent()) {
			var elements = new DescElementCollector();
			for (var e : effects()) {
				e.getDescElements(elements);
			}
			list.add(elements.parse(Util.makeDescriptionId("set_effect", id) + ".desc"));
		} else {
			for (var e : effects()) {
				e.getFullDesc(list);
			}
		}
	}

}
