package dev.xplate.create_security.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static dev.xplate.create_security.CSecurity.MODID;

public class SecurityPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        
    }
}
