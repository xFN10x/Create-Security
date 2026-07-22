package dev.xplate.create_security.blocks.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.xplate.create_security.CSSecurityConfigs;
import dev.xplate.create_security.blocks.ChunkDetector;
import dev.xplate.create_security.blocks.LaserDiode;
import dev.xplate.create_security.blocks.entity.ChunkDetectorEntity;
import dev.xplate.create_security.blocks.entity.LaserDiodeEntity;
import dev.xplate.create_security.misc.rendering.SecurityRenderTypes;
import dev.xplate.create_security.reg.SecurityPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.HashMap;

public class ChunkDetectorRenderer extends KineticBlockEntityRenderer<ChunkDetectorEntity> {
    public ChunkDetectorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(ChunkDetectorEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.translucent());

        int reqMet = ((ChunkDetector) state.getBlock()).getMinimumRequiredSpeedLevel().compareTo(IRotate.SpeedLevel.of(be.getSpeed()));
        int sacle = be.getSpeed() > 0 ? 1 : -1;
        SuperByteBuffer shaftHalf =
                CachedBuffers.partialFacing(
                        reqMet == 0 ? SecurityPartialModels.CHUNK_DETECTOR_CENTER : SecurityPartialModels.CHUNK_DETECTOR_CENTER_ALT,
                        state,
                        sacle == 1 ? Direction.NORTH : Direction.SOUTH
                );
        //ms.scale(-sacle,sacle,1);
        
        standardKineticRotationTransform(shaftHalf, be, light).renderInto(ms, vb);
    }
}
