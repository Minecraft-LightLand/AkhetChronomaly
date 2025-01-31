package dev.xkmc.akhet_chronomaly.content.set.core;

import dev.xkmc.l2core.capability.conditionals.ConditionalToken;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class SetEffectData extends ConditionalToken {

	@SerialField
	public int life;

	public boolean tick(Player player) {
		if (life > 0)
			life--;
		if (life == 0) {
			remove(player);
		}
		return life <= 0;
	}

	public void update(int time) {
		life = time;
	}

	protected void remove(Player player) {
	}

}
