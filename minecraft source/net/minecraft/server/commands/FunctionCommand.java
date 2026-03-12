/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.CustomCommandExecutor;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.ExecutionControl;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ import net.minecraft.commands.execution.tasks.CallFunction;
/*     */ import net.minecraft.commands.execution.tasks.FallthroughTask;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.ServerFunctionManager;
/*     */ import net.minecraft.server.commands.data.DataAccessor;
/*     */ import net.minecraft.server.commands.data.DataCommands;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ 
/*     */ 
/*     */ public class FunctionCommand
/*     */ {
/*  50 */   private static final DynamicCommandExceptionType ERROR_ARGUMENT_NOT_COMPOUND = new DynamicCommandExceptionType(type -> Component.translatableEscape("commands.function.error.argument_not_compound", new Object[] { type }));
/*  51 */   private static final DynamicCommandExceptionType ERROR_NO_FUNCTIONS = new DynamicCommandExceptionType(name -> Component.translatableEscape("commands.function.scheduled.no_functions", new Object[] { name }));
/*     */   @VisibleForTesting
/*  53 */   public static final Dynamic2CommandExceptionType ERROR_FUNCTION_INSTANTATION_FAILURE = new Dynamic2CommandExceptionType((id, reason) -> Component.translatableEscape("commands.function.instantiationFailure", new Object[] { id, reason }));
/*     */   
/*     */   public static final SuggestionProvider<CommandSourceStack> SUGGEST_FUNCTION = (c, p) -> {
/*  56 */       ServerFunctionManager manager = ((CommandSourceStack)c.getSource()).getServer().getFunctions();
/*  57 */       SharedSuggestionProvider.suggestResource(manager.getTagNames(), p, "#");
/*  58 */       return SharedSuggestionProvider.suggestResource(manager.getFunctionNames(), p);
/*     */     };
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  62 */     LiteralArgumentBuilder<CommandSourceStack> sources = Commands.literal("with");
/*  63 */     for (DataCommands.DataProvider provider : DataCommands.SOURCE_PROVIDERS) {
/*  64 */       provider.wrap(sources, p -> p
/*  65 */           .executes(new FunctionCustomExecutor()
/*     */             {
/*     */               protected CompoundTag arguments(CommandContext<CommandSourceStack> context) {
/*  68 */                 return provider.access(context).getData();
/*     */               }
/*  71 */             }).then(
/*  72 */             Commands.argument("path", NbtPathArgument.nbtPath())
/*  73 */             .executes(new FunctionCustomExecutor()
/*     */               {
/*     */                 protected CompoundTag arguments(CommandContext<CommandSourceStack> context) {
/*  76 */                   return FunctionCommand.getArgumentTag(NbtPathArgument.getPath(context, "path"), provider.access(context));
/*     */                 }
/*     */               })));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  83 */     dispatcher.register(
/*  84 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("function")
/*  85 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  86 */         .then((
/*  87 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("name", FunctionArgument.functions())
/*  88 */           .suggests(SUGGEST_FUNCTION)
/*  89 */           .executes(new FunctionCustomExecutor()
/*     */             {
/*     */               protected CompoundTag arguments(CommandContext<CommandSourceStack> context) {
/*  92 */                 return null;
/*     */               }
/*  95 */             })).then(
/*  96 */             Commands.argument("arguments", CompoundTagArgument.compoundTag())
/*  97 */             .executes(new FunctionCustomExecutor()
/*     */               {
/*     */                 protected CompoundTag arguments(CommandContext<CommandSourceStack> context) {
/* 100 */                   return CompoundTagArgument.getCompoundTag(context, "arguments");
/*     */                 }
/* 104 */               }))).then(sources)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CompoundTag getArgumentTag(NbtPathArgument.NbtPath path, DataAccessor accessor) throws CommandSyntaxException {
/* 112 */     Tag tag = DataCommands.getSingleTag(path, accessor);
/* 113 */     if (tag instanceof CompoundTag) return (CompoundTag)tag;
/*     */ 
/*     */ 
/*     */     
/* 117 */     throw ERROR_ARGUMENT_NOT_COMPOUND.create(tag.getType().getName());
/*     */   }
/*     */   
/*     */   private static abstract class FunctionCustomExecutor
/*     */     extends CustomCommandExecutor.WithErrorHandling<CommandSourceStack> implements CustomCommandExecutor.CommandAdapter<CommandSourceStack> {
/*     */     protected abstract CompoundTag arguments(CommandContext<CommandSourceStack> param1CommandContext);
/*     */     
/*     */     public void runGuarded(CommandSourceStack sender, ContextChain<CommandSourceStack> currentStep, ChainModifiers modifiers, ExecutionControl<CommandSourceStack> output) throws CommandSyntaxException {
/* 125 */       CommandContext<CommandSourceStack> currentContext = currentStep.getTopContext().copyFor(sender);
/*     */       
/* 127 */       Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> nameAndFunctions = FunctionArgument.getFunctionCollection(currentContext, "name");
/* 128 */       Collection<CommandFunction<CommandSourceStack>> functions = (Collection)nameAndFunctions.getSecond();
/* 129 */       if (functions.isEmpty()) {
/* 130 */         throw FunctionCommand.ERROR_NO_FUNCTIONS.create(Component.translationArg((Identifier)nameAndFunctions.getFirst()));
/*     */       }
/*     */       
/* 133 */       CompoundTag arguments = arguments(currentContext);
/*     */       
/* 135 */       CommandSourceStack commonFunctionContext = FunctionCommand.modifySenderForExecution(sender);
/*     */       
/* 137 */       if (functions.size() == 1) {
/* 138 */         sender.sendSuccess(() -> Component.translatable("commands.function.scheduled.single", new Object[] { Component.translationArg(((CommandFunction)functions.iterator().next()).id()) }), true);
/*     */       } else {
/* 140 */         sender.sendSuccess(() -> Component.translatable("commands.function.scheduled.multiple", new Object[] { ComponentUtils.formatList(functions.stream().map(CommandFunction::id).toList(), Component::translationArg) }), true);
/*     */       } 
/*     */       
/* 143 */       FunctionCommand.queueFunctions(functions, arguments, sender, commonFunctionContext, output, FunctionCommand.FULL_CONTEXT_CALLBACKS, modifiers);
/*     */     }
/*     */   }
/*     */   
/*     */   public static CommandSourceStack modifySenderForExecution(CommandSourceStack sender) {
/* 148 */     return sender
/* 149 */       .withSuppressedOutput()
/* 150 */       .withMaximumPermission(LevelBasedPermissionSet.GAMEMASTER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   private static final Callbacks<CommandSourceStack> FULL_CONTEXT_CALLBACKS = new Callbacks<CommandSourceStack>()
/*     */     {
/*     */       public void signalResult(CommandSourceStack originalSource, Identifier id, int newValue) {
/* 160 */         originalSource.sendSuccess(() -> Component.translatable("commands.function.result", new Object[] { Component.translationArg(id), Integer.valueOf(newValue) }), true);
/*     */       }
/*     */     };
/*     */   
/*     */   public static <T extends ExecutionCommandSource<T>> void queueFunctions(Collection<CommandFunction<T>> functions, CompoundTag arguments, T originalSource, T functionSource, ExecutionControl<T> output, Callbacks<T> callbacks, ChainModifiers modifiers) throws CommandSyntaxException {
/* 165 */     if (modifiers.isReturn()) {
/* 166 */       queueFunctionsAsReturn(functions, arguments, originalSource, functionSource, output, callbacks);
/*     */     } else {
/* 168 */       queueFunctionsNoReturn(functions, arguments, originalSource, functionSource, output, callbacks);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends ExecutionCommandSource<T>> void instantiateAndQueueFunctions(CompoundTag arguments, ExecutionControl<T> output, CommandDispatcher<T> dispatcher, T noCallbackSource, CommandFunction<T> function, Identifier id, CommandResultCallback functionResultCollector, boolean returnParentFrame) throws CommandSyntaxException {
/*     */     try {
/* 174 */       InstantiatedFunction<T> instantiatedFunction = function.instantiate(arguments, dispatcher);
/* 175 */       output.queueNext((new CallFunction(instantiatedFunction, functionResultCollector, returnParentFrame)).bind(noCallbackSource));
/* 176 */     } catch (FunctionInstantiationException exception) {
/* 177 */       throw ERROR_FUNCTION_INSTANTATION_FAILURE.create(id, exception.messageComponent());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends ExecutionCommandSource<T>> CommandResultCallback decorateOutputIfNeeded(T originalSource, Callbacks<T> callbacks, Identifier id, CommandResultCallback callback) {
/* 183 */     if (originalSource.isSilent()) {
/* 184 */       return callback;
/*     */     }
/* 186 */     return (success, result) -> {
/* 187 */         callbacks.signalResult(originalSource, id, result);
/* 188 */         callback.onResult(success, result);
/*     */       };
/*     */   }
/*     */   
/*     */   private static <T extends ExecutionCommandSource<T>> void queueFunctionsAsReturn(Collection<CommandFunction<T>> functions, CompoundTag arguments, T originalSource, T functionSource, ExecutionControl<T> output, Callbacks<T> callbacks) throws CommandSyntaxException {
/* 193 */     CommandDispatcher<T> dispatcher = originalSource.dispatcher();
/*     */     
/* 195 */     T noCallbackSource = (T)functionSource.clearCallbacks();
/*     */ 
/*     */     
/* 198 */     CommandResultCallback functionCommandOutputCallback = CommandResultCallback.chain(originalSource
/* 199 */         .callback(), output
/* 200 */         .currentFrame().returnValueConsumer());
/*     */ 
/*     */ 
/*     */     
/* 204 */     for (CommandFunction<T> function : functions) {
/* 205 */       Identifier id = function.id();
/* 206 */       CommandResultCallback functionResultCollector = decorateOutputIfNeeded(originalSource, callbacks, id, functionCommandOutputCallback);
/* 207 */       instantiateAndQueueFunctions(arguments, output, dispatcher, noCallbackSource, function, id, functionResultCollector, true);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     output.queueNext(FallthroughTask.instance());
/*     */   }
/*     */   
/*     */   private static <T extends ExecutionCommandSource<T>> void queueFunctionsNoReturn(Collection<CommandFunction<T>> functions, CompoundTag arguments, T originalSource, T functionSource, ExecutionControl<T> output, Callbacks<T> callbacks) throws CommandSyntaxException {
/* 217 */     CommandDispatcher<T> dispatcher = originalSource.dispatcher();
/*     */     
/* 219 */     T noCallbackSource = (T)functionSource.clearCallbacks();
/*     */     
/* 221 */     CommandResultCallback originalCallback = originalSource.callback();
/* 222 */     if (functions.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 227 */     if (functions.size() == 1) {
/*     */       
/* 229 */       CommandFunction<T> function = (CommandFunction)functions.iterator().next();
/*     */       
/* 231 */       Identifier id = function.id();
/* 232 */       CommandResultCallback functionResultCollector = decorateOutputIfNeeded(originalSource, callbacks, id, originalCallback);
/* 233 */       instantiateAndQueueFunctions(arguments, output, dispatcher, noCallbackSource, function, id, functionResultCollector, false);
/*     */     
/*     */     }
/* 236 */     else if (originalCallback == CommandResultCallback.EMPTY) {
/*     */       
/* 238 */       for (CommandFunction<T> function : functions) {
/* 239 */         Identifier id = function.id();
/* 240 */         CommandResultCallback functionResultCollector = decorateOutputIfNeeded(originalSource, callbacks, id, originalCallback);
/* 241 */         instantiateAndQueueFunctions(arguments, output, dispatcher, noCallbackSource, function, id, functionResultCollector, false);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 254 */       Accumulator accumulator = new Accumulator();
/* 255 */       CommandResultCallback partialResultCallback = (success, result) -> accumulator.add(result);
/* 256 */       for (CommandFunction<T> function : functions) {
/* 257 */         Identifier id = function.id();
/* 258 */         CommandResultCallback functionResultCollector = decorateOutputIfNeeded(originalSource, callbacks, id, partialResultCallback);
/* 259 */         instantiateAndQueueFunctions(arguments, output, dispatcher, noCallbackSource, function, id, functionResultCollector, false);
/*     */       } 
/* 261 */       output.queueNext((context, frame) -> {
/*     */             
/* 263 */             if (accumulator.anyResult)
/* 264 */               originalCallback.onSuccess(accumulator.sum); 
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface Callbacks<T> {
/*     */     void signalResult(T param1T, Identifier param1Identifier, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\FunctionCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */