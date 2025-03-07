package dev.xkmc.akhet_chronomaly.engine.core.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class TooltipConsumer {

	private static final String[] SPACE = {"· ", "  · "};

	private final List<? super MutableComponent> list;
	private int count = 0;

	public TooltipConsumer(List<? super MutableComponent> list) {
		this.list = list;
	}

	public void push() {
		count += 1;
	}

	public void pop() {
		count -= 1;
	}

	public void add(MutableComponent eff) {
		if (count <= 0) list.add(eff);
		else list.add(Component.literal(SPACE[count - 1]).append(eff));
	}

}
