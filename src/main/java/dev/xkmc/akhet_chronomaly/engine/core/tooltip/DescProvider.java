package dev.xkmc.akhet_chronomaly.engine.core.tooltip;

import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public interface DescProvider extends DescElementsProvider {

	MutableComponent getDesc();

	@Override
	default void getDescElements(DescElementCollector list) {
		list.add(getDesc());
	}

}
