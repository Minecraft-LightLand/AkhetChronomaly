package dev.xkmc.akhet_chronomaly.content.engine.core.type;

import dev.xkmc.akhet_chronomaly.content.engine.core.codec.AutoCodecTypeRegistry;
import dev.xkmc.akhet_chronomaly.content.engine.core.codec.WR;
import dev.xkmc.akhet_chronomaly.content.engine.core.effect.IStatusEffect;
import dev.xkmc.akhet_chronomaly.content.engine.core.effect.ITriggerEffect;
import dev.xkmc.akhet_chronomaly.content.engine.core.trigger.TriggerType;
import dev.xkmc.akhet_chronomaly.content.engine.effect.AttributeStatusEffect;
import dev.xkmc.akhet_chronomaly.content.engine.effect.MobEffectStatusEffect;
import dev.xkmc.akhet_chronomaly.content.engine.entry.StatusEffectEntry;
import dev.xkmc.akhet_chronomaly.content.engine.entry.TriggerEffectEntry;
import dev.xkmc.akhet_chronomaly.content.engine.predicate.DirectDamagePredicate;
import dev.xkmc.akhet_chronomaly.content.engine.predicate.PlayerLightPredicate;
import dev.xkmc.akhet_chronomaly.content.engine.trigger.*;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class AutoReg {

	public static final AutoCodecTypeRegistry<IEffectEntry<?>> ENTRY = AutoCodecTypeRegistry.of("effect_entry", IEffectEntry.class);
	public static final AutoCodecTypeRegistry<IUserPredicate<?>> PREDICATE = AutoCodecTypeRegistry.of("user_predicate", IUserPredicate.class);
	public static final AutoCodecTypeRegistry<IStatusEffect<?>> STATUS_EFFECT = AutoCodecTypeRegistry.of("status_effect", IStatusEffect.class);
	public static final AutoCodecTypeRegistry<ITriggerEffect<?, ?>> TRIGGER_EFFECT = AutoCodecTypeRegistry.of("trigger_effect", ITriggerEffect.class);

	public static final L2Registrate.RegistryInstance<TriggerType<?>> TRIGGER_TYPE = AkhetChronomaly.REGISTRATE.newRegistry("trigger_type", TriggerType.class);

	private static final SR<TriggerType<?>> SRTT = SR.of(AkhetChronomaly.REG, TRIGGER_TYPE.key());

	public static final Val<TriggerType<DamageData.Offence>> ON_HURT_TARGET = reg("on_hurt_target", DamageData.Offence.class);
	public static final Val<TriggerType<DamageData.DefenceMax>> AFT_DMG_TARGET = reg("after_damage_target", DamageData.DefenceMax.class);
	public static final Val<TriggerType<DamageData.Defence>> ON_DMG_SELF = reg("on_damage_self", DamageData.Defence.class);
	public static final Val<TriggerType<DamageData.DefenceMax>> AFT_DMG_SELF = reg("after_damage_self", DamageData.DefenceMax.class);
	public static final Val<TriggerType<LivingDeathEvent>> ON_KILL_TARGET = reg("on_kill_target", LivingDeathEvent.class);

	private static final WR<IEffectEntry<?>> REG_ENT = WR.of(AkhetChronomaly.REG, ENTRY);
	private static final WR<IUserPredicate<?>> REG_PRED = WR.of(AkhetChronomaly.REG, PREDICATE);
	private static final WR<IStatusEffect<?>> REG_STATUS = WR.of(AkhetChronomaly.REG, STATUS_EFFECT);
	private static final WR<ITriggerEffect<?, ?>> REG_TRIGGERED = WR.of(AkhetChronomaly.REG, TRIGGER_EFFECT);

	static {
		// entry
		REG_ENT.reg("status", StatusEffectEntry.class);
		REG_ENT.reg("trigger", TriggerEffectEntry.class);

		// predicate
		REG_PRED.reg("direct_damage", DirectDamagePredicate.class);
		REG_PRED.reg("player_light", PlayerLightPredicate.class);

		// status
		REG_STATUS.reg("attribute", AttributeStatusEffect.class);
		REG_STATUS.reg("mob_effect", MobEffectStatusEffect.class);

		// on hurt target

		// after damage target
		REG_TRIGGERED.reg("hit_apply_effect", HitApplyEffect.class);
		REG_TRIGGERED.reg("heal_on_hit", HealOnHit.class);

		// on damage self
		REG_TRIGGERED.reg("self_damage_factor", SelfDamageFactor.class);

		// after damage self

		// on kill target
		REG_TRIGGERED.reg("heal_on_kill", HealOnKill.class);
		REG_TRIGGERED.reg("kill_target_gain_effect", KillTargetSelfGainEffect.class);
	}

	private static <T> Val<TriggerType<T>> reg(String id, Class<T> cls) {
		return SRTT.reg(id, () -> new TriggerType<>(cls));
	}

	public static void register() {

	}

}
