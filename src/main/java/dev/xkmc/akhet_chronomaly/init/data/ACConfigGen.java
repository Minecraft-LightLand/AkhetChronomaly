package dev.xkmc.akhet_chronomaly.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.akhet_chronomaly.content.core.stat.SlotStatEntry;
import dev.xkmc.akhet_chronomaly.content.core.stat.SlotStatMap;
import dev.xkmc.akhet_chronomaly.content.core.stat.StatType;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSlot;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.LinkedHashMap;

import static dev.xkmc.akhet_chronomaly.init.registrate.ACTypeRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.*;

public class ACConfigGen {

	public static final ResourceLocation HEALTH_ADD = regLang("health_add", "Health +");
	public static final ResourceLocation HEALTH_MULT = regLang("health_mult", "Health +%%");
	public static final ResourceLocation ARMOR_ADD = regLang("armor_add", "Armor +");
	public static final ResourceLocation ARMOR_MULT = regLang("armor_mult", "Armor +%%");
	public static final ResourceLocation TOUGH_ADD = regLang("tough_add", "Armor Toughness +");
	public static final ResourceLocation ATK_ADD = regLang("attack_add", "Melee Damage +");
	public static final ResourceLocation ATK_MULT = regLang("attack_mult", "Melee Damage +%%");
	public static final ResourceLocation RANGE_ADD = regLang("attack_range_add", "Attack Range +");
	public static final ResourceLocation REACH_ADD = regLang("mining_range_add", "Mining Range +");
	public static final ResourceLocation CR_ADD = regLang("crit_rate_add", "Crit Rate +%%");
	public static final ResourceLocation CD_ADD = regLang("crit_damage_add", "Crit Damage +%%");
	public static final ResourceLocation SPEED_MULT = regLang("speed_mult", "Speed +%%");
	public static final ResourceLocation ATK_SPEED_MULT = regLang("attack_speed_mult", "Attack Speed +%%");
	public static final ResourceLocation ABSORB = regLang("absorption", "Damage Absorption");
	public static final ResourceLocation REDUCTION = regLang("reduction", "Damage Reduction");
	public static final ResourceLocation REGEN = regLang("regeneration_rate", "Regen Rate +%%");
	public static final ResourceLocation BOW_ADD = regLang("bow_strength_add", "Bow Strength +%%");
	public static final ResourceLocation EXPLOSION_ADD = regLang("explosion_add", "Explosion Damage +%%");
	public static final ResourceLocation MAGIC_ADD = regLang("magic_add", "Magic Damage +%%");

	private static ResourceLocation regLang(String id, String name) {
		AkhetChronomaly.REGISTRATE.addRawLang("stat_type." + AkhetChronomaly.MODID + "." + id, name);
		return AkhetChronomaly.loc(id);
	}

	public static void genSlotType(BootstrapContext<StatType> map) {
		regStat(map, HEALTH_ADD, Attributes.MAX_HEALTH, ADD_VALUE, 2);
		regStat(map, HEALTH_MULT, Attributes.MAX_HEALTH, ADD_MULTIPLIED_BASE, 2);
		regStat(map, ARMOR_ADD, Attributes.ARMOR, ADD_VALUE, 2);
		regStat(map, ARMOR_MULT, Attributes.ARMOR, ADD_MULTIPLIED_BASE, 2);
		regStat(map, TOUGH_ADD, Attributes.ARMOR_TOUGHNESS, ADD_VALUE, 1);
		regStat(map, ATK_ADD, Attributes.ATTACK_DAMAGE, ADD_VALUE, 2);
		regStat(map, RANGE_ADD, Attributes.ENTITY_INTERACTION_RANGE, ADD_VALUE, 0.1);
		regStat(map, REACH_ADD, Attributes.BLOCK_INTERACTION_RANGE, ADD_VALUE, 0.2);
		regStat(map, CR_ADD, L2DamageTracker.CRIT_RATE, ADD_VALUE, 0.05);
		regStat(map, CD_ADD, L2DamageTracker.CRIT_DMG, ADD_VALUE, 0.1);
		regStat(map, ATK_MULT, Attributes.ATTACK_DAMAGE, ADD_MULTIPLIED_BASE, 0.1);
		regStat(map, SPEED_MULT, Attributes.MOVEMENT_SPEED, ADD_MULTIPLIED_BASE, 0.05);
		regStat(map, ATK_SPEED_MULT, Attributes.ATTACK_SPEED, ADD_MULTIPLIED_BASE, 0.05);
		regStat(map, BOW_ADD, L2DamageTracker.BOW_STRENGTH, ADD_VALUE, 0.1);
		regStat(map, ABSORB, L2DamageTracker.ABSORB, ADD_VALUE, 0.5);
		regStat(map, REDUCTION, L2DamageTracker.REDUCTION, ADD_MULTIPLIED_TOTAL, 0.05);
		regStat(map, REGEN, L2DamageTracker.REGEN, ADD_VALUE, 0.2);
		regStat(map, EXPLOSION_ADD, L2DamageTracker.EXPLOSION_FACTOR, ADD_VALUE, 0.2);
		regStat(map, MAGIC_ADD, L2DamageTracker.MAGIC_FACTOR, ADD_VALUE, 0.15);
	}

	private static class SlotBuilder {

		private static SlotBuilder primary(DataMapProvider.Builder<SlotStatMap, ArtifactSlot> builder, Holder<ArtifactSlot> slot) {
			return new SlotBuilder(builder, slot, 1, 4, true, true);
		}

		private static SlotBuilder side(DataMapProvider.Builder<SlotStatMap, ArtifactSlot> builder, Holder<ArtifactSlot> slot) {
			return new SlotBuilder(builder, slot, 0.6, 3, true, false);
		}

		private static SlotBuilder curio(DataMapProvider.Builder<SlotStatMap, ArtifactSlot> builder, Holder<ArtifactSlot> slot) {
			return new SlotBuilder(builder, slot, 0.4, 2, false, false);
		}

		private final DataMapProvider.Builder<SlotStatMap, ArtifactSlot> builder;
		private final Holder<ArtifactSlot> slot;
		private final LinkedHashMap<ResourceLocation, SlotStatEntry> map = new LinkedHashMap<>();

		private final double scale;
		private final int refineChance;
		private final boolean mayReforge, mayAscend;

		private SlotBuilder(DataMapProvider.Builder<SlotStatMap, ArtifactSlot> builder, Holder<ArtifactSlot> slot,
							double scale, int refineChance, boolean mayReforge, boolean mayAscend) {
			this.builder = builder;
			this.slot = slot;
			this.scale = scale;
			this.refineChance = refineChance;
			this.mayReforge = mayReforge;
			this.mayAscend = mayAscend;
		}

		private SlotBuilder put(int weight, double scale, ResourceLocation... stats) {
			for (var e : stats) {
				map.put(e, new SlotStatEntry(weight, scale));
			}
			return this;
		}

		private void build() {
			builder.add(slot, new SlotStatMap(scale, refineChance, mayReforge, mayAscend, map), false);
		}

	}

	public static void genStatMap(RegistrateDataMapProvider pvd) {
		var builder = pvd.builder(STAT_MAP.reg());
		ResourceLocation[] misc = {EXPLOSION_ADD, MAGIC_ADD};
		int miscweight = 50;

		SlotBuilder.primary(builder, SLOT_HEAD)
				.put(100, 1, ARMOR_ADD, ARMOR_MULT)
				.put(50, 0.75, TOUGH_ADD, CR_ADD, CD_ADD, REDUCTION)
				.put(miscweight, 0.75, misc)
				.build();

		SlotBuilder.primary(builder, SLOT_BODY)
				.put(100, 1.25, ARMOR_ADD, ARMOR_MULT)
				.put(50, 1, HEALTH_ADD, HEALTH_MULT, TOUGH_ADD, ABSORB, REGEN, REDUCTION)
				.build();

		SlotBuilder.primary(builder, SLOT_CAPE)
				.put(100, 1, SPEED_MULT)
				.put(50, 1, ABSORB, REDUCTION)
				.put(50, 0.75, ARMOR_ADD, ARMOR_MULT)
				.build();

		SlotBuilder.primary(builder, SLOT_BELT)
				.put(100, 1, HEALTH_ADD, HEALTH_MULT)
				.put(50, 1, REGEN)
				.put(50, 0.75, ARMOR_ADD, ARMOR_MULT, CR_ADD, CD_ADD)
				.put(miscweight, 0.75, misc)
				.build();

		SlotBuilder.primary(builder, SLOT_LEGS)
				.put(100, 1.25, ARMOR_ADD, ARMOR_MULT)
				.put(50, 1, ABSORB, REDUCTION)
				.put(50, 1, HEALTH_ADD, HEALTH_MULT, TOUGH_ADD, SPEED_MULT, REGEN)
				.build();

		SlotBuilder.primary(builder, SLOT_FEET)
				.put(100, 1, ARMOR_ADD, ARMOR_MULT, SPEED_MULT)
				.put(50, 1, ABSORB, REDUCTION)
				.put(50, 0.75, TOUGH_ADD)
				.build();

		SlotBuilder.side(builder, SLOT_SHOULDER)
				.put(100, 1, ARMOR_ADD, ARMOR_MULT, ATK_ADD, ATK_MULT, BOW_ADD, ATK_SPEED_MULT)
				.put(50, 1, REDUCTION, REGEN)
				.put(50, 0.75, HEALTH_ADD, HEALTH_MULT, TOUGH_ADD)
				.build();

		SlotBuilder.side(builder, SLOT_WRIST)
				.put(100, 1, ATK_ADD, ATK_MULT, BOW_ADD, CR_ADD, CD_ADD, ATK_SPEED_MULT)
				.put(50, 0.75, ARMOR_ADD, ARMOR_MULT)
				.put(miscweight, 1, misc)
				.build();

		SlotBuilder.side(builder, SLOT_HAND)
				.put(100, 1, ATK_ADD, ATK_MULT, BOW_ADD, CR_ADD, CD_ADD, RANGE_ADD, REACH_ADD)
				.put(50, 0.75, ARMOR_ADD, ARMOR_MULT)
				.put(miscweight, 1, misc)
				.build();

		SlotBuilder.curio(builder, SLOT_NECK)
				.put(100, 1, HEALTH_ADD, HEALTH_MULT, TOUGH_ADD, SPEED_MULT, CR_ADD, CD_ADD)
				.put(50, 1, REGEN)
				.put(miscweight, 1, misc)
				.build();

		SlotBuilder.curio(builder, SLOT_RING)
				.put(100, 1, SPEED_MULT, ATK_ADD, ATK_MULT, BOW_ADD, CR_ADD, CD_ADD, RANGE_ADD, REACH_ADD, ATK_SPEED_MULT)
				.put(50, 1, REGEN)
				.put(miscweight, 1, misc)
				.build();

	}

	private static void regStat(BootstrapContext<StatType> map, ResourceLocation id, Holder<Attribute> attr, AttributeModifier.Operation op, double base) {
		map.register(ResourceKey.create(STAT_TYPE.key(), id), genEntry(attr, op, base));
	}

	private static StatType genEntry(Holder<Attribute> attr, AttributeModifier.Operation op, double base) {
		return new StatType(base, 0.5, 1, 0.1, 0.2, attr, op, null);
	}

	public static void register() {

	}

}