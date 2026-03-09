/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ParticleArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParticleCommand
/*    */ {
/* 31 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.particle.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 34 */     dispatcher.register(
/* 35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("particle")
/* 36 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 37 */         .then((
/* 38 */           (RequiredArgumentBuilder)Commands.argument("name", ParticleArgument.particle(context))
/* 39 */           .executes(c -> sendParticles((CommandSourceStack)c.getSource(), ParticleArgument.getParticle(c, "name"), ((CommandSourceStack)c.getSource()).getPosition(), Vec3.ZERO, 0.0F, 0, false, ((CommandSourceStack)c.getSource()).getServer().getPlayerList().getPlayers())))
/* 40 */           .then((
/* 41 */             (RequiredArgumentBuilder)Commands.argument("pos", Vec3Argument.vec3())
/* 42 */             .executes(c -> sendParticles((CommandSourceStack)c.getSource(), ParticleArgument.getParticle(c, "name"), Vec3Argument.getVec3(c, "pos"), Vec3.ZERO, 0.0F, 0, false, ((CommandSourceStack)c.getSource()).getServer().getPlayerList().getPlayers())))
/* 43 */             .then(
/* 44 */               Commands.argument("delta", Vec3Argument.vec3(false))
/* 45 */               .then(
/* 46 */                 Commands.argument("speed", FloatArgumentType.floatArg(0.0F))
/* 47 */                 .then((
/* 48 */                   (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("count", IntegerArgumentType.integer(0))
/* 49 */                   .executes(c -> sendParticles((CommandSourceStack)c.getSource(), ParticleArgument.getParticle(c, "name"), Vec3Argument.getVec3(c, "pos"), Vec3Argument.getVec3(c, "delta"), FloatArgumentType.getFloat(c, "speed"), IntegerArgumentType.getInteger(c, "count"), false, ((CommandSourceStack)c.getSource()).getServer().getPlayerList().getPlayers())))
/* 50 */                   .then((
/* 51 */                     (LiteralArgumentBuilder)Commands.literal("force")
/* 52 */                     .executes(c -> sendParticles((CommandSourceStack)c.getSource(), ParticleArgument.getParticle(c, "name"), Vec3Argument.getVec3(c, "pos"), Vec3Argument.getVec3(c, "delta"), FloatArgumentType.getFloat(c, "speed"), IntegerArgumentType.getInteger(c, "count"), true, ((CommandSourceStack)c.getSource()).getServer().getPlayerList().getPlayers())))
/* 53 */                     .then(
/* 54 */                       Commands.argument("viewers", EntityArgument.players())
/* 55 */                       .executes(c -> sendParticles((CommandSourceStack)c.getSource(), ParticleArgument.getParticle(c, "name"), Vec3Argument.getVec3(c, "pos"), Vec3Argument.getVec3(c, "delta"), FloatArgumentType.getFloat(c, "speed"), IntegerArgumentType.getInteger(c, "count"), true, EntityArgument.getPlayers(c, "viewers"))))))
/*    */ 
/*    */                   
/* 58 */                   .then((
/* 59 */                     (LiteralArgumentBuilder)Commands.literal("normal")
/* 60 */                     .executes(c -> sendParticles((CommandSourceStack)c.getSource(), ParticleArgument.getParticle(c, "name"), Vec3Argument.getVec3(c, "pos"), Vec3Argument.getVec3(c, "delta"), FloatArgumentType.getFloat(c, "speed"), IntegerArgumentType.getInteger(c, "count"), false, ((CommandSourceStack)c.getSource()).getServer().getPlayerList().getPlayers())))
/* 61 */                     .then(
/* 62 */                       Commands.argument("viewers", EntityArgument.players())
/* 63 */                       .executes(c -> sendParticles((CommandSourceStack)c.getSource(), ParticleArgument.getParticle(c, "name"), Vec3Argument.getVec3(c, "pos"), Vec3Argument.getVec3(c, "delta"), FloatArgumentType.getFloat(c, "speed"), IntegerArgumentType.getInteger(c, "count"), false, EntityArgument.getPlayers(c, "viewers")))))))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int sendParticles(CommandSourceStack source, ParticleOptions particle, Vec3 pos, Vec3 delta, float speed, int count, boolean force, Collection<ServerPlayer> players) throws CommandSyntaxException {
/* 75 */     int result = 0;
/*    */     
/* 77 */     for (ServerPlayer player : players) {
/* 78 */       if (source.getLevel().sendParticles(player, particle, force, false, pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, speed)) {
/* 79 */         result++;
/*    */       }
/*    */     } 
/*    */     
/* 83 */     if (result == 0) {
/* 84 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 87 */     source.sendSuccess(() -> Component.translatable("commands.particle.success", new Object[] { BuiltInRegistries.PARTICLE_TYPE.getKey(particle.getType()).toString() }), true);
/*    */     
/* 89 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ParticleCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */