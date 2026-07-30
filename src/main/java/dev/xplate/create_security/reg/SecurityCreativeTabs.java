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
                        o.accept(SecurityBlocks.SIGHT_SENSOR);
                        o.accept(SecurityBlocks.LASER_DIODE);
                        o.accept(SecurityBlocks.CHUNK_DETECTOR);
                        
                        o.accept(SecurityItems.LOG);

                        o.accept(SecurityItems.KEYCARD);

                        o.accept(SecurityBlocks.FINIRANIUM_ORE);
                        o.accept(SecurityItems.FINIRANIUM);
                        o.accept(SecurityItems.FINIRANIUM_DUST);
                        o.accept(SecurityBlocks.FINIRANIUM_BLOCK);
                        o.accept(SecurityFluids.LIQUID_FINIRANIUM.getBucket().orElse(SecurityBlocks.THE_BLOCK.asItem()));

                        o.accept(SecurityItems.STURDIER_SHEET);

                        o.accept(SecurityItems.FINI_GOGGLES);
                        o.accept(SecurityItems.EMPTY_FINI_GOGGLES);
                        
                        o.accept(SecurityItems.FINIRANIUM_SENSOR);

                        o.accept(SecurityBlocks.NETHER_GLASS);
                    })
                    .build()
    );

    public static void reg(IEventBus bus) {
        TABS.register(bus);
    }
}
