package dev.xplate.create_security.items.menus.screens;

import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import dev.xplate.create_security.items.menus.LogMenu;
import dev.xplate.create_security.reg.SecurityGUITexture;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.gui.widget.AbstractSimiWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

///some of the code here is based on {@link com.simibubi.create.content.logistics.filter.AbstractFilterScreen}
public class LogScreen extends AbstractSimiContainerScreen<LogMenu> {

    public int menuPosX;
    public int menuPosY = topPos;
    
    private final SecurityGUITexture bg = SecurityGUITexture.BG_LOGMENU;
    public LogScreen(LogMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowOffset(16 * 2, 0);
        setWindowSize(bg.getWidth() + 16 * 4, bg.getHeight());
        super.init();

        menuPosX = leftPos;
        menuPosY = topPos;
        
        LogCloseButton closeButton = new LogCloseButton(menuPosX + 7, menuPosY + 165 );
        closeButton.withCallback(() -> minecraft.player.closeContainer());
        
        addRenderableWidget(closeButton);
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
