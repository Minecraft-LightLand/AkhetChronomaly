package dev.xkmc.akhet_chronomaly.content.core;

import dev.xkmc.akhet_chronomaly.init.data.ACSlotCuriosType;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.resources.ResourceLocation;

public class ArtifactSlot extends NamedEntry<ArtifactSlot> {

	private final ACSlotCuriosType curios;

	public ArtifactSlot(ACSlotCuriosType curios) {
		super(ACTypeRegistry.SLOT);
		this.curios = curios;
		curios.slot = this;
	}

	public int ordinal() {
		return curios.ordinal();
	}

	public ResourceLocation icon() {
		return getRegistryName().withPath(e -> "textures/slot/empty_artifact_" + e + "_slot.png");
	}

	public String getCurioIdentifier() {
		return curios.getIdentifier();
	}
}
