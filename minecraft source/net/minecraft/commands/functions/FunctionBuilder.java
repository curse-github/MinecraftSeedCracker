/*    */ package net.minecraft.commands.functions;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.UnboundEntryAction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ class FunctionBuilder<T extends ExecutionCommandSource<T>>
/*    */   extends Object
/*    */ {
/* 15 */   private List<UnboundEntryAction<T>> plainEntries = new ArrayList();
/*    */   private List<MacroFunction.Entry<T>> macroEntries;
/* 17 */   private final List<String> macroArguments = new ArrayList();
/*    */   
/*    */   public void addCommand(UnboundEntryAction<T> command) {
/* 20 */     if (this.macroEntries != null) {
/* 21 */       this.macroEntries.add(new MacroFunction.PlainTextEntry(command));
/*    */     } else {
/* 23 */       this.plainEntries.add(command);
/*    */     } 
/*    */   }
/*    */   
/*    */   private int getArgumentIndex(String id) {
/* 28 */     int index = this.macroArguments.indexOf(id);
/* 29 */     if (index == -1) {
/* 30 */       index = this.macroArguments.size();
/* 31 */       this.macroArguments.add(id);
/*    */     } 
/* 33 */     return index;
/*    */   }
/*    */   
/*    */   private IntList convertToIndices(List<String> ids) {
/* 37 */     IntArrayList result = new IntArrayList(ids.size());
/* 38 */     for (String id : ids) {
/* 39 */       result.add(getArgumentIndex(id));
/*    */     }
/* 41 */     return result;
/*    */   }
/*    */   
/*    */   public void addMacro(String command, int line, T compilationContext) {
/*    */     StringTemplate parseResults;
/*    */     try {
/* 47 */       parseResults = StringTemplate.fromString(command);
/* 48 */     } catch (Exception e) {
/* 49 */       throw new IllegalArgumentException("Can't parse function line " + line + ": '" + command + "'", e);
/*    */     } 
/*    */     
/* 52 */     if (this.plainEntries != null) {
/* 53 */       this.macroEntries = new ArrayList(this.plainEntries.size() + 1);
/* 54 */       for (UnboundEntryAction<T> plainEntry : this.plainEntries) {
/* 55 */         this.macroEntries.add(new MacroFunction.PlainTextEntry(plainEntry));
/*    */       }
/* 57 */       this.plainEntries = null;
/*    */     } 
/*    */     
/* 60 */     this.macroEntries.add(new MacroFunction.MacroEntry(parseResults, convertToIndices(parseResults.variables()), compilationContext));
/*    */   }
/*    */   
/*    */   public CommandFunction<T> build(Identifier id) {
/* 64 */     if (this.macroEntries != null) {
/* 65 */       return new MacroFunction(id, this.macroEntries, this.macroArguments);
/*    */     }
/*    */     
/* 68 */     return new PlainTextFunction(id, this.plainEntries);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\functions\FunctionBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */