/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.CustomCommandExecutor;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.ExecutionControl;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ import net.minecraft.commands.execution.TraceCallbacks;
/*     */ import net.minecraft.commands.execution.tasks.CallFunction;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DebugCommand
/*     */ {
/*  48 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  49 */   private static final SimpleCommandExceptionType ERROR_NOT_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.debug.notRunning"));
/*  50 */   private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType(Component.translatable("commands.debug.alreadyRunning"));
/*     */   
/*  52 */   private static final SimpleCommandExceptionType NO_RECURSIVE_TRACES = new SimpleCommandExceptionType(Component.translatable("commands.debug.function.noRecursion"));
/*  53 */   private static final SimpleCommandExceptionType NO_RETURN_RUN = new SimpleCommandExceptionType(Component.translatable("commands.debug.function.noReturnRun"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  56 */     dispatcher.register(
/*  57 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debug")
/*  58 */         .requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  59 */         .then(Commands.literal("start").executes(c -> start((CommandSourceStack)c.getSource()))))
/*  60 */         .then(Commands.literal("stop").executes(c -> stop((CommandSourceStack)c.getSource()))))
/*  61 */         .then((
/*  62 */           (LiteralArgumentBuilder)Commands.literal("function").requires(Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  63 */           .then(
/*  64 */             Commands.argument("name", FunctionArgument.functions())
/*  65 */             .suggests(FunctionCommand.SUGGEST_FUNCTION)
/*  66 */             .executes(new TraceCustomExecutor()))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int start(CommandSourceStack source) throws CommandSyntaxException {
/*  73 */     MinecraftServer server = source.getServer();
/*  74 */     if (server.isTimeProfilerRunning()) {
/*  75 */       throw ERROR_ALREADY_RUNNING.create();
/*     */     }
/*  77 */     server.startTimeProfiler();
/*  78 */     source.sendSuccess(() -> Component.translatable("commands.debug.started"), true);
/*  79 */     return 0;
/*     */   }
/*     */   
/*     */   private static int stop(CommandSourceStack source) throws CommandSyntaxException {
/*  83 */     MinecraftServer server = source.getServer();
/*  84 */     if (!server.isTimeProfilerRunning()) {
/*  85 */       throw ERROR_NOT_RUNNING.create();
/*     */     }
/*  87 */     ProfileResults results = server.stopTimeProfiler();
/*     */     
/*  89 */     double seconds = results.getNanoDuration() / TimeUtil.NANOSECONDS_PER_SECOND;
/*  90 */     double tps = results.getTickDuration() / seconds;
/*  91 */     source.sendSuccess(() -> Component.translatable("commands.debug.stopped", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(seconds) }), Integer.valueOf(results.getTickDuration()), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(tps) }) }), true);
/*     */     
/*  93 */     return (int)tps;
/*     */   }
/*     */   
/*     */   private static class TraceCustomExecutor
/*     */     extends CustomCommandExecutor.WithErrorHandling<CommandSourceStack> implements CustomCommandExecutor.CommandAdapter<CommandSourceStack> {
/*     */     public void runGuarded(CommandSourceStack source, ContextChain<CommandSourceStack> currentStep, ChainModifiers modifiers, ExecutionControl<CommandSourceStack> context) throws CommandSyntaxException {
/*  99 */       if (modifiers.isReturn()) {
/* 100 */         throw DebugCommand.NO_RETURN_RUN.create();
/*     */       }
/*     */       
/* 103 */       if (context.tracer() != null) {
/* 104 */         throw DebugCommand.NO_RECURSIVE_TRACES.create();
/*     */       }
/* 106 */       CommandContext<CommandSourceStack> currentContext = currentStep.getTopContext();
/*     */       
/* 108 */       Collection<CommandFunction<CommandSourceStack>> functions = FunctionArgument.getFunctions(currentContext, "name");
/*     */       
/* 110 */       MinecraftServer server = source.getServer();
/* 111 */       String outputName = "debug-trace-" + Util.getFilenameFormattedDateTime() + ".txt";
/*     */       
/* 113 */       CommandDispatcher<CommandSourceStack> dispatcher = source.getServer().getFunctions().getDispatcher();
/*     */       
/* 115 */       int commandCount = 0;
/*     */       try {
/* 117 */         Path dirPath = server.getFile("debug");
/* 118 */         Files.createDirectories(dirPath, new java.nio.file.attribute.FileAttribute[0]);
/*     */         
/* 120 */         final PrintWriter output = new PrintWriter(Files.newBufferedWriter(dirPath.resolve(outputName), StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]));
/* 121 */         DebugCommand.Tracer tracer = new DebugCommand.Tracer(output);
/* 122 */         context.tracer(tracer);
/*     */         
/* 124 */         for (CommandFunction<CommandSourceStack> function : functions) {
/*     */           try {
/* 126 */             CommandSourceStack functionSource = source.withSource(tracer).withMaximumPermission(LevelBasedPermissionSet.GAMEMASTER);
/*     */             
/* 128 */             InstantiatedFunction<CommandSourceStack> instantiatedFunction = function.instantiate(null, dispatcher);
/* 129 */             context.queueNext((new CallFunction<CommandSourceStack>(this, instantiatedFunction, CommandResultCallback.EMPTY, false)
/*     */                 {
/*     */                   public void execute(CommandSourceStack sender, ExecutionContext<CommandSourceStack> context, Frame frame) {
/* 132 */                     output.println(function.id());
/* 133 */                     super.execute(sender, context, frame);
/*     */                   }
/* 135 */                 }).bind(functionSource));
/* 136 */             commandCount += instantiatedFunction.entries().size();
/* 137 */           } catch (FunctionInstantiationException exception) {
/* 138 */             source.sendFailure(exception.messageComponent());
/*     */           } 
/*     */         } 
/* 141 */       } catch (UncheckedIOException|java.io.IOException e) {
/* 142 */         DebugCommand.LOGGER.warn("Tracing failed", e);
/* 143 */         source.sendFailure(Component.translatable("commands.debug.function.traceFailed"));
/*     */       } 
/*     */       
/* 146 */       int finalCommandCount = commandCount;
/* 147 */       context.queueNext((c, frame) -> {
/* 148 */             if (functions.size() == 1) {
/* 149 */               source.sendSuccess((), true);
/*     */             } else {
/* 151 */               source.sendSuccess((), true);
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */   class null extends CallFunction<CommandSourceStack> { null(DebugCommand.TraceCustomExecutor this$0, InstantiatedFunction<CommandSourceStack> function, CommandResultCallback resultCallback, boolean returnParentFrame) { super(function, resultCallback, returnParentFrame); }
/*     */     public void execute(CommandSourceStack sender, ExecutionContext<CommandSourceStack> context, Frame frame) {
/*     */       output.println(function.id());
/*     */       super.execute(sender, context, frame);
/*     */     } }
/*     */   private static class Tracer implements CommandSource, TraceCallbacks { public static final int INDENT_OFFSET = 1;
/*     */     private final PrintWriter output;
/*     */     
/* 164 */     private Tracer(PrintWriter output) { this.output = output; }
/*     */     private int lastIndent; private boolean waitingForResult;
/*     */     
/*     */     private void indentAndSave(int value) {
/* 168 */       printIndent(value);
/* 169 */       this.lastIndent = value;
/*     */     }
/*     */     
/*     */     private void printIndent(int value) {
/* 173 */       for (int i = 0; i < value + 1; i++) {
/* 174 */         this.output.write("    ");
/*     */       }
/*     */     }
/*     */     
/*     */     private void newLine() {
/* 179 */       if (this.waitingForResult) {
/* 180 */         this.output.println();
/* 181 */         this.waitingForResult = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onCommand(int depth, String command) {
/* 187 */       newLine();
/* 188 */       indentAndSave(depth);
/* 189 */       this.output.print("[C] ");
/* 190 */       this.output.print(command);
/* 191 */       this.waitingForResult = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onReturn(int depth, String command, int result) {
/* 196 */       if (this.waitingForResult) {
/* 197 */         this.output.print(" -> ");
/* 198 */         this.output.println(result);
/* 199 */         this.waitingForResult = false;
/*     */       } else {
/* 201 */         indentAndSave(depth);
/* 202 */         this.output.print("[R = ");
/* 203 */         this.output.print(result);
/* 204 */         this.output.print("] ");
/* 205 */         this.output.println(command);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onCall(int depth, Identifier function, int size) {
/* 211 */       newLine();
/* 212 */       indentAndSave(depth);
/* 213 */       this.output.print("[F] ");
/* 214 */       this.output.print(function);
/* 215 */       this.output.print(" size=");
/* 216 */       this.output.println(size);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(String message) {
/* 221 */       newLine();
/* 222 */       indentAndSave(this.lastIndent + 1);
/* 223 */       this.output.print("[E] ");
/* 224 */       this.output.print(message);
/*     */     }
/*     */ 
/*     */     
/*     */     public void sendSystemMessage(Component message) {
/* 229 */       newLine();
/* 230 */       printIndent(this.lastIndent + 1);
/* 231 */       this.output.print("[M] ");
/* 232 */       this.output.println(message.getString());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 237 */     public boolean acceptsSuccess() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     public boolean acceptsFailure() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     public boolean shouldInformAdmins() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     public boolean alwaysAccepts() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     public void close() { IOUtils.closeQuietly(this.output); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DebugCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */