package dev.xplate.create_security.misc.rendering;

import com.mojang.blaze3d.platform.Window;
import dev.xplate.create_security.reg.SecurityEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

import static dev.xplate.create_security.CSecurity.res;

public class FiniraniumGogglesPostProcessingHandler {

    //TODO: to make entities visible, change entity.isvisabletoplayer
    public boolean shouldRenderFiniraniumGoggles() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player.hasEffect(SecurityEffects.END_SICKNESS);
    }

    public final PostChain postFilter;

    public FiniraniumGogglesPostProcessingHandler(Minecraft mc) throws IOException {
        ResourceLocation postJsonLoc = res("shaders/post/finiranium_goggles.json");
        postFilter = new PostChain(
                mc.getTextureManager(),
                mc.getResourceManager(),
                mc.getMainRenderTarget(),
                postJsonLoc
        );
        Window win = mc.getWindow();
        postFilter.resize(win.getWidth(), win.getHeight());
    }

    public void resize(int wid, int hei) {
        if (postFilter == null)
            return;
        postFilter.resize(wid, hei);
    }
}
