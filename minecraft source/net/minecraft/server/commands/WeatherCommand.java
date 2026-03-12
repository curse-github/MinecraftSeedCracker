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
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class WeatherCommand {
/*    */   private static final int DEFAULT_TIME = -1;
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 19 */     dispatcher.register(
/* 20 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("weather")
/* 21 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 22 */         .then((
/* 23 */           (LiteralArgumentBuilder)Commands.literal("clear")
/* 24 */           .executes(c -> setClear((CommandSourceStack)c.getSource(), -1)))
/* 25 */           .then(
/* 26 */             Commands.argument("duration", TimeArgument.time(1))
/* 27 */             .executes(c -> setClear((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "duration"))))))
/*    */ 
/*    */         
/* 30 */         .then((
/* 31 */           (LiteralArgumentBuilder)Commands.literal("rain")
/* 32 */           .executes(c -> setRain((CommandSourceStack)c.getSource(), -1)))
/* 33 */           .then(
/* 34 */             Commands.argument("duration", TimeArgument.time(1))
/* 35 */             .executes(c -> setRain((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "duration"))))))
/*    */ 
/*    */         
/* 38 */         .then((
/* 39 */           (LiteralArgumentBuilder)Commands.literal("thunder")
/* 40 */           .executes(c -> setThunder((CommandSourceStack)c.getSource(), -1)))
/* 41 */           .then(
/* 42 */             Commands.argument("duration", TimeArgument.time(1))
/* 43 */             .executes(c -> setThunder((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "duration"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int getDuration(CommandSourceStack source, int input, IntProvider defaultDistribution) {
/* 50 */     if (input == -1) {
/* 51 */       return defaultDistribution.sample(source.getServer().overworld().getRandom());
/*    */     }
/* 53 */     return input;
/*    */   }
/*    */   
/*    */   private static int setClear(CommandSourceStack source, int duration) {
/* 57 */     source.getServer().overworld().setWeatherParameters(getDuration(source, duration, ServerLevel.RAIN_DELAY), 0, false, false);
/* 58 */     source.sendSuccess(() -> Component.translatable("commands.weather.set.clear"), true);
/* 59 */     return duration;
/*    */   }
/*    */   
/*    */   private static int setRain(CommandSourceStack source, int duration) {
/* 63 */     source.getServer().overworld().setWeatherParameters(0, getDuration(source, duration, ServerLevel.RAIN_DURATION), true, false);
/* 64 */     source.sendSuccess(() -> Component.translatable("commands.weather.set.rain"), true);
/* 65 */     return duration;
/*    */   }
/*    */   
/*    */   private static int setThunder(CommandSourceStack source, int duration) {
/* 69 */     source.getServer().overworld().setWeatherParameters(0, getDuration(source, duration, ServerLevel.THUNDER_DURATION), true, true);
/* 70 */     source.sendSuccess(() -> Component.translatable("commands.weather.set.thunder"), true);
/* 71 */     return duration;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\WeatherCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */