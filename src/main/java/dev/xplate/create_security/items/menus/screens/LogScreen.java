package dev.xplate.create_security.items.menus.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.util.RealmsUtil;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.ScreenOverlay;
import dev.xplate.create_security.datagen.CSSDataGen;
import dev.xplate.create_security.items.menus.LogMenu;
import dev.xplate.create_security.misc.LogEntry;
import dev.xplate.create_security.reg.SecurityGUITexture;
import dev.xplate.create_security.reg.SecurityItemComponents;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.gui.widget.AbstractSimiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;

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
    private int maxScroll = 0;
    private int scroll = maxScroll;
    private int goTo = 0;
    private int minScroll = 0;
    private final int minScrollOffset = 10;
    private final ScreenOverlay scrollArea = new ScreenOverlay(100);
    private final static int entryHeight = 32;
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
        
        LogCloseButton closeButton = new LogCloseButton(((bg.getWidth()/2) - (LogCloseButton.BG_NORMAL.getWidth()/2)) + leftPos, (scrollAreaBottom.get()) + topPos);
        closeButton.withCallback(() -> minecraft.player.closeContainer());

        IconButton goToNewest = new IconButton(scrollAreaRight.get() + 4, scrollAreaBottom.get() - 18, SecurityGUITexture.ICON_DOWN_ARROW);
        goToNewest.withCallback(() -> goTo = -1);
        goToNewest.setToolTip(CSSDataGen.goToNewestComp.getB());

        IconButton goToOldest = new IconButton(scrollAreaRight.get() + 4, scrollAreaTop.get(), SecurityGUITexture.ICON_UP_ARROW);
        goToOldest.withCallback(() -> goTo = 1);
        goToOldest.setToolTip(CSSDataGen.goToOldestComp.getB());

        //addRenderableWidget(scrollInput);
        addRenderableWidget(goToOldest);
        addRenderableWidget(goToNewest);
        
        addRenderableWidget(scrollArea);
        addRenderableWidget(closeButton);

        if (firstInit) {
            int y = 8;
            for (LogEntry entry : menu.contentHolder.get(SecurityItemComponents.LOGS)) {
                entries.add(new LogEntryWidget(entry, 0, y, scrollAreaLeft, scrollPosTop));
                y += entryHeight;
            }
            goTo = -1;
        }
        firstInit = false;
    }

    protected void renderLogEntries(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        //guiGraphics.drawString(font, String.valueOf(scroll), 0, 0, Color.BLACK.getRGB());
        entries.forEach(widget -> widget.doRender(guiGraphics, mouseX, mouseY, partialTick));
        int lastY = 0;
        if (!entries.isEmpty()) {
            lastY = entries.getLast().getY();
        }
        int secretHeight = lastY + entryHeight - 5;
        int textWidth = font.width(secret);
        guiGraphics.drawString(font, secret, (scrollViewWidth/2) - (textWidth/2), secretHeight, 0x44000000, false);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        scroll = (int) Math.clamp(scroll + (scrollY * 4), minScroll, maxScroll);
        return true;
        //return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
//        int invX = getLeftOfCentered(PLAYER_INVENTORY.getWidth());
//        int invY = topPos + bg.getHeight() + 4;
//        renderPlayerInventory(guiGraphics, invX, invY);
        scrollPosTop.set(18 + topPos + scroll);
        minScroll = Math.min((scrollViewHeight - (entries.size() * entryHeight)) - 3, 0) - minScrollOffset;
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

        entries.forEach(entry -> entry.renderTooltip(guiGraphics, mouseX, mouseY, partialTick));
    }

    public static class LogEntryWidget extends AbstractSimiWidget {
        private final Minecraft mc = Minecraft.getInstance();

        private final Font font = mc.font;
        private final int color = FastColor.ARGB32.color(128, 128, 128);
        private final int lineColor = FastColor.ARGB32.color(229, 229, 229);
        private final Instant time;
        private final LogEntry entry;
        private final AtomicInteger scrollAreaUp;
        private final AtomicInteger scrollAreaLeft;
        private final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        //private volatile PlayerSkin skin;

        //private static final HashMap<String, PlayerSkin> skinCache = new HashMap<>();

        protected LogEntryWidget(LogEntry entry, int x, int y, AtomicInteger scrollAreaLeft, AtomicInteger scrollAreaUp) {
            super(x, y, 156, entryHeight);
            long rlSeconds = entry.time().rlSecond();
            this.time = Instant.ofEpochSecond(rlSeconds);
            toolTip.add(Component.literal(
                            entry.target().strEntityName() + " " + entry.message()
                    )
            );
            toolTip.add(Component.literal(
                            "Real Time: " + format.format(Date.from(time))
                    )
            );
            toolTip.add(Component.literal(
                                    "Game Time: Day " + Math.floor((double) entry.time().igt() /24000L)
                    )
            );
            this.entry = entry;
            this.scrollAreaUp = scrollAreaUp;
            this.scrollAreaLeft = scrollAreaLeft;
//            String uuid = entry.playerUUID();
//            if (!uuid.isBlank()) {
//                if (skinCache.containsKey(uuid)) {
//                    skin = skinCache.get(uuid);
//                } else {
//                    new Thread(() -> {
//                        try {
//                            GameProfile profile = new GameProfile(UUID.fromString(uuid), entry.targetName());
//                            PlayerSkin playerSkin = mc.getSkinManager().getInsecureSkin(profile);
//                            skinCache.put(uuid, playerSkin);
//                            skin = playerSkin;
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    }).start();
//                }
//            } else skin = null;t
        }

        public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.renderTooltip(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        protected void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            int left = getX() + scrollAreaLeft.get();
            int top = getY() + scrollAreaUp.get();
            int right = getRight() + scrollAreaLeft.get();
            int bottom = getBottom() + scrollAreaUp.get();
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
                RealmsUtil.renderPlayerFace(graphics, x, y - ((headSize - 8) / 2), headSize, UUID.fromString(entry.target().plrUUID()));
                x += headSize + 4;
            }

            String name = "§l" + entry.target().strEntityName();
            graphics.drawString(font, name, x, y, color, false);
            x += font.width(name) + 4;
            int dateX = x;
            int dateY = y + 2;
            x = 4;
            y += 12;
            graphics.drawString(font, "..." + entry.message().trim(), x, y, color, false);
            y += 10;

            graphics.hLine(0, 154, y, lineColor);
            pose.popPose();

            pose.pushPose();
            float dateScale = 0.7f;
            pose.scale(dateScale, dateScale, dateScale);
            pose.translate((dateX + 10) / dateScale, (dateY / dateScale), 0 / dateScale);
            graphics.drawString(font, format.format(Date.from(time)), 0, 0, color, false);
            pose.popPose();
        }
    }

    public static class LogCloseButton extends AbstractSimiWidget {

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
