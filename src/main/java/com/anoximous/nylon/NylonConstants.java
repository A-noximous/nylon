package com.anoximous.nylon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NylonConstants {
    private NylonConstants(){}
    public static final message MESSAGE = new message();
    public static final blockpos BLOCKPOS = new blockpos();
    public static final render RENDER = new render();
    public static boolean renderExpValue = false;
    public static boolean renderLivingEntityHealth = false;
}
class message {
    public final int Output, Error;

    public message(){
        this.Output = 1;
        this.Error = 2;
    }
}
class blockpos {
    BlockPos getCrosshair(MinecraftClient mc){
        assert mc.crosshairTarget != null;
        return new BlockPos(mc.crosshairTarget.getPos());
    }
    BlockPos getFeet(MinecraftClient mc, int os){
        assert mc.player != null;
        return (new BlockPos(new Vec3d(mc.player.getX(), mc.player.getY() + os, mc.player.getZ())));
    }
}
class render {
    void renderExpValue(boolean val) {
        NylonConstants.renderExpValue = val;}
    void renderLivingEntityHealth(boolean val) {
        NylonConstants.renderLivingEntityHealth = val;}
}