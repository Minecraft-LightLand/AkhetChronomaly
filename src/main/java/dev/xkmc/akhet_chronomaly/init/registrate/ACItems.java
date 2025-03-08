package dev.xkmc.akhet_chronomaly.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactStats;
import dev.xkmc.akhet_chronomaly.content.core.item.BaseArtifact;
import dev.xkmc.akhet_chronomaly.content.misc.RandomArtifactItem;
import dev.xkmc.akhet_chronomaly.content.misc.RandomArtifactSetItem;
import dev.xkmc.akhet_chronomaly.content.misc.SetGroup;
import dev.xkmc.akhet_chronomaly.engine.core.type.AutoReg;
import dev.xkmc.akhet_chronomaly.engine.effect.AttributeBonusStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.effect.AttributeStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.effect.AttributeTransferStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.effect.BonusStatusEffect;
import dev.xkmc.akhet_chronomaly.engine.entry.StatusEffectEntry;
import dev.xkmc.akhet_chronomaly.engine.entry.TriggerEffectEntry;
import dev.xkmc.akhet_chronomaly.engine.predicate.*;
import dev.xkmc.akhet_chronomaly.engine.trigger.*;
import dev.xkmc.akhet_chronomaly.engine.util.BonusRecord;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

import java.util.List;

import static dev.xkmc.akhet_chronomaly.init.AkhetChronomaly.REGISTRATE;

@SuppressWarnings({"raw_type", "unchecked"})
public class ACItems {

	public static final String[] RANK_NAME = {" [Worn]", " -Common-", " =Rare=", " >Epic<", " »Legendary«", " -»Godly«-"};

	static {
		REGISTRATE.buildL2CreativeTab("artifacts", "Akhet Chronomaly", b -> b
				.icon(ACItems.RANDOM[0]::asStack));
	}

	public static final ItemEntry<RandomArtifactItem>[] RANDOM;
	public static final ItemEntry<RandomArtifactSetItem>[] RANDOM_SET;

	private static final DCReg DC = DCReg.of(AkhetChronomaly.REG);
	public static final DCVal<ArtifactStats> STATS = DC.reg("stats", ArtifactStats.class, false);
	public static final DCVal<SetGroup> GROUP = DC.reg("set_group", SetGroup.class, true);
	public static final DCVal<Unit> FLIP = DC.unit("flip");

	static {
		{
			int n = BaseArtifact.maxRank();
			RANDOM = new ItemEntry[n];
			for (int i = 0; i < n; i++) {
				int I = i;
				TagKey<Item> artifact = ItemTags.create(AkhetChronomaly.loc("artifact"));
				RANDOM[i] = REGISTRATE.item("random_" + I, p -> new RandomArtifactItem(p, I))
						.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", AkhetChronomaly.loc("item/rank/" + I))
								.texture("layer1", AkhetChronomaly.loc("item/random")))
						.tag(artifact)
						.lang("Random Artifact" + RANK_NAME[i]).register();
			}
			RANDOM_SET = new ItemEntry[n];
			for (int i = 0; i < n; i++) {
				int I = i;
				RANDOM_SET[i] = REGISTRATE.item("random_set_" + I, p -> new RandomArtifactSetItem(p, I))
						.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", AkhetChronomaly.loc("item/rank/" + I))
								.texture("layer1", AkhetChronomaly.loc("item/random_set")))
						.lang("Random Artifact Set" + RANK_NAME[i]).register();
			}
		}
		{
			var set = REGISTRATE.getSetHelper("count_sanguivore");
			var rootBuilder = set.regSet("Count Sanguivore").addClothPreset();
			if (DatagenModLoader.isRunningDataGen()) {
				rootBuilder.effect("bloodshed", 3, () -> List.of(TriggerEffectEntry.of(
								new DirectDamagePredicate(true), new HealOnHit(0.05f))),
						"%s on direct attack");
				rootBuilder.effect("blood_drain", 6, () -> List.of(TriggerEffectEntry.of(
						new HealOnHit(0.02f))));
				rootBuilder.effect("necrovore_festival", 9, () -> List.of(TriggerEffectEntry.of(
								new HealOnKill(0.15f, 4, 0.6f, 0.05f))),
						"Heal %s of your health and restore nutrition when you kill enemies");
				rootBuilder.effect("blood_bath", 12, () -> List.of(
						StatusEffectEntry.of(
								new SelfHealthPredicate(0, 0.5),
								AttributeStatusEffect.total(L2DamageTracker.REDUCTION, -0.2)
						),
						StatusEffectEntry.of(
								new SelfHealthPredicate(0.5, 1),
								AttributeStatusEffect.add(L2DamageTracker.CRIT_RATE, 0.2)
						)
				));
				rootBuilder.effect("night_party", 15, () -> List.of(StatusEffectEntry.of(
						new PlayerLightPredicate(0, 7, true),
						AttributeStatusEffect.add(L2DamageTracker.REGEN, 2)
				)), "When you are not under sunlight: %s");
			}
			var root = rootBuilder.register();
			{
				var sub = root.subSet("pure_blood");
				var builder = sub.regSet("Pure Blood").addArmorPreset();
				if (DatagenModLoader.isRunningDataGen()) {
					builder.effect("scarlet_shadow", 3, () -> List.of(
							TriggerEffectEntry.of(new GainBonus<>(BonusRecord.add("pure_blood_speed", 100, 0.2), AutoReg.ON_HEAL.get())),
							StatusEffectEntry.of(AttributeBonusStatusEffect.base(Attributes.MOVEMENT_SPEED, "pure_blood_speed"))
					), "When you heal, gain %s for %s seconds");
					builder.effect("primal_strength", 6, () -> List.of(
							TriggerEffectEntry.of(new GainBonus<>(BonusRecord.add("pure_blood_attack", 100, 0.3), AutoReg.ON_HEAL.get())),
							StatusEffectEntry.of(AttributeBonusStatusEffect.base(Attributes.ATTACK_DAMAGE, "pure_blood_attack"))
					), "When you heal, gain %s for %s seconds");
					builder.effect("inherited_spell", 9, () -> List.of(
							TriggerEffectEntry.of(new GainBonus<>(BonusRecord.add("pure_blood_magic", 100, 0.4), AutoReg.ON_HEAL.get())),
							StatusEffectEntry.of(AttributeBonusStatusEffect.base(L2DamageTracker.MAGIC_FACTOR, "pure_blood_magic"))
					), "When you heal, gain %s for %s seconds");
					builder.effect("nobel_blood", 12, () -> List.of(StatusEffectEntry.of(
							BonusStatusEffect.total("pure_blood_speed", 0.5),
							BonusStatusEffect.total("pure_blood_attack", 0.5),
							BonusStatusEffect.total("pure_blood_magic", 0.5)
					)), "Amplify previous effects in this set by %s/%s/%s");
				}
				builder.register();
			}
			{
				var sub = root.subSet("pale_moon");
				var builder = sub.regSet("Pale Moon").addArmorPreset();
				if (DatagenModLoader.isRunningDataGen()) {
					builder.effect("moon_shadow", 3, () -> List.of(
							TriggerEffectEntry.of(new TargetHealthPredicate(0.99, 1), new SetCritEffect())
					), "Critical hit when target is at full health");
					builder.effect("pale_claw", 6, () -> List.of(
							TriggerEffectEntry.of(new CritDamagePredicate(), new HealOnHit(0.3f))
					), "%s on critical hit");
					builder.effect("night_predator", 9, () -> List.of(StatusEffectEntry.of(
							new PlayerLightPredicate(0, 7, true),
							AttributeStatusEffect.add(L2DamageTracker.CRIT_RATE, 0.5)
					)), "When you are not under sunlight: %s");
					builder.effect("lunar_blade", 12, () -> List.of(StatusEffectEntry.of(
							new PlayerLightPredicate(0, 7, true),
							AttributeStatusEffect.add(L2DamageTracker.CRIT_DMG, 1)
					)), "When you are not under sunlight: %s");
				}
				builder.register();
			}
			{
				var sub = root.subSet("draining_sentinel");
				var builder = sub.regSet("Draining Sentinel").addArmorPreset();
				if (DatagenModLoader.isRunningDataGen()) {
					builder.effect("blood_pool", 3, () -> List.of(
							StatusEffectEntry.of(AttributeStatusEffect.add(
									Attributes.MAX_HEALTH, 0.3))
					));
					builder.effect("blood_core", 6, () -> List.of(
							StatusEffectEntry.of(AttributeTransferStatusEffect.Type.MAX_HEALTH.add(
									Attributes.MAX_ABSORPTION, 0.5)),
							TriggerEffectEntry.of(new OverhealEffect())
					), "Convert overheal to absorption, up to %s of max health");
					builder.effect("scarlet_infusion", 9, () -> List.of(
							StatusEffectEntry.of(AttributeTransferStatusEffect.Type.MAX_HEALTH.add(
									Attributes.ATTACK_DAMAGE, 0.2))
					), "Increase attack damage by %s of current health");
					builder.effect("flesh_burse", 12, () -> List.of(
							TriggerEffectEntry.of(new DirectDamagePredicate(true),
									new ConvertAbsorptionToDamage(0.5f))
					), "On direct attack, convert %s of absorption to damage");
				}
				builder.register();
			}
		}
	}

	public static void register() {

	}

}