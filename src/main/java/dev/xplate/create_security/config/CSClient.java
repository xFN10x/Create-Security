package dev.xplate.create_security.config;

import net.createmod.catnip.config.ConfigBase;

public class CSClient extends ConfigBase {

    public final ConfigGroup client = group(0,
            "client",
            "Client Only Configs");

    public final ConfigGroup visual = group(1,
            "visualSettings",
            "Configure visual effects");
    public final ConfigFloat laserFlickerStrength = f(1, 0, 2,
            "laserFlickerStrength",
            "The visual flicker of a Laser Diode's laser.",
            "1.00 is the default, so it flickers between 0-10 transparency.");

    @Override
    public String getName() {
        return "client";
    }
}
