package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SetEntry<T extends ArtifactSet> extends RegistryEntry<ArtifactSet, T> {

	private final ACRegistrate owner;

	public SetEntry(ACRegistrate owner, DeferredHolder<ArtifactSet, T> delegate) {
		super(owner, delegate);
		this.owner = owner;
	}


	public SetRegHelper subSet(String id) {
		return new SetRegHelper(owner, id);
	}


}
