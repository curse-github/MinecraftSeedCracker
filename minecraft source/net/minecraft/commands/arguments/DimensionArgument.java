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
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class DimensionArgument extends Object implements ArgumentType<Identifier> {
/* 25 */   private static final Collection<String> EXAMPLES = (Collection)Stream.of(new ResourceKey[] { Level.OVERWORLD, Level.NETHER }).map(key -> key.identifier().toString()).collect(Collectors.toList());
/*    */   
/* 27 */   private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.dimension.invalid", new Object[] { value }));
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Identifier parse(StringReader reader) throws CommandSyntaxException { return Identifier.read(reader); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 36 */     if (context.getSource() instanceof SharedSuggestionProvider) {
/* 37 */       return SharedSuggestionProvider.suggestResource(((SharedSuggestionProvider)context.getSource()).levels().stream().map(ResourceKey::identifier), builder);
/*    */     }
/* 39 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public static DimensionArgument dimension() { return new DimensionArgument(); }
/*    */ 
/*    */   
/*    */   public static ServerLevel getDimension(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 52 */     Identifier location = (Identifier)context.getArgument(name, Identifier.class);
/* 53 */     ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, location);
/* 54 */     ServerLevel level = ((CommandSourceStack)context.getSource()).getServer().getLevel(key);
/* 55 */     if (level == null) {
/* 56 */       throw ERROR_INVALID_VALUE.create(location);
/*    */     }
/* 58 */     return level;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\DimensionArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */