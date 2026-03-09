/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.Stopwatch;
/*     */ import net.minecraft.world.Stopwatches;
/*     */ 
/*     */ public class StopwatchCommand {
/*  24 */   private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.stopwatch.already_exists", new Object[] { id }));
/*  25 */   public static final DynamicCommandExceptionType ERROR_DOES_NOT_EXIST = new DynamicCommandExceptionType(id -> Component.translatableEscape("commands.stopwatch.does_not_exist", new Object[] { id }));
/*     */   
/*  27 */   public static final SuggestionProvider<CommandSourceStack> SUGGEST_STOPWATCHES = (c, p) -> SharedSuggestionProvider.suggestResource(((CommandSourceStack)c.getSource()).getServer().getStopwatches().ids(), p);
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  30 */     dispatcher.register(
/*  31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stopwatch")
/*  32 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  33 */         .then(
/*  34 */           Commands.literal("create")
/*  35 */           .then(
/*  36 */             Commands.argument("id", IdentifierArgument.id())
/*  37 */             .executes(c -> createStopwatch((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"))))))
/*     */ 
/*     */         
/*  40 */         .then(
/*  41 */           Commands.literal("query")
/*  42 */           .then((
/*  43 */             (RequiredArgumentBuilder)Commands.argument("id", IdentifierArgument.id())
/*  44 */             .suggests(SUGGEST_STOPWATCHES)
/*  45 */             .then(
/*  46 */               Commands.argument("scale", DoubleArgumentType.doubleArg())
/*  47 */               .executes(c -> queryStopwatch((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"), DoubleArgumentType.getDouble(c, "scale")))))
/*     */             
/*  49 */             .executes(c -> queryStopwatch((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"), 1.0D)))))
/*     */ 
/*     */         
/*  52 */         .then(
/*  53 */           Commands.literal("restart")
/*  54 */           .then(
/*  55 */             Commands.argument("id", IdentifierArgument.id())
/*  56 */             .suggests(SUGGEST_STOPWATCHES)
/*  57 */             .executes(c -> restartStopwatch((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"))))))
/*     */ 
/*     */         
/*  60 */         .then(
/*  61 */           Commands.literal("remove")
/*  62 */           .then(
/*  63 */             Commands.argument("id", IdentifierArgument.id())
/*  64 */             .suggests(SUGGEST_STOPWATCHES)
/*  65 */             .executes(c -> removeStopwatch((CommandSourceStack)c.getSource(), IdentifierArgument.getId(c, "id"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int createStopwatch(CommandSourceStack source, Identifier id) throws CommandSyntaxException {
/*  72 */     MinecraftServer server = source.getServer();
/*  73 */     Stopwatches stopwatches = server.getStopwatches();
/*  74 */     Stopwatch now = new Stopwatch(Stopwatches.currentTime());
/*  75 */     if (!stopwatches.add(id, now)) {
/*  76 */       throw ERROR_ALREADY_EXISTS.create(id);
/*     */     }
/*  78 */     source.sendSuccess(() -> Component.translatable("commands.stopwatch.create.success", new Object[] { Component.translationArg(id) }), true);
/*  79 */     return 1;
/*     */   }
/*     */   
/*     */   private static int queryStopwatch(CommandSourceStack source, Identifier id, double scale) throws CommandSyntaxException {
/*  83 */     MinecraftServer server = source.getServer();
/*  84 */     Stopwatches stopwatches = server.getStopwatches();
/*  85 */     Stopwatch stopwatch = stopwatches.get(id);
/*  86 */     if (stopwatch == null) {
/*  87 */       throw ERROR_DOES_NOT_EXIST.create(id);
/*     */     }
/*  89 */     long currentTime = Stopwatches.currentTime();
/*  90 */     double elapsedSeconds = stopwatch.elapsedSeconds(currentTime);
/*  91 */     source.sendSuccess(() -> Component.translatable("commands.stopwatch.query", new Object[] { Component.translationArg(id), Double.valueOf(elapsedSeconds) }), true);
/*  92 */     return (int)(elapsedSeconds * scale);
/*     */   }
/*     */   
/*     */   private static int restartStopwatch(CommandSourceStack source, Identifier id) throws CommandSyntaxException {
/*  96 */     MinecraftServer server = source.getServer();
/*  97 */     Stopwatches stopwatches = server.getStopwatches();
/*  98 */     if (!stopwatches.update(id, stopwatch -> new Stopwatch(Stopwatches.currentTime()))) {
/*  99 */       throw ERROR_DOES_NOT_EXIST.create(id);
/*     */     }
/* 101 */     source.sendSuccess(() -> Component.translatable("commands.stopwatch.restart.success", new Object[] { Component.translationArg(id) }), true);
/* 102 */     return 1;
/*     */   }
/*     */   
/*     */   private static int removeStopwatch(CommandSourceStack source, Identifier id) throws CommandSyntaxException {
/* 106 */     MinecraftServer server = source.getServer();
/* 107 */     Stopwatches stopwatches = server.getStopwatches();
/* 108 */     if (!stopwatches.remove(id)) {
/* 109 */       throw ERROR_DOES_NOT_EXIST.create(id);
/*     */     }
/* 111 */     source.sendSuccess(() -> Component.translatable("commands.stopwatch.remove.success", new Object[] { Component.translationArg(id) }), true);
/* 112 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\StopwatchCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */