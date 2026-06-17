package dev.xplate.create_security.misc;

import dev.xplate.create_security.effects.ColourGradient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.awt.*;

public class Utils {
    public static final ColourGradient FiniraniumGrad = new ColourGradient(Color.decode("#DE63E7"), Color.decode("#FF00E5"));

    public static MutableComponent createGradiant(ColourGradient grad, String input) {
        MutableComponent builder = Component.empty();
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            float delta = (float) i / chars.length;
            builder.append(Component.literal(String.valueOf(c)).withColor(grad.getAtPercent(delta)));
        }
        return builder;
    }

    public static MutableComponent createGradiant(ColourGradient grad, ItemLike input) {
        return createGradiant(grad, Component.translatable(input.asItem().getDescriptionId()).getString());
    }

    public static MutableComponent createGradiant(ColourGradient grad, MutableComponent input) {
        return createGradiant(grad, input.getString());
    }
}
