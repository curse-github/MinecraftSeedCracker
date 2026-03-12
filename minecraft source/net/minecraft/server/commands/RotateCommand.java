/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RotateCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 25 */     dispatcher.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("rotate")
/* 27 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 28 */         .then((
/* 29 */           (RequiredArgumentBuilder)Commands.argument("target", EntityArgument.entity())
/* 30 */           .then(
/* 31 */             Commands.argument("rotation", RotationArgument.rotation())
/* 32 */             .executes(c -> rotate((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), RotationArgument.getRotation(c, "rotation")))))
/*    */           
/* 34 */           .then((
/* 35 */             (LiteralArgumentBuilder)Commands.literal("facing")
/* 36 */             .then(
/* 37 */               Commands.literal("entity")
/* 38 */               .then((
/* 39 */                 (RequiredArgumentBuilder)Commands.argument("facingEntity", EntityArgument.entity())
/* 40 */                 .executes(c -> rotate((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), new LookAt.LookAtEntity(EntityArgument.getEntity(c, "facingEntity"), EntityAnchorArgument.Anchor.FEET))))
/* 41 */                 .then(
/* 42 */                   Commands.argument("facingAnchor", EntityAnchorArgument.anchor())
/* 43 */                   .executes(c -> rotate((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), new LookAt.LookAtEntity(EntityArgument.getEntity(c, "facingEntity"), EntityAnchorArgument.getAnchor(c, "facingAnchor"))))))))
/*    */ 
/*    */ 
/*    */             
/* 47 */             .then(
/* 48 */               Commands.argument("facingLocation", Vec3Argument.vec3())
/* 49 */               .executes(c -> rotate((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), new LookAt.LookAtPosition(Vec3Argument.getVec3(c, "facingLocation"))))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int rotate(CommandSourceStack source, Entity entity, Coordinates rotation) {
/* 57 */     Vec2 rot = rotation.getRotation(source);
/* 58 */     float relativeOrAbsoluteYRot = rotation.isYRelative() ? (rot.y - entity.getYRot()) : rot.y;
/* 59 */     float relativeOrAbsoluteXRot = rotation.isXRelative() ? (rot.x - entity.getXRot()) : rot.x;
/* 60 */     entity.forceSetRotation(relativeOrAbsoluteYRot, rotation.isYRelative(), relativeOrAbsoluteXRot, rotation.isXRelative());
/* 61 */     source.sendSuccess(() -> Component.translatable("commands.rotate.success", new Object[] { entity.getDisplayName() }), true);
/* 62 */     return 1;
/*    */   }
/*    */   
/*    */   private static int rotate(CommandSourceStack source, Entity entity, LookAt facing) {
/* 66 */     facing.perform(source, entity);
/* 67 */     source.sendSuccess(() -> Component.translatable("commands.rotate.success", new Object[] { entity.getDisplayName() }), true);
/* 68 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\RotateCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */