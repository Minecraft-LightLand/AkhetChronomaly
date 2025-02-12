package dev.xkmc.akhet_chronomaly.init.registrate;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.akhet_chronomaly.content.client.effect.TabSetEffects;
import dev.xkmc.akhet_chronomaly.content.client.equip.TabEquip;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.data.ACLang;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.resources.ResourceLocation;

public class ACTabRegistry {

	private static final ResourceLocation DUMMY = L2Tabs.loc(AkhetChronomaly.MODID);

	public static final SR<TabToken<?, ?>> TAB_REG = SR.of(AkhetChronomaly.REG, L2Tabs.TABS.key());

	public static Val<TabToken<InvTabData, TabSetEffects>> TAB_SET_EFFECTS = TAB_REG.reg("set_effects",
			() -> L2Tabs.GROUP.registerTab(() -> TabSetEffects::new, ACLang.TAB_SET_EFFECT.get()));

	public static Val<TabToken<InvTabData, TabEquip>> TAB_EQUIP = TAB_REG.reg("equip",
			() -> L2Tabs.GROUP.registerTab(() -> TabEquip::new, ACLang.TAB_EQUIP.get()));

	public static void register() {

	}

	public static void genTabs(RegistrateDataMapProvider pvd) {
		var icon = pvd.builder(L2Tabs.ICON.reg());
		icon.add(TAB_SET_EFFECTS.id(), ACItems.RANDOM[4].get(), false);
		icon.add(TAB_EQUIP.id(), ACItems.RANDOM_SET[4].get(), false);

		var order = pvd.builder(L2Tabs.ORDER.reg());
		order.add(TAB_SET_EFFECTS.id(), 2900, false);
		order.add(TAB_EQUIP.id(), 2100, false);

	}
}
