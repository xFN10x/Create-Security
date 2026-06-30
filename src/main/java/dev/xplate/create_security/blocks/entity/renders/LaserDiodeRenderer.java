package dev.xplate.create_security.blocks.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.xplate.create_security.blocks.LaserDiode;
import dev.xplate.create_security.blocks.entity.LaserDiodeEntity;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class LaserDiodeRenderer extends KineticBlockEntityRenderer<LaserDiodeEntity> {
    public LaserDiodeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    private float currentLength = 0;

    @Override
    protected void renderSafe(LaserDiodeEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        //probably could have figured this out myself, but this is a snippet from power loader
        Level level = be.getLevel();
        BlockState state = be.getBlockState();
        Direction direction = state
                .getValue(LaserDiode.FACING);
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        SuperByteBuffer shaftHalf =
                CachedBuffers.partialFacing(
                        SecurityPartialModels.DIODE_CENTER,
                        state,
                        direction.getOpposite()
                );
        if (!state.getValue(LaserDiode.RECEIVER))
            standardKineticRotationTransform(shaftHalf, be, light).renderInto(ms, vb);
        else
            return;
        if (!be.laserActive()) return;
        ms.pushPose();

        VertexConsumer buf = buffer.getBuffer(SecurityRenderTypes.LASER);
        Matrix4f pose = ms.last().pose();
        //create laser
        float half = (float) 7 / 16;
        float onePixel = 1f / 16f;
        float twoPixel = onePixel * 2;
        float speed = Mth.abs(be.getSpeed());
        float targetLength = be.getLength();
        float maxLength = be.getMaxLength();
        float length = targetLength < currentLength ? targetLength : Mth.lerp(0.01f + speed / 512f, currentLength, targetLength);
        currentLength = length;
        int r = 255;
        int a = 255;
        int endA = 0;
        int endR = (int) (((1 - (length / maxLength)) * r) + Mth.randomBetween(level.getRandom(), 0f, 10f));

        ms.translate(0.5, 0.5, 0.5);
        ms.mulPose(state.getValue(LaserDiode.FACING).getRotation());
        ms.translate(-0.5, -0.5, -0.5);

        {
            buf.addVertex(pose, half, length, half)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(endR, 0, 0, endA)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, length, half + twoPixel)
                    .setNormal(1, 1, 0)
                    .setUv(0, 0)
                    .setColor(endR, 0, 0, endA)
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
                    .setColor(endR, 0, 0, endA)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half, length, half)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(endR, 0, 0, endA)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
        }

        {
            buf.addVertex(pose, half, length, half + twoPixel)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(endR, 0, 0, endA)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, length, half)
                    .setNormal(1, 1, 0)
                    .setUv(0, 0)
                    .setColor(endR, 0, 0, endA)
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
                    .setColor(endR, 0, 0, endA)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half, length, half + twoPixel)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(endR, 0, 0, endA)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
        }
        if (be.isHittingReceiver()) {
            buf.addVertex(pose, half + twoPixel, length - twoPixel, half + twoPixel)
                    .setNormal(0, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, length - twoPixel, half)
                    .setNormal(1, 0, 0)
                    .setUv(0, 0)
                    .setColor(r, 0, 0, a)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half + twoPixel, length - twoPixel, half)
                    .setNormal(1, 1, 0)
                    .setUv(0, 0)
                    .setColor(endR, 0, 0, endA)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
            buf.addVertex(pose, half, length - twoPixel, half + twoPixel)
                    .setNormal(0, 1, 0)
                    .setUv(0, 0)
                    .setColor(endR, 0, 0, endA)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
        }

        ms.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(LaserDiodeEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 300;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull LaserDiodeEntity blockEntity) {
        return AABB.INFINITE;
    }
}
