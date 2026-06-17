package dev.xplate.create_security;

import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import static dev.xplate.create_security.CSecurity.res;

public class SecurityEnumProxies {

    public static final EnumProxy<Gui.HeartType> EndSicknessHeartType = new EnumProxy<>(Gui.HeartType.class,
            res("hud/heart/end_sick_full"),
            res("hud/heart/end_sick_full_blinking"),
            res("hud/heart/end_sick_half"),
            res("hud/heart/end_sick_half_blinking"),
            res("hud/heart/end_sick_hardcore_full"),
            res("hud/heart/end_sick_hardcore_full_blinking"),
            res("hud/heart/end_sick_hardcore_half"),
            res("hud/heart/end_sick_hardcore_half_blinking"));
}
