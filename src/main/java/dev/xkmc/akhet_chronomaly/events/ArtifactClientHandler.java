package dev.xkmc.akhet_chronomaly.events;

import dev.xkmc.akhet_chronomaly.content.client.select.SetSelectScreen;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.GatherSkippedAttributeTooltipsEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = AkhetChronomaly.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ArtifactClientHandler {

	@SubscribeEvent
	public static void tooltipSkip(GatherSkippedAttributeTooltipsEvent event) {
		if (!(event.getStack().getItem() instanceof BaseArtifact)) return;
		var opt = BaseArtifact.getStats(event.getStack());
		if (opt.isEmpty() || opt.get().stats().isEmpty()) return;
		var slot = opt.get().slot().getCurioIdentifier();
		var id = ResourceLocation.fromNamespaceAndPath("curios", slot);
		opt.get().gatherAttributeIds(event::skipId, id);
		opt.get().gatherAttributeIds(event::skipId, id.withSuffix("0"));
		opt.get().gatherAttributeIds(event::skipId, id.withSuffix("/0"));

	}

	public static void openSelectionScreen() {
		Minecraft.getInstance().setScreen(new SetSelectScreen());
	}

}
