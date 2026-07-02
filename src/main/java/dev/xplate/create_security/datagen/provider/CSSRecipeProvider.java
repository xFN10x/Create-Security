package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.xplate.create_security.reg.SecurityBlocks;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class CSSRecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public CSSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SecurityBlocks.SIGHT_SENSOR)
                .pattern(" g ")
                .pattern("rer")
                .pattern("aoa")

                .define('a', AllBlocks.ANDESITE_CASING)
                .define('o', Blocks.OBSIDIAN)
                .define('e', Items.ENDER_EYE)
                .define('r', Blocks.REDSTONE_WIRE)
                .define('g', Blocks.GLASS)

                .unlockedBy("has_self", has(SecurityBlocks.SIGHT_SENSOR))
                .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))

                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SecurityBlocks.LASER_DIODE,2)
                .pattern("ggg")
                .pattern("rmr")
                .pattern("asa")

                .define('g', Blocks.GLASS)
                .define('r', Blocks.REDSTONE_WIRE)
                .define('m', Blocks.AMETHYST_BLOCK)
                .define('a', AllBlocks.ANDESITE_CASING)
                .define('s', AllBlocks.SHAFT)

                .unlockedBy("has_self", has(SecurityBlocks.LASER_DIODE))
                .unlockedBy("has_amethystBlock", has(Blocks.AMETHYST_BLOCK))
                .unlockedBy("has_amethystShard", has(Items.AMETHYST_SHARD))

                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SecurityItems.KEYCARD)
                .requires(AllItems.STURDY_SHEET)
                .requires(AllItems.BRASS_NUGGET)
                .requires(AllItems.CARDBOARD)

                .unlockedBy("has_self", has(SecurityItems.KEYCARD))
                .unlockedBy("has_cardboard", has(AllItems.CARDBOARD))
                .unlockedBy("has_sturdy", has(AllItems.STURDY_SHEET))

                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SecurityItems.EMPTY_FINI_GOGGLES)
                .pattern("sss")
                .pattern("ttt")
                .pattern("gtg")

                .define('s', Items.STRING)
                .define('t', SecurityItems.STURDIER_SHEET)
                .define('g', Items.GLASS)

                .unlockedBy("has_self", has(SecurityItems.EMPTY_FINI_GOGGLES))
                .unlockedBy("has_self_filled", has(SecurityItems.FINI_GOGGLES))
                .unlockedBy("has_sheet", has(SecurityItems.STURDIER_SHEET))

                .save(output);
    }
}
