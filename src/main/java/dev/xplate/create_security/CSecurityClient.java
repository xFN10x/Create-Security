package dev.xplate.create_security;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.xplate.create_security.misc.rendering.FiniraniumGogglesPostProcessingHandler;
import dev.xplate.create_security.ponder.SecurityPonderPlugin;
import dev.xplate.create_security.reg.SecurityEffects;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;

import java.io.IOException;

import static dev.xplate.create_security.CSecurity.LOGGER;

@Mod(value = CSecurity.MODID, dist = Dist.CLIENT)
public class CSecurityClient {
    public static FiniraniumGogglesPostProcessingHandler googlesEffectHandler;

    public CSecurityClient(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(CSecurityClient::onGetPlayerHeartType);
        modEventBus.addListener(CSecurityClient::onClientSetup);
    }

    @SubscribeEvent
    public static void onGetPlayerHeartType(PlayerHeartTypeEvent event) {
        if (event.getEntity().hasEffect(SecurityEffects.END_SICKNESS)) {
            event.setType(SecurityEnumProxies.EndSicknessHeartType.getValue());
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new SecurityPonderPlugin());
        LOGGER.info("Hello from Create Security client!");

        //this thread is not the render thread, so this makes sure that this code does run on it vvvv
        Minecraft.getInstance().execute(() -> {
            try {
                googlesEffectHandler = new FiniraniumGogglesPostProcessingHandler(Minecraft.getInstance());
            } catch (IOException e) {
                LOGGER.error("Failed to make goggles processing handler!", e);
            }
        });
    }

}