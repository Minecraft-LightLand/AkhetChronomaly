package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import net.minecraft.resources.ResourceLocation;

public class SetRegHelper {

	private final ACRegistrate reg;
	private final String id;

	public SetRegHelper(ACRegistrate reg, String id) {
		this.reg = reg;
		this.id = id;
	}

	public ResourceLocation getId() {
		return ResourceLocation.fromNamespaceAndPath(reg.getModid(), id);
	}

	public SetBuilder<ArtifactSet, BaseArtifact, ACRegistrate> regSet(String lang) {
		return reg.regSet(id, ArtifactSet::new, lang);
	}

}
