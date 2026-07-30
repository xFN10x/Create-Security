package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.foundation.recipe.ItemCopyingRecipe;
import dev.xplate.create_security.CSSecurity;
import dev.xplate.create_security.reg.SecurityBlocks;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static dev.xplate.create_security.CSSecurity.res;

public class CSSRecipeProvider extends BaseRecipeProvider {
    public CSSRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CSSecurity.MODID);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        createSpecial(ItemCopyingRecipe::new, "crafting", "item_copying");
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

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SecurityBlocks.CHUNK_DETECTOR)
                .pattern("ggg")
                .pattern("sft")
                .pattern(" s ")

                .define('s', AllBlocks.SHAFT)
                .define('f', SecurityItems.FINIRANIUM)
                .define('g', SecurityBlocks.NETHER_GLASS)
                .define('t', SecurityItems.STURDIER_SHEET)

                .unlockedBy("has_self", has(SecurityBlocks.CHUNK_DETECTOR))
                .unlockedBy("has_nether_glass", has(SecurityBlocks.NETHER_GLASS))

                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SecurityBlocks.NETHER_GLASS, 4)
                .pattern("ngn")
                .pattern("g g")
                .pattern("ngn")

                .define('n', Blocks.NETHER_BRICKS)
                .define('g', Blocks.GLASS)

                .unlockedBy("has_self", has(SecurityBlocks.NETHER_GLASS))
                .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS))

                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SecurityItems.LOG)
                .pattern("bpb")
                .pattern(" p ")
                .pattern(" p ")

                .define('b', AllItems.BRASS_NUGGET)
                .define('p', Items.PAPER)

                .unlockedBy("has_self", has(SecurityItems.LOG))
                .unlockedBy("has_paper", has(Items.PAPER))

                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SecurityItems.FINIRANIUM_SENSOR)
                .pattern(" g ")
                .pattern("sps")
                .pattern(" s ")

                .define('g', Items.GLASS)
                .define('p', AllItems.PRECISION_MECHANISM)
                .define('s', AllItems.STURDY_SHEET)

                .unlockedBy("has_self", has(SecurityItems.FINIRANIUM_SENSOR))
                .unlockedBy("has_sturdy", has(AllItems.STURDY_SHEET))

                .save(output);
    }
    
    BaseRecipeProvider.GeneratedRecipe createSpecial(Function<CraftingBookCategory, Recipe<?>> builder, String recipeType,
                                                     String path) {
        ResourceLocation location = res(recipeType + "/" + path);
        return register(consumer -> {
            SpecialRecipeBuilder b = SpecialRecipeBuilder.special(builder);
            b.save(consumer, location.toString());
        });
    }
}
