package dev.xplate.create_security.reg;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.xplate.create_security.blocks.FiniraniumRelatedFluidBlock;
import dev.xplate.create_security.items.FiniraniumRelatedBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import static dev.xplate.create_security.CSecurity.REG;
import static dev.xplate.create_security.CSecurity.res;

public class SecurityLiquids {

    public static final FluidEntry<BaseFlowingFluid.Flowing> LIQUID_FINIRANIUM =
            REG.fluid("liquid_finranium",
                            res("block/fluid/liquid_finiranium_still"),
                            res("block/fluid/liquid_finiranium_flow")
                    )
                    .lang("Liquid Finiranium")
                    .properties(b -> b.viscosity(4000)
                            .density(400))
                    .fluidProperties(p -> p.levelDecreasePerBlock(3)
                            .tickRate(50)
                            .slopeFindDistance(2)
                            .explosionResistance(500f))
                    .source(BaseFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
                    .block( (f,p) -> new FiniraniumRelatedFluidBlock(f,p,10))
                    .properties(p -> p.mapColor(MapColor.COLOR_MAGENTA))
                    .build()
                    .bucket(FiniraniumRelatedBucketItem::new)
                    .onRegister(SecurityLiquids::registerFluidDispenseBehavior)
                    .build()
                    .register();

    //taken from create, or should i say tooken from create
    private static final DispenseItemBehavior DEFAULT = new DefaultDispenseItemBehavior();
    private static final DispenseItemBehavior DISPENSE_FLUID = new DefaultDispenseItemBehavior(){
        @Override
        protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
            DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) pStack.getItem();
            BlockPos pos = pSource.pos().relative(pSource.state().getValue(DispenserBlock.FACING));
            Level level = pSource.level();
            if (dispensibleContainerItem.emptyContents(null, level, pos, null, pStack)) {
                return new ItemStack(Items.BUCKET);
            }
            return DEFAULT.dispense(pSource, pStack);
        }
    };

    private static void registerFluidDispenseBehavior(BucketItem bucket) {
        DispenserBlock.registerBehavior(bucket, DISPENSE_FLUID);
    }

    public static void reg() {

    }
}
