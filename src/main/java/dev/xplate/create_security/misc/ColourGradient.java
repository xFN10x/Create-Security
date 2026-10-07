package dev.xplate.create_security.misc;

import java.awt.*;

import static net.minecraft.util.FastColor.ARGB32.lerp;

public record ColourGradient(Color start, Color end) {

    public int getAtPercent(float percent) {
        return (lerp(percent,
                start.getRGB(),
                end.getRGB()
        ));
    }
}
