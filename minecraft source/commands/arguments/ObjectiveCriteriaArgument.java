/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.google.common.collect.Lists;
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
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.stats.Stat;
/*    */ import net.minecraft.stats.StatType;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class ObjectiveCriteriaArgument extends Object implements ArgumentType<ObjectiveCriteria> {
/* 25 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo.bar.baz", "minecraft:foo" });
/* 26 */   public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.criteria.invalid", new Object[] { value }));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static ObjectiveCriteriaArgument criteria() { return new ObjectiveCriteriaArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public static ObjectiveCriteria getCriteria(CommandContext<CommandSourceStack> context, String name) { return (ObjectiveCriteria)context.getArgument(name, ObjectiveCriteria.class); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectiveCriteria parse(StringReader reader) throws CommandSyntaxException {
/* 41 */     int start = reader.getCursor();
/* 42 */     while (reader.canRead() && reader.peek() != ' ') {
/* 43 */       reader.skip();
/*    */     }
/* 45 */     String id = reader.getString().substring(start, reader.getCursor());
/* 46 */     return (ObjectiveCriteria)ObjectiveCriteria.byName(id).orElseThrow(() -> {
/* 47 */           reader.setCursor(start);
/* 48 */           return ERROR_INVALID_VALUE.createWithContext(reader, id);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 54 */     List<String> ids = Lists.newArrayList(ObjectiveCriteria.getCustomCriteriaNames());
/* 55 */     for (StatType<?> type : BuiltInRegistries.STAT_TYPE) {
/* 56 */       for (Object value : type.getRegistry()) {
/* 57 */         String name = getName(type, value);
/* 58 */         ids.add(name);
/*    */       } 
/*    */     } 
/* 61 */     return SharedSuggestionProvider.suggest(ids, builder);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public <T> String getName(StatType<T> type, Object value) { return Stat.buildName(type, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ObjectiveCriteriaArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */