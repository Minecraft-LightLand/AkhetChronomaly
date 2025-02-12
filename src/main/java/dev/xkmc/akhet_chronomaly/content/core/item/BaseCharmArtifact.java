package dev.xkmc.akhet_chronomaly.content.core.item;

import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class BaseCharmArtifact extends Item implements IArtifact {


	public final Supplier<ArtifactSet> set;

	public BaseCharmArtifact(Properties properties, Supplier<ArtifactSet> set) {
		super(properties);
		this.set = set;
	}

	@Override
	public ArtifactSet getSet() {
		return set.get();
	}

}
