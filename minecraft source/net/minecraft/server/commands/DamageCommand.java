/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DamageCommand
/*    */ {
/* 26 */   private static final SimpleCommandExceptionType ERROR_INVULNERABLE = new SimpleCommandExceptionType(Component.translatable("commands.damage.invulnerable"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 29 */     dispatcher.register(
/* 30 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("damage")
/* 31 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 32 */         .then(
/* 33 */           Commands.argument("target", EntityArgument.entity())
/* 34 */           .then((
/* 35 */             (RequiredArgumentBuilder)Commands.argument("amount", FloatArgumentType.floatArg(0.0F))
/* 36 */             .executes(c -> damage((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), FloatArgumentType.getFloat(c, "amount"), ((CommandSourceStack)c.getSource()).getLevel().damageSources().generic())))
/* 37 */             .then((
/* 38 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("damageType", ResourceArgument.resource(context, Registries.DAMAGE_TYPE))
/* 39 */               .executes(c -> damage((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), FloatArgumentType.getFloat(c, "amount"), new DamageSource(ResourceArgument.getResource(c, "damageType", Registries.DAMAGE_TYPE)))))
/* 40 */               .then(
/* 41 */                 Commands.literal("at")
/* 42 */                 .then(
/* 43 */                   Commands.argument("location", Vec3Argument.vec3())
/* 44 */                   .executes(c -> damage((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), FloatArgumentType.getFloat(c, "amount"), new DamageSource(ResourceArgument.getResource(c, "damageType", Registries.DAMAGE_TYPE), Vec3Argument.getVec3(c, "location")))))))
/*    */ 
/*    */               
/* 47 */               .then(
/* 48 */                 Commands.literal("by")
/* 49 */                 .then((
/* 50 */                   (RequiredArgumentBuilder)Commands.argument("entity", EntityArgument.entity())
/* 51 */                   .executes(c -> damage((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), FloatArgumentType.getFloat(c, "amount"), new DamageSource(ResourceArgument.getResource(c, "damageType", Registries.DAMAGE_TYPE), EntityArgument.getEntity(c, "entity")))))
/* 52 */                   .then(
/* 53 */                     Commands.literal("from")
/* 54 */                     .then(
/* 55 */                       Commands.argument("cause", EntityArgument.entity())
/* 56 */                       .executes(c -> damage((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), FloatArgumentType.getFloat(c, "amount"), new DamageSource(ResourceArgument.getResource(c, "damageType", Registries.DAMAGE_TYPE), EntityArgument.getEntity(c, "entity"), EntityArgument.getEntity(c, "cause"))))))))))));
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
/*    */   private static int damage(CommandSourceStack stack, Entity target, float amount, DamageSource source) throws CommandSyntaxException {
/* 68 */     if (target.hurtServer(stack.getLevel(), source, amount)) {
/* 69 */       stack.sendSuccess(() -> Component.translatable("commands.damage.success", new Object[] { Float.valueOf(amount), target.getDisplayName() }), true);
/* 70 */       return 1;
/*    */     } 
/*    */     
/* 73 */     throw ERROR_INVULNERABLE.create();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DamageCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */