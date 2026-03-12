/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.ServerTickRateManager;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ 
/*     */ 
/*     */ public class TickCommand
/*     */ {
/*     */   private static final float MAX_TICKRATE = 10000.0F;
/*  25 */   private static final String DEFAULT_TICKRATE = String.valueOf(20);
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  28 */     dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tick")
/*  29 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  30 */         .then(Commands.literal("query")
/*  31 */           .executes(c -> tickQuery((CommandSourceStack)c.getSource()))))
/*  32 */         .then(Commands.literal("rate")
/*  33 */           .then(Commands.argument("rate", FloatArgumentType.floatArg(1.0F, 10000.0F))
/*  34 */             .suggests((c, b) -> SharedSuggestionProvider.suggest(new String[] { DEFAULT_TICKRATE }, b))
/*  35 */             .executes(c -> setTickingRate((CommandSourceStack)c.getSource(), FloatArgumentType.getFloat(c, "rate"))))))
/*  36 */         .then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("step")
/*  37 */           .executes(c -> step((CommandSourceStack)c.getSource(), 1)))
/*  38 */           .then(Commands.literal("stop")
/*  39 */             .executes(c -> stopStepping((CommandSourceStack)c.getSource()))))
/*  40 */           .then(Commands.argument("time", TimeArgument.time(1))
/*  41 */             .suggests((c, b) -> SharedSuggestionProvider.suggest(new String[] { "1t", "1s" }, b))
/*  42 */             .executes(c -> step((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "time"))))))
/*  43 */         .then(((LiteralArgumentBuilder)Commands.literal("sprint")
/*  44 */           .then(Commands.literal("stop")
/*  45 */             .executes(c -> stopSprinting((CommandSourceStack)c.getSource()))))
/*  46 */           .then(Commands.argument("time", TimeArgument.time(1))
/*  47 */             .suggests((c, b) -> SharedSuggestionProvider.suggest(new String[] { "60s", "1d", "3d" }, b))
/*  48 */             .executes(c -> sprint((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "time"))))))
/*  49 */         .then(Commands.literal("unfreeze").executes(c -> setFreeze((CommandSourceStack)c.getSource(), false))))
/*  50 */         .then(Commands.literal("freeze").executes(c -> setFreeze((CommandSourceStack)c.getSource(), true))));
/*     */   }
/*     */ 
/*     */   
/*  54 */   private static String nanosToMilisString(long nanos) { return String.format(Locale.ROOT, "%.1f", new Object[] { Float.valueOf((float)nanos / (float)TimeUtil.NANOSECONDS_PER_MILLISECOND) }); }
/*     */ 
/*     */   
/*     */   private static int setTickingRate(CommandSourceStack source, float rate) {
/*  58 */     ServerTickRateManager manager = source.getServer().tickRateManager();
/*  59 */     manager.setTickRate(rate);
/*  60 */     String tickRateString = String.format(Locale.ROOT, "%.1f", new Object[] { Float.valueOf(rate) });
/*  61 */     source.sendSuccess(() -> Component.translatable("commands.tick.rate.success", new Object[] { tickRateString }), true);
/*  62 */     return (int)rate;
/*     */   }
/*     */   
/*     */   private static int tickQuery(CommandSourceStack source) {
/*  66 */     ServerTickRateManager manager = source.getServer().tickRateManager();
/*  67 */     String busyTime = nanosToMilisString(source.getServer().getAverageTickTimeNanos());
/*     */     
/*  69 */     float tickRate = manager.tickrate();
/*  70 */     String tickRateString = String.format(Locale.ROOT, "%.1f", new Object[] { Float.valueOf(tickRate) });
/*  71 */     if (manager.isSprinting()) {
/*  72 */       source.sendSuccess(() -> Component.translatable("commands.tick.status.sprinting"), false);
/*  73 */       source.sendSuccess(() -> Component.translatable("commands.tick.query.rate.sprinting", new Object[] { tickRateString, busyTime }), false);
/*     */     } else {
/*  75 */       if (manager.isFrozen()) {
/*  76 */         source.sendSuccess(() -> Component.translatable("commands.tick.status.frozen"), false);
/*     */       }
/*  78 */       else if (manager.nanosecondsPerTick() < source.getServer().getAverageTickTimeNanos()) {
/*  79 */         source.sendSuccess(() -> Component.translatable("commands.tick.status.lagging"), false);
/*     */       } else {
/*  81 */         source.sendSuccess(() -> Component.translatable("commands.tick.status.running"), false);
/*     */       } 
/*     */       
/*  84 */       String milliSecondsPerTickTarget = nanosToMilisString(manager.nanosecondsPerTick());
/*  85 */       source.sendSuccess(() -> Component.translatable("commands.tick.query.rate.running", new Object[] { tickRateString, busyTime, milliSecondsPerTickTarget }), false);
/*     */     } 
/*     */     
/*  88 */     long[] samples = Arrays.copyOf(source.getServer().getTickTimesNanos(), source.getServer().getTickTimesNanos().length);
/*  89 */     Arrays.sort(samples);
/*  90 */     String p50 = nanosToMilisString(samples[samples.length / 2]);
/*  91 */     String p95 = nanosToMilisString(samples[(int)(samples.length * 0.95D)]);
/*  92 */     String p99 = nanosToMilisString(samples[(int)(samples.length * 0.99D)]);
/*     */     
/*  94 */     source.sendSuccess(() -> Component.translatable("commands.tick.query.percentiles", new Object[] { p50, p95, p99, Integer.valueOf(samples.length) }), false);
/*  95 */     return (int)tickRate;
/*     */   }
/*     */   
/*     */   private static int sprint(CommandSourceStack source, int time) {
/*  99 */     boolean interrupted = source.getServer().tickRateManager().requestGameToSprint(time);
/* 100 */     if (interrupted) {
/* 101 */       source.sendSuccess(() -> Component.translatable("commands.tick.sprint.stop.success"), true);
/*     */     }
/* 103 */     source.sendSuccess(() -> Component.translatable("commands.tick.status.sprinting"), true);
/* 104 */     return 1;
/*     */   }
/*     */   
/*     */   private static int setFreeze(CommandSourceStack source, boolean freeze) {
/* 108 */     ServerTickRateManager manager = source.getServer().tickRateManager();
/* 109 */     if (freeze) {
/* 110 */       if (manager.isSprinting()) {
/* 111 */         manager.stopSprinting();
/*     */       }
/* 113 */       if (manager.isSteppingForward()) {
/* 114 */         manager.stopStepping();
/*     */       }
/*     */     } 
/* 117 */     manager.setFrozen(freeze);
/* 118 */     if (freeze) {
/* 119 */       source.sendSuccess(() -> Component.translatable("commands.tick.status.frozen"), true);
/*     */     } else {
/* 121 */       source.sendSuccess(() -> Component.translatable("commands.tick.status.running"), true);
/*     */     } 
/* 123 */     return freeze ? 1 : 0;
/*     */   }
/*     */   
/*     */   private static int step(CommandSourceStack source, int advance) {
/* 127 */     ServerTickRateManager manager = source.getServer().tickRateManager();
/* 128 */     boolean success = manager.stepGameIfPaused(advance);
/* 129 */     if (success) {
/* 130 */       source.sendSuccess(() -> Component.translatable("commands.tick.step.success", new Object[] { Integer.valueOf(advance) }), true);
/*     */     } else {
/* 132 */       source.sendFailure(Component.translatable("commands.tick.step.fail"));
/*     */     } 
/* 134 */     return 1;
/*     */   }
/*     */   
/*     */   private static int stopStepping(CommandSourceStack source) {
/* 138 */     ServerTickRateManager manager = source.getServer().tickRateManager();
/* 139 */     boolean success = manager.stopStepping();
/* 140 */     if (success) {
/* 141 */       source.sendSuccess(() -> Component.translatable("commands.tick.step.stop.success"), true);
/* 142 */       return 1;
/*     */     } 
/* 144 */     source.sendFailure(Component.translatable("commands.tick.step.stop.fail"));
/* 145 */     return 0;
/*     */   }
/*     */   
/*     */   private static int stopSprinting(CommandSourceStack source) {
/* 149 */     ServerTickRateManager manager = source.getServer().tickRateManager();
/* 150 */     boolean success = manager.stopSprinting();
/* 151 */     if (success) {
/* 152 */       source.sendSuccess(() -> Component.translatable("commands.tick.sprint.stop.success"), true);
/* 153 */       return 1;
/*     */     } 
/* 155 */     source.sendFailure(Component.translatable("commands.tick.sprint.stop.fail"));
/* 156 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TickCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */