package dev.xplate.create_security.items.menus;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import com.simibubi.create.foundation.gui.menu.HeldItemGhostItemMenu;
import dev.xplate.create_security.misc.LogEntry;
import dev.xplate.create_security.reg.SecurityItemComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class LogMenu extends HeldItemGhostItemMenu {
    public List<LogEntry> entries;
    
    public LogMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public LogMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    @Override
    protected void init(Inventory inv, ItemStack contentHolderIn) {
        super.init(inv, contentHolderIn);
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        ItemStackHandler handler = new ItemStackHandler(1);
        handler.setStackInSlot(0, contentHolder);
        return handler;
    }

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @Override
    protected void addSlots() {
        //addPlayerSlots(27, 158);
    }

    @Override
    protected void initAndReadInventory(ItemStack contentHolder) {
        super.initAndReadInventory(contentHolder);

        entries = contentHolder.getOrDefault(SecurityItemComponents.LOGS, List.of());
    }

    @Override
    protected void saveData(ItemStack contentHolder) {
        contentHolder.set(SecurityItemComponents.LOGS, entries);
    }
}
