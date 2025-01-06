package dev.xkmc.akhet_chronomaly.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.akhet_chronomaly.content.config.ArtifactSetConfig;
import dev.xkmc.akhet_chronomaly.content.config.LinearParam;
import dev.xkmc.akhet_chronomaly.content.config.StatType;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSet;
import dev.xkmc.akhet_chronomaly.content.core.ArtifactSlot;
import dev.xkmc.akhet_chronomaly.content.core.LinearFunc;
import dev.xkmc.akhet_chronomaly.content.set.core.SetEffect;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.datapack.DatapackReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;

import static dev.xkmc.akhet_chronomaly.init.AkhetChronomaly.REGISTRATE;

public class ArtifactTypeRegistry {

	public static final L2Registrate.RegistryInstance<ArtifactSlot> SLOT = REGISTRATE.newRegistry("slot", ArtifactSlot.class);
	public static final L2Registrate.RegistryInstance<ArtifactSet> SET = REGISTRATE.newRegistry("set", ArtifactSet.class);
	public static final L2Registrate.RegistryInstance<SetEffect> SET_EFFECT = REGISTRATE.newRegistry("set_effect", SetEffect.class);
	public static final L2Registrate.RegistryInstance<LinearFunc> LINEAR = REGISTRATE.newRegistry("linear", LinearFunc.class);

	public static final DatapackReg<StatType> STAT_TYPE = AkhetChronomaly.REG.dataReg("stat_type", StatType.class);

	public static final DataMapReg<LinearFunc, LinearParam> LINEAR_CONFIG = AkhetChronomaly.REG.dataMap("linear", LINEAR.key(), LinearParam.class);
	public static final DataMapReg<ArtifactSet, ArtifactSetConfig> ARTIFACT_SETS = AkhetChronomaly.REG.dataMap("artifact_sets", SET.key(), ArtifactSetConfig.class);

	public static final SimpleEntry<ArtifactSlot> SLOT_HEAD = regSlot("head", () -> new ArtifactSlot(ArtifactSlotCuriosType.HEAD));
	public static final SimpleEntry<ArtifactSlot> SLOT_NECKLACE = regSlot("necklace", () -> new ArtifactSlot(ArtifactSlotCuriosType.NECKLACE));
	public static final SimpleEntry<ArtifactSlot> SLOT_BRACELET = regSlot("bracelet", () -> new ArtifactSlot(ArtifactSlotCuriosType.BRACELET));
	public static final SimpleEntry<ArtifactSlot> SLOT_BODY = regSlot("body", () -> new ArtifactSlot(ArtifactSlotCuriosType.BODY));
	public static final SimpleEntry<ArtifactSlot> SLOT_BELT = regSlot("belt", () -> new ArtifactSlot(ArtifactSlotCuriosType.BELT));

	public static void register() {

	}

	private static SimpleEntry<ArtifactSlot> regSlot(String id, NonNullSupplier<ArtifactSlot> slot) {
		return new SimpleEntry<>(REGISTRATE.generic(SLOT, id, slot).defaultLang().register());
	}

}
