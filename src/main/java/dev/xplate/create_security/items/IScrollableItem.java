package dev.xplate.create_security.items;

import net.minecraft.world.item.ItemStack;

public interface IScrollableItem {

    void onScrollUp(ItemStack stack);
    void onScrollDown(ItemStack stack);
}
