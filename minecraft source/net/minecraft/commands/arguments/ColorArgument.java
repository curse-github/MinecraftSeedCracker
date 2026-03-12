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
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ColorArgument extends Object implements ArgumentType<ChatFormatting> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "red", "green" });
/* 21 */   public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.color.invalid", new Object[] { value }));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static ColorArgument color() { return new ColorArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static ChatFormatting getColor(CommandContext<CommandSourceStack> context, String name) { return (ChatFormatting)context.getArgument(name, ChatFormatting.class); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ChatFormatting parse(StringReader reader) throws CommandSyntaxException {
/* 36 */     String id = reader.readUnquotedString();
/* 37 */     ChatFormatting result = ChatFormatting.getByName(id);
/* 38 */     if (result == null || result.isFormat()) {
/* 39 */       throw ERROR_INVALID_VALUE.createWithContext(reader, id);
/*    */     }
/* 41 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) { return SharedSuggestionProvider.suggest(ChatFormatting.getNames(true, false), builder); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ColorArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */