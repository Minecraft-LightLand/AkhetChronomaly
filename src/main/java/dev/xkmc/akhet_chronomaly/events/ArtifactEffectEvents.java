package dev.xkmc.akhet_chronomaly.events;

import dev.xkmc.akhet_chronomaly.content.config.ArtifactSetConfig;
import dev.xkmc.akhet_chronomaly.content.core.CuriosUtils;
import dev.xkmc.akhet_chronomaly.content.set.core.SetEffect;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

@EventBusSubscriber(modid = AkhetChronomaly.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ArtifactEffectEvents {

	public static <T> void postEvent(LivingEntity entity, T event, EventConsumer<T> cons) {
		var map = CuriosUtils.findAll(entity);
		if (map.isEmpty()) return;
		for (var ent : map.entrySet()) {
			ent.getKey().propagateEvent(ent.getValue(), entity, event, cons);
		}
	}

	public static <T> boolean postEvent(LivingEntity entity, T event, EventPredicate<T> cons) {
		var map = CuriosUtils.findAll(entity);
		if (map.isEmpty()) return false;
		boolean ans = false;
		for (var ent : map.entrySet()) {
			ans |= ent.getKey().propagateEvent(ent.getValue(), entity, event, cons);
		}
		return ans;
	}

	@SubscribeEvent
	public static void onKillEvent(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof LivingEntity player)
			postEvent(player, event, SetEffect::playerKillOpponentEvent);
	}

	@SubscribeEvent
	public static void onShieldBlock(LivingShieldBlockEvent event) {
		postEvent(event.getEntity(), event, SetEffect::playerShieldBlock);
	}

	public interface EventConsumer<T> {

		void apply(SetEffect set, LivingEntity player, ArtifactSetConfig.Entry ent, int rank, T event);

	}

	public interface EventPredicate<T> {

		boolean apply(SetEffect set, LivingEntity player, ArtifactSetConfig.Entry ent, int rank, T event);

	}

}
