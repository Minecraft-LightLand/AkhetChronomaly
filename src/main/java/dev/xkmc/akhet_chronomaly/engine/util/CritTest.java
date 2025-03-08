package dev.xkmc.akhet_chronomaly.engine.util;

import dev.xkmc.l2damagetracker.contents.attack.PlayerAttackCache;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

public class CritTest {

	public final PlayerAttackCache data;
	public final CriticalHitEvent event;
	private boolean flag;

	public CritTest(PlayerAttackCache data, CriticalHitEvent event) {
		this.data = data;
		this.event = event;
	}

	public void setCrit(boolean crit) {
		flag = crit;
	}

	public boolean shouldCrit() {
		return flag;
	}

}
