/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.scores.ScoreAccess;
/*     */ 
/*     */ public class OperationArgument
/*     */   extends Object implements ArgumentType<OperationArgument.Operation> {
/*  21 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "=", ">", "<" });
/*  22 */   private static final SimpleCommandExceptionType ERROR_INVALID_OPERATION = new SimpleCommandExceptionType(Component.translatable("arguments.operation.invalid"));
/*  23 */   private static final SimpleCommandExceptionType ERROR_DIVIDE_BY_ZERO = new SimpleCommandExceptionType(Component.translatable("arguments.operation.div0"));
/*     */ 
/*     */   
/*  26 */   public static OperationArgument operation() { return new OperationArgument(); }
/*     */ 
/*     */ 
/*     */   
/*  30 */   public static Operation getOperation(CommandContext<CommandSourceStack> context, String name) { return (Operation)context.getArgument(name, Operation.class); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Operation parse(StringReader reader) throws CommandSyntaxException {
/*  35 */     if (reader.canRead()) {
/*  36 */       int start = reader.getCursor();
/*  37 */       while (reader.canRead() && reader.peek() != ' ') {
/*  38 */         reader.skip();
/*     */       }
/*  40 */       return getOperation(reader.getString().substring(start, reader.getCursor()));
/*     */     } 
/*     */     
/*  43 */     throw ERROR_INVALID_OPERATION.createWithContext(reader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.suggest(new String[] { "=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><" }, builder); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public Collection<String> getExamples() { return EXAMPLES; }
/*     */ 
/*     */   
/*     */   private static Operation getOperation(String op) throws CommandSyntaxException {
/*  57 */     if (op.equals("><")) {
/*  58 */       return (a, b) -> {
/*  59 */           int swap = a.get();
/*  60 */           a.set(b.get());
/*  61 */           b.set(swap);
/*     */         };
/*     */     }
/*     */     
/*  65 */     return getSimpleOperation(op);
/*     */   }
/*     */   
/*     */   private static SimpleOperation getSimpleOperation(String op) throws CommandSyntaxException {
/*  69 */     switch (op) {
/*     */       case "=":
/*     */       
/*     */       
/*     */       case "+=":
/*     */       
/*     */       case "-=":
/*     */       
/*     */       case "*=":
/*     */       
/*     */       case "/=":
/*     */       
/*     */       case "%=":
/*     */       
/*     */       case "<":
/*     */       
/*     */       case ">":
/*     */       
/*     */     } 
/*  88 */     throw ERROR_INVALID_OPERATION.create();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Operation
/*     */   {
/*     */     void apply(ScoreAccess param1ScoreAccess1, ScoreAccess param1ScoreAccess2) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface SimpleOperation
/*     */     extends Operation
/*     */   {
/*     */     int apply(int param1Int1, int param1Int2) throws CommandSyntaxException;
/*     */     
/* 103 */     default void apply(ScoreAccess a, ScoreAccess b) throws CommandSyntaxException { a.set(apply(a.get(), b.get())); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\OperationArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */