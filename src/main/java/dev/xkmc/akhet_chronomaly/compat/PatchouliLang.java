package dev.xkmc.akhet_chronomaly.compat;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;

public enum PatchouliLang {
	TITLE("title", "L2Artifacts Guide"),
	LANDING("landing", "Welcome to L2Artifact");

	private final String key, def;

	PatchouliLang(String key, String def) {
		this.key = "patchouli." + AkhetChronomaly.MODID + "." + key;
		this.def = def;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (PatchouliLang lang : PatchouliLang.values()) {
			pvd.add(lang.key, lang.def);
		}
	}

}
