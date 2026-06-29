package dev.xplate.create_security.misc.rendering;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class SecurityRenderTypes {

    public static final RenderType LAZER = RenderType.create(
            "lazer",
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            786432, true, true,
            RenderType.CompositeState.builder()
            .setLightmapState(LIGHTMAP)
            .setShaderState(RenderType.RENDERTYPE_TRANSLUCENT_SHADER)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setTransparencyState(ADDITIVE_TRANSPARENCY)
            .setOutputState(TRANSLUCENT_TARGET)
            .createCompositeState(true));
}
