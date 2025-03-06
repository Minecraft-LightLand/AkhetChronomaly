package dev.xkmc.akhet_chronomaly.engine.core.type;

import dev.xkmc.akhet_chronomaly.engine.core.tooltip.TooltipConsumer;
import dev.xkmc.akhet_chronomaly.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.engine.core.codec.IAutoCodec;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementCollector;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescElementsProvider;
import dev.xkmc.akhet_chronomaly.engine.core.tooltip.DescProvider;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IEffectEntry<T extends Record & IEffectEntry<T>> extends IAutoCodec<T>, DescElementsProvider {

	@Override
	default AutoCodecTypeRegistry<?> registry() {
		return AutoReg.ENTRY;
	}

	Optional<IUserPredicate<?>> predicate();

	List<? extends IEffect<?>> listeners();

	@Override
	default void getDescElements(DescElementCollector list) {
		if (predicate().isPresent()) {
			predicate().get().getDescElements(list);
		}
		for (var e : listeners()) {
			e.getDescElements(list);
		}
	}

	default void getFullDesc(TooltipConsumer list) {
		List<MutableComponent> temp = new ArrayList<>();
		for (var e : listeners()) {
			if (e instanceof DescProvider pvd) {
				temp.add(pvd.getDesc());
			}
		}
		if (temp.isEmpty()) return;
		if (predicate().isPresent() && predicate().get() instanceof DescProvider pvd) {
			var pred = pvd.getDesc();
			if (temp.size() == 1) {
				list.add(ACLang.COMBINE.get(pred, temp.getFirst()));
			} else {
				list.add(ACLang.CONDITION.get(pred));
				list.push();
				for (var e : temp) {
					list.add(e);
				}
				list.pop();
			}
		} else {
			for (var e : temp) {
				list.add(e);
			}
		}
	}

}
