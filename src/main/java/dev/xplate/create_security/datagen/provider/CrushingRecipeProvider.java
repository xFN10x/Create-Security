package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import dev.xplate.create_security.reg.SecurityBlocks;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class CrushingRecipeProvider extends CrushingRecipeGen {
    public CrushingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);

        create(() -> SecurityItems.FINIRANIUM, b ->
                b.duration(20 * 30)
                        .output(0.80f, SecurityItems.FINIRANIUM_DUST.get(), 2)
                        .output(0.50f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.10f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.15f, SecurityItems.FINIRANIUM.get())
        );
        create(() -> SecurityBlocks.FINIRANIUM_BLOCK, b ->
                b.duration(20 * 45)
                        .output(SecurityItems.FINIRANIUM.get(), 7)
                        .output(0.60f, SecurityItems.FINIRANIUM.get(), 1)
                        .output(0.60f, SecurityItems.FINIRANIUM.get(), 1)
        );
        create(() -> SecurityItems.FINI_GOGGLES, b ->
                b.duration(20 * 5)
                        .output(0.40f, SecurityItems.FINIRANIUM_DUST.get(), 2)
                        .output(0.20f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.20f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(Items.STRING, 1)
                        .output(0.5f, Items.STRING, 1)
                        .output(0.5f, Items.STRING, 1)
        );
        create(() -> Blocks.END_STONE, b ->
                b.duration(20 * 5)
                        .output(0.10f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.05f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.05f, SecurityItems.FINIRANIUM_DUST.get(), 1)
        );
        ore(Blocks.END_STONE, SecurityBlocks.FINIRANIUM_ORE, SecurityItems.FINIRANIUM, )
    }
}
