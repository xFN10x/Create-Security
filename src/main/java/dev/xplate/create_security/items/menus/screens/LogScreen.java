package dev.xplate.create_security.items.menus.screens;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.ScreenOverlay;
import dev.xplate.create_security.items.menus.LogMenu;
import dev.xplate.create_security.misc.LogEntry;
import dev.xplate.create_security.reg.SecurityGUITexture;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.gui.widget.AbstractSimiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

///some of the code here is based on {@link com.simibubi.create.content.logistics.filter.AbstractFilterScreen}
public class LogScreen extends AbstractSimiContainerScreen<LogMenu> {

    public int menuPosX;
    public int menuPosY = topPos;

    private final SecurityGUITexture bg = SecurityGUITexture.BG_LOGMENU;
    private final SecurityGUITexture scrollPart = SecurityGUITexture.LOG_SCROLL;
    
    //private final ScrollInput scrollInput = new ScrollInput(13,18, 168, 137);
    private double scroll = 0;
    private final ScreenOverlay scrollArea = new ScreenOverlay(100);
    
    public LogScreen(LogMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowOffset(16 * 2, 0);
        setWindowSize(bg.getWidth() + 16 * 4, bg.getHeight());
        super.init();

        //scrollInput.calling(integer -> scroll += integer);
        
        menuPosX = leftPos;
        menuPosY = topPos;
        
        LogCloseButton closeButton = new LogCloseButton(menuPosX + 7, menuPosY + 165 );
        closeButton.withCallback(() -> minecraft.player.closeContainer());
        
        //addRenderableWidget(scrollInput);
        addRenderableWidget(scrollArea);
        addRenderableWidget(closeButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        scroll = Math.min(scroll + (scrollY * 4), 0);
        return true;
        //return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
//        int invX = getLeftOfCentered(PLAYER_INVENTORY.getWidth());
//        int invY = topPos + bg.getHeight() + 4;
//        renderPlayerInventory(guiGraphics, invX, invY);

        bg.render(guiGraphics, menuPosX, menuPosY);

        GuiGameElement.of(menu.contentHolder).<GuiGameElement
                        .GuiRenderBuilder>at(215 + menuPosX, (menuPosY + (float) bg.getHeight() /2) - 16 *2, -200)
                .scale(4)
                .render(guiGraphics);

        PoseStack pose = guiGraphics.pose();
        
        guiGraphics.blit(scrollPart.getLocation(), 13 + leftPos, 18 + topPos, scrollPart.getStartX(), (int) -scroll, scrollPart.getWidth(), 137);
        
        pose.pushPose();
        pose.translate(13 + leftPos, 18 + topPos + scroll,0);

        guiGraphics.drawString(font, String.valueOf(scroll), 0, 0, Color.BLACK.getRGB());
        //CSSecurity.LOGGER.info(String.valueOf(scroll));
        pose.popPose();
    }

    public static class LogEntryWidget extends AbstractSimiWidget {
        private final Minecraft mc = Minecraft.getInstance();

        private final Font font = mc.font;
        private final int color = 0x808080;
        private final LogEntry entry;
        
        protected LogEntryWidget(LogEntry entry, int x, int y) {
            super(x, y);
            this.entry = entry;
        }

        @Override
        protected void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            int x = getX();
            int y = getY();
            
            graphics.drawString(font, "•", x, y,color);
            if (entry.isTargetPlayer()) {
                mc.getSkinManager().getOrLoad(new GameProfile());
            }
        }
    }

    public static class LogCloseButton extends AbstractSimiWidget {

        private static final SecurityGUITexture BG_NORMAL = SecurityGUITexture.BUTTONBG_WIDE_NORMAL;
        private static final SecurityGUITexture BG_HOVERED = SecurityGUITexture.BUTTONBG_WIDE_SELECT;
        private static final SecurityGUITexture ICON = SecurityGUITexture.ICON_BIG_ARROW;

        protected LogCloseButton(int x, int y) {
            super(x, y, BG_NORMAL.getWidth(), BG_NORMAL.getHeight());
        }

        @Override
        protected void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                isHovered = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
                
                SecurityGUITexture bg = isHovered ? BG_HOVERED : BG_NORMAL;
                
                bg.render(graphics, getX(), getY());
                int iconW = (bg.getWidth() - ICON.getWidth()) / 2;
                int iconH = (bg.getHeight() - ICON.getHeight()) / 2;
                ICON.render(graphics, getX() + iconW, getY() + iconH);
            }
        }
    }
}
