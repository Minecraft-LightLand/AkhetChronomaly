package dev.xkmc.akhet_chronomaly.content.engine.core.trigger;

import dev.xkmc.akhet_chronomaly.content.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class TriggerType<R> extends NamedEntry<TriggerType<?>> {

	private final Class<R> cls;

	public TriggerType(Class<R> cls) {
		super(AutoReg.TRIGGER_TYPE);
		this.cls = cls;
	}

	public void trigger(LivingEntity le, R event) {
		if (!(le instanceof Player player)) return;
		ACTypeRegistry.CAP.type().getOrCreate(player).trigger(player, this, event);
	}

}
