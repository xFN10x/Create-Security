package dev.xplate.create_security.items;

import com.simibubi.create.content.equipment.wrench.WrenchItemRenderer;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import dev.xplate.create_security.items.renderers.FiniGogglesRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class FiniGoggles extends FiniraniumRelatedItem implements Equipable {
    public FiniGoggles(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return swapWithEquipmentSlot(this, level, player, usedHand);
    }

    @Override
    public long sickAmount() {
        return 1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new FiniGogglesRenderer()));
    }
}
