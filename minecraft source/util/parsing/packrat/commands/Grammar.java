/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.Dictionary;
/*    */ import net.minecraft.util.parsing.packrat.ErrorCollector;
/*    */ import net.minecraft.util.parsing.packrat.ErrorEntry;
/*    */ 
/*    */ public final class Grammar<T> extends Record implements CommandArgumentParser<T> {
/*    */   private final Dictionary<StringReader> rules;
/*    */   private final NamedRule<StringReader, T> top;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/commands/Grammar;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar<TT;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/commands/Grammar;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar<TT;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/commands/Grammar;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar<TT;>; }
/*    */   
/* 20 */   public Dictionary<StringReader> rules() { return this.rules; } public NamedRule<StringReader, T> top() { return this.top; }
/*    */   public Grammar(Dictionary<StringReader> rules, NamedRule<StringReader, T> top) {
/* 22 */     rules.checkAllBound();
/*    */     this.rules = rules;
/*    */     this.top = top;
/*    */   }
/* 26 */   public Optional<T> parse(ParseState<StringReader> state) { return state.parseTopRule(this.top); }
/*    */ 
/*    */ 
/*    */   
/*    */   public T parseForCommands(StringReader reader) throws CommandSyntaxException {
/* 31 */     ErrorCollector.LongestOnly<StringReader> errorCollector = new ErrorCollector.LongestOnly<StringReader>();
/* 32 */     StringReaderParserState state = new StringReaderParserState(errorCollector, reader);
/* 33 */     Optional<T> result = parse(state);
/* 34 */     if (result.isPresent()) {
/* 35 */       return (T)result.get();
/*    */     }
/*    */ 
/*    */     
/* 39 */     List<ErrorEntry<StringReader>> errorEntries = errorCollector.entries();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     List<Exception> exceptions = errorEntries.stream().mapMulti((entry, output) -> { Object patt0$temp = entry.reason(); if (patt0$temp instanceof DelayedException) { DelayedException<?> delayedException = (DelayedException)patt0$temp; output.accept(delayedException.create(reader.getString(), entry.cursor())); } else { Object patt1$temp = entry.reason(); if (patt1$temp instanceof Exception) { Exception exception = (Exception)patt1$temp; output.accept(exception); }  }  }).toList();
/*    */ 
/*    */     
/* 49 */     for (Exception exception : exceptions) {
/* 50 */       if (exception instanceof CommandSyntaxException) { CommandSyntaxException cse = (CommandSyntaxException)exception;
/* 51 */         throw cse; }
/*    */     
/*    */     } 
/*    */ 
/*    */     
/* 56 */     if (exceptions.size() == 1) { Object object = exceptions.get(0); if (object instanceof RuntimeException) { RuntimeException re = (RuntimeException)object;
/* 57 */         throw re; }
/*    */        }
/*    */     
/* 60 */     throw new IllegalStateException("Failed to parse: " + (String)errorEntries.stream().map(ErrorEntry::toString).collect(Collectors.joining(", ")));
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder suggestionsBuilder) {
/* 65 */     StringReader reader = new StringReader(suggestionsBuilder.getInput());
/* 66 */     reader.setCursor(suggestionsBuilder.getStart());
/*    */     
/* 68 */     ErrorCollector.LongestOnly<StringReader> errorCollector = new ErrorCollector.LongestOnly<StringReader>();
/* 69 */     StringReaderParserState state = new StringReaderParserState(errorCollector, reader);
/* 70 */     parse(state);
/*    */     
/* 72 */     List<ErrorEntry<StringReader>> errorEntries = errorCollector.entries();
/* 73 */     if (errorEntries.isEmpty()) {
/* 74 */       return suggestionsBuilder.buildFuture();
/*    */     }
/*    */ 
/*    */     
/* 78 */     SuggestionsBuilder offsetBuilder = suggestionsBuilder.createOffset(errorCollector.cursor());
/* 79 */     for (ErrorEntry<StringReader> entry : errorEntries) {
/* 80 */       SuggestionSupplier suggestionSupplier = entry.suggestions(); if (suggestionSupplier instanceof ResourceSuggestion) { ResourceSuggestion resourceSuggestionTerm = (ResourceSuggestion)suggestionSupplier;
/*    */         
/* 82 */         SharedSuggestionProvider.suggestResource(resourceSuggestionTerm.possibleResources(), offsetBuilder); continue; }
/*    */       
/* 84 */       SharedSuggestionProvider.suggest(entry.suggestions().possibleValues(state), offsetBuilder);
/*    */     } 
/*    */     
/* 87 */     return offsetBuilder.buildFuture();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\commands\Grammar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */