package dev.xplate.create_security;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.xplate.create_security.config.CSSecServer;
import dev.xplate.create_security.datagen.CSSDataGen;
import dev.xplate.create_security.items.FiniraniumRelatedItem;
import dev.xplate.create_security.misc.IEndSickining;
import dev.xplate.create_security.reg.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@EventBusSubscriber()
@Mod(CSSecurity.MODID)
public class CSSecurity {
    public static final String MODID = "create_security";
    public static final Logger LOGGER = LogUtils.getLogger();
    public final static CreateRegistrate REG = CreateRegistrate.create(MODID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    public static ResourceLocation res(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public CSSecurity(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(EventPriority.HIGHEST, CSSDataGen::gatherHigherData);
        modEventBus.addListener(EventPriority.NORMAL, CSSDataGen::gatherData);
        modEventBus.addListener(CSSecurity::onCommonSetup);
        //NeoForge.EVENT_BUS.register(this);
        REG.registerEventListeners(modEventBus);

        SecurityFeatures.reg(modEventBus);
        SecurityEffects.reg(modEventBus);
        SecurityItems.reg();
        SecurityBlocks.reg();
        SecurityBlockEntities.reg();
        SecurityCreativeTabs.reg(modEventBus);
        SecurityEntityAttachmentTypes.reg(modEventBus);
        SecurityFluids.reg();
        SecurityPartialModels.reg();
        SecurityItemComponents.reg(modEventBus);

        CSSecurityConfigs.register(modContainer);
    }

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onWorldTick(ServerTickEvent.Pre event) {
        Iterable<ServerLevel> slevs = event.getServer().getAllLevels();
        tickCounter++;
        CSSecServer serverConfig = CSSecurityConfigs.server();
        boolean endSicknessEnabled = serverConfig.endSicknessEnabled.get();
        int everyXTick = serverConfig.endSicknessTickRate.get();
        int decreaseAmount = serverConfig.endSicknessDecreaseRate.get();
        if (!endSicknessEnabled || tickCounter % everyXTick != 0) {
            return;
        }
        tickCounter = 0;
        slevs.forEach(slev -> {
            //you dont get end sickness in the nether
            if (slev.dimension() != Level.NETHER)
                slev.getEntities().getAll().forEach(e -> {
                    if (!(e instanceof EnderMan || e instanceof EnderDragon) && e instanceof LivingEntity le) {
                        BlockPos entityPos = BlockPos.containing(le.getPosition(.5f));
                        BlockPos firstCorner = entityPos.above(8).west(8).north(8);
                        BlockPos secondCorner = entityPos.below(8).east(8).south(8);

                        Stream<BlockPos> stream = BlockPos.betweenClosedStream(firstCorner, secondCorner);
                        Stream<BlockPos> finiraniumBlocks = stream.filter((bp) -> (slev.getBlockState(bp).getBlock() instanceof IEndSickining));

                        final AtomicBoolean didAnything = new AtomicBoolean(false);
                        final AtomicReference<Long> sick = new AtomicReference<>(le.getData(SecurityEntityAttachmentTypes.END_SICKNESS_COUNTER));
                        sick.set(le.getData(SecurityEntityAttachmentTypes.END_SICKNESS_COUNTER));

                        finiraniumBlocks.forEach(bp -> {
                            BlockState bs = slev.getBlockState(bp);
                            IEndSickining block = (IEndSickining) bs.getBlock();
                            sick.set(sick.get() + (block.sickAmount() * everyXTick));
                            didAnything.set(true);
                        });
                        if (serverConfig.endSicknessEnabledInInventory.get()) {
                            if (le instanceof InventoryCarrier inventoryCarrier) {
                                for (ItemStack item : inventoryCarrier.getInventory().getItems()) {
                                    if (item.getItem() instanceof IEndSickining it) {
                                        sick.set(sick.get() + (it.sickAmount() * everyXTick / 2) * (item.getCount() / 2));
                                    }
                                }
                            }
//we have to add another check here because mojang decided players are special
                            if (le instanceof Player plr) {
                                for (ItemStack item : plr.getInventory().items) {
                                    if (item.getItem() instanceof IEndSickining it) {
                                        sick.set(sick.get() + (it.sickAmount() * everyXTick) * item.getCount());
                                    }
                                }
                            }
                        }
//TODO: make the level configurable
                        long sicknessThreshold = 40000;
                        long sicknessLevelThreshold = 20000;
                        if (sick.get() > sicknessThreshold) {
                            int sickLevel = Math.toIntExact((sick.get() - sicknessThreshold) / sicknessLevelThreshold);
                            le.addEffect(new MobEffectInstance(SecurityEffects.END_SICKNESS, 20 * (60 * 2), sickLevel));
                        }
                        if (!didAnything.get())
                            le.setData(SecurityEntityAttachmentTypes.END_SICKNESS_COUNTER, Math.max(sick.get() - (decreaseAmount * everyXTick), 0));
                        else
                            le.setData(SecurityEntityAttachmentTypes.END_SICKNESS_COUNTER, sick.get());
                    }
                });
        });
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Hello from Create Security server!");
    }

}
