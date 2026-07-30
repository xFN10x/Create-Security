package dev.xplate.create_security.items.menus.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.util.RealmsUtil;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.ScreenOverlay;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.xplate.create_security.datagen.CSSDataGen;
import dev.xplate.create_security.items.menus.LogMenu;
import dev.xplate.create_security.misc.LogEntry;
import dev.xplate.create_security.misc.Utils;
import dev.xplate.create_security.reg.SecurityGUITexture;
import dev.xplate.create_security.reg.SecurityItemComponents;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.gui.widget.AbstractSimiWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.sql.Date;
import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/// some of the code here is based on {@link com.simibubi.create.content.logistics.filter.AbstractFilterScreen}
public class LogScreen extends AbstractSimiContainerScreen<LogMenu> {
    private final SecurityGUITexture bg = SecurityGUITexture.BG_LOGMENU;
    private final SecurityGUITexture scrollPart = SecurityGUITexture.LOG_SCROLL;

    //private final ScrollInput scrollInput = new ScrollInput(13,18, 168, 137);
    private final int maxScroll = 0;
    public int scroll = maxScroll;
    private int goTo = 0;
    private int minScroll = 0;
    private final int minScrollOffset = 16;
    private final ScreenOverlay scrollArea = new ScreenOverlay(100);
    private final static int entryHeight = 32;
    private final static int entryOffset = 2;
    private final static int scrollViewHeight = 205;
    private final static int scrollViewWidth = 201;
    private final ArrayList<LogEntryWidget> entries = new ArrayList<>();
    private final String secret = CSSDataGen.getLogBottomText().getB().getString();

    public final AtomicInteger scrollAreaLeft = new AtomicInteger();
    public final AtomicInteger scrollAreaTop = new AtomicInteger();
    public final AtomicInteger scrollAreaBottom = new AtomicInteger();
    public final AtomicInteger scrollAreaRight = new AtomicInteger();
    public final AtomicInteger scrollPosTop = new AtomicInteger();

    public LogScreen(LogMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }


    private boolean firstInit = true;

    @Override
    protected void init() {
        setWindowOffset(16 * 2, 0);
        setWindowSize(bg.getWidth() + 16 * 4, bg.getHeight());
        super.init();

        scrollAreaLeft.set(13 + leftPos);
        scrollAreaTop.set(18 + topPos);
        scrollAreaBottom.set(205 + scrollAreaTop.get());
        scrollAreaRight.set(201 + scrollAreaLeft.get());

        LogCloseButton closeButton = new LogCloseButton((((bg.getWidth() / 2) - 8)- (LogCloseButton.BG_NORMAL.getWidth() / 2)) + leftPos, scrollAreaBottom.get() + 10);
        closeButton.withCallback(() -> minecraft.player.closeContainer());

        IconButton goToNewest = new IconButton(scrollAreaRight.get() + 4, scrollAreaBottom.get() - 18, SecurityGUITexture.ICON_DOWN_ARROW);
        goToNewest.withCallback(() -> goTo = -1);
        goToNewest.setToolTip(CSSDataGen.goToNewestComp.getB());

        IconButton goToOldest = new IconButton(scrollAreaRight.get() + 4, scrollAreaTop.get(), SecurityGUITexture.ICON_UP_ARROW);
        goToOldest.withCallback(() -> goTo = 1);
        goToOldest.setToolTip(CSSDataGen.goToOldestComp.getB());

        //addRenderableWidget(scrollInput);

        if (firstInit) {
            int y = 8;
            for (LogEntry entry : menu.contentHolder.get(SecurityItemComponents.LOGS)) {
                entries.add(new LogEntryWidget(entry, 0, y, scrollAreaLeft, scrollPosTop));
                y += entryHeight + entryOffset;
            }
            goTo = -1;
        }
        firstInit = false;
        
        addRenderableWidget(scrollArea);
        addRenderableWidget(closeButton);
        addRenderableWidget(goToOldest);
        addRenderableWidget(goToNewest);
    }

