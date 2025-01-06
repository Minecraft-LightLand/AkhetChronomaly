package dev.xkmc.akhet_chronomaly.compat;

import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ArtifactJEIPlugin implements IModPlugin {

	public static final ResourceLocation ID = AkhetChronomaly.loc("main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration r) {
	}

}
