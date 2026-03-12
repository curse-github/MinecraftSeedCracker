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
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.ParserUtils;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.inventory.SlotRange;
/*    */ import net.minecraft.world.inventory.SlotRanges;
/*    */ 
/*    */ public class SlotsArgument extends Object implements ArgumentType<SlotRange> {
/* 22 */   private static final Collection<String> EXAMPLES = List.of("container.*", "container.5", "weapon");
/* 23 */   private static final DynamicCommandExceptionType ERROR_UNKNOWN_SLOT = new DynamicCommandExceptionType(id -> Component.translatableEscape("slot.unknown", new Object[] { id }));
/*    */ 
/*    */   
/* 26 */   public static SlotsArgument slots() { return new SlotsArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static SlotRange getSlots(CommandContext<CommandSourceStack> context, String name) { return (SlotRange)context.getArgument(name, SlotRange.class); }
/*    */ 
/*    */ 
/*    */   
/*    */   public SlotRange parse(StringReader reader) throws CommandSyntaxException {
/* 35 */     String name = ParserUtils.readWhile(reader, c -> (c != ' '));
/* 36 */     SlotRange result = SlotRanges.nameToIds(name);
/* 37 */     if (result == null) {
/* 38 */       throw ERROR_UNKNOWN_SLOT.createWithContext(reader, name);
/*    */     }
/* 40 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) { return SharedSuggestionProvider.suggest(SlotRanges.allNames(), builder); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\SlotsArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */