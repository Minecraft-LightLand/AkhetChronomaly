package dev.xkmc.akhet_chronomaly.content.client.equip;

import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.l2menustacker.init.L2MenuStacker;
import dev.xkmc.l2menustacker.init.MouseCache;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.network.chat.Component;

public class TabEquip extends TabBase<InvTabData, TabEquip> {

	public TabEquip(int index, TabToken<InvTabData, TabEquip> token, TabManager<InvTabData> manager, Component title) {
		super(index, token, manager, title);
	}

	@Override
	public void onTabClicked() {
		MouseCache.cacheMousePos();
		AkhetChronomaly.HANDLER.toServer(new OpenEquipToServer());
	}

}
