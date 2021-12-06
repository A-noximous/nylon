package com.anoximous.nylon.mixin;

import com.anoximous.nylon.NylonConstants;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ExperienceOrbEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

@Mixin(ExperienceOrbEntityRenderer.class)
public abstract class OrbValueRenderMixin {
    @Shadow protected abstract int getBlockLight(ExperienceOrbEntity experienceOrbEntity, BlockPos blockPos);

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/entity/ExperienceOrbEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private void RenderOrbValueMixin(ExperienceOrbEntity experienceOrbEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (NylonConstants.renderExpValue)
            RenderOrbValue(experienceOrbEntity, matrixStack, vertexConsumerProvider);
    }

    private void RenderOrbValue(ExperienceOrbEntity experienceOrbEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int expValue = experienceOrbEntity.getExperienceAmount();
        float height = experienceOrbEntity.getHeight() + 0.5F;
        Text text = new LiteralText("")
                .append(new LiteralText("value: ").styled(style -> style.withColor(Formatting.WHITE)))
                .append(new LiteralText(""+expValue).styled(style -> style.withColor(Formatting.GREEN)));
        matrixStack.push();
        matrixStack.translate(0.0D, height, 0.0D);
        matrixStack.multiply(mc.getEntityRenderDispatcher().getRotation());
        matrixStack.scale(-0.025F, -0.025F, 0.025F);
        float bo = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
        int j = (int)(bo * 255.0F) << 24;
        TextRenderer textRenderer = mc.textRenderer;
        float h = (float)(-textRenderer.getWidth(text) / 2);
        Matrix4f matrices = matrixStack.peek().getModel();
        int light = getBlockLight(experienceOrbEntity, experienceOrbEntity.getBlockPos());
        textRenderer.draw(text, h, 0f, Color.WHITE.getRGB(), false, matrices, vertexConsumerProvider, true, j, light);
        matrixStack.pop();
    }
}