package dev.xkmc.akhet_chronomaly.content.set.core;

import dev.xkmc.akhet_chronomaly.content.config.ArtifactSetConfig;
import dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

import java.util.List;

public abstract class SetEffect extends NamedEntry<SetEffect> {

	public SetEffect() {
		super(ACTypeRegistry.SET_EFFECT);
	}

	/**
	 * 获取词条描述
	 */
	public List<MutableComponent> getDetailedDescription() {
		return List.of(Component.translatable(getDescriptionId() + ".desc"));
	}

	/**
	 * when the set count changes. Entry contains an uuid if one needs to add it. for Attributes, it must be transient
	 */
	public void update(LivingEntity player, ArtifactSetConfig.Entry ent, boolean enabled) {
	}

	/**
	 * always ticks regardless if it's enabled or not
	 */
	public void tick(LivingEntity player, ArtifactSetConfig.Entry ent, boolean enabled) {
	}

	/**
	 * 当玩家试图发动近战攻击时触发，可以修改伤害数值。此时目标已确定，但是伤害来源还未创建。
	 */
	public boolean playerAttackModifyEvent(LivingEntity player, ArtifactSetConfig.Entry ent, CriticalHitEvent event) {
		return false;
	}

	public final boolean playerAttackedEvent(LivingEntity player, ArtifactSetConfig.Entry ent, DamageData.Attack cache) {
		var source = cache.getSource();
		if (!source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			return playerAttackedCancel(player, ent, source, cache);
		}
		return false;
	}

	public final void playerHurtEvent(LivingEntity player, ArtifactSetConfig.Entry ent, DamageData.Defence cache) {
		var source = cache.getSource();
		if (!source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			playerReduceDamage(player, ent, source, cache);
		}
	}

	public boolean playerAttackedCancel(LivingEntity player, ArtifactSetConfig.Entry ent, DamageSource source, DamageData.Attack cache) {
		return false;
	}

	public void playerReduceDamage(LivingEntity player, ArtifactSetConfig.Entry ent, DamageSource source, DamageData.Defence cache) {
	}


	/**
	 * 当玩家试图对怪物造成伤害时触发，可以修改伤害数值。此时已通过怪物的免疫判定，但是还未处理怪物减伤判定。
	 */
	public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, DamageData.Offence event) {

	}

	/**
	 * 当玩家对怪物造成确实伤害时触发。此时已处理过怪物减伤判定。
	 */
	public void playerDamageOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, DamageData.DefenceMax event) {
	}

	/**
	 * 当玩家击杀怪物时触发。
	 */
	public void playerKillOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, LivingDeathEvent event) {

	}

	public void playerShieldBlock(LivingEntity player, ArtifactSetConfig.Entry entry, LivingShieldBlockEvent event) {
	}

}
