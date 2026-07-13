package dev.xplate.create_security.config;

import net.createmod.catnip.config.ConfigBase;

public class CSSecServer extends ConfigBase {

    public final ConfigGroup endSicknessGroup = group(0,
            "endSickness",
            "Configurations about End Sickness");

    public final ConfigBool endSicknessEnabled = b(true,
            "endSicknessEnabled",
            "Determines if End sickness is obtainable.");

    public final ConfigBool endSicknessEnabledInInventory = b(true,
            "endSicknessEnabledInInventory",
            "Determines if End Sickness can build-up from inventory items. (Affects all entities.)");

    public final ConfigInt endSicknessThreshold = i(200000, 10000,
            "endSicknessThreshold",
            "The Threshold of getting End Sickness.");

    public final ConfigInt endSicknessLevelThreshold = i(300000, 5000,
            "endSicknessLevelThreshold",
            "The Threshold of End Sickness leveling up after the first threshold.");

    public final ConfigInt endSicknessTickRate = i(10, 1, 40,
            "endSicknessTickRate",
            "The Tick rate of which End Sickness build-up is checked.",
            "Higher values will be more inaccurate, lower ones will be more accurate.");

    public final ConfigInt endSicknessDecreaseRate = i(25, 0,
            "endSicknessDecreaseRate",
            "The rate of which entities will lose End Sickness buildup when not around sickening blocks & items.");

    public final ConfigBool endSicknessCheckableByPlayers = b(false,
            "endSicknessCheckableByPlayers",
            "Determines if non-OP players can check their own sickness build-up with commands.");

    public final ConfigGroup extrasGroup = group(0,
            "extras",
            "Extra feature configurations");

    public final ConfigBool obfuscateInvisiblePlayerNames = b(true,
            "obfuscateInvisiblePlayerNames",
            "Whether or not player's names are obfuscated when they are invisible.",
            "This doesn't affect commands, or the tab menu, it only obfuscates names in the chat.",
            "Obfuscated names look like: §kThisShouldAppearJumbled");
    public final ConfigBool obfuscatedNamesRandomLength = b(true,
            "obfuscatedNamesRandomLength",
            "Whether or not to make obfuscated name lengths random.",
            "If false, obfuscated names can be deduced by the length of the name.",
            "The names length will only be randomized once, so people can still tell invisible players apart.");

    @Override
    public String getName() {
        return "server";
    }
}
