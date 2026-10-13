package dev.xplate.create_security;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.xplate.create_security.config.CSSecServer;
import dev.xplate.create_security.datagen.CSSDataGen;
import dev.xplate.create_security.datagen.provider.tags.CSSEntityTagsProvider;
import dev.xplate.create_security.misc.IEndSickining;
import dev.xplate.create_security.reg.*;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@EventBusSubscriber()
@Mod(CSSecurity.MODID)
//TODO: make finiranium block entity for finiranium blocks
public class CSSecurity {
    public static final String MODID = "create_security";
    public static final Logger LOGGER = LogUtils.getLogger();
    public final static CreateRegistrate REG = CreateRegistrate.create(MODID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null).setTooltipModifierFactory(item ->
            new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item))));

    public static ResourceLocation res(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public CSSecurity(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(EventPriority.HIGHEST, CSSDataGen::gatherHigherData);
        modEventBus.addListener(EventPriority.NORMAL, CSSDataGen::gatherData);
        //NeoForge.EVENT_BUS.register(this);
        REG.registerEventListeners(modEventBus);

        SecurityFeatures.reg(modEventBus);
        SecurityEffects.reg(modEventBus);
        SecurityItems.reg();
        SecurityBlocks.reg();
        SecurityBlockEntities.reg();
        SecurityCreativeTabs.reg(modEventBus);
        SecurityAttachmentTypes.reg(modEventBus);
        SecurityFluids.reg();
        SecurityPartialModels.reg();
        SecurityItemComponents.reg(modEventBus);
        SecuritySoundEvents.reg(modEventBus);
        SecurityMenus.reg();
        SecurityDamageTypes.reg();

        CSSecurityConfigs.register(modContainer);
    }

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        MinecraftServer server = event.getServer();
        Iterable<ServerLevel> slevs = server.getAllLevels();
        tickCounter++;
        CSSecServer serverConfig = CSSecurityConfigs.server();
        boolean endSicknessEnabled = serverConfig.endSicknessEnabled.get();
        int everyXTick = serverConfig.endSicknessTickRate.get();
        int decreaseAmount = serverConfig.endSicknessDecreaseRate.get();
        if (!endSicknessEnabled || tickCounter % everyXTick != 0) {
            return;
        }
        tickCounter = 0;
        for (ServerLevel slev : slevs) {//you dont get end sickness in the nether
            ///end sickness
            if (slev.dimension() != Level.NETHER) {
                slev.getEntities().getAll().forEach(e -> {
                    if (e.getType().getTags().noneMatch(tag -> tag.equals(CSSEntityTagsProvider.IMMUNE_TO_END_SICKNESS)) && e instanceof LivingEntity le) {
                        //region end sickining blocks
                        BlockPos entityPos = BlockPos.containing(le.getPosition(.5f));
                        BlockPos firstCorner = entityPos.above(8).west(8).north(8);
                        BlockPos secondCorner = entityPos.below(8).east(8).south(8);

                        Stream<BlockPos> stream = BlockPos.betweenClosedStream(firstCorner, secondCorner);
                        Stream<BlockPos> finiraniumBlocks = stream.filter((bp) -> (slev.getBlockState(bp).getBlock() instanceof IEndSickining));

                        final AtomicBoolean didAnything = new AtomicBoolean(false);
                        final AtomicReference<Long> sick = new AtomicReference<>(le.getData(SecurityAttachmentTypes.END_SICKNESS_COUNTER));
                        sick.set(le.getData(SecurityAttachmentTypes.END_SICKNESS_COUNTER));

                        finiraniumBlocks.forEach(bp -> {
                            BlockState bs = slev.getBlockState(bp);
                            IEndSickining block = (IEndSickining) bs.getBlock();
                            sick.set(sick.get() + (block.sickAmount() * everyXTick));
                            didAnything.set(true);
                        });
                        //endregion
                        if (serverConfig.endSicknessEnabledInInventory.get()) {

                            Container inventory = null;
                            if (le instanceof InventoryCarrier inventoryCarrier) {
                                inventory = inventoryCarrier.getInventory();
                            } else if (le instanceof Player plr) {
//we have to add another check here because mojang decided players are special
                                inventory = plr.getInventory();
                            }
                            if (inventory != null) {
                                inventory.hasAnyMatching(stack -> {
                                    if (stack.getItem() instanceof IEndSickining it) {
                                        sick.set(sick.get() + (it.sickAmount() * everyXTick / 2) * (stack.getCount() / 2));
                                        return true;
                                    }
                                    return false;
                                });
                            }
                        }

                        long sicknessThreshold = serverConfig.endSicknessThreshold.get();
                        long sicknessLevelThreshold = serverConfig.endSicknessLevelThreshold.get();
                        if (sick.get() > sicknessThreshold) {
                            int sickLevel = Math.toIntExact((sick.get() - sicknessThreshold) / sicknessLevelThreshold);
                            le.addEffect(new MobEffectInstance(SecurityEffects.END_SICKNESS, 20 * 15, sickLevel));
                        }
                        if (!didAnything.get())
                            le.setData(SecurityAttachmentTypes.END_SICKNESS_COUNTER, Math.max(sick.get() - ((long) decreaseAmount * everyXTick), 0));
                        else
                            le.setData(SecurityAttachmentTypes.END_SICKNESS_COUNTER, sick.get());
                    }

                });
            }
            ///check for blocks that are affected by finiranium
            for (ChunkHolder chunkHolder : slev.getChunkSource().chunkMap.getChunks()) {
                ChunkAccess chunk = chunkHolder.getLatestChunk();
                if (chunk != null)
                    chunk.findBlocks(bs -> bs.getBlock() instanceof IEndSickining, (endSickeningPos, endSickeningState) -> {
                        IEndSickining block = (IEndSickining) endSickeningState.getBlock();
                        int range = block.sickRange() / 2;
                        Stream<BlockPos> blocks = BlockPos.betweenClosedStream(endSickeningPos.above(range).west(range).north(range), endSickeningPos.below(range).east(range).south(range));
                        Stream<BlockPos> testBlocks = blocks.filter(bp0 -> chunk.getBlockState(bp0).is(SecurityBlocks.THE_BLOCK));
                        testBlocks.forEach(bp0 -> {
                            double dist = bp0.getCenter().distanceTo(endSickeningPos.getCenter());
                            double multi = Math.max(0, 1 - dist / range);
                            if (multi == 0) return;
                            double sickness = block.sickAmount() * multi;
                            //chunk.getBlockState(bp0)
                            Map<BlockPos, Long> sicknessData = new HashMap<>(chunk.getData(SecurityAttachmentTypes.END_SICKNESS_COUNTER_CHUNK));
                            double sickNow = sicknessData.getOrDefault(bp0, 0L) + sickness;
                            sicknessData.put(bp0, Math.round(sickNow));
                            chunk.setData(SecurityAttachmentTypes.END_SICKNESS_COUNTER_CHUNK, sicknessData);
                            //CSSecurity.LOGGER.debug(String.valueOf(sickNow));
                        });
                    });
            }
        }


    }
}
