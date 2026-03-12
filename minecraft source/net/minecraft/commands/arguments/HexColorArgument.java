/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class HexColorArgument
/*    */   extends Object implements ArgumentType<Integer> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "F00", "FF0000" });
/* 21 */   public static final DynamicCommandExceptionType ERROR_INVALID_HEX = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.hexcolor.invalid", new Object[] { value }));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static HexColorArgument hexColor() { return new HexColorArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static Integer getHexColor(CommandContext<CommandSourceStack> context, String name) { return (Integer)context.getArgument(name, Integer.class); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer parse(StringReader reader) throws CommandSyntaxException {
/* 36 */     String colorString = reader.readUnquotedString();
/*    */     
/* 38 */     switch (colorString.length()) {
/*    */       case 3:
/*    */       
/*    */ 
/*    */ 
/*    */       
/*    */       case 6:
/*    */       
/*    */     } 
/*    */ 
/*    */     
/* 49 */     throw ERROR_INVALID_HEX.createWithContext(reader, colorString);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   private static int duplicateDigit(int digit) { return digit * 17; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) { return SharedSuggestionProvider.suggest(EXAMPLES, builder); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\HexColorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */