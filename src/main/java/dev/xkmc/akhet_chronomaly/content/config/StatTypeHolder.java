package dev.xkmc.akhet_chronomaly.content.config;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record StatTypeHolder(Holder<StatType> holder) {

	public ResourceLocation icon() {
		var override = holder.value().icon();
		if (override != null) return override;
		return getID().withPath(e -> "textures/stat_type/" + e + ".png");
	}

	public MutableComponent getDesc() {
		return Component.translatable(getID().toLanguageKey("stat_type"));
	}


	public StatType value() {
		return holder.value();
	}

	public ResourceLocation getID() {
		return holder.unwrapKey().orElseThrow().location();
	}

}
