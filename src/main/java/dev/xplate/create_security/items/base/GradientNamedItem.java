package dev.xplate.create_security.items.base;

import dev.xplate.create_security.blocks.base.GradientNamedBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class GradientNamedItem extends Item {
    protected final GradientNamedBlock.ColourGradient gradient;

    public GradientNamedItem(Properties properties, GradientNamedBlock.ColourGradient grad) {
        super(properties);
        this.gradient = grad;
    }

    protected GradientNamedBlock.ColourGradient getGradient() {
        return gradient;
    }

    @Override
    public Component getName(ItemStack stack) {
        MutableComponent rawName = Component.translatable(this.getDescriptionId(stack));
        MutableComponent builder = Component.empty();
        char[] chars = rawName.getString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            float delta = (float) i / chars.length;
            GradientNamedBlock.ColourGradient gradient = getGradient();
            builder.append(Component.literal(String.valueOf(c)).withColor(gradient.getAtPercent(delta)));
        }
        return builder;
    }
}
