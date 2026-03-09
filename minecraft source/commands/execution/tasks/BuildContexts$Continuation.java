/*     */ package net.minecraft.commands.execution.tasks;
/*     */ 
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.EntryAction;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Continuation<T extends ExecutionCommandSource<T>>
/*     */   extends BuildContexts<T>
/*     */   implements EntryAction<T>
/*     */ {
/*     */   private final ChainModifiers modifiers;
/*     */   private final T originalSource;
/*     */   private final List<T> sources;
/*     */   
/*     */   public Continuation(String commandInput, ContextChain<T> command, ChainModifiers modifiers, T originalSource, List<T> sources) {
/* 166 */     super(commandInput, command);
/* 167 */     this.originalSource = originalSource;
/* 168 */     this.sources = sources;
/* 169 */     this.modifiers = modifiers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 174 */   public void execute(ExecutionContext<T> context, Frame frame) { execute(this.originalSource, this.sources, context, frame, this.modifiers); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\tasks\BuildContexts$Continuation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */