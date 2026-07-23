package dev.xplate.create_security.items;

import com.simibubi.create.foundation.recipe.ItemCopyingRecipe;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class LogItem extends Item implements MenuProvider, ItemCopyingRecipe.SupportsItemCopying {
    public LogItem(Properties properties) {
        super(properties);
    }

    @Override
    public DataComponentType<?> getComponentType() {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return getDescription();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }
}
