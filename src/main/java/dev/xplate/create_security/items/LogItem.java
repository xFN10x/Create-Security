package dev.xplate.create_security.items;

import com.simibubi.create.foundation.recipe.ItemCopyingRecipe;
import dev.xplate.create_security.blocks.base.LoggableKineticBlock;
import dev.xplate.create_security.datagen.CSSDataGen;
import dev.xplate.create_security.items.menus.LogMenu;
import dev.xplate.create_security.misc.LogEntry;
import dev.xplate.create_security.reg.SecurityItemComponents;
import dev.xplate.create_security.reg.SecurityMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LogItem extends Item implements MenuProvider, ItemCopyingRecipe.SupportsItemCopying {
    public LogItem(Properties properties) {
        super(properties);
    }

    @Override
    public DataComponentType<?> getComponentType() {
        return null;
    }

    @Override
    public Component getDisplayName() {
        return getDescription();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        List<LogEntry> entries = stack.get(SecurityItemComponents.LOGS);
        int toolColour = FastColor.ARGB32.color(150, 150, 150);
        tooltipComponents.add(Component.translatable(CSSDataGen.entriesComp.getA(), entries.size()).withColor(toolColour));
        ArrayList<Holder<Block>> unqiueBlocks = getUniqueSources(entries);
        tooltipComponents.add(Component.translatable(CSSDataGen.blocksLoggedComp.getA(), unqiueBlocks.toArray()).withColor(toolColour));
    }

    public static @NotNull String[] getBlocksInLog(List<LogEntry> entries) {
        ArrayList<String> names = new ArrayList<>();
        for (LogEntry entry : entries) {
            Optional<Block> block = entry.blockSource().unwrap().right();
            block.ifPresent(value -> names.add(value.getName().getString()));
        }
        return names.toArray(new String[0]);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack logStack) {
        if (!logStack.has(SecurityItemComponents.LOGS)) {
            logStack.set(SecurityItemComponents.LOGS, new ArrayList<>());
        } else {
            List<LogEntry> existingEntries = logStack.getOrDefault(SecurityItemComponents.LOGS, new ArrayList<>());
            if (!(existingEntries instanceof ArrayList<LogEntry>)) {
                ArrayList<LogEntry> arrayList = new ArrayList<>(existingEntries);
                logStack.set(SecurityItemComponents.LOGS, arrayList);
            }
        }
    }

    public static @NotNull ArrayList<Holder<Block>> getUniqueSources(List<LogEntry> entries) {
        ArrayList<Holder<Block>> unqiueBlocks = new ArrayList<>();
        entries.stream().filter(entry -> {
            Holder<Block> source = entry.blockSource();
            if (!unqiueBlocks.contains(source)) {
                unqiueBlocks.add(source);
                return true;
            }
            return false;
        });
        return unqiueBlocks;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.PASS;
        BlockState bs = level.getBlockState(clickedPos);
        Block block = bs.getBlock();
        if (block instanceof LoggableKineticBlock<?>) {
            ((LoggableKineticBlock<?>) block).useItemOn(player.getItemInHand(hand), bs, level, clickedPos, player, hand, null);
            return InteractionResult.PASS;
        }
        return use(level, player, hand).getResult();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack usedStack = player.getItemInHand(usedHand);
        if (level instanceof ServerLevel slev) {
            //only for testing
            //List<LogEntry> entries = usedStack.get(SecurityItemComponents.LOGS);
            //entries.add(new LogEntry("was being used to test logs.", LogEntry.LogTarget.of(player), SecurityBlocks.THE_BLOCK, LogEntry.LogTime.now(slev)));
            //usedStack.set(SecurityItemComponents.LOGS, entries);
            //------
            player.openMenu(this, buf ->
                    ItemStack.STREAM_CODEC.encode(buf, usedStack)
            );
            return InteractionResultHolder.success(usedStack);
        }
        return InteractionResultHolder.pass(usedStack);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        ItemStack heldItem = player.getMainHandItem();
        return new LogMenu(SecurityMenus.LOG_MENU.get(), containerId, playerInventory, heldItem);
    }
}
