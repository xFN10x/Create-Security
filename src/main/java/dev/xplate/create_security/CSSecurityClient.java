package dev.xplate.create_security;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.xplate.create_security.config.enums.EndSicknessWarningLevel;
import dev.xplate.create_security.items.IScrollableItem;
import dev.xplate.create_security.misc.SecurityCommands;
import dev.xplate.create_security.misc.Utils;
import dev.xplate.create_security.misc.rendering.FiniraniumGogglesPostProcessingHandler;
import dev.xplate.create_security.ponder.SecurityPonderPlugin;
import dev.xplate.create_security.reg.SecurityEffects;
import dev.xplate.create_security.reg.SecurityEntityAttachmentTypes;
import net.createmod.catnip.command.CatnipCommands;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.io.IOException;

import static dev.xplate.create_security.CSSecurity.LOGGER;

@EventBusSubscriber(Dist.CLIENT)
@Mod(value = CSSecurity.MODID, dist = Dist.CLIENT)
public class CSSecurityClient {
    public static FiniraniumGogglesPostProcessingHandler googlesEffectHandler;

    public CSSecurityClient(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(CSSecurityClient::onGetPlayerHeartType);
        modEventBus.addListener(CSSecurityClient::onClientSetup);
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

    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> rootCommands = Commands.literal("csecurity")
                .then(SecurityCommands.getEndSicknessBuildup());

        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSourceStack> root = dispatcher.register(rootCommands);
        CatnipCommands.buildRedirect("security", root);
        CatnipCommands.buildRedirect("stealth", root);
        CatnipCommands.buildRedirect("cs", root);
    }

    private static int tickCounter = 0;
    private static Long lastCheck = 0L;

    @SubscribeEvent
    public static void onClientLevelTick(LevelTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        tickCounter++;
        if (tickCounter % 20 != 0) return;
        LocalPlayer plr = mc.player;
        if (plr == null) return;
        long sickness = plr.getData(SecurityEntityAttachmentTypes.END_SICKNESS_COUNTER);
        //plr.sendSystemMessage(Component.literal(Long.toString(sickness)));

        long sicknessThreshold = CSSecurityConfigs.server().endSicknessThreshold.get();
        long warningThreshold = sicknessThreshold/4;
        EndSicknessWarningLevel configedWarningLevel = CSSecurityConfigs.client().endSicknessWarnings.get();
        if (sickness >= warningThreshold && lastCheck < warningThreshold
        && configedWarningLevel.isLevelAtLeast(EndSicknessWarningLevel.EVERY_OTHER)) {
            plr.sendSystemMessage(Utils.createGradiant(Utils.FiniraniumGrad, Component.translatable("chat.end_sick.warning1")));
        } else if (sickness >= (warningThreshold*2) && lastCheck < (warningThreshold*2)
                && configedWarningLevel.isLevelAtLeast(EndSicknessWarningLevel.NORMAL)) {
            plr.sendSystemMessage(Utils.createGradiant(Utils.FiniraniumGrad, Component.translatable("chat.end_sick.warning2")));

        } else if (sickness >= (warningThreshold*3) && lastCheck < (warningThreshold*3)
                && configedWarningLevel.isLevelAtLeast(EndSicknessWarningLevel.LAST)) {
            plr.sendSystemMessage(Utils.createGradiant(Utils.FiniraniumGrad, Component.translatable("chat.end_sick.warning3")));

        } else if (sickness >= (warningThreshold*4) && lastCheck < (warningThreshold*4)
                && configedWarningLevel.isLevelAtLeast(EndSicknessWarningLevel.NONE)) {
            plr.sendSystemMessage(Utils.createGradiant(Utils.FiniraniumGrad, Component.translatable("chat.end_sick.warning4")));
        }
        lastCheck = sickness;
    }

    @SubscribeEvent
    public static void onScroll(ScreenEvent.MouseScrolled.Post event) {
        Screen screen = event.getScreen();
        if (screen instanceof AbstractContainerScreen<?> cs) {
            Slot slot = cs.getSlotUnderMouse();
            if (slot == null) return;
            ItemStack is = slot.getItem();
            Item item = is.getItem();
            if (item instanceof IScrollableItem) {
                double scroll = event.getScrollDeltaY();
                if (scroll > 0) {
                    ((IScrollableItem) item).onScrollUp(is);
                } else if (scroll < 0) {
                    ((IScrollableItem) item).onScrollDown(is);
                }
            }
        }
    }

}