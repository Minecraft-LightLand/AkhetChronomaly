package dev.xkmc.akhet_chronomaly.init.data;

import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class SlotGen extends CuriosDataProvider {

	public SlotGen(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
		super(modId, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
		for (var e : ACSlotCuriosType.values()) {
			createSlot(e.getIdentifier()).order(e.priority)
					.icon(AkhetChronomaly.loc("slot/empty_" + e.getIdentifier() + "_slot"))
					.size(e.count)
					.useNativeGui(false);
		}
		createEntities("l2artifact_player")
				.addEntities(EntityType.PLAYER)
				.addSlots(Stream.of(ACSlotCuriosType.values())
						.map(ACSlotCuriosType::getIdentifier).toArray(String[]::new));
	}

}
