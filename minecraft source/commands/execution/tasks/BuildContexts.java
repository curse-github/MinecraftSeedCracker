/*     */ package net.minecraft.commands.execution.tasks;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.CommandQueueEntry;
/*     */ import net.minecraft.commands.execution.CustomCommandExecutor;
/*     */ import net.minecraft.commands.execution.CustomModifierExecutor;
/*     */ import net.minecraft.commands.execution.EntryAction;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.ExecutionControl;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ import net.minecraft.commands.execution.TraceCallbacks;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class BuildContexts<T extends ExecutionCommandSource<T>> extends Object {
/*     */   @VisibleForTesting
/*  30 */   public static final DynamicCommandExceptionType ERROR_FORK_LIMIT_REACHED = new DynamicCommandExceptionType(limit -> Component.translatableEscape("command.forkLimit", new Object[] { limit }));
/*     */   
/*     */   private final String commandInput;
/*     */   private final ContextChain<T> command;
/*     */   
/*     */   public BuildContexts(String commandInput, ContextChain<T> command) {
/*  36 */     this.commandInput = commandInput;
/*  37 */     this.command = command;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void execute(T originalSource, List<T> initialSources, ExecutionContext<T> context, Frame frame, ChainModifiers initialModifiers) {
/*  45 */     ContextChain<T> currentStage = this.command;
/*     */     
/*  47 */     ChainModifiers modifiers = initialModifiers;
/*  48 */     ObjectArrayList objectArrayList = initialSources;
/*     */     
/*  50 */     if (currentStage.getStage() != ContextChain.Stage.EXECUTE) {
/*  51 */       context.profiler().push(() -> "prepare " + this.commandInput);
/*     */       try {
/*  53 */         int forkLimit = context.forkLimit();
/*  54 */         while (currentStage.getStage() != ContextChain.Stage.EXECUTE) {
/*  55 */           CommandContext<T> contextToRun = currentStage.getTopContext();
/*  56 */           if (contextToRun.isForked()) {
/*  57 */             modifiers = modifiers.setForked();
/*     */           }
/*     */           
/*  60 */           RedirectModifier<T> modifier = contextToRun.getRedirectModifier();
/*  61 */           if (modifier instanceof CustomModifierExecutor) {
/*  62 */             CustomModifierExecutor<T> customModifierExecutor = (CustomModifierExecutor)modifier;
/*  63 */             customModifierExecutor.apply(originalSource, objectArrayList, currentStage, modifiers, ExecutionControl.create(context, frame));
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*  68 */           if (modifier != null) {
/*     */ 
/*     */             
/*  71 */             context.incrementCost();
/*     */             
/*  73 */             boolean forkedMode = modifiers.isForked();
/*  74 */             ObjectArrayList objectArrayList1 = new ObjectArrayList();
/*  75 */             for (Iterator iterator = objectArrayList.iterator(); iterator.hasNext(); ) { T source = (T)(ExecutionCommandSource)iterator.next();
/*     */               
/*     */               try {
/*  78 */                 Collection<T> newSources = ContextChain.runModifier(contextToRun, source, (c, s, r) -> {  }forkedMode);
/*  79 */                 if (objectArrayList1.size() + newSources.size() >= forkLimit) {
/*  80 */                   originalSource.handleError(ERROR_FORK_LIMIT_REACHED.create(Integer.valueOf(forkLimit)), forkedMode, context.tracer());
/*     */                   return;
/*     */                 } 
/*  83 */                 objectArrayList1.addAll(newSources);
/*  84 */               } catch (CommandSyntaxException e) {
/*  85 */                 source.handleError(e, forkedMode, context.tracer());
/*  86 */                 if (!forkedMode) {
/*     */                   return;
/*     */                 }
/*     */               }  }
/*     */ 
/*     */             
/*  92 */             objectArrayList = objectArrayList1;
/*     */           } 
/*  94 */           currentStage = currentStage.nextStage();
/*     */         } 
/*     */       } finally {
/*  97 */         context.profiler().pop();
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     if (objectArrayList.isEmpty()) {
/* 102 */       if (modifiers.isReturn()) {
/* 103 */         context.queueNext(new CommandQueueEntry(frame, FallthroughTask.instance()));
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 108 */     CommandContext<T> executeContext = currentStage.getTopContext();
/* 109 */     Command<T> command = executeContext.getCommand();
/* 110 */     if (command instanceof CustomCommandExecutor) {
/* 111 */       CustomCommandExecutor<T> customCommandExecutor = (CustomCommandExecutor)command;
/* 112 */       ExecutionControl<T> executionControl = ExecutionControl.create(context, frame);
/* 113 */       for (Iterator iterator = objectArrayList.iterator(); iterator.hasNext(); ) { T executionSource = (T)(ExecutionCommandSource)iterator.next();
/* 114 */         customCommandExecutor.run(executionSource, currentStage, modifiers, executionControl); }
/*     */     
/*     */     } else {
/* 117 */       List list; if (modifiers.isReturn()) {
/*     */         
/* 119 */         T returningSource = (T)(ExecutionCommandSource)objectArrayList.get(0);
/*     */         
/* 121 */         returningSource = (T)returningSource.withCallback(CommandResultCallback.chain(returningSource
/* 122 */               .callback(), frame
/* 123 */               .returnValueConsumer()));
/*     */         
/* 125 */         list = List.of(returningSource);
/*     */       } 
/*     */ 
/*     */       
/* 129 */       ExecuteCommand<T> action = new ExecuteCommand<T>(this.commandInput, modifiers, executeContext);
/* 130 */       ContinuationTask.schedule(context, frame, list, (frame1, entrySource) -> 
/* 131 */           new CommandQueueEntry(frame1, action.bind(entrySource)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void traceCommandStart(ExecutionContext<T> context, Frame frame) {
/* 137 */     TraceCallbacks tracer = context.tracer();
/* 138 */     if (tracer != null) {
/* 139 */       tracer.onCommand(frame.depth(), this.commandInput);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public String toString() { return this.commandInput; }
/*     */   
/*     */   public static class Unbound<T extends ExecutionCommandSource<T>>
/*     */     extends BuildContexts<T>
/*     */     implements UnboundEntryAction<T> {
/* 150 */     public Unbound(String commandInput, ContextChain<T> command) { super(commandInput, command); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void execute(T sender, ExecutionContext<T> context, Frame frame) {
/* 155 */       traceCommandStart(context, frame);
/* 156 */       execute(sender, List.of(sender), context, frame, ChainModifiers.DEFAULT);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Continuation<T extends ExecutionCommandSource<T>> extends BuildContexts<T> implements EntryAction<T> {
/*     */     private final ChainModifiers modifiers;
/*     */     private final T originalSource;
/*     */     private final List<T> sources;
/*     */     
/*     */     public Continuation(String commandInput, ContextChain<T> command, ChainModifiers modifiers, T originalSource, List<T> sources) {
/* 166 */       super(commandInput, command);
/* 167 */       this.originalSource = originalSource;
/* 168 */       this.sources = sources;
/* 169 */       this.modifiers = modifiers;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 174 */     public void execute(ExecutionContext<T> context, Frame frame) { execute(this.originalSource, this.sources, context, frame, this.modifiers); }
/*     */   }
/*     */   
/*     */   public static class TopLevel<T extends ExecutionCommandSource<T>>
/*     */     extends BuildContexts<T> implements EntryAction<T> {
/*     */     private final T source;
/*     */     
/*     */     public TopLevel(String commandInput, ContextChain<T> command, T source) {
/* 182 */       super(commandInput, command);
/* 183 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute(ExecutionContext<T> context, Frame frame) {
/* 188 */       traceCommandStart(context, frame);
/* 189 */       execute(this.source, List.of(this.source), context, frame, ChainModifiers.DEFAULT);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\tasks\BuildContexts.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */