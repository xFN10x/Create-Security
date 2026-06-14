package dev.xplate.create_security.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xplate.create_security.ponder.scenes.SightSensorScenes;
import dev.xplate.create_security.reg.SecurityBlocks;
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
        //v from the create mod
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(SecurityBlocks.SIGHT_SENSOR)
                .addStoryBoard("sight_sensor/basic", SightSensorScenes::basic);
    }
}
