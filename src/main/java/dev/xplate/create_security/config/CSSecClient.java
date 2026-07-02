package dev.xplate.create_security.config;

import dev.xplate.create_security.config.enums.EndSicknessWarningLevel;
import net.createmod.catnip.config.ConfigBase;

public class CSSecClient extends ConfigBase {

    public final ConfigGroup client = group(0,
            "client",
            "Client Only Configs");

    public final ConfigEnum<EndSicknessWarningLevel> endSicknessWarnings = e(EndSicknessWarningLevel.NORMAL,
            "endSicknessWarningLevel",
            "The level of warnings you will get as your end sickness builds up.",
            "There are 4 warning messages, NORMAL will show them all, EVERY_OTHER will only show the 2nd last warning, and the second one.");

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
