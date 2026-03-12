/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceKeyArgument;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RecipeCommand
/*    */ {
/* 24 */   private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.recipe.give.failed"));
/* 25 */   private static final SimpleCommandExceptionType ERROR_TAKE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.recipe.take.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 28 */     dispatcher.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("recipe")
/* 30 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 31 */         .then(
/* 32 */           Commands.literal("give")
/* 33 */           .then((
/* 34 */             (RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players())
/* 35 */             .then(
/* 36 */               Commands.argument("recipe", ResourceKeyArgument.key(Registries.RECIPE))
/* 37 */               .executes(c -> giveRecipes((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Collections.singleton(ResourceKeyArgument.getRecipe(c, "recipe"))))))
/*    */             
/* 39 */             .then(
/* 40 */               Commands.literal("*")
/* 41 */               .executes(c -> giveRecipes((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ((CommandSourceStack)c.getSource()).getServer().getRecipeManager().getRecipes()))))))
/*    */ 
/*    */ 
/*    */         
/* 45 */         .then(
/* 46 */           Commands.literal("take")
/* 47 */           .then((
/* 48 */             (RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players())
/* 49 */             .then(
/* 50 */               Commands.argument("recipe", ResourceKeyArgument.key(Registries.RECIPE))
/* 51 */               .executes(c -> takeRecipes((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), Collections.singleton(ResourceKeyArgument.getRecipe(c, "recipe"))))))
/*    */             
/* 53 */             .then(
/* 54 */               Commands.literal("*")
/* 55 */               .executes(c -> takeRecipes((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ((CommandSourceStack)c.getSource()).getServer().getRecipeManager().getRecipes()))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int giveRecipes(CommandSourceStack source, Collection<ServerPlayer> players, Collection<RecipeHolder<?>> recipes) throws CommandSyntaxException {
/* 63 */     int success = 0;
/*    */     
/* 65 */     for (ServerPlayer player : players) {
/* 66 */       success += player.awardRecipes(recipes);
/*    */     }
/*    */     
/* 69 */     if (success == 0) {
/* 70 */       throw ERROR_GIVE_FAILED.create();
/*    */     }
/*    */     
/* 73 */     if (players.size() == 1) {
/* 74 */       source.sendSuccess(() -> Component.translatable("commands.recipe.give.success.single", new Object[] { Integer.valueOf(recipes.size()), ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 76 */       source.sendSuccess(() -> Component.translatable("commands.recipe.give.success.multiple", new Object[] { Integer.valueOf(recipes.size()), Integer.valueOf(players.size()) }), true);
/*    */     } 
/*    */     
/* 79 */     return success;
/*    */   }
/*    */   
/*    */   private static int takeRecipes(CommandSourceStack source, Collection<ServerPlayer> players, Collection<RecipeHolder<?>> recipes) throws CommandSyntaxException {
/* 83 */     int success = 0;
/*    */     
/* 85 */     for (ServerPlayer player : players) {
/* 86 */       success += player.resetRecipes(recipes);
/*    */     }
/*    */     
/* 89 */     if (success == 0) {
/* 90 */       throw ERROR_TAKE_FAILED.create();
/*    */     }
/*    */     
/* 93 */     if (players.size() == 1) {
/* 94 */       source.sendSuccess(() -> Component.translatable("commands.recipe.take.success.single", new Object[] { Integer.valueOf(recipes.size()), ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 96 */       source.sendSuccess(() -> Component.translatable("commands.recipe.take.success.multiple", new Object[] { Integer.valueOf(recipes.size()), Integer.valueOf(players.size()) }), true);
/*    */     } 
/*    */     
/* 99 */     return success;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\RecipeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */