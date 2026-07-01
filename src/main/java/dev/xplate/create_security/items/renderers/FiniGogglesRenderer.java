package dev.xplate.create_security.items.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.xplate.create_security.reg.SecurityItemComponents;
import dev.xplate.create_security.reg.SecurityPartialModels;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

import static dev.xplate.create_security.CSecurity.res;

public class FiniGogglesRenderer extends CustomRenderedItemModelRenderer {

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        int offset = stack.get(SecurityItemComponents.EYE_OFFSET).offset();
        BakedModel bmodel = switch (offset) {
            case 1 -> SecurityPartialModels.FINI_GOGGLES_PLUS1.get();
            case 2 -> SecurityPartialModels.FINI_GOGGLES_PLUS2.get();
            case 3 -> SecurityPartialModels.FINI_GOGGLES_PLUS3.get();
            case -1 -> SecurityPartialModels.FINI_GOGGLES_MINUS1.get();
            case -2 -> SecurityPartialModels.FINI_GOGGLES_MINUS2.get();
            case -3 -> SecurityPartialModels.FINI_GOGGLES_MINUS3.get();
            default -> SecurityPartialModels.FINI_GOGGLES.get();
        };

        bmodel.applyTransform(transformType, ms, false);
        renderer.render(bmodel, light);
        ms.pushPose();
        ms.translate(0,offset/16f,0);
        renderer.renderGlowing(SecurityPartialModels.FINI_GOGGLES_LIT.get(), LightTexture.FULL_BRIGHT);
        ms.popPose();
    }
}
