package dev.xkmc.akhet_chronomaly.content.effects.vampire;

import dev.xkmc.akhet_chronomaly.content.engine.effect.AttributeStatusEffect;
import dev.xkmc.akhet_chronomaly.content.engine.effect.MobEffectStatusEffect;
import dev.xkmc.akhet_chronomaly.content.engine.entry.StatusEffectEntry;
import dev.xkmc.akhet_chronomaly.content.engine.entry.TriggerEffectEntry;
import dev.xkmc.akhet_chronomaly.content.engine.predicate.DirectDamagePredicate;
import dev.xkmc.akhet_chronomaly.content.engine.predicate.PlayerLightPredicate;
import dev.xkmc.akhet_chronomaly.content.engine.trigger.HealOnKill;
import dev.xkmc.akhet_chronomaly.content.engine.trigger.HitApplyEffect;
import dev.xkmc.akhet_chronomaly.content.engine.trigger.KillTargetSelfGainEffect;
import dev.xkmc.akhet_chronomaly.content.engine.trigger.SelfDamageFactor;
import dev.xkmc.akhet_chronomaly.content.engine.util.EffectRecord;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class VampireSet {

	static {
		List.of(TriggerEffectEntry.of(
				new PlayerLightPredicate(8, 15, true),
				new SelfDamageFactor(1.1f)
		), TriggerEffectEntry.of(
				new DirectDamagePredicate(true),
				new HitApplyEffect(new EffectRecord(MobEffects.WITHER, 0, 100, 0.5, false, 0))
		));

		List.of(
				StatusEffectEntry.of(
						new PlayerLightPredicate(0, 7, true),
						new MobEffectStatusEffect(MobEffects.NIGHT_VISION, 0),
						AttributeStatusEffect.base(Attributes.MOVEMENT_SPEED, 0.1),
						AttributeStatusEffect.base(Attributes.ATTACK_DAMAGE, 0.1)
				)
		);

		List.of(TriggerEffectEntry.of(new HealOnKill(0.15f, 4, 0.6f, 0.05f)));

		List.of(
				StatusEffectEntry.of(
						AttributeStatusEffect.base(Attributes.MAX_HEALTH, 0.25),
						AttributeStatusEffect.add(L2DamageTracker.REGEN, 0.5)
				)
		);

		List.of(TriggerEffectEntry.of(
				new KillTargetSelfGainEffect(new EffectRecord(MobEffects.WITHER, 0, 100, 0.5, false, 0))
		));

	}

}
