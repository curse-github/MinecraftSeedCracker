/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
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
/*     */ class MacroEntry<T extends ExecutionCommandSource<T>>
/*     */   extends Object
/*     */   implements MacroFunction.Entry<T>
/*     */ {
/*     */   private final StringTemplate template;
/*     */   private final IntList parameters;
/*     */   private final T compilationContext;
/*     */   
/*     */   public MacroEntry(StringTemplate template, IntList parameters, T compilationContext) {
/* 142 */     this.template = template;
/* 143 */     this.parameters = parameters;
/* 144 */     this.compilationContext = compilationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public IntList parameters() { return this.parameters; }
/*     */ 
/*     */ 
/*     */   
/*     */   public UnboundEntryAction<T> instantiate(List<String> substitutions, CommandDispatcher<T> dispatcher, Identifier functionId) throws FunctionInstantiationException {
/* 154 */     String command = this.template.substitute(substitutions);
/*     */     try {
/* 156 */       return CommandFunction.parseCommand(dispatcher, this.compilationContext, new StringReader(command));
/* 157 */     } catch (CommandSyntaxException e) {
/* 158 */       throw new FunctionInstantiationException(Component.translatable("commands.function.error.parse", new Object[] { Component.translationArg(functionId), command, e.getMessage() }));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\functions\MacroFunction$MacroEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */