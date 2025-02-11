package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SetEntry<T extends ArtifactSet> extends RegistryEntry<ArtifactSet, T> {

	public final ItemEntry<BaseArtifact>[] items;

	public SetEntry(ACRegistrate owner, DeferredHolder<ArtifactSet, T> delegate,
					ItemEntry<BaseArtifact>[] items) {
		super(owner, delegate);
		this.items = items;
	}


}
