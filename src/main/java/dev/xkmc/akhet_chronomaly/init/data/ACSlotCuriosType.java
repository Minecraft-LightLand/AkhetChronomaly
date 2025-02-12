package dev.xkmc.akhet_chronomaly.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.akhet_chronomaly.content.core.item.ArtifactSlot;

import java.util.Locale;

public enum ACSlotCuriosType {
	HEAD("akhet_head", 15231, 1),
	CAPE("akhet_cape", 15232, 1),
	BODY("akhet_body", 15233, 1),
	BELT("akhet_belt", 15234, 1),
	LEGS("akhet_legs", 15235, 1),
	FEET("akhet_feet", 15236, 1),
	SHOULDER("akhet_shoulder", 15237, 2),
	WRIST("akhet_wrist", 15238, 2),
	HAND("akhet_hand", 15239, 2),
	NECK("akhet_neck", 15240, 1),
	RING("akhet_ring", 15241, 2),
	CHARM("akhet_charm", 15242, 3);

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

}