package dev.xplate.create_security.reg;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xplate.create_security.items.menus.LogMenu;
import dev.xplate.create_security.items.menus.screens.LogScreen;
import net.minecraft.world.inventory.MenuType;

import static dev.xplate.create_security.CSSecurity.REG;

public class SecurityMenus {
    
    public static final MenuEntry<LogMenu> LOG_MENU = REG.menu("log_menu", LogMenu::new, () -> LogScreen::new).register();

    public static void reg() {}
}
