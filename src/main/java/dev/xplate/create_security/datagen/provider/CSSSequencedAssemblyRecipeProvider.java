package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.xplate.create_security.reg.SecurityItems;
import dev.xplate.create_security.reg.SecurityLiquids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class CSSSequencedAssemblyRecipeProvider extends SequencedAssemblyRecipeGen {
    public CSSSequencedAssemblyRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);

        create("create_sturdier_sheet", b ->
                b.require(AllItems.STURDY_SHEET)
                        .transitionTo(SecurityItems.INCOMPLETE_STURDIER_SHEET)
                        .addOutput(SecurityItems.STURDIER_SHEET.get(), 85)
                        .addOutput(AllItems.STURDY_SHEET.get(), 5)
                        .addOutput(AllItems.POWDERED_OBSIDIAN.get(), 5)
                        .addOutput(SecurityItems.FINIRANIUM_DUST.get(), 3)
                        .addOutput(SecurityItems.FINIRANIUM.get(), 2)

                        .loops(2)
                        .addStep(PressingRecipe::new, bu -> bu)
                        .addStep(PressingRecipe::new, bu -> bu)
                        .addStep(PressingRecipe::new, bu -> bu)
                        .addStep(PressingRecipe::new, bu -> bu)
                        .addStep(FillingRecipe::new, bu -> bu.require(SecurityLiquids.LIQUID_FINIRANIUM.get(), 600))
                        .addStep(DeployerApplicationRecipe::new, bu -> bu.require(SecurityItems.FINIRANIUM))
        );
    }
}
