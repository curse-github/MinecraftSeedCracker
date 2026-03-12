/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.level.timers.FunctionCallback;
/*     */ import net.minecraft.world.level.timers.FunctionTagCallback;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ 
/*     */ public class ScheduleCommand
/*     */ {
/*  35 */   private static final SimpleCommandExceptionType ERROR_SAME_TICK = new SimpleCommandExceptionType(Component.translatable("commands.schedule.same_tick"));
/*  36 */   private static final DynamicCommandExceptionType ERROR_CANT_REMOVE = new DynamicCommandExceptionType(s -> Component.translatableEscape("commands.schedule.cleared.failure", new Object[] { s }));
/*  37 */   private static final SimpleCommandExceptionType ERROR_MACRO = new SimpleCommandExceptionType(Component.translatableEscape("commands.schedule.macro", new Object[0]));
/*     */   
/*  39 */   private static final SuggestionProvider<CommandSourceStack> SUGGEST_SCHEDULE = (c, p) -> SharedSuggestionProvider.suggest(((CommandSourceStack)c.getSource()).getServer().getWorldData().overworldData().getScheduledEvents().getEventsIds(), p);
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  42 */     dispatcher.register(
/*  43 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("schedule")
/*  44 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  45 */         .then(
/*  46 */           Commands.literal("function")
/*  47 */           .then(
/*  48 */             Commands.argument("function", FunctionArgument.functions())
/*  49 */             .suggests(FunctionCommand.SUGGEST_FUNCTION)
/*  50 */             .then((
/*  51 */               (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("time", TimeArgument.time())
/*  52 */               .executes(c -> schedule((CommandSourceStack)c.getSource(), FunctionArgument.getFunctionOrTag(c, "function"), IntegerArgumentType.getInteger(c, "time"), true)))
/*  53 */               .then(
/*  54 */                 Commands.literal("append")
/*  55 */                 .executes(c -> schedule((CommandSourceStack)c.getSource(), FunctionArgument.getFunctionOrTag(c, "function"), IntegerArgumentType.getInteger(c, "time"), false))))
/*     */               
/*  57 */               .then(
/*  58 */                 Commands.literal("replace")
/*  59 */                 .executes(c -> schedule((CommandSourceStack)c.getSource(), FunctionArgument.getFunctionOrTag(c, "function"), IntegerArgumentType.getInteger(c, "time"), true)))))))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  64 */         .then(
/*  65 */           Commands.literal("clear")
/*  66 */           .then(
/*  67 */             Commands.argument("function", StringArgumentType.greedyString())
/*  68 */             .suggests(SUGGEST_SCHEDULE)
/*  69 */             .executes(c -> remove((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "function"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int schedule(CommandSourceStack source, Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> callback, int time, boolean replace) throws CommandSyntaxException {
/*  76 */     if (time == 0) {
/*  77 */       throw ERROR_SAME_TICK.create();
/*     */     }
/*     */     
/*  80 */     long tickTime = source.getLevel().getGameTime() + time;
/*     */     
/*  82 */     Identifier callbackId = (Identifier)callback.getFirst();
/*  83 */     TimerQueue<MinecraftServer> queue = source.getServer().getWorldData().overworldData().getScheduledEvents();
/*  84 */     Optional<CommandFunction<CommandSourceStack>> function = ((Either)callback.getSecond()).left();
/*  85 */     if (function.isPresent()) {
/*  86 */       if (function.get() instanceof net.minecraft.commands.functions.MacroFunction) {
/*  87 */         throw ERROR_MACRO.create();
/*     */       }
/*  89 */       String scheduleId = callbackId.toString();
/*  90 */       if (replace) {
/*  91 */         queue.remove(scheduleId);
/*     */       }
/*  93 */       queue.schedule(scheduleId, tickTime, new FunctionCallback(callbackId));
/*  94 */       source.sendSuccess(() -> Component.translatable("commands.schedule.created.function", new Object[] { Component.translationArg(callbackId), Integer.valueOf(time), Long.valueOf(tickTime) }), true);
/*     */     } else {
/*  96 */       String scheduleId = "#" + String.valueOf(callbackId);
/*  97 */       if (replace) {
/*  98 */         queue.remove(scheduleId);
/*     */       }
/* 100 */       queue.schedule(scheduleId, tickTime, new FunctionTagCallback(callbackId));
/* 101 */       source.sendSuccess(() -> Component.translatable("commands.schedule.created.tag", new Object[] { Component.translationArg(callbackId), Integer.valueOf(time), Long.valueOf(tickTime) }), true);
/*     */     } 
/*     */     
/* 104 */     return Math.floorMod(tickTime, 2147483647);
/*     */   }
/*     */   
/*     */   private static int remove(CommandSourceStack source, String id) throws CommandSyntaxException {
/* 108 */     int count = source.getServer().getWorldData().overworldData().getScheduledEvents().remove(id);
/* 109 */     if (count == 0) {
/* 110 */       throw ERROR_CANT_REMOVE.create(id);
/*     */     }
/* 112 */     source.sendSuccess(() -> Component.translatable("commands.schedule.cleared.success", new Object[] { Integer.valueOf(count), id }), true);
/* 113 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ScheduleCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */