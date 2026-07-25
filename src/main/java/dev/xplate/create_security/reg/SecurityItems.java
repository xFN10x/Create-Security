package dev.xplate.create_security.reg;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xplate.create_security.items.*;
import dev.xplate.create_security.items.datacomps.EyeOffsetComponent;
import dev.xplate.create_security.items.propfuncs.FiniraniumSensorPropertyFunction;
import dev.xplate.create_security.misc.LogEntry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.List;

import static dev.xplate.create_security.CSSecurity.REG;
import static dev.xplate.create_security.CSSecurity.res;

public class SecurityItems {
    public final static ResourceLocation FINIRANIUM_READING_PROPERTY = res("finiranium_reading");

    public static final ItemEntry<KeycardItem> KEYCARD = REG
            .item("keycard", KeycardItem::new)
            .properties(p -> p.fireResistant()
                    .stacksTo(1))
            .lang("Keycard")
            .defaultModel()
            .register();

    public static final ItemEntry<FiniraniumRelatedItem> FINIRANIUM = REG
            .item("finiranium", p -> new FiniraniumRelatedItem(p, 100))
            .properties(p -> p.fireResistant().rarity(Rarity.EPIC))
            .lang("Finiranium")
            .burnTime((20 * 60 * 30))
            .defaultModel()
            .register();

    public static final ItemEntry<FiniraniumRelatedItem> FINIRANIUM_DUST = REG
            .item("finiranium_dust", p -> new FiniraniumRelatedItem(p, 140))
            .properties(p -> p.fireResistant().rarity(Rarity.EPIC))
            .lang("Finiranium Dust")
            .burnTime((20 * 60 * 40))
            .defaultModel()
            .register();

    public static final ItemEntry<FiniGoggles> FINI_GOGGLES = REG
            .item("fini_goggles", FiniGoggles::new)
            .properties(p ->
                    p.fireResistant()
                            .stacksTo(1)
                            .durability(180)
                            .component(SecurityItemComponents.EYE_OFFSET, new EyeOffsetComponent(0)))
            .lang("Fini-Goggles")
            .model((ctx, mod) -> mod.getExistingFile(mod.modLoc("item/fini_goggles")))
            .register();

    public static final ItemEntry<FiniraniumRelatedItem> STURDIER_SHEET = REG
            .item("sturdier_sheet", p -> new FiniraniumRelatedItem(p, 5))
            .properties(p ->
                    p.fireResistant())
            .lang("Sturdier Sheet")
            .defaultModel()
            .register();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_STURDIER_SHEET = REG
            .item("incomplete_sturdier_sheet", SequencedAssemblyItem::new)
            .lang("Incomplete Sturdier Sheet")
            .defaultModel()
            .register();

    public static final ItemEntry<Item> EMPTY_FINI_GOGGLES = REG
            .item("empty_fini_goggles", Item::new)
            .properties(p ->
                    p.fireResistant()
                            .stacksTo(1))
            .lang("Empty Fini-Goggles")
            .model((ctx, mod) -> mod.getExistingFile(mod.modLoc("item/empty_fini_goggles")))
            .register();

    private static ModelFile getGenModelFromId(String texture, RegistrateItemModelProvider mod) {
        return mod.getBuilder(texture)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", res("item/" + texture));
    }

    public static final ItemEntry<FiniraniumSensor> FINIRANIUM_SENSOR = REG
            .item("finiranium_sensor", FiniraniumSensor::new)
            .properties(p ->
                    p.fireResistant()
                            .stacksTo(1)
                            .component(SecurityItemComponents.FINIRANIUM_LEVEL.get(), 0f))
            .lang("Finiranium Sensor")
            .model((ctx, mod) -> mod.generated(ctx::getEntry, res("item/finiranium_sensor_0"))
                    .override()
                    .model(getGenModelFromId("finiranium_sensor_0", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 0)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_1", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 1)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_2", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 2)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_3", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 3)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_4", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 4)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_5", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 5)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_6", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 6)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_7", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 7)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_8", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 8)
                    .end()

                    .override()
                    .model(getGenModelFromId("finiranium_sensor_9", mod))
                    .predicate(FINIRANIUM_READING_PROPERTY, 9)
                    .end()
            )
            .onRegister(item -> {
                ItemProperties.register(
                        item,
                        FINIRANIUM_READING_PROPERTY,
                        new FiniraniumSensorPropertyFunction());
            })
            .register();
    
    public static final ItemEntry<LogItem> LOG = REG
            .item("log", LogItem::new)
            .properties(p ->
                    p.stacksTo(1)
                            .component(SecurityItemComponents.LOGS, new ArrayList<>()))
            .lang("Log")
            .defaultModel()
            .register();
    
    public static void reg() {
    }
}
