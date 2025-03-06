package dev.xkmc.akhet_chronomaly.content.effects.vampire;

import dev.xkmc.akhet_chronomaly.engine.effect.AttributeBonusStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.effect.AttributeStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.effect.BonusStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.entry.StatusEffectEntry;
import dev.xkmc.akhet_chronomaly.engine.entry.TriggerEffectEntry;
import dev.xkmc.akhet_chronomaly.engine.predicate.DirectDamagePredicate;
import dev.xkmc.akhet_chronomaly.engine.predicate.PlayerLightPredicate;
import dev.xkmc.akhet_chronomaly.engine.predicate.SelfHealthPredicate;
import dev.xkmc.akhet_chronomaly.engine.trigger.HealGainBonus;
import dev.xkmc.akhet_chronomaly.engine.trigger.HealOnHit;
import dev.xkmc.akhet_chronomaly.engine.trigger.HealOnKill;
import dev.xkmc.akhet_chronomaly.engine.util.BonusRecord;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class VampireSet {

	static {
		// base
		List.of(TriggerEffectEntry.of(new DirectDamagePredicate(true), new HealOnHit(0.05f)));
		List.of(TriggerEffectEntry.of(new HealOnHit(0.02f)));
		List.of(TriggerEffectEntry.of(new HealOnKill(0.15f, 4, 0.6f, 0.05f)));
		List.of(
				StatusEffectEntry.of(
						new SelfHealthPredicate(0, 0.5),
						AttributeStatusEffect.total(L2DamageTracker.REDUCTION, -0.2)
				),
				StatusEffectEntry.of(
						new SelfHealthPredicate(0.5, 1),
						AttributeStatusEffect.add(L2DamageTracker.CRIT_RATE, 0.2)
				)
		);
		List.of(
				StatusEffectEntry.of(
						new PlayerLightPredicate(0, 7, true),
						AttributeStatusEffect.add(L2DamageTracker.REGEN, 2)
				)
		);

		// pure blood
		List.of(
				StatusEffectEntry.of(AttributeBonusStatusEffect.base(Attributes.MOVEMENT_SPEED, "pure_blood_speed")),
				TriggerEffectEntry.of(new HealGainBonus(BonusRecord.add("pure_blood_speed", 100, 0.2)))
		);

		List.of(
				StatusEffectEntry.of(AttributeBonusStatusEffect.base(Attributes.ATTACK_DAMAGE, "pure_blood_attack")),
				TriggerEffectEntry.of(new HealGainBonus(BonusRecord.add("pure_blood_attack", 100, 0.2)))
		);

		List.of(
				StatusEffectEntry.of(AttributeBonusStatusEffect.base(L2DamageTracker.MAGIC_FACTOR, "pure_blood_magic")),
				TriggerEffectEntry.of(new HealGainBonus(BonusRecord.add("pure_blood_magic", 100, 0.4)))
		);

		List.of(StatusEffectEntry.of(
				BonusStatusEffect.total("pure_blood_speed", 0.5),
				BonusStatusEffect.total("pure_blood_attack", 0.5),
				BonusStatusEffect.total("pure_blood_magic", 0.5)
		));

	}

}
