package dev.xplate.create_security.reg;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xplate.create_security.CSecurity;
import dev.xplate.create_security.items.KeycardItem;

import static dev.xplate.create_security.CSecurity.REG;

public class SecurityItems {

    public static final ItemEntry<KeycardItem> KEYCARD = REG.item("keycard", KeycardItem::new)
            .properties(p -> p.fireResistant()
                    .stacksTo(1))
            .lang("Keycard")
            .defaultModel()
            .tab(CSecurity.CREATIVE_TAB.getKey())
            .register();

    public static void reg() {}
}
