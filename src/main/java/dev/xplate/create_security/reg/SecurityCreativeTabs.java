package dev.xplate.create_security.reg;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.xplate.create_security.CSSecurity.MODID;

public class SecurityCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = TABS.register("create_security_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.create_security"))
                    .icon(SecurityBlocks.SIGHT_SENSOR::asStack)
                    .displayItems((p, o) -> {
                        o.accept(SecurityBlocks.SIGHT_SENSOR.get());
                        o.accept(SecurityBlocks.LASER_DIODE.get());

                        o.accept(SecurityItems.KEYCARD.get());

                        o.accept(SecurityBlocks.FINIRANIUM_ORE.get());
                        o.accept(SecurityItems.FINIRANIUM.get());
                        o.accept(SecurityItems.FINIRANIUM_DUST.get());
                        o.accept(SecurityBlocks.FINIRANIUM_BLOCK.get());
                        o.accept(SecurityFluids.LIQUID_FINIRANIUM.getBucket().get());

                        o.accept(SecurityItems.STURDIER_SHEET.get());

                        o.accept(SecurityItems.FINI_GOGGLES.get());
                        o.accept(SecurityItems.EMPTY_FINI_GOGGLES.get());

                    })
                    .build()
    );

    public static void reg(IEventBus bus) {
        TABS.register(bus);
    }
}
