package dev.xkmc.akhet_chronomaly.events;

import dev.xkmc.akhet_chronomaly.content.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = AkhetChronomaly.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ArtifactEffectEvents {

	@SubscribeEvent
	public static void onKillEvent(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity player)
			AutoReg.ON_KILL_TARGET.get().trigger(player, event);
	}


}
