package dev.xkmc.akhet_chronomaly.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.akhet_chronomaly.content.core.BaseArtifact;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.registrate.entries.SetEntry;
import dev.xkmc.l2core.serial.configval.BooleanValueCondition;
import dev.xkmc.l2core.serial.recipe.ConditionalRecipeWrapper;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {

		// rank up recipes
		for (SetEntry<?> set : AkhetChronomaly.REGISTRATE.SET_LIST) {
			ItemEntry<BaseArtifact>[][] items = set.items;
			for (ItemEntry<BaseArtifact>[] slot : items) {
				int n = slot.length;
				for (int i = 1; i < n; i++) {
					BaseArtifact input = slot[i - 1].get();
					BaseArtifact output = slot[i].get();
					unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 1)::unlockedBy, input)
							.requires(input, 2)
							.save(ConditionalRecipeWrapper.of(pvd, BooleanValueCondition.of(ArtifactConfig.COMMON,
											e -> e.enableArtifactRankUpRecipe, true)),
									slot[i].getId().withPrefix("rank_up/"));
				}
			}
		}
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}

}
