package dev.xplate.create_security.blocks;

import dev.xplate.create_security.blocks.base.GradientNamedBlock;

import java.awt.*;

public class FiniraniumRelatedBlock extends GradientNamedBlock {

    public static final ColourGradient finiraniumGrad = new ColourGradient(Color.decode("#DE63E7"), Color.decode("#FF00E5"));
    public FiniraniumRelatedBlock(Properties properties) {
        super(properties, finiraniumGrad);
    }
}
