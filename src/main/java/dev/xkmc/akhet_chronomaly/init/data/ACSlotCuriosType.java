package dev.xkmc.akhet_chronomaly.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSlot;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.Locale;

public enum ACSlotCuriosType {
	HEAD("artifact_head", 15231, 1),
	NECKLACE("artifact_necklace", 15232, 1),
	BRACELET("artifact_bracelet", 15233, 1),
	BODY("artifact_body", 15234, 1),
	BELT("artifact_belt", -15235, 1);

	final String id;
	public final int priority, count;

	public ArtifactSlot slot;

	ACSlotCuriosType(String id, int priority, int count) {
		this.id = id;
		this.priority = priority;
		this.count = count;
	}

	public String getIdentifier() {
		return this.id;
	}

	public String getDefTranslation() {
		return "Artifact - " + RegistrateLangProvider.toEnglishName(name().toLowerCase(Locale.ROOT));
	}

	public String getDesc() {
		return "curios.identifier." + id;
	}

	public String getModifier() {
		return "curios.modifiers." + id;
	}

	public void buildConfig(CuriosDataProvider cons) {
	}
}