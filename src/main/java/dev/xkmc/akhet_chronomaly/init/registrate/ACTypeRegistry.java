package dev.xkmc.akhet_chronomaly.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.akhet_chronomaly.content.config.SlotStatMap;
import dev.xkmc.akhet_chronomaly.content.config.StatType;
import dev.xkmc.akhet_chronomaly.content.core.*;
import dev.xkmc.akhet_chronomaly.init.AkhetChronomaly;
import dev.xkmc.akhet_chronomaly.init.data.ACSlotCuriosType;
import dev.xkmc.l2core.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.datapack.DatapackReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.AttReg;
import dev.xkmc.l2core.init.reg.simple.AttVal;

import static dev.xkmc.akhet_chronomaly.init.AkhetChronomaly.REGISTRATE;

public class ACTypeRegistry {

	public static final L2Registrate.RegistryInstance<ArtifactSlot> SLOT = REGISTRATE.newRegistry("slot", ArtifactSlot.class);
	public static final L2Registrate.RegistryInstance<ArtifactSet> SET = REGISTRATE.newRegistry("set", ArtifactSet.class);

	public static final DatapackReg<StatType> STAT_TYPE = AkhetChronomaly.REG.dataReg("stat_type", StatType.class);
	public static final DatapackReg<SetEffect> SET_EFFECT = AkhetChronomaly.REG.dataReg("set_effect", SetEffect.class);

	public static final DataMapReg<ArtifactSlot, SlotStatMap> STAT_MAP = AkhetChronomaly.REG.dataMap("stat_map", SLOT.key(), SlotStatMap.class);
	public static final DataMapReg<ArtifactSet, SetConfig> SET_CONFIG = AkhetChronomaly.REG.dataMap("set_config", SET.key(), SetConfig.class);

	public static final AttVal.PlayerVal<AkhetCapability> CAP = AttReg.of(AkhetChronomaly.REG)
			.player("main", AkhetCapability.class, AkhetCapability::new, PlayerCapabilityNetworkHandler::new);

	public static final SimpleEntry<ArtifactSlot> SLOT_HEAD = regSlot("head", () -> new ArtifactSlot(ACSlotCuriosType.HEAD));
	public static final SimpleEntry<ArtifactSlot> SLOT_BODY = regSlot("body", () -> new ArtifactSlot(ACSlotCuriosType.BODY));
	public static final SimpleEntry<ArtifactSlot> SLOT_CAPE = regSlot("cape", () -> new ArtifactSlot(ACSlotCuriosType.CAPE));
	public static final SimpleEntry<ArtifactSlot> SLOT_BELT = regSlot("belt", () -> new ArtifactSlot(ACSlotCuriosType.BELT));
	public static final SimpleEntry<ArtifactSlot> SLOT_LEGS = regSlot("legs", () -> new ArtifactSlot(ACSlotCuriosType.LEGS));
	public static final SimpleEntry<ArtifactSlot> SLOT_FEET = regSlot("feet", () -> new ArtifactSlot(ACSlotCuriosType.FEET));
	public static final SimpleEntry<ArtifactSlot> SLOT_SHOULDER = regSlot("shoulder", () -> new ArtifactSlot(ACSlotCuriosType.SHOULDER));
	public static final SimpleEntry<ArtifactSlot> SLOT_WRIST = regSlot("wrist", () -> new ArtifactSlot(ACSlotCuriosType.WRIST));
	public static final SimpleEntry<ArtifactSlot> SLOT_HAND = regSlot("hand", () -> new ArtifactSlot(ACSlotCuriosType.HAND));
	public static final SimpleEntry<ArtifactSlot> SLOT_NECK = regSlot("neck", () -> new ArtifactSlot(ACSlotCuriosType.NECK));
	public static final SimpleEntry<ArtifactSlot> SLOT_RING = regSlot("ring", () -> new ArtifactSlot(ACSlotCuriosType.RING));

	public static void register() {

	}

	private static SimpleEntry<ArtifactSlot> regSlot(String id, NonNullSupplier<ArtifactSlot> slot) {
		return new SimpleEntry<>(REGISTRATE.generic(SLOT, id, slot).defaultLang().register());
	}

}
