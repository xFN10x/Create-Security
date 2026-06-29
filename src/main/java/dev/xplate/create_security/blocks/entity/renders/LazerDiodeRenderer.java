package dev.xplate.create_security.blocks.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.xplate.create_security.blocks.LazerDiode;
import dev.xplate.create_security.blocks.entity.LazerDiodeEntity;
import dev.xplate.create_security.misc.rendering.SecurityRenderTypes;
import dev.xplate.create_security.reg.SecurityPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class LazerDiodeRenderer extends KineticBlockEntityRenderer<LazerDiodeEntity> {
    public LazerDiodeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    private float currentLength = 0;

    @Override
    protected void renderSafe(LazerDiodeEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //probably could have figured this out myself, but this is a snippet from power loader
        BlockState state = be.getBlockState();
        Direction direction = state
                .getValue(LazerDiode.FACING);
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        SuperByteBuffer shaftHalf =
                CachedBuffers.partialFacing(
                        SecurityPartialModels.DIODE_CENTER,
                        state,
                        direction.getOpposite()
                );
        if (!state.getValue(LazerDiode.RECEIVER))
            standardKineticRotationTransform(shaftHalf, be, light).renderInto(ms, vb);
        else
            return;
        if (!be.isSpeedRequirementFulfilled()) return;
        ms.pushPose();

        VertexConsumer buf = buffer.getBuffer(SecurityRenderTypes.LAZER);
        Matrix4f pose = ms.last().pose();
        //create laser
        float half = (float) 7 /16;
        float onePixel = 1f / 16f;
        float twoPixel = onePixel * 2;
        float speed = Mth.abs(be.getSpeed());
        float length = Mth.lerp(0.01f + speed/512f, currentLength, speed/4);
        currentLength = length;
        int r = 255;
        int a = 255;

        ms.translate(0.5, 0.5, 0.5);
        ms.mulPose(state.getValue(LazerDiode.FACING).getRotation());
        ms.translate(-0.5, -0.5, -0.5);

        {
            buf.addVertex(pose, half, length, half)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, length, half + twoPixel)
                    .setNormal(1, 1, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, 0, half + twoPixel)
                    .setNormal(1, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half, 0, half)
                    .setNormal(0, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
        }

        {
            buf.addVertex(pose, half, 0, half)
                    .setNormal(0, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, 0, half + twoPixel)
                    .setNormal(1, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, length, half + twoPixel)
                    .setNormal(1, 1, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half, length, half)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
        }

        {
            buf.addVertex(pose, half, length, half + twoPixel)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, length, half)
                    .setNormal(1, 1, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, 0, half)
                    .setNormal(1, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half, 0, half + twoPixel)
                    .setNormal(0, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
        }

        {
            buf.addVertex(pose, half, 0, half + twoPixel)
                    .setNormal(0, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, 0, half)
                    .setNormal(1, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, length, half)
                    .setNormal(1, 1, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half, length, half + twoPixel)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
        }

        ms.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(LazerDiodeEntity blockEntity) {
        return true;
    }
    @Override
    public int getViewDistance() {
        return 300;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull LazerDiodeEntity blockEntity) {
        return AABB.INFINITE;
    }
}
