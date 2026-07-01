package dev.xplate.create_security.reg;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xplate.create_security.CSecurity;
import dev.xplate.create_security.items.FiniGoggles;
import dev.xplate.create_security.items.FiniraniumRelatedItem;
import dev.xplate.create_security.items.KeycardItem;
import dev.xplate.create_security.items.datacomps.EyeOffsetComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.xplate.create_security.CSecurity.REG;

public class SecurityItems {

    public static final ItemEntry<KeycardItem> KEYCARD = REG
            .item("keycard", KeycardItem::new)
            .properties(p -> p.fireResistant()
                    .stacksTo(1))
            .lang("Keycard")
            .defaultModel()
            .register();

    public static final ItemEntry<FiniraniumRelatedItem> FINIRANIUM = REG
            .item("finiranium", FiniraniumRelatedItem::new)
            .properties(p -> p.fireResistant().rarity(Rarity.EPIC))
            .lang("Finiranium")
            .burnTime((20 * 60 * 30))
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
            .model((ctx, mod) -> mod.getExistingFile(mod.modLoc("item/fini_goggles/fini_goggles")))
            .register();

    public static void reg() {}
}
