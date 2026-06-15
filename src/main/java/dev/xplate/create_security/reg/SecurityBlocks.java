package dev.xplate.create_security.reg;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xplate.create_security.blocks.FiniraniumOre;
import dev.xplate.create_security.blocks.SightSensor;
import dev.xplate.create_security.datagen.blockstate.SightSensorGenerator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.Tags;

import static dev.xplate.create_security.CSecurity.REG;

public class SecurityBlocks {
    public static final BlockEntry<SightSensor> SIGHT_SENSOR = REG.block("sight_sensor", SightSensor::new)
            .initialProperties(AllBlocks.ANDESITE_CASING::get)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .simpleItem()
            .defaultLoot()
            .lang("Sight Sensor")
            .blockstate(new SightSensorGenerator()::generate)
            .register();
    public static final BlockEntry<FiniraniumOre> FINIRANIUM_ORE = REG
            .block("finiranium_ore", FiniraniumOre::new)
            .simpleItem()
            .initialProperties(() -> Blocks.ANCIENT_DEBRIS)
            .properties(p -> p
                    .strength(2f)
                    .emissiveRendering(
                            (b1,b2,b3) -> true)
                    .requiresCorrectToolForDrops()
                    )
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .lang("Finiranium Ore")
            .loot((lt, b) ->  {
                HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lt.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
                lt.add(b,
                        lt.createSilkTouchDispatchTable(b,
                                lt.applyExplosionDecay(b, LootItem.lootTableItem(SecurityItems.FINIRANIUM.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE))))));
            })
            .defaultBlockstate()
            .register();

    public static void reg() {}
}