    protected void renderLogEntries(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        //guiGraphics.drawString(font, String.valueOf(scroll), 0, 0, Color.BLACK.getRGB());
        entries.forEach(widget -> {
            widget.doRender(guiGraphics, mouseX, mouseY, partialTick);
            //guiGraphics.fill(widget.getLeft(), widget.getTop(), widget.getRight(), widget.getBottom(), 0xff000000);
        });
        int lastY = 0;
        if (!entries.isEmpty()) {
            lastY = entries.getLast().getY();
        }
        int secretHeight = lastY + entryHeight - entryOffset;
        int textWidth = font.width(secret);
        guiGraphics.drawString(font, secret, (scrollViewWidth / 2) - (textWidth / 2), secretHeight, 0x44000000, false);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        scroll = (int) Math.clamp(scroll + (scrollY * 8), minScroll, maxScroll);
        return true;
        //return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
//        int invX = getLeftOfCentered(PLAYER_INVENTORY.getWidth());
//        int invY = topPos + bg.getHeight() + 4;
//        renderPlayerInventory(guiGraphics, invX, invY);
        scrollPosTop.set(18 + topPos + scroll);
        minScroll = Math.min((scrollViewHeight - (entries.size() * (entryHeight + entryOffset))) - 3, 0) - minScrollOffset;
        if (goTo > 0) {
            scroll = maxScroll;
            goTo = 0;
        } else if (goTo < 0) {
            scroll = minScroll + minScrollOffset;
            goTo = 0;
        }

        bg.render(guiGraphics, leftPos, topPos);

        GuiGameElement.of(menu.contentHolder).<GuiGameElement
                        .GuiRenderBuilder>at(bg.getWidth() + leftPos, (topPos + (float) bg.getHeight() / 2) - 16 * 2, -200)
                .scale(4)
                .render(guiGraphics);

        PoseStack pose = guiGraphics.pose();
        
        guiGraphics.blit(scrollPart.getLocation(), scrollAreaLeft.get(), scrollAreaTop.get(), scrollPart.getStartX(), -scroll, scrollPart.getWidth(), 205);

        guiGraphics.enableScissor(scrollAreaLeft.get(), scrollAreaTop.get(), scrollAreaRight.get(), scrollAreaBottom.get());
        pose.pushPose();
        pose.translate(scrollAreaLeft.get(), scrollPosTop.get(), 0);
        renderLogEntries(guiGraphics, partialTick, mouseX, mouseY);
        pose.popPose();
        guiGraphics.disableScissor();

        for (LogEntryWidget entry : entries) {
            entry.renderTooltip(guiGraphics, mouseX, mouseY, partialTick);
        }
    }
    
    public class LogEntryWidget extends AbstractSimiWidget {
        private final Minecraft mc = Minecraft.getInstance();

        private final Font font = mc.font;
        private final int color = FastColor.ARGB32.color(128, 128, 128);
        private final int lineColor = FastColor.ARGB32.color(229, 229, 229);
        private final Instant time;
        private final LogEntry entry;
        private final AtomicInteger scrollAreaUp;
        private final AtomicInteger scrollAreaLeft;
        private final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        private final String name;
        private final BlockState targetState;
        
        private final static int yOffset = 6;
        //private volatile PlayerSkin skin;

        //private static final HashMap<String, PlayerSkin> skinCache = new HashMap<>();

        protected LogEntryWidget(LogEntry entry, int x, int y, AtomicInteger scrollAreaLeft, AtomicInteger scrollAreaUp) {
            super(x, y, 156, entryHeight);
            long rlSeconds = entry.time().rlSecond();
            this.time = Instant.ofEpochSecond(rlSeconds);
            int timeColor = FastColor.ARGB32.color(155, 155, 155);
            int fromColor = FastColor.ARGB32.color(120, 120, 120);
            toolTip.add(Component.literal(
                            entry.target().strEntityName() + " " + entry.message()
                    )
            );
            toolTip.add(Component.literal("From: ").append(entry.getSourceBlock().getName()).withStyle(ChatFormatting.ITALIC).withColor(fromColor));
            toolTip.add(Component.empty());
            toolTip.add(Component.literal(
                            "Real Time: " + format.format(Date.from(time))
                    ).withColor(timeColor)
            );
            toolTip.add(Component.literal(
                            "Game Time: Day " + Math.floor((double) entry.time().igt() / 24000L)
                    ).withColor(timeColor)
            );
            this.entry = entry;
            this.scrollAreaUp = scrollAreaUp;
            this.scrollAreaLeft = scrollAreaLeft;
            this.name = "§l" + (!entry.target().isTargetInvisible() ? entry.target().strEntityName() : "§kInvisible§r");
            this.targetState = entry.getSourceBlockState();
        }

        public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.renderTooltip(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        protected void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if (getY() + scroll < -entryHeight || getY() + scroll > scrollAreaBottom.get()) return;
            int left = getLeft();
            int top = getTop();
            int right = getRight();
            int bottom = getBottom();
            isHovered = mouseX >= left && mouseY >= top && mouseX <= right && mouseY <= bottom;
            PoseStack pose = graphics.pose();
            int x = getX();
            int y = getY();
            pose.pushPose();
            float scale = 1;
            pose.translate(10, 0, 0);
            pose.scale(scale, scale, scale);
            graphics.drawString(font, "•", x, y, color, false);
            x += 8;
            if (entry.target().isTargetPlayer()) {
                //graphics.blit(skin.texture(), x, y, 8, 8, 8, 8, 64, 64);
                int headSize = 12;
                Utils.renderPlayerFace(graphics, x, y - ((headSize - 8) / 2), headSize, entry.target());
                x += headSize + 4;
            }

            graphics.drawString(font, name, x, y, color, false);
            x += font.width(name) + 4;
            int dateX = x;
            int dateY = y + 2;
            x = 4;
            y += 12;
            graphics.drawString(font, "..." + entry.message().trim(), x, y, color, false);
            y += 10;

            graphics.hLine(0, scrollViewWidth - 16, y, lineColor);
            pose.popPose();

            pose.pushPose();
            float dateScale = 0.7f;
            pose.scale(dateScale, dateScale, dateScale);
            pose.translate((dateX + 10) / dateScale, (dateY / dateScale), 0 / dateScale);
            graphics.drawString(font, format.format(Date.from(time)), 0, 0, color, false);
            pose.popPose();
            PoseTransformStack trans = TransformStack.of(pose);
            int blockScale = 12;
            trans.pushPose()
                    .translate(scrollViewWidth - blockScale * 2, dateY + 4, 200)
                    .scale(blockScale)
                    .rotateXDegrees(-22)
                    .rotateYDegrees(45);
            GuiGameElement.of(targetState).render(graphics);
            trans.popPose();
        }

        @Override
        public int getBottom() {
            return super.getBottom() + scrollAreaUp.get() - yOffset;
        }

        @Override
        public int getRight() {
            return super.getRight() + scrollAreaLeft.get();
        }

        public int getTop() {
            return getY() + scrollAreaUp.get() - yOffset;
        }

        public int getLeft() {
            return getX() + scrollAreaLeft.get();
        }
    }

    public class LogCloseButton extends AbstractSimiWidget {

        public static final SecurityGUITexture BG_NORMAL = SecurityGUITexture.BUTTONBG_WIDE_NORMAL;
        public static final SecurityGUITexture BG_HOVERED = SecurityGUITexture.BUTTONBG_WIDE_SELECT;
        public static final SecurityGUITexture ICON = SecurityGUITexture.ICON_BIG_ARROW;

        protected LogCloseButton(int x, int y) {
            super(x, y, BG_NORMAL.getWidth(), BG_NORMAL.getHeight());
        }

        @Override
        protected void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                isHovered = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + super.width && mouseY < getY() + super.height;

                SecurityGUITexture bg = isHovered ? BG_HOVERED : BG_NORMAL;

                bg.render(graphics, getX(), getY());
                int iconW = (bg.getWidth() - ICON.getWidth()) / 2;
                int iconH = (bg.getHeight() - ICON.getHeight()) / 2;
                ICON.render(graphics, getX() + iconW, getY() + iconH);
            }
        }
    }
}
