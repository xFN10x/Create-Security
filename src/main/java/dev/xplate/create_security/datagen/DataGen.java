package dev.xplate.create_security.datagen;

import com.tterrag.registrate.providers.ProviderType;
import dev.xplate.create_security.datagen.provider.GeneratedEntriesProvider;
import dev.xplate.create_security.datagen.provider.RecipeProvider;
import dev.xplate.create_security.ponder.SecurityPonderPlugin;
import dev.xplate.create_security.reg.SecurityCreativeTabs;
import dev.xplate.create_security.reg.SecurityEffects;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static dev.xplate.create_security.CSecurity.MODID;
import static dev.xplate.create_security.CSecurity.REG;

public class DataGen {

    public static void gatherHigherData(GatherDataEvent event) {
        if (event.getMods().contains(MODID)) {
            REG.addDataGenerator(ProviderType.LANG, prov -> {
                prov.add(SecurityCreativeTabs.CREATIVE_TAB.get(), "Create: Security");
                prov.add(SecurityEffects.END_SICKNESS.get(), "End Sickness");
                prov.add("chat.end_sick.warning1", "You start to feel weird...");
                prov.add("chat.end_sick.warning2", "Your head feels like it's banging...");
                prov.add("chat.end_sick.warning3", "You really need to leave the area...");
                prov.add("chat.end_sick.warning4", "You can feel your hearts draining...");
                providePonderLang(prov::add);
            });
        }
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        GeneratedEntriesProvider generatedEntriesProvider = new GeneratedEntriesProvider(output, lookup);
        lookup = generatedEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), generatedEntriesProvider);

        generator.addProvider(event.includeServer(), new RecipeProvider(output, lookup));
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        // Register this since FMLClientSetupEvent does not run during datagen
        PonderIndex.addPlugin(new SecurityPonderPlugin());

        PonderIndex.getLangAccess().provideLang(MODID, consumer);
    }
}
