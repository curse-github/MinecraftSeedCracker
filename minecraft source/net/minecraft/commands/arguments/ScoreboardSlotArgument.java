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
/*    */ import net.minecraft.world.scores.DisplaySlot;
/*    */ 
/*    */ public class ScoreboardSlotArgument extends Object implements ArgumentType<DisplaySlot> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "sidebar", "foo.bar" });
/* 21 */   public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.scoreboardDisplaySlot.invalid", new Object[] { value }));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static ScoreboardSlotArgument displaySlot() { return new ScoreboardSlotArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static DisplaySlot getDisplaySlot(CommandContext<CommandSourceStack> context, String name) { return (DisplaySlot)context.getArgument(name, DisplaySlot.class); }
/*    */ 
/*    */ 
/*    */   
/*    */   public DisplaySlot parse(StringReader reader) throws CommandSyntaxException {
/* 36 */     String name = reader.readUnquotedString();
/* 37 */     DisplaySlot result = (DisplaySlot)DisplaySlot.CODEC.byName(name);
/* 38 */     if (result == null) {
/* 39 */       throw ERROR_INVALID_VALUE.createWithContext(reader, name);
/*    */     }
/* 41 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return SharedSuggestionProvider.suggest(Arrays.stream(DisplaySlot.values()).map(DisplaySlot::getSerializedName), builder); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ScoreboardSlotArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */