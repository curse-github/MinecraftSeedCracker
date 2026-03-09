/*    */ package net.minecraft.commands.synchronization;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RegisteredSuggestion
/*    */   extends Record
/*    */   implements SuggestionProvider<SharedSuggestionProvider>
/*    */ {
/*    */   private final Identifier name;
/*    */   private final SuggestionProvider<SharedSuggestionProvider> delegate;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/synchronization/SuggestionProviders$RegisteredSuggestion;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 49 */   private RegisteredSuggestion(Identifier name, SuggestionProvider<SharedSuggestionProvider> delegate) { this.name = name; this.delegate = delegate; } public Identifier name() { return this.name; } public SuggestionProvider<SharedSuggestionProvider> delegate() { return this.delegate; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public CompletableFuture<Suggestions> getSuggestions(CommandContext<SharedSuggestionProvider> context, SuggestionsBuilder builder) throws CommandSyntaxException { return this.delegate.getSuggestions(context, builder); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\SuggestionProviders$RegisteredSuggestion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */