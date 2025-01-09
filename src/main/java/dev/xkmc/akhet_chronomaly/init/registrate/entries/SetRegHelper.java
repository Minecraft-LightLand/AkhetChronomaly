package dev.xkmc.akhet_chronomaly.init.registrate.entries;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import dev.xkmc.akhet_chronomaly.content.set.core.SetEffect;
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

	public <T extends SetEffect> SetEffectBuilder<T, ACRegistrate> setEffect(String id, NonNullSupplier<T> sup) {
		return reg.setEffect(id, sup);
	}
}
