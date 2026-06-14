package dev.xplate.create_security.datagen;

import com.tterrag.registrate.providers.ProviderType;
import dev.xplate.create_security.CSecurity;
import dev.xplate.create_security.datagen.provider.RecipeProvider;
import dev.xplate.create_security.ponder.SecurityPonderPlugin;
import dev.xplate.create_security.reg.SecurityCreativeTabs;
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
        if (event.getMods().contains(MODID))
            REG.addDataGenerator(ProviderType.LANG, prov -> {
                prov.add(SecurityCreativeTabs.CREATIVE_TAB.get(), "Create: Security");
                providePonderLang(prov::add);
            });
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new RecipeProvider(output, lookup));
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        // Register this since FMLClientSetupEvent does not run during datagen
        PonderIndex.addPlugin(new SecurityPonderPlugin());

        PonderIndex.getLangAccess().provideLang(MODID, consumer);
    }
}
