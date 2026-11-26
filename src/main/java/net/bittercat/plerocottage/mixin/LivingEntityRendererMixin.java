package net.bittercat.plerocottage.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bittercat.plerocottage.effects.FlattenState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(
        method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V",
            shift = At.Shift.AFTER)
    )
    private void pleroCottage$flattenModel(LivingEntity entity, float entityYaw, float partialTicks,
                                         PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                         CallbackInfo ci) {
        if (((FlattenState) entity).pleroCottage$isFlattened()) {
            poseStack.scale(1.3F, 0.5F, 1.3F);
        }
    }
}
