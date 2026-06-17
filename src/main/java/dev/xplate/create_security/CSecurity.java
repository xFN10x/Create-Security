package dev.xplate.create_security;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.xplate.create_security.datagen.DataGen;
import dev.xplate.create_security.misc.rendering.FiniraniumGogglesPostProcessingHandler;
import dev.xplate.create_security.ponder.SecurityPonderPlugin;
import dev.xplate.create_security.reg.*;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
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
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.io.IOException;

@Mod(CSecurity.MODID)
public class CSecurity {
    public static final String MODID = "create_security";
    public static final Logger LOGGER = LogUtils.getLogger();
    public final static CreateRegistrate REG = CreateRegistrate.create(MODID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    public static ResourceLocation res(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public CSecurity(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(EventPriority.HIGHEST, DataGen::gatherHigherData);
        modEventBus.addListener(EventPriority.NORMAL, DataGen::gatherData);
        modEventBus.addListener(CSecurity::onCommonSetup);
        //NeoForge.EVENT_BUS.register(this);
        REG.registerEventListeners(modEventBus);

        SecurityFeatures.reg(modEventBus);
        SecurityEffects.reg(modEventBus);
        SecurityItems.reg();
        SecurityBlocks.reg();
        SecurityBlockEntities.reg();
        SecurityCreativeTabs.reg(modEventBus);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Hello from Create Security server!");
    }

}
