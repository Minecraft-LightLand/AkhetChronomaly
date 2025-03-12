package dev.xkmc.akhet_chronomaly.events;

import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.engine.util.OverhealTest;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

@EventBusSubscriber(modid = AkhetChronomaly.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ArtifactEffectEvents {

	@SubscribeEvent
	public static void onKillEvent(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity player)
			AutoReg.ON_KILL_TARGET.get().trigger(player, event);
	}

	@SubscribeEvent
	public static void onHealEvent(LivingHealEvent event) {
		if (event.getEntity() instanceof LivingEntity player)
			AutoReg.ON_HEAL.get().trigger(player, event);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onConsumeHealEvent(LivingHealEvent event) {
		if (event.getEntity() instanceof LivingEntity player) {
			float amount = event.getAmount();
			if (player.getHealth() + amount > player.getMaxHealth()) {
				float overheal = player.getHealth() + amount - player.getMaxHealth();
				AutoReg.OVERHEAL.get().trigger(player, new OverhealTest(overheal));
			}
		}
	}


}
