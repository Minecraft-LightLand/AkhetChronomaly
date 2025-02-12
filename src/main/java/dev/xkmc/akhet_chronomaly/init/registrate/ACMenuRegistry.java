package dev.xkmc.akhet_chronomaly.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.akhet_chronomaly.content.client.equip.EquipMenu;
import dev.xkmc.akhet_chronomaly.content.client.equip.EquipScreen;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

public class ACMenuRegistry {

	public static final MenuEntry<EquipMenu> EQUIP = AkhetChronomaly.REGISTRATE.menu("equip",
			EquipMenu::fromNetwork, () -> EquipScreen::new).register();

	public static String getLangKey(MenuType<?> menu) {
		ResourceLocation rl = BuiltInRegistries.MENU.getKey(menu);
		assert rl != null;
		return "container." + rl.getNamespace() + "." + rl.getPath();
	}

	public static void register() {
	}

}
