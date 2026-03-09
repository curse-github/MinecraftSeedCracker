/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ import net.minecraft.commands.execution.tasks.CallFunction;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
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
/*     */ class null
/*     */   extends CallFunction<CommandSourceStack>
/*     */ {
/* 129 */   null(DebugCommand.TraceCustomExecutor this$0, InstantiatedFunction<CommandSourceStack> function, CommandResultCallback resultCallback, boolean returnParentFrame) { super(function, resultCallback, returnParentFrame); }
/*     */   
/*     */   public void execute(CommandSourceStack sender, ExecutionContext<CommandSourceStack> context, Frame frame) {
/* 132 */     output.println(function.id());
/* 133 */     super.execute(sender, context, frame);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\DebugCommand$TraceCustomExecutor$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */