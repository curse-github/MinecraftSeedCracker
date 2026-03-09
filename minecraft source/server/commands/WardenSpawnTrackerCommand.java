/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ 
/*    */ public class WardenSpawnTrackerCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 20 */     dispatcher.register(
/* 21 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("warden_spawn_tracker")
/* 22 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 23 */         .then(
/* 24 */           Commands.literal("clear")
/* 25 */           .executes(c -> resetTracker((CommandSourceStack)c.getSource(), ImmutableList.of(((CommandSourceStack)c.getSource()).getPlayerOrException())))))
/*    */         
/* 27 */         .then(
/* 28 */           Commands.literal("set")
/* 29 */           .then(
/* 30 */             Commands.argument("warning_level", IntegerArgumentType.integer(0, 4))
/* 31 */             .executes(c -> setWarningLevel((CommandSourceStack)c.getSource(), ImmutableList.of(((CommandSourceStack)c.getSource()).getPlayerOrException()), IntegerArgumentType.getInteger(c, "warning_level"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setWarningLevel(CommandSourceStack source, Collection<? extends Player> players, int warningLevel) {
/* 38 */     for (Player player : players) {
/* 39 */       player.getWardenSpawnTracker().ifPresent(wardenSpawnTracker -> wardenSpawnTracker.setWarningLevel(warningLevel));
/*    */     }
/*    */     
/* 42 */     if (players.size() == 1) {
/* 43 */       source.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.set.success.single", new Object[] { ((Player)players.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 45 */       source.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.set.success.multiple", new Object[] { Integer.valueOf(players.size()) }), true);
/*    */     } 
/*    */     
/* 48 */     return players.size();
/*    */   }
/*    */   
/*    */   private static int resetTracker(CommandSourceStack source, Collection<? extends Player> players) {
/* 52 */     for (Player player : players) {
/* 53 */       player.getWardenSpawnTracker().ifPresent(WardenSpawnTracker::reset);
/*    */     }
/*    */     
/* 56 */     if (players.size() == 1) {
/* 57 */       source.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.clear.success.single", new Object[] { ((Player)players.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 59 */       source.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.clear.success.multiple", new Object[] { Integer.valueOf(players.size()) }), true);
/*    */     } 
/*    */     
/* 62 */     return players.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\WardenSpawnTrackerCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */