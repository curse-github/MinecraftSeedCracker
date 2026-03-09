/*     */ package net.minecraft.commands.execution;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*     */ import net.minecraft.commands.execution.tasks.CallFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ExecutionContext<T>
/*     */   extends Object
/*     */   implements AutoCloseable
/*     */ {
/*     */   private static final int MAX_QUEUE_DEPTH = 10000000;
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final int commandLimit;
/*     */   
/*     */   private final int forkLimit;
/*     */   private final ProfilerFiller profiler;
/*     */   private TraceCallbacks tracer;
/*     */   
/*     */   public ExecutionContext(int commandLimit, int forkLimit, ProfilerFiller profiler) {
/*  31 */     this.commandQueue = Queues.newArrayDeque();
/*  32 */     this.newTopCommands = new ObjectArrayList();
/*     */ 
/*     */ 
/*     */     
/*  36 */     this.commandLimit = commandLimit;
/*  37 */     this.forkLimit = forkLimit;
/*  38 */     this.profiler = profiler;
/*     */     
/*  40 */     this.commandQuota = commandLimit;
/*     */   }
/*     */   private int commandQuota; private boolean queueOverflow; private final Deque<CommandQueueEntry<T>> commandQueue; private final List<CommandQueueEntry<T>> newTopCommands; private int currentFrameDepth;
/*     */   private static <T extends net.minecraft.commands.ExecutionCommandSource<T>> Frame createTopFrame(ExecutionContext<T> context, CommandResultCallback frameResult) {
/*  44 */     if (context.currentFrameDepth == 0) {
/*  45 */       Objects.requireNonNull(context.commandQueue); return new Frame(0, frameResult, context.commandQueue::clear);
/*     */     } 
/*  47 */     int reentrantFrameDepth = context.currentFrameDepth + 1;
/*  48 */     return new Frame(reentrantFrameDepth, frameResult, context.frameControlForDepth(reentrantFrameDepth));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static <T extends net.minecraft.commands.ExecutionCommandSource<T>> void queueInitialFunctionCall(ExecutionContext<T> context, InstantiatedFunction<T> function, T sender, CommandResultCallback functionReturn) { context.queueNext(new CommandQueueEntry(createTopFrame(context, functionReturn), (new CallFunction(function, sender.callback(), false)).bind(sender))); }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static <T extends net.minecraft.commands.ExecutionCommandSource<T>> void queueInitialCommandExecution(ExecutionContext<T> context, String command, ContextChain<T> executionChain, T sender, CommandResultCallback commandReturn) { context.queueNext(new CommandQueueEntry(createTopFrame(context, commandReturn), new BuildContexts.TopLevel(command, executionChain, sender))); }
/*     */ 
/*     */   
/*     */   private void handleQueueOverflow() {
/*  61 */     this.queueOverflow = true;
/*     */     
/*  63 */     this.newTopCommands.clear();
/*  64 */     this.commandQueue.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueNext(CommandQueueEntry<T> entry) {
/*  70 */     if (this.newTopCommands.size() + this.commandQueue.size() > 10000000) {
/*  71 */       handleQueueOverflow();
/*     */     }
/*     */     
/*  74 */     if (!this.queueOverflow) {
/*  75 */       this.newTopCommands.add(entry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void discardAtDepthOrHigher(int depthToDiscard) {
/*  81 */     while (!this.commandQueue.isEmpty() && ((CommandQueueEntry)this.commandQueue.peek()).frame().depth() >= depthToDiscard) {
/*  82 */       this.commandQueue.removeFirst();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  87 */   public Frame.FrameControl frameControlForDepth(int depthToDiscard) { return () -> discardAtDepthOrHigher(depthToDiscard); }
/*     */ 
/*     */   
/*     */   public void runCommandQueue() {
/*  91 */     pushNewCommands();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  96 */       if (this.commandQuota <= 0) {
/*  97 */         LOGGER.info("Command execution stopped due to limit (executed {} commands)", Integer.valueOf(this.commandLimit));
/*     */         
/*     */         break;
/*     */       } 
/* 101 */       CommandQueueEntry<T> command = (CommandQueueEntry)this.commandQueue.pollFirst();
/* 102 */       if (command == null) {
/*     */         return;
/*     */       }
/* 105 */       this.currentFrameDepth = command.frame().depth();
/* 106 */       command.execute(this);
/*     */       
/* 108 */       if (this.queueOverflow) {
/* 109 */         LOGGER.error("Command execution stopped due to command queue overflow (max {})", Integer.valueOf(10000000));
/*     */         
/*     */         break;
/*     */       } 
/* 113 */       pushNewCommands();
/*     */     } 
/* 115 */     this.currentFrameDepth = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void pushNewCommands() {
/* 120 */     for (int i = this.newTopCommands.size() - 1; i >= 0; i--) {
/* 121 */       this.commandQueue.addFirst((CommandQueueEntry)this.newTopCommands.get(i));
/*     */     }
/* 123 */     this.newTopCommands.clear();
/*     */   }
/*     */ 
/*     */   
/* 127 */   public void tracer(TraceCallbacks tracer) { this.tracer = tracer; }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public TraceCallbacks tracer() { return this.tracer; }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public ProfilerFiller profiler() { return this.profiler; }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public int forkLimit() { return this.forkLimit; }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public void incrementCost() { this.commandQuota--; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 148 */     if (this.tracer != null)
/* 149 */       this.tracer.close(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\ExecutionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */