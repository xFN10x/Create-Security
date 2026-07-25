package dev.xplate.create_security.items;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import dev.xplate.create_security.CSSecurity;
import dev.xplate.create_security.datagen.CSSDataGen;
import dev.xplate.create_security.items.datacomps.EyeOffsetComponent;
import dev.xplate.create_security.items.renderers.FiniGogglesRenderer;
import dev.xplate.create_security.reg.SecurityCreativeTabs;
import dev.xplate.create_security.reg.SecurityItemComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static dev.xplate.create_security.datagen.CSSDataGen.eyeOffsetComp;

public class FiniGoggles extends FiniraniumRelatedItem implements Equipable, IScrollableItem {
    public FiniGoggles(Properties properties) {
        super(properties, 1L);
    }


    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return swapWithEquipmentSlot(this, level, player, usedHand);
    }

    @Override
    public long sickAmount() {
        return 1;
    }

    @SuppressWarnings("removal")
    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new FiniGogglesRenderer()));
    }

    @Override
    public void onScrollUp(ItemStack stack) {
        EyeOffsetComponent offset = stack.get(SecurityItemComponents.EYE_OFFSET);
        if (offset == null)
            stack.set(SecurityItemComponents.EYE_OFFSET, new EyeOffsetComponent(0));
        else
            stack.set(SecurityItemComponents.EYE_OFFSET, offset.add(1, -3, 3));
    }

    @Override
    public void onScrollDown(ItemStack stack) {
        EyeOffsetComponent offset = stack.get(SecurityItemComponents.EYE_OFFSET);
        if (offset == null)
            stack.set(SecurityItemComponents.EYE_OFFSET, new EyeOffsetComponent(0));
        else
            stack.set(SecurityItemComponents.EYE_OFFSET, offset.add(-1, -3, 3));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        CreativeModeTab tab = SecurityCreativeTabs.CREATIVE_TAB.get();
        Component creativeTabName = tab.getDisplayName();
        if (!tab.contains(stack)) {
            tooltipComponents.add(creativeTabName.copy().withStyle(ChatFormatting.BLUE));
        }
        tooltipComponents.add(Component.translatable(eyeOffsetComp.getA(), stack.get(SecurityItemComponents.EYE_OFFSET).offset()));
    }
}
