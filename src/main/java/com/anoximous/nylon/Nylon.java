package com.anoximous.nylon;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Nylon implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Nylon");

    @Override
    public void onInitializeClient() {
        MinecraftClient mc = MinecraftClient.getInstance();
        posRegister(mc);
        platformRegister(mc);
        shrugRegister(mc);
        emoteRegister();
        nudgeRegister(mc);
        nylonRegister(mc);
        LOGGER.info("Initialized Nylon!");
    }

    public void posRegister(MinecraftClient mc) {
        //DONE: pos command
        assert mc.player != null;
        LiteralCommandNode<FabricClientCommandSource> posnode =
                ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("pos")
                        .then(ClientCommandManager.literal("say")
                                .then(ClientCommandManager.literal("block")
                                        .executes(ctx ->{
                                            HitResult hit = mc.crosshairTarget;
                                            assert hit != null;
                                            if (hit.getType() == HitResult.Type.BLOCK){
                                                BlockHitResult blockHit = (BlockHitResult) hit;
                                                BlockPos blockPos = blockHit.getBlockPos();
                                                mc.player.sendChatMessage(blockPos.toShortString());
                                            }
                                            else{
                                                sendNylonMessage("No Block in reach!", NylonConstants.MESSAGE.Error, mc, false);
                                            }
                                            return 1;
                                        }))
                                .executes(ctx -> {
                                    mc.player.sendChatMessage(mc.player.getBlockPos().toShortString());
                                    return 1;
                                }))
                        .then(ClientCommandManager.literal("copy")
                                .then(ClientCommandManager.literal("block")
                                        .executes(ctx ->{
                                            HitResult hit = mc.crosshairTarget;
                                            assert hit != null;
                                            if (hit.getType() == HitResult.Type.BLOCK){
                                                BlockHitResult blockHit = (BlockHitResult) hit;
                                                BlockPos blockPos = blockHit.getBlockPos();
                                                mc.keyboard.setClipboard(blockPos.toShortString());
                                                sendNylonMessage("Copied Target Block Coordinates!", NylonConstants.MESSAGE.Output, mc, false);
                                            }
                                            else{
                                                sendNylonMessage("No Block in reach!", NylonConstants.MESSAGE.Error, mc, false);
                                            }
                                            return 1;
                                        }))
                                .executes(ctx -> {
                                    mc.keyboard.setClipboard(mc.player.getBlockPos().toShortString());
                                    sendNylonMessage("Copied Current Coordinates!", NylonConstants.MESSAGE.Output, mc, false);
                                    return 1;
                                }))
                );

        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("sp").redirect(posnode.getChild("say")).executes(ctx -> {
            mc.player.sendChatMessage(mc.player.getBlockPos().toShortString());
            return 1;
        }));
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("cp").redirect(posnode.getChild("copy")).executes(ctx -> {
            mc.keyboard.setClipboard(mc.player.getBlockPos().toShortString());
            sendNylonMessage("Copied Current Coordinates!", NylonConstants.MESSAGE.Output, mc, false);
            return 1;
        }));
    }
    public void platformRegister(MinecraftClient mc) {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("platform")
                .executes(ctx ->{
                    placeBlock(mc, NylonConstants.BLOCKPOS.getFeet(mc, -1));
                    sendNylonMessage("Created Platform", NylonConstants.MESSAGE.Output, mc, true);
                    return 1;
                }));
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("plt")
                .executes(ctx -> {
                    placeBlock(mc, NylonConstants.BLOCKPOS.getFeet(mc, -1));
                    sendNylonMessage("Created Platform", NylonConstants.MESSAGE.Output, mc, true);
                    return 1;
                }));
    }
    public void shrugRegister(MinecraftClient mc) {
        assert mc.player != null;
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("shrug").executes(ctx -> {
            mc.player.sendChatMessage("¯\\_(ツ)_/¯");
            return 1;
        }));

    }
    public void emoteRegister() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("cmote")
                .then(ClientCommandManager.literal("yay")
                        .executes(ctx -> emote("╲(｡◕‿◕｡)╱")))
                .then(ClientCommandManager.literal("nice")
                        .executes(ctx -> emote("(๑˃̵ᴗ˂̵)و nice!")))
                .then(ClientCommandManager.literal("cry")
                        .executes(ctx -> emote("(╥_╥)"))
                        .then(ClientCommandManager.literal("bawling")
                                .executes(ctx -> emote("༼ ╥╥ ෴ ╥╥ ༽ ")))
                        .then(ClientCommandManager.literal("blubbering")
                                .executes(ctx -> emote("༼☯﹏☯༽")))
                        .then(ClientCommandManager.literal("snivelling")
                                .executes(ctx -> emote("(つ﹏⊂)")))
                        .then(ClientCommandManager.literal("lament")
                                .executes(ctx -> emote("（>﹏<）"))))
                .then(ClientCommandManager.literal("flip")
                        .executes(ctx -> emote("(╯°□°)╯︵ ┻━┻"))
                        .then(ClientCommandManager.literal("pissed")
                                .executes(ctx -> emote("(ノಠ益ಠ)ノ彡┻━┻")))
                        .then(ClientCommandManager.literal("lookin")
                                .executes(ctx -> emote("(┛ಠ_ಠ)┛彡┻━┻"))))
                .then(ClientCommandManager.literal("sus")
                        .executes(ctx -> emote("（・―・）"))
                        .then(ClientCommandManager.literal("mini")
                                .executes(ctx -> emote("・-・")))
                        .then(ClientCommandManager.literal("wide_open")
                                .executes(ctx -> emote("( ⊙‿⊙)")))
                        .then(ClientCommandManager.literal("bri")
                                .executes(ctx -> emote("(╭ರ_⊙)")))
                        .then(ClientCommandManager.literal("squint")
                                .executes(ctx -> emote("(≖-≖)")))
                        .then(ClientCommandManager.literal("hardcore")
                                .executes(ctx -> emote("（´◉◞⊖◟◉｀）")))) // that expression when you are really doubting someone and purse your lips
                .then(ClientCommandManager.literal("notplussed")
                        .executes(ctx -> emote("ب_ب"))) //the "I'm not angry, I'm just disappointed"
                .then(ClientCommandManager.literal("bear")
                        .executes(ctx -> emote("ʕ•ᴥ•ʔ"))
                        .then(ClientCommandManager.literal("left")
                                .executes(ctx -> emote("ʕ·ᴥ·　ʔ")))
                        .then(ClientCommandManager.literal("right")
                                .executes(ctx -> emote("ʕ　·ᴥ·ʔ")))
                        .then(ClientCommandManager.literal("fighting")
                                .executes(ctx -> emote("ʕง•ᴥ•ʔง")))
                        .then(ClientCommandManager.literal("nice")
                                .executes(ctx -> emote("ʕง ˃ᴥ˂ʔو nice!")))
                        .then(ClientCommandManager.literal("star")
                                .executes(ctx -> emote("ʕ•̀ω•́ʔ✧")))
                        .then(ClientCommandManager.literal("cry")
                                .executes(ctx -> emote("ʕ>⌓<｡ʔ")))
                        .then(ClientCommandManager.literal("interested")
                                .executes(ctx -> emote("ʕ◉ᴥ◉ʔ")))
                        .then(ClientCommandManager.literal("oh")
                                .executes(ctx -> emote("ʕ • ₒ • ʔ"))
                                .then(ClientCommandManager.literal("OH")
                                        .executes(ctx -> emote("ʕ • O • ʔ"))))
                        .then(ClientCommandManager.literal("bearception")
                                .executes(ctx -> emote("ʕ•ᴥ•ʔ ʕ·ᴥ·　ʔ ʕ　·ᴥ·ʔ ʕง•ᴥ•ʔง"))
                                .then(ClientCommandManager.literal("secret")
                                        .then(ClientCommandManager.literal("fu")
                                                .executes(ctx -> emote("╭∩╮ʕ•ᴥ•ʔ╭∩╮"))))))
                .then(ClientCommandManager.literal("lenny")
                        .executes(ctx -> emote("( ͡° ͜ʖ ͡°)"))
                        .then(ClientCommandManager.literal("stash")
                                .executes(ctx -> emote("( ͡°╭͜ʖ╮͡° )"))
                                .then(ClientCommandManager.literal("bigger")
                                        .executes(ctx -> emote("( ͡ʘ╭͜ʖ╮͡ʘ)"))))
                        .then(ClientCommandManager.literal("peepin")
                                .executes(ctx -> emote("┴┬┴┤( ͡° ͜ʖ├┬┴┬")))
                        .then(ClientCommandManager.literal("flip")
                                .executes(ctx -> emote("(ノ͡° ͜ʖ ͡°)ノ︵┻┻"))))
                .then(ClientCommandManager.literal("bird")
                        .executes(ctx -> emote("(•ө•)"))
                        .then(ClientCommandManager.literal("love")
                                .executes(ctx -> emote("(•ө•)♡"))))
                .then(ClientCommandManager.literal("goobers")
                        .then(ClientCommandManager.literal("gwah")
                                .executes(ctx -> emote(" ⊹⋛⋋( ՞ਊ ՞)⋌⋚⊹")))
                        .then(ClientCommandManager.literal("noesknows")
                                .executes(ctx -> emote("꒡ꆚ꒡")))
                        .then(ClientCommandManager.literal("salute")
                                .executes(ctx -> emote("(￣-￣)ゞ"))
                                .then(ClientCommandManager.literal("bird")
                                        .executes(ctx -> emote("（’◇’）ゞ"))))));

    }
    public void nudgeRegister(MinecraftClient mc) {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("nudge")
                .then(ClientCommandManager.literal("look")
                        .then(ClientCommandManager.argument("yaw", FloatArgumentType.floatArg())
                                .then(ClientCommandManager.argument("pitch", FloatArgumentType.floatArg())
                                        .executes(ctx -> {
                                            assert mc.player !=null;
                                            assert mc.getNetworkHandler() !=null;
                                            mc.player.refreshPositionAndAngles(mc.player.getX(), mc.player.getY(), mc.player.getZ(), FloatArgumentType.getFloat(ctx, "yaw"), FloatArgumentType.getFloat(ctx, "pitch"));
                                            return 1;
                                        }))))
                .then(ClientCommandManager.literal("move")
                        .then(ClientCommandManager.argument("x", FloatArgumentType.floatArg())
                                .then(ClientCommandManager.argument("z", FloatArgumentType.floatArg())
                                        .executes(ctx -> {
                                            assert mc.player !=null;
                                            assert mc.getNetworkHandler() !=null;
                                            float x = FloatArgumentType.getFloat(ctx, "x");
                                            float z = FloatArgumentType.getFloat(ctx, "z");
                                            double newx = mc.player.getBlockX()+x;
                                            double newz = mc.player.getBlockZ()+z;
                                            mc.player.refreshPositionAndAngles(newx, mc.player.getY(), newz, mc.player.getYaw(), mc.player.getPitch());
                                            return 1;
                                        }))))
        );}
    public void nylonRegister(MinecraftClient mc) {
        assert mc.player != null;
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("nylon")
                        .then(ClientCommandManager.literal("help")
                            .executes(ctx -> {
                                mc.player.sendMessage(new LiteralText("Nylon Commands:").styled(style -> style.withColor(Formatting.GRAY).withBold(true)), false);
                                mc.player.sendMessage(new LiteralText("/pos - copy or send a chat message containing the players or target blocks coordinates"), false);
                                mc.player.sendMessage(new LiteralText("/platform - place a block below the players feet"), false);
                                mc.player.sendMessage(new LiteralText("/shrug - print ¯\\_(ツ)_/¯ to chat"), false);
                                mc.player.sendMessage(new LiteralText("/emote - send a variety of ASCII emoticons in chat"), false);
                                mc.player.sendMessage(new LiteralText("/nudge - tweak you position and rotation precisely"), false);
                                mc.player.sendMessage(new LiteralText("/nylon - more options relating to the mod"), false);

                                mc.player.sendMessage(new LiteralText("Nylon Aliases:").styled(style -> style.withColor(Formatting.GRAY).withBold(true)), false);
                                mc.player.sendMessage(new LiteralText("/sp - alias for /pos say"), false);
                                mc.player.sendMessage(new LiteralText("/cp - alias for /pos copy"), false);
                                mc.player.sendMessage(new LiteralText("/plt - alias for /platform"), false);
                                return 1;
                        }))
                        .then(ClientCommandManager.literal("showXpOrbValue")
                                .executes(ctx -> {
                                    NylonConstants.RENDER.renderExpValue(!NylonConstants.renderExpValue);
                                    sendNylonMessage("Toggled XP Orb Value Rendering", NylonConstants.MESSAGE.Output, mc, true);
                                    return 1;
                        }))
                        .then(ClientCommandManager.literal("showLivingEntityHealth")
                                .executes(ctx -> {
                                    NylonConstants.RENDER.renderLivingEntityHealth(!NylonConstants.renderLivingEntityHealth);
                                    sendNylonMessage("Toggled Living Entity Health Rendering", NylonConstants.MESSAGE.Output, mc, true);
                                    return 1;
                        }))
        );

    }


    public void sendNylonMessage(String message, int type, MinecraftClient mc, boolean actionBar) {
        Text text;
        String prefix = actionBar? "":"[Nylon]: ";
        assert mc.player !=null;
        switch (type) {
            case 1 -> {
                text = new LiteralText("")
                        .append(new LiteralText(prefix).styled(style -> style.withColor(Formatting.GRAY).withBold(true)))
                        .append(new LiteralText(message));
                mc.player.sendMessage(text, actionBar);
            }
            case 2 -> {
                text = new LiteralText("")
                        .append(new LiteralText(prefix).styled(style -> style.withColor(Formatting.GRAY).withBold(true)))
                        .append(new LiteralText(message).styled(style -> style.withColor(Formatting.RED)));
                mc.player.sendMessage(text, actionBar);
            }
        }
    }
    private static int emote(String text) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendChatMessage(text);
        return 0;
    }
    public void placeBlock(MinecraftClient mc, BlockPos loc){
        assert mc.player != null;
        BlockHitResult blockHitResult = new BlockHitResult(mc.player.getPos(), mc.player.getHorizontalFacing(), loc, false);
        assert mc.interactionManager != null;
        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, blockHitResult);
    }
}