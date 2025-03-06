package dev.xkmc.akhet_chronomaly.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactStats;
import dev.xkmc.akhet_chronomaly.content.core.item.BaseArtifact;
import dev.xkmc.akhet_chronomaly.content.misc.RandomArtifactItem;
import dev.xkmc.akhet_chronomaly.content.misc.RandomArtifactSetItem;
import dev.xkmc.akhet_chronomaly.content.misc.SetGroup;
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
				rootBuilder.effect(3, () -> List.of(TriggerEffectEntry.of(
						new DirectDamagePredicate(true), new HealOnHit(0.05f))));
				rootBuilder.effect(6, () -> List.of(TriggerEffectEntry.of(new HealOnHit(0.02f))));
				rootBuilder.effect(9, () -> List.of(TriggerEffectEntry.of(new HealOnKill(0.15f, 4, 0.6f, 0.05f))));
				rootBuilder.effect(12, () -> List.of(
						StatusEffectEntry.of(
								new SelfHealthPredicate(0, 0.5),
								AttributeStatusEffect.total(L2DamageTracker.REDUCTION, -0.2)
						),
						StatusEffectEntry.of(
								new SelfHealthPredicate(0.5, 1),
								AttributeStatusEffect.add(L2DamageTracker.CRIT_RATE, 0.2)
						)
				));
				rootBuilder.effect(15, () -> List.of(StatusEffectEntry.of(
						new PlayerLightPredicate(0, 7, true),
						AttributeStatusEffect.add(L2DamageTracker.REGEN, 2)
				)));
			}
			var root = rootBuilder.register();
			{
				var sub = root.subSet("pale_moon");
				var builder = sub.regSet("Pale Moon").addArmorPreset();
				if (DatagenModLoader.isRunningDataGen()) {
					builder.effect(3, () -> List.of(
							StatusEffectEntry.of(AttributeBonusStatusEffect.base(Attributes.MOVEMENT_SPEED, "pure_blood_speed")),
							TriggerEffectEntry.of(new HealGainBonus(BonusRecord.add("pure_blood_speed", 100, 0.2)))
					));
					builder.effect(6, () -> List.of(
							StatusEffectEntry.of(AttributeBonusStatusEffect.base(Attributes.ATTACK_DAMAGE, "pure_blood_attack")),
							TriggerEffectEntry.of(new HealGainBonus(BonusRecord.add("pure_blood_attack", 100, 0.3)))
					));
					builder.effect(9, () -> List.of(
							StatusEffectEntry.of(AttributeBonusStatusEffect.base(L2DamageTracker.MAGIC_FACTOR, "pure_blood_magic")),
							TriggerEffectEntry.of(new HealGainBonus(BonusRecord.add("pure_blood_magic", 100, 0.4)))
					));
					builder.effect(12, () -> List.of(StatusEffectEntry.of(
							BonusStatusEffect.total("pure_blood_speed", 0.5),
							BonusStatusEffect.total("pure_blood_attack", 0.5),
							BonusStatusEffect.total("pure_blood_magic", 0.5)
					)));
				}
				builder.register();
			}
			{
				var sub = root.subSet("pure_blood");
				sub.regSet("Pure Blood").addArmorPreset().register();
			}
			{
				var sub = root.subSet("draining_sentinel");
				sub.regSet("Draining Sentinel").addArmorPreset().register();
			}
		}
	}

	public static void register() {

	}

}