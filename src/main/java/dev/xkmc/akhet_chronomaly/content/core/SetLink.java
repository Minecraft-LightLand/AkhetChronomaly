package dev.xkmc.akhet_chronomaly.content.core;

import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SetLink {

	private final List<BaseArtifact> list = new ArrayList<>();
	private final Map<ArtifactSlot, BaseArtifact> map = new LinkedHashMap<>();

	public void add(BaseArtifact item) {
		list.add(item);
		map.put(item.slot.get(), item);
	}

	public ItemStack getItem(int slot, int rank) {
		return list.get(slot).getDefaultInstance();
	}

	public int size() {
		return list.size();
	}

	public boolean isFullSet() {
		return map.size() == list.size() && map.size() == ACTypeRegistry.SLOT.reg().size();
	}

	public List<BaseArtifact> getItems() {
		return list;
	}

}
