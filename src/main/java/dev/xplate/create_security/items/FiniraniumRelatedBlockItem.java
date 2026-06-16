package dev.xplate.create_security.items;

import dev.xplate.create_security.blocks.base.GradientNamedBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import static dev.xplate.create_security.blocks.FiniraniumRelatedBlock.finiraniumGrad;

public class FiniraniumRelatedBlockItem extends BlockItem {
    protected final GradientNamedBlock.ColourGradient gradient;

    public FiniraniumRelatedBlockItem(Block block, Properties properties) {
        super(block, properties);
        this.gradient = finiraniumGrad;
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
