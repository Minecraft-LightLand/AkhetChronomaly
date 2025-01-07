package dev.xkmc.akhet_chronomaly.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.akhet_chronomaly.content.config.SetGroup;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactStats;
import dev.xkmc.akhet_chronomaly.content.misc.RandomArtifactItem;
import dev.xkmc.akhet_chronomaly.content.misc.RandomArtifactSetItem;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static dev.xkmc.akhet_chronomaly.init.AkhetChronomaly.REGISTRATE;

@SuppressWarnings({"raw_type", "unchecked"})
public class ACItems {

	public static final String[] RANK_NAME = {" -Common-", " =Rare=", " >Epic<", " »Legendary«", " -»Godly«-"};

	static {
		REGISTRATE.buildL2CreativeTab("artifacts", "L2 Artifacts", b -> b
				.icon(ACItems.RANDOM[4]::asStack));
	}

	public static final ItemEntry<RandomArtifactItem>[] RANDOM;
	public static final ItemEntry<RandomArtifactSetItem>[] RANDOM_SET;

	private static final DCReg DC = DCReg.of(AkhetChronomaly.REG);
	public static final DCVal<Integer> EXP = DC.intVal("experience");
	public static final DCVal<String> STAT = DC.str("stat");
	public static final DCVal<ArtifactStats> STATS = DC.reg("stats", ArtifactStats.class, false);
	public static final DCVal<SetGroup> GROUP = DC.reg("set_group", SetGroup.class, true);

	static {
		int n = 5;
		RANDOM = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			TagKey<Item> artifact = ItemTags.create(AkhetChronomaly.loc("artifact"));
			TagKey<Item> rank_tag = ItemTags.create(AkhetChronomaly.loc("rank_" + r));
			RANDOM[i] = REGISTRATE.item("random_" + r, p -> new RandomArtifactItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", AkhetChronomaly.loc("item/rank/" + r))
							.texture("layer1", AkhetChronomaly.loc("item/random")))
					.tag(rank_tag, artifact)
					.lang("Random Artifact" + RANK_NAME[i]).register();
		}
		RANDOM_SET = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			RANDOM_SET[i] = REGISTRATE.item("random_set_" + r, p -> new RandomArtifactSetItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", AkhetChronomaly.loc("item/rank/" + r))
							.texture("layer1", AkhetChronomaly.loc("item/random_set")))
					.lang("Random Artifact Set" + RANK_NAME[i]).register();
		}
	}

	public static void register() {

	}

}