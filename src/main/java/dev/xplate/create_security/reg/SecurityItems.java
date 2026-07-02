package dev.xplate.create_security.reg;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xplate.create_security.items.FiniGoggles;
import dev.xplate.create_security.items.FiniraniumRelatedItem;
import dev.xplate.create_security.items.KeycardItem;
import dev.xplate.create_security.items.datacomps.EyeOffsetComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static dev.xplate.create_security.CSSecurity.REG;

public class SecurityItems {

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

    public static final ItemEntry<Item> INCOMPLETE_STURDIER_SHEET = REG
            .item("incomplete_sturdier_sheet", Item::new)
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

    public static void reg() {}
}
