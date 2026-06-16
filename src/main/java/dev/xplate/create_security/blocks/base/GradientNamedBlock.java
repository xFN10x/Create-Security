package dev.xplate.create_security.blocks.base;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;

import java.awt.*;

import static net.minecraft.util.FastColor.ARGB32.lerp;

public class GradientNamedBlock extends Block {
    protected final ColourGradient gradient;

    public GradientNamedBlock(Properties properties, ColourGradient grad) {
        super(properties);
        this.gradient = grad;
    }

    protected ColourGradient getGradient() {
        return gradient;
    }

    public MutableComponent getName() {
        MutableComponent rawName = Component.translatable(this.getDescriptionId());
        MutableComponent builder = Component.empty();
        char[] chars = rawName.getString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            float delta = (float) i / chars.length;
            ColourGradient gradient = getGradient();
            builder.append(Component.literal(String.valueOf(c)).withColor(gradient.getAtPercent(delta)));
        }
        return builder;
    }

    public record ColourGradient(Color start, Color end) {

        public int getAtPercent(float percent) {
            return (lerp(percent,
                    start.getRGB(),
                    end.getRGB()
            ));
        }
    }

}
