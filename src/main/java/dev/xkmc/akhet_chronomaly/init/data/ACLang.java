package dev.xkmc.akhet_chronomaly.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.Locale;

public enum ACLang {
	RAW_ARTIFACT("tooltip.raw_artifact", "Right Click to Reveal Stats"),
	ARTIFACT_LEVEL("tooltip.artifact_level", "Level: %s", 1, null),
	UPGRADE("tooltip.upgrade", "Right Click to Reveal Upgrade Result", 0, ChatFormatting.GOLD),
	STAT("tooltip.sub_stat", "Stats", 0, ChatFormatting.GRAY),
	SHIFT_TEXT("tooltip.shift", "Hold Shift for set effects", 0, ChatFormatting.AQUA),
	SET("tooltip.set", "Set: %s", 1, null),
	ALL_SET_EFFECTS("set.all_set_effects", "%s: %s piece(s)", 2, null),

	TITLE_SELECT_SET("title.select_set", "Select Artifact Set"),
	TITLE_SELECT_SLOT("title.select_slot", "Select Artifact Slot"),
	TITLE_SELECT_RANK("title.select_rank", "Select Artifact Rank"),

	TAB_SET_EFFECT("menu.tabs.set_effects", "Activated Set Effects"),
	TAB_EQUIP("menu.tabs.equip", "Akhet Chronomaly"),

	LOOT_POOL("tooltip.loot_pool", "Possible Sets:", 0, ChatFormatting.YELLOW),
	LOOT_POOL_ALL("tooltip.loot_pool_all", "Possible Sets: All Sets", 0, ChatFormatting.YELLOW),

	;

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;

	ACLang(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = AkhetChronomaly.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	ACLang(String key, String def) {
		this(key, def, 0, null);
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public static MutableComponent getTranslate(String s, Object... args) {
		return Component.translatable(AkhetChronomaly.MODID + "." + s, args);
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		MutableComponent ans = Component.translatable(key, args);
		if (format != null) {
			return ans.withStyle(format);
		}
		return ans;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (ACLang lang : ACLang.values()) {
			pvd.add(lang.key, lang.def);
		}
		for (ACSlotCuriosType type : ACSlotCuriosType.values()) {
			pvd.add(type.getDesc(), type.getDefTranslation());
			pvd.add(type.getModifier(), "When equipped as " + type.getDefTranslation() + ":");
		}
	}

}
