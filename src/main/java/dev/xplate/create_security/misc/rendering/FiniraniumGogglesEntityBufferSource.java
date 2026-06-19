package dev.xplate.create_security.misc.rendering;

import com.mojang.blaze3d.vertex.*;
import net.createmod.catnip.impl.client.render.ColoringVertexConsumer;
import net.createmod.catnip.outliner.Outline;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Optional;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class FiniraniumGogglesEntityBufferSource implements MultiBufferSource {
    private final MultiBufferSource.BufferSource bufferSource;
    public FiniraniumGogglesEntityBufferSource(BufferSource bufferSource) {
        super();
        this.bufferSource = bufferSource;
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        return new SolidColourVertexConsumer(bufferSource.getBuffer(renderType), 1f,1f,1f,1f);
//        return bufferSource.getBuffer(
//                RenderType.create("solid_colour_entity",
//                        DefaultVertexFormat.NEW_ENTITY,
//                        VertexFormat.Mode.QUADS,
//                        4194304,
//                        true,
//                        false,
//                        RenderType.CompositeState.builder()
//                                .setLightmapState(LIGHTMAP)
//                                .setTextureState(NO_TEXTURE)
//                                .setColorLogicState(NO_COLOR_LOGIC)
//                                .setTransparencyState(NO_TRANSPARENCY)
//                                .setOverlayState(OVERLAY)
//                                .setShaderState(RENDERTYPE_SOLID_SHADER)
//                                .createCompositeState(false))
//        );

    }

    public record SolidColourVertexConsumer(VertexConsumer delegate, float red, float green, float blue,
                                         float alpha) implements VertexConsumer {
        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            delegate.addVertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer setColor(int r, int g, int b, int a) {
            delegate.setColor(red, green, blue, alpha);
            delegate.setOverlay(OverlayTexture.pack(OverlayTexture.u(1.0F), 1));
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            delegate.setUv(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            delegate.setUv1(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            delegate.setUv2(LightTexture.FULL_BRIGHT,LightTexture.FULL_BRIGHT);
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            delegate.setNormal(x, y, z);
            return this;
        }
    }
}
