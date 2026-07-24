package dev.xplate.create_security.items;

import com.simibubi.create.foundation.recipe.ItemCopyingRecipe;
import dev.xplate.create_security.items.menus.LogMenu;
import dev.xplate.create_security.reg.SecurityMenus;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack usedStack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            player.openMenu(this, buf ->
                    ItemStack.STREAM_CODEC.encode(buf, usedStack)
            );
            return InteractionResultHolder.success(usedStack);
        }
        return InteractionResultHolder.pass(usedStack);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        ItemStack heldItem = player.getMainHandItem();
        return new LogMenu(SecurityMenus.LOG_MENU.get(), containerId, playerInventory, heldItem);
    }
}
