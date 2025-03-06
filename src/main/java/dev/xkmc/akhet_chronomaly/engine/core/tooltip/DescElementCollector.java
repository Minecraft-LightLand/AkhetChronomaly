package dev.xkmc.akhet_chronomaly.engine.core.tooltip;

import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DescElementCollector {

	private final List<MutableComponent> list = new ArrayList<>();
	private final Map<Class<?>, Object> map = new LinkedHashMap<>();

	public void add(MutableComponent c) {
		list.add(c);
	}

	public void cache(Object obj) {
		map.put(obj.getClass(), obj);
	}

	@Nullable
	public <T> T fetch(Class<T> cls) {
		return Wrappers.cast(map.get(cls));
	}

	public MutableComponent parse(String id) {
		return Component.translatable(id, list.toArray()).withStyle(ChatFormatting.GRAY);
	}

}
