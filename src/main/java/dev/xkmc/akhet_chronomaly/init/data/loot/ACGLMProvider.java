package dev.xkmc.akhet_chronomaly.init.data.loot;

import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class ACGLMProvider extends GlobalLootModifierProvider {

	public ACGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, AkhetChronomaly.MODID);
	}

	@Override
	protected void start() {

	}
}
