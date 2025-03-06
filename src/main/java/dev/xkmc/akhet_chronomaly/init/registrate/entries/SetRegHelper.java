package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.item.BaseArtifact;
import net.minecraft.resources.ResourceLocation;

public class SetRegHelper {

	private final ACRegistrate reg;
	private final String id;
	private final SetEntry<?> root;

	public SetRegHelper(ACRegistrate reg, String id) {
		this.reg = reg;
		this.id = id;
		this.root = null;
	}

	SetRegHelper(ACRegistrate reg, String id, SetEntry<?> root) {
		this.reg = reg;
		this.id = id;
		this.root = root;
	}

	public ResourceLocation getId() {
		return ResourceLocation.fromNamespaceAndPath(reg.getModid(), id);
	}

	public SetBuilder<ArtifactSet, BaseArtifact, ACRegistrate> regSet(String lang) {
		return reg.regSet(id, ()-> new ArtifactSet(root), lang);
	}

}
