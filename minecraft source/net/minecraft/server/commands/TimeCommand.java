/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.TimeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ public class TimeCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 17 */     dispatcher.register(
/* 18 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("time")
/* 19 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 20 */         .then((
/* 21 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("set")
/* 22 */           .then(
/* 23 */             Commands.literal("day")
/* 24 */             .executes(c -> setTime((CommandSourceStack)c.getSource(), 1000))))
/* 25 */           .then(
/* 26 */             Commands.literal("noon")
/* 27 */             .executes(c -> setTime((CommandSourceStack)c.getSource(), 6000))))
/* 28 */           .then(
/* 29 */             Commands.literal("night")
/* 30 */             .executes(c -> setTime((CommandSourceStack)c.getSource(), 13000))))
/* 31 */           .then(
/* 32 */             Commands.literal("midnight")
/* 33 */             .executes(c -> setTime((CommandSourceStack)c.getSource(), 18000))))
/* 34 */           .then(
/* 35 */             Commands.argument("time", TimeArgument.time())
/* 36 */             .executes(c -> setTime((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "time"))))))
/*    */ 
/*    */         
/* 39 */         .then(
/* 40 */           Commands.literal("add")
/* 41 */           .then(
/* 42 */             Commands.argument("time", TimeArgument.time())
/* 43 */             .executes(c -> addTime((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "time"))))))
/*    */ 
/*    */         
/* 46 */         .then((
/* 47 */           (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("query")
/* 48 */           .then(
/* 49 */             Commands.literal("daytime")
/* 50 */             .executes(c -> queryTime((CommandSourceStack)c.getSource(), getDayTime(((CommandSourceStack)c.getSource()).getLevel())))))
/*    */           
/* 52 */           .then(
/* 53 */             Commands.literal("gametime")
/* 54 */             .executes(c -> queryTime((CommandSourceStack)c.getSource(), (int)(((CommandSourceStack)c.getSource()).getLevel().getGameTime() % 2147483647L)))))
/*    */           
/* 56 */           .then(
/* 57 */             Commands.literal("day")
/* 58 */             .executes(c -> queryTime((CommandSourceStack)c.getSource(), (int)(((CommandSourceStack)c.getSource()).getLevel().getDayCount() % 2147483647L))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   private static int getDayTime(ServerLevel level) { return (int)(level.getDayTime() % 24000L); }
/*    */ 
/*    */   
/*    */   private static int queryTime(CommandSourceStack source, int time) {
/* 69 */     source.sendSuccess(() -> Component.translatable("commands.time.query", new Object[] { Integer.valueOf(time) }), false);
/* 70 */     return time;
/*    */   }
/*    */   
/*    */   public static int setTime(CommandSourceStack source, int time) {
/* 74 */     for (ServerLevel level : source.getServer().getAllLevels()) {
/* 75 */       level.setDayTime(time);
/*    */     }
/* 77 */     source.getServer().forceTimeSynchronization();
/* 78 */     source.sendSuccess(() -> Component.translatable("commands.time.set", new Object[] { Integer.valueOf(time) }), true);
/* 79 */     return getDayTime(source.getLevel());
/*    */   }
/*    */   
/*    */   public static int addTime(CommandSourceStack source, int time) {
/* 83 */     for (ServerLevel level : source.getServer().getAllLevels()) {
/* 84 */       level.setDayTime(level.getDayTime() + time);
/*    */     }
/* 86 */     source.getServer().forceTimeSynchronization();
/* 87 */     int newTime = getDayTime(source.getLevel());
/* 88 */     source.sendSuccess(() -> Component.translatable("commands.time.set", new Object[] { Integer.valueOf(newTime) }), true);
/* 89 */     return newTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TimeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */