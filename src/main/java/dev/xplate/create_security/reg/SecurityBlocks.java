package dev.xplate.create_security.reg;

import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xplate.create_security.blocks.FiniraniumRelatedBlock;
import dev.xplate.create_security.blocks.SightSensor;
import dev.xplate.create_security.blocks.base.GradientNamedBlock;
import dev.xplate.create_security.blocks.movement.SightSensorMovement;
import dev.xplate.create_security.datagen.blockstate.SightSensorGenerator;
import dev.xplate.create_security.items.FiniraniumRelatedBlockItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.neoforged.neoforge.common.Tags;

import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static dev.xplate.create_security.CSecurity.REG;

public class SecurityBlocks {
    public static final BlockEntry<SightSensor> SIGHT_SENSOR = REG.block("sight_sensor", SightSensor::new)
            .initialProperties(AllBlocks.ANDESITE_CASING::get)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .defaultLoot()
            .lang("Sight Sensor")
            .blockstate(new SightSensorGenerator()::generate)
            .onRegister(movementBehaviour(new SightSensorMovement()))
            .register();

    public static final BlockEntry<FiniraniumRelatedBlock> FINIRANIUM_ORE = REG
            .block("finiranium_ore", FiniraniumRelatedBlock::new)
            .item(FiniraniumRelatedBlockItem::new)
            .build()
            .initialProperties(() -> Blocks.ANCIENT_DEBRIS)
            .properties(p -> p
                    .strength(2f)
                    .emissiveRendering(
                            (b1, b2, b3) -> true)
                    .requiresCorrectToolForDrops()
            )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .lang("Finiranium Ore")
            .loot((lt, b) -> {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(SecurityItems.FINIRANIUM.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })

            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .tag(Tags.Blocks.ORES)
            .tag(Tags.Blocks.END_STONES)
            .tag(Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST)
            .tag(BlockTags.INFINIBURN_END)
            .defaultBlockstate()
            .register();

    public static final BlockEntry<Block> THE_BLOCK = REG
            .block("the_block", Block::new)
            .defaultBlockstate()
            .lang("")
            .loot((lt, t) -> lt.dropOther(t, Items.AIR))
            .register();

    public static void reg() {
    }
}
