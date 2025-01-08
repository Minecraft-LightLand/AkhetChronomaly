package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SetEntry<T extends ArtifactSet> extends RegistryEntry<ArtifactSet, T> {

	public final ItemEntry<BaseArtifact>[] items;

	public SetEntry(ACRegistrate owner, DeferredHolder<ArtifactSet, T> delegate,
					ItemEntry<BaseArtifact>[] items) {
		super(owner, delegate);
		this.items = items;
		owner.SET_LIST.add(this);
		owner.SET_MAP.put(getId(), this);
	}

	public int size() {
		return items.length;
	}

	public ItemStack getItem(int slot, int rank) {
		if (rank < 0) rank = 0;
		return items[slot].asStack();//TODO
	}

}
