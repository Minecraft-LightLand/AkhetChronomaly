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
	SHIFT_TEXT("tooltip.shift", "Hold Shift for set effects", 0, ChatFormatting.DARK_AQUA),

	TITLE_SELECT_SET("title.select_set", "Select Artifact Set"),
	TITLE_SELECT_SLOT("title.select_slot", "Select Artifact Slot"),
	TITLE_SELECT_RANK("title.select_rank", "Select Artifact Rank"),

	TAB_SET_EFFECT("menu.tabs.set_effects", "Activated Set Effects"),
	TAB_EQUIP("menu.tabs.equip", "Akhet Chronomaly"),

	LOOT_POOL("tooltip.loot_pool", "Possible Sets:", 0, ChatFormatting.YELLOW),
	LOOT_POOL_ALL("tooltip.loot_pool_all", "Possible Sets: All Sets", 0, ChatFormatting.YELLOW),


	SET("set.set_name", "Set: %s", 1, ChatFormatting.GRAY),
	EFFECT_COUNT("set.effect_count", "%s: %s piece(s)", 2, ChatFormatting.GOLD),
	COMBINE("set.combine", "%s: %s", 2, ChatFormatting.GRAY),
	CONDITION("set.condition", "%s:", 1, ChatFormatting.GRAY),

	PHP_LOW("effect.predicate.php_low", "When your HP is below %s", 1, ChatFormatting.GRAY),
	PHP_HIGH("effect.predicate.php_high", "When your HP is above %s", 1, ChatFormatting.GRAY),
	PHP_MID("effect.predicate.php_mid", "When your HP is between %s ~ %s", 2, ChatFormatting.GRAY),

	HEAL_HIT("effect.trigger.heal_hit", "Heal %s of the damage inflicted", 1, ChatFormatting.GRAY);

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;

	ACLang(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = AkhetChronomaly.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	ACLang(String key, String def, int arg) {
		this(key, def, arg, null);
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


	public static MutableComponent num(int val) {
		return Component.literal("" + val).withStyle(ChatFormatting.DARK_AQUA);
	}


	public static MutableComponent decimal(double val) {
		if (Math.abs(val - Math.round(val)) < 1e-3) {
			return num((int) Math.round(val));
		}
		return Component.literal("" + val).withStyle(ChatFormatting.DARK_AQUA);
	}

	public static MutableComponent perc(double val) {
		return Component.literal(Math.round(val * 100) + "%").withStyle(ChatFormatting.DARK_AQUA);
	}

	public static MutableComponent percSmall(double val) {
		return Component.literal(val * 100 + "%").withStyle(ChatFormatting.DARK_AQUA);
	}

}
