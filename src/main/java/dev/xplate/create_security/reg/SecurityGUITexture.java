package dev.xplate.create_security.reg;

import net.createmod.catnip.gui.TextureSheetSegment;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static dev.xplate.create_security.CSSecurity.res;

public enum SecurityGUITexture implements ScreenElement, TextureSheetSegment {
    BG_LOGMENU("log", 0, 0, 247, 256),
    LOG_SCROLL("log_sprites", 248,0,8,143),

    ICON_BIG_ARROW("log_sprites", 0,26, 122,7),
    ICON_DOWN_ARROW("log_sprites", 0,50,18,18),
    ICON_UP_ARROW("log_sprites", 18,50,18,18),
    BUTTONBG_WIDE_NORMAL("log_sprites", 0,0, 193,13),
    BUTTONBG_WIDE_SELECT("log_sprites", 0,13, 193,13),
    ;

    public static final int FONT_COLOR = 0x575F7A;

    public final ResourceLocation location;
    private final int width;
    private final int height;
    private final int startX;
    private final int startY;

    SecurityGUITexture(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    SecurityGUITexture(String location, int startX, int startY, int width, int height) {
        this.location = res("textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public ResourceLocation getLocation() {
        return location;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(location, x, y, startX, startY, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(graphics, c, x, y, startX, startY, width, height);
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
