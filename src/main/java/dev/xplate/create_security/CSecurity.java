package dev.xplate.create_security;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xplate.create_security.datagen.DataGen;
import dev.xplate.create_security.ponder.SecurityPonderPlugin;
import dev.xplate.create_security.reg.SecurityBlockEntities;
import dev.xplate.create_security.reg.SecurityBlocks;
import dev.xplate.create_security.reg.SecurityItems;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CSecurity.MODID)
public class CSecurity {
    public static final String MODID = "create_security";
    public static final Logger LOGGER = LogUtils.getLogger();
    public final static CreateRegistrate REG = CreateRegistrate.create(MODID);

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = REG.defaultCreativeTab("create_security_tab",
                    t -> CreativeModeTab.builder()
                            .title(Component.translatable("creativeTab.create_security_tab"))
                            .icon(() -> SecurityBlocks.SIGHT_SENSOR.asItem().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(SecurityBlocks.SIGHT_SENSOR.asItem().getDefaultInstance());
                            }))
            .register();
//    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB =
//            CREATIVE_MODE_TABS
//                    .register("example_tab", () -> CreativeModeTab.builder()
//                            .title(Component.translatable("itemGroup.create_security"))
//                            .withTabsBefore(CreativeModeTabs.COMBAT)
//                            .icon(() -> SecurityBlocks.SIGHT_SENSOR.asItem().getDefaultInstance())
//                            .displayItems((parameters, output) -> {
//                                output.accept(SecurityBlocks.SIGHT_SENSOR.asItem().getDefaultInstance());
//    }).build());

    public CSecurity(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        REG.registerEventListeners(modEventBus);
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        SecurityItems.reg();
        SecurityBlocks.reg();
        SecurityBlockEntities.reg();

        modEventBus.addListener(EventPriority.HIGHEST, DataGen::gatherHigherData);
        modEventBus.addListener(EventPriority.NORMAL, DataGen::gatherData);

        //modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PonderIndex.addPlugin(new SecurityPonderPlugin());

            LOGGER.info("Hello from Create Security client!");
        }
    }
}
