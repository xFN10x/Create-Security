package dev.xplate.create_security.datagen.provider;

import dev.xplate.create_security.reg.SecuritySoundEvents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import static dev.xplate.create_security.CSSecurity.*;

public class CSSSoundDefinitionsProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    public CSSSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, MODID, helper);
    }

    @Override
    public void registerSounds() {
        add(SecuritySoundEvents.WARNING_SOUND, SoundDefinition.definition()
                .with(
                        sound(res("warning"), SoundDefinition.SoundType.SOUND)
                                .attenuationDistance(5)
                )
                .subtitle("sound.create_security.finiranium_warning")
        );
    }
}
