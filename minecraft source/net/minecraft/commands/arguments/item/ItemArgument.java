/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ 
/*    */ public class ItemArgument
/*    */   extends Object implements ArgumentType<ItemInput> {
/* 16 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stick", "minecraft:stick", "stick{foo=bar}" });
/*    */   
/*    */   private final ItemParser parser;
/*    */ 
/*    */   
/* 21 */   public ItemArgument(CommandBuildContext context) { this.parser = new ItemParser(context); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static ItemArgument item(CommandBuildContext context) { return new ItemArgument(context); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemInput parse(StringReader reader) throws CommandSyntaxException {
/* 30 */     ItemParser.ItemResult result = this.parser.parse(reader);
/* 31 */     return new ItemInput(result.item(), result.components());
/*    */   }
/*    */ 
/*    */   
/* 35 */   public static <S> ItemInput getItem(CommandContext<S> context, String name) { return (ItemInput)context.getArgument(name, ItemInput.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) { return this.parser.fillSuggestions(builder); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ItemArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */