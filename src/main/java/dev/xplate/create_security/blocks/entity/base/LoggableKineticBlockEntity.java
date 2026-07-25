package dev.xplate.create_security.blocks.entity.base;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.xplate.create_security.misc.LogEntry;
import dev.xplate.create_security.reg.SecurityItemComponents;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class LoggableKineticBlockEntity extends KineticBlockEntity implements Container {

    protected NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    public LoggableKineticBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return stack.is(SecurityItems.LOG);
    }

    public void attemptLog(String message, LivingEntity target, ServerLevel level, BlockState state) {
        ItemStack logStack = inventory.get(0);
        if (logStack.isEmpty()) return;
        List<LogEntry> existingEntries = logStack.getOrDefault(SecurityItemComponents.LOGS, new ArrayList<>());
        existingEntries.add(new LogEntry(
                message,
                LogEntry.LogTarget.of(target),
                state.getBlockHolder(),
                LogEntry.LogTime.now(level)
        ));
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        if (!clientPacket) {
            inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, inventory, registries);
        }
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        if (!clientPacket)
            ContainerHelper.saveAllItems(tag, inventory, registries);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return removeItem(slot, amount, null);
    }

    public @NotNull ItemStack removeItem(int slot, int amount, @Nullable ServerPlayer plr) {
        ItemStack itemstack = ContainerHelper.removeItem(inventory, slot, amount);
        if (!itemstack.isEmpty()) {
            setChanged();
        }
        if (plr != null)
            itemstack.get(SecurityItemComponents.LOGS).add(
                    new LogEntry(
                            "removed this log from a " + getBlockState().getBlock().getName().getString() + ".",
                            LogEntry.LogTarget.of(plr),
                            getBlockState().getBlockHolder(),
                            LogEntry.LogTime.now((ServerLevel) plr.level())
                    )
            );

        return itemstack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, null);
    }

    public void setItem(int slot, ItemStack stack, @Nullable ServerPlayer plr) {
        if (stack.is(SecurityItems.LOG.get())) {
            inventory.set(slot, stack);
            if (plr != null)
                stack.get(SecurityItemComponents.LOGS).add(
                        new LogEntry(
                                "added this log to a " + getBlockState().getBlock().getName().getString() + ".",
                                LogEntry.LogTarget.of(plr),
                                getBlockState().getBlockHolder(),
                                LogEntry.LogTime.now((ServerLevel) plr.level())
                        )
                );
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }
}
