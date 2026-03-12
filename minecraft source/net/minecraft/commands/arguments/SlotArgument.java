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
/*    */ import net.minecraft.commands.ParserUtils;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.inventory.SlotRange;
/*    */ import net.minecraft.world.inventory.SlotRanges;
/*    */ 
/*    */ public class SlotArgument extends Object implements ArgumentType<Integer> {
/* 22 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "container.5", "weapon" });
/* 23 */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_SLOT = new DynamicCommandExceptionType(id -> Component.translatableEscape("slot.unknown", new Object[] { id }));
/* 24 */   private static final DynamicCommandExceptionType ERROR_ONLY_SINGLE_SLOT_ALLOWED = new DynamicCommandExceptionType(id -> Component.translatableEscape("slot.only_single_allowed", new Object[] { id }));
/*    */ 
/*    */   
/* 27 */   public static SlotArgument slot() { return new SlotArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static int getSlot(CommandContext<CommandSourceStack> context, String name) { return ((Integer)context.getArgument(name, Integer.class)).intValue(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer parse(StringReader reader) throws CommandSyntaxException {
/* 36 */     String name = ParserUtils.readWhile(reader, c -> (c != ' '));
/* 37 */     SlotRange result = SlotRanges.nameToIds(name);
/* 38 */     if (result == null) {
/* 39 */       throw ERROR_UNKNOWN_SLOT.createWithContext(reader, name);
/*    */     }
/* 41 */     if (result.size() != 1) {
/* 42 */       throw ERROR_ONLY_SINGLE_SLOT_ALLOWED.createWithContext(reader, name);
/*    */     }
/* 44 */     return Integer.valueOf(result.slots().getInt(0));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) { return SharedSuggestionProvider.suggest(SlotRanges.singleSlotNames(), builder); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\SlotArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */