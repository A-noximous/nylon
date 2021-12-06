package com.anoximous.nylon.mixin;

import com.anoximous.nylon.NylonConstants;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityHealthRenderMixin {

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private void RenderLivingEntityHealthMixin(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (NylonConstants.renderLivingEntityHealth)
            RenderLivingEntityHealth(livingEntity);

    }

    private void RenderLivingEntityHealth(LivingEntity livingEntity) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if(livingEntity.equals(mc.player)){return;}
        assert mc.player != null;
        RaycastContext rc = new RaycastContext(mc.player.getEyePos(), livingEntity.getEyePos(), RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.ANY, mc.player);
        BlockHitResult block = mc.player.world.raycast(rc);
        if (block.getType() == HitResult.Type.BLOCK){return;}
        float health = (float) (Math.round(livingEntity.getHealth() * 100.0) / 100.0);
        float maxHealth = livingEntity.getMaxHealth();
        Formatting color = (health/maxHealth>0.75)? Formatting.GREEN: (health/maxHealth>0.50)? Formatting.YELLOW : (health/maxHealth>0.25)? Formatting.GOLD : Formatting.RED;
        Text text = new LiteralText(":").append(new LiteralText(" "+health).styled(style -> style.withColor(color)));
        if(livingEntity.hasCustomName()){
            livingEntity.setCustomName(Objects.requireNonNull(livingEntity.getCustomName()).copy().append(text));
            return;
        }
        String Name = livingEntity.getType().getUntranslatedName();
        Name = Name.substring(0, 1).toUpperCase() + Name.substring(1);
        MutableText name = new LiteralText(Name).styled(style -> style.withColor(Formatting.WHITE));
        text = name.append(text);
        livingEntity.setCustomName(text);
        livingEntity.setCustomNameVisible(true);
    }
}