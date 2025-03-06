package dev.xkmc.akhet_chronomaly.engine.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;

public class PlayerLight {


	public static int playerUnderSun(LivingEntity entity) {
		return entity.level().getBrightness(LightLayer.SKY, entity.getOnPos().above()) - entity.level().getSkyDarken();
	}

	public static int playerLight(LivingEntity entity) {
		return entity.level().getMaxLocalRawBrightness(entity.getOnPos().above());
	}

}
