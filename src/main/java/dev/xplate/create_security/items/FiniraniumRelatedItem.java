package dev.xplate.create_security.items;

import dev.xplate.create_security.items.base.GradientNamedItem;

import static dev.xplate.create_security.blocks.FiniraniumRelatedBlock.finiraniumGrad;

public class FiniraniumRelatedItem extends GradientNamedItem {
    public FiniraniumRelatedItem(Properties properties) {
        super(properties, finiraniumGrad);
    }
}
