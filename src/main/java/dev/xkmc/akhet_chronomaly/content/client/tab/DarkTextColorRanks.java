package dev.xkmc.akhet_chronomaly.content.client.tab;

import net.minecraft.ChatFormatting;

public enum DarkTextColorRanks {
	WHITE(ChatFormatting.DARK_GRAY, ChatFormatting.BLACK),
	GREEN(ChatFormatting.GREEN, ChatFormatting.DARK_GREEN),
	BLUE(ChatFormatting.BLUE, ChatFormatting.DARK_BLUE),
	PURPLE(ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_PURPLE),
	GOLD(ChatFormatting.RED, ChatFormatting.DARK_RED);

	public static ChatFormatting getLight() {
		return values()[0].light;
	}

	public static ChatFormatting getDark() {
		return values()[0].dark;
	}

	public final ChatFormatting light, dark;

	DarkTextColorRanks(ChatFormatting light, ChatFormatting dark) {
		this.light = light;
		this.dark = dark;
	}
}
