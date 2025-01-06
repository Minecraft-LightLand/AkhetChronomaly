package dev.xkmc.akhet_chronomaly.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.akhet_chronomaly.content.client.select.ChooseArtifactToServer;
import dev.xkmc.akhet_chronomaly.content.config.StatType;
import dev.xkmc.akhet_chronomaly.events.ArtifactAttackListener;
import dev.xkmc.akhet_chronomaly.init.data.*;
import dev.xkmc.akhet_chronomaly.init.data.loot.ArtifactGLMProvider;
import dev.xkmc.akhet_chronomaly.init.data.loot.ArtifactLootGen;
import dev.xkmc.akhet_chronomaly.init.registrate.*;
import dev.xkmc.akhet_chronomaly.init.registrate.entries.ArtifactRegistrate;
import dev.xkmc.l2core.init.L2TagGen;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AkhetChronomaly.MODID)
@EventBusSubscriber(modid = AkhetChronomaly.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AkhetChronomaly {

	public static final String MODID = "akhet_chronomaly";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final ArtifactRegistrate REGISTRATE = new ArtifactRegistrate();

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 1,
			e -> e.create(ChooseArtifactToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER)
	);


	public AkhetChronomaly() {
		ArtifactTypeRegistry.register();
		ArtifactItems.register();
		ArtifactMenuRegistry.register();
		ArtifactEffects.register();
		ArtifactTabRegistry.register();
		ArtifactConfig.init();
		Handlers.registerReg(StatType.class, ArtifactTypeRegistry.STAT_TYPE.key());

		AttackEventHandler.register(3000, new ArtifactAttackListener());
	}

	@SubscribeEvent
	public static void commonInit(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}


	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		ConfigGen.register();
		REGISTRATE.addDataGenerator(ProviderType.LANG, ArtifactLang::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, ArtifactLootGen::onLootGen);
		REGISTRATE.addDataGenerator(L2TagGen.EFF_TAGS, ArtifactTagGen::onEffectTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, ArtifactTagGen::onEntityTypeGen);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, AkhetChronomaly::onDataMapGen);
		var init = REGISTRATE.getDataGenInitializer();
		init.add(ArtifactTypeRegistry.STAT_TYPE.key(), ConfigGen::genSlotType);

		var run = event.includeServer();
		var gen = event.getGenerator();
		var reg = event.getLookupProvider();
		var out = gen.getPackOutput();
		var file = event.getExistingFileHelper();

		gen.addProvider(run, new SlotGen(MODID, out, file, reg));
		gen.addProvider(run, new ArtifactGLMProvider(out, reg));
	}

	private static void onDataMapGen(RegistrateDataMapProvider pvd) {
		ArtifactTabRegistry.genTabs(pvd);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
