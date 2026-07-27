package dev.xplate.create_security.datagen;

import com.tterrag.registrate.providers.ProviderType;
import dev.xplate.create_security.datagen.provider.*;
import dev.xplate.create_security.ponder.SecurityPonderPlugin;
import dev.xplate.create_security.reg.SecurityCreativeTabs;
import dev.xplate.create_security.reg.SecurityEffects;
import joptsimple.internal.Strings;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import oshi.util.tuples.Pair;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static dev.xplate.create_security.CSSecurity.MODID;
import static dev.xplate.create_security.CSSecurity.REG;

public class CSSDataGen {

    public static final Pair<String, MutableComponent> eyeOffsetComp = rawLang("item.create_security.tooltip.fini_goggles.eye_offset", "Eye Offset: %s");
    public static final Pair<String, MutableComponent> entriesComp = rawLang("item.create_security.tooltip.log.entries", "%s entries. ");
    public static final Pair<String, MutableComponent> blocksLoggedComp = rawLang("item.create_security.tooltip.log.loggedBlocks", "Logged blocks; %s");
    //gog_tooltips must be indented with 2 tabs.
    public static final Pair<String, MutableComponent> logInsertStats = gogLang("item.create_security.gog_tooltip.loggable_block.log_stats", "Log inserted;");
    public static final Pair<String, MutableComponent> logInsertStats1 = gogLang("item.create_security.gog_tooltip.loggable_block.log_stats1", "%s entries,", 2);
    public static final Pair<String, MutableComponent> logInsertStats2 = gogLang("item.create_security.gog_tooltip.loggable_block.log_stats2", "%s from this block.", 2);
    public static final Pair<String, MutableComponent> chunkDetectorHeading = gogLang("item.create_security.gog_tooltip.chunk_detector", "Chunk Detector Stats");
    
    public static final Pair<String, MutableComponent> detectionDistanceComp = rawLang("blocks.create_security.sight_sensor.distance", "Detection Distance");
    public static final Pair<String, MutableComponent> goToNewestComp = rawLang("gui.create_security.tooltip.log.goToNewestButton", "Go to Newest");
    public static final Pair<String, MutableComponent> goToOldestComp = rawLang("gui.create_security.tooltip.log.goToOldestButton", "Go to Oldest");

    public static final Pair<String, MutableComponent> logBottomText1 = rawLang("gui.create_security.log.bottom1", "Welp, there isn't a man here.");
    public static final Pair<String, MutableComponent> logBottomText2 = rawLang("gui.create_security.log.bottom2", "You have reached an end.");
    public static final Pair<String, MutableComponent> logBottomText3 = rawLang("gui.create_security.log.bottom3", "Nothin' new.");
    public static final Pair<String, MutableComponent> logBottomText4 = rawLang("gui.create_security.log.bottom4", "\uD83D\uDD48︎♒︎⍓︎ ⧫︎❒︎♋︎■︎⬧︎●︎♋︎⧫︎♏︎✍︎");
    public static final Pair<String, MutableComponent> logBottomText5 = rawLang("gui.create_security.log.bottom5", "*Over.*");
    public static final Pair<String, MutableComponent> logBottomText6 = rawLang("gui.create_security.log.bottom6", "You've reached the bottom!");

    public static Pair<String, MutableComponent> gogLang(String key, String eng) {
        return gogLang(key,eng,0);
    }
    
    public static Pair<String, MutableComponent> gogLang(String key, String eng, int indents) {
        return rawLang(key, Strings.repeat(' ', 4 + indents) + eng);
    }

    public static Pair<String, MutableComponent> rawLang(String key, String eng) {
        return new Pair<>(key, REG.addRawLang(key,eng));
    }
    
    public static Pair<String, MutableComponent> getLogBottomText() {
        int log = RandomSource.create().nextIntBetweenInclusive(1, 6);
        return switch (log) {
            case 1 -> logBottomText1;
            case 2 -> logBottomText2;
            case 3 -> logBottomText3;
            case 4 -> logBottomText4;
            case 5 -> logBottomText5;
            default -> logBottomText6;
        };
    }

    public static void gatherHigherData(GatherDataEvent event) {
        if (event.getMods().contains(MODID)) {
            REG.addDataGenerator(ProviderType.LANG, prov -> {
                prov.add(SecurityCreativeTabs.CREATIVE_TAB.get(), "Stealth & Security");
                prov.add(SecurityEffects.END_SICKNESS.get(), "End Sickness");
                prov.add("chat.end_sick.command", "%s has %s end sickness build-up.");
                prov.add("chat.end_sick.warning1", "You start to feel weird...");
                prov.add("chat.end_sick.warning2", "Your head feels like it's banging...");
                prov.add("chat.end_sick.warning3", "You really need to leave the area...");
                prov.add("chat.end_sick.warning4", "You can feel your hearts draining...");
                prov.add("chat.invisiblePlayer", "This player was invisible when this message was sent.");
                prov.add("sound.create_security.finiranium_warning", "Finiranium Detector Beeps (Warning!)");

                providePonderLang(prov::add);
            });
        }
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        CSSGeneratedEntriesProvider generatedEntriesProvider = new CSSGeneratedEntriesProvider(output, lookup);
        lookup = generatedEntriesProvider.getRegistryProvider();
        boolean incServer = event.includeServer();
        generator.addProvider(incServer, generatedEntriesProvider);

        generator.addProvider(incServer, new CSSMixingRecipeProvider(output, lookup, MODID));
        generator.addProvider(incServer, new CSSCrushingRecipeProvider(output, lookup, MODID));
        generator.addProvider(incServer, new CSSCompactingRecipeProvider(output, lookup, MODID));
        generator.addProvider(incServer, new CSSSequencedAssemblyRecipeProvider(output, lookup, MODID));
        generator.addProvider(incServer, new CSSFillingRecipeProvider(output, lookup, MODID));
        generator.addProvider(incServer, new CSSRecipeProvider(output, lookup));

        generator.addProvider(incServer, new CSSSoundDefinitionsProvider(output, existingFileHelper));
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        // Register this since FMLClientSetupEvent does not run during datagen
        PonderIndex.addPlugin(new SecurityPonderPlugin());

        PonderIndex.getLangAccess().provideLang(MODID, consumer);
    }
}
