/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.LevelData;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetSpawnCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 31 */     dispatcher.register(
/* 32 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawnpoint")
/* 33 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 34 */         .executes(c -> setSpawn((CommandSourceStack)c.getSource(), Collections.singleton(((CommandSourceStack)c.getSource()).getPlayerOrException()), BlockPos.containing(((CommandSourceStack)c.getSource()).getPosition()), WorldCoordinates.ZERO_ROTATION)))
/* 35 */         .then((
/* 36 */           (RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players())
/* 37 */           .executes(c -> setSpawn((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), BlockPos.containing(((CommandSourceStack)c.getSource()).getPosition()), WorldCoordinates.ZERO_ROTATION)))
/* 38 */           .then((
/* 39 */             (RequiredArgumentBuilder)Commands.argument("pos", BlockPosArgument.blockPos())
/* 40 */             .executes(c -> setSpawn((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), BlockPosArgument.getSpawnablePos(c, "pos"), WorldCoordinates.ZERO_ROTATION)))
/* 41 */             .then(
/* 42 */               Commands.argument("rotation", RotationArgument.rotation())
/* 43 */               .executes(c -> setSpawn((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), BlockPosArgument.getSpawnablePos(c, "pos"), RotationArgument.getRotation(c, "rotation")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setSpawn(CommandSourceStack source, Collection<ServerPlayer> targets, BlockPos pos, Coordinates rotation) {
/* 51 */     ResourceKey<Level> dimension = source.getLevel().dimension();
/* 52 */     Vec2 rotationVector = rotation.getRotation(source);
/* 53 */     float yaw = Mth.wrapDegrees(rotationVector.y);
/* 54 */     float pitch = Mth.clamp(rotationVector.x, -90.0F, 90.0F);
/* 55 */     for (ServerPlayer target : targets) {
/* 56 */       target.setRespawnPosition(new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(dimension, pos, yaw, pitch), true), false);
/*    */     }
/*    */     
/* 59 */     String dimensionName = dimension.identifier().toString();
/* 60 */     if (targets.size() == 1) {
/* 61 */       source.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.single", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()), Float.valueOf(yaw), Float.valueOf(pitch), dimensionName, ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 63 */       source.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.multiple", new Object[] { Integer.valueOf(pos.getX()), Integer.valueOf(pos.getY()), Integer.valueOf(pos.getZ()), Float.valueOf(yaw), Float.valueOf(pitch), dimensionName, Integer.valueOf(targets.size()) }), true);
/*    */     } 
/*    */     
/* 66 */     return targets.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\SetSpawnCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */