package dev.xkmc.akhet_chronomaly.init;

import dev.xkmc.akhet_chronomaly.content.client.tooltip.ClientItemTooltip;
import dev.xkmc.akhet_chronomaly.content.client.tooltip.ItemTooltip;
import dev.xkmc.akhet_chronomaly.init.registrate.ACItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = AkhetChronomaly.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ACClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.registerGeneric(AkhetChronomaly.loc("flip"),
					(stack, level, entity, index) -> ACItems.FLIP.get(stack) != null ? 1 : 0);
		});
	}

	@SubscribeEvent
	public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ItemTooltip.class, ClientItemTooltip::new);
	}


}
