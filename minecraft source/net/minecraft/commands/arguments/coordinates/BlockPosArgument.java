/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class BlockPosArgument
/*    */   extends Object implements ArgumentType<Coordinates> {
/* 24 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5" });
/*    */   
/* 26 */   public static final SimpleCommandExceptionType ERROR_NOT_LOADED = new SimpleCommandExceptionType(Component.translatable("argument.pos.unloaded"));
/* 27 */   public static final SimpleCommandExceptionType ERROR_OUT_OF_WORLD = new SimpleCommandExceptionType(Component.translatable("argument.pos.outofworld"));
/* 28 */   public static final SimpleCommandExceptionType ERROR_OUT_OF_BOUNDS = new SimpleCommandExceptionType(Component.translatable("argument.pos.outofbounds"));
/*    */ 
/*    */   
/* 31 */   public static BlockPosArgument blockPos() { return new BlockPosArgument(); }
/*    */ 
/*    */   
/*    */   public static BlockPos getLoadedBlockPos(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 35 */     ServerLevel level = ((CommandSourceStack)context.getSource()).getLevel();
/* 36 */     return getLoadedBlockPos(context, level, name);
/*    */   }
/*    */   
/*    */   public static BlockPos getLoadedBlockPos(CommandContext<CommandSourceStack> context, ServerLevel level, String name) throws CommandSyntaxException {
/* 40 */     BlockPos pos = getBlockPos(context, name);
/* 41 */     if (!level.hasChunkAt(pos)) {
/* 42 */       throw ERROR_NOT_LOADED.create();
/*    */     }
/* 44 */     if (!level.isInWorldBounds(pos)) {
/* 45 */       throw ERROR_OUT_OF_WORLD.create();
/*    */     }
/* 47 */     return pos;
/*    */   }
/*    */ 
/*    */   
/* 51 */   public static BlockPos getBlockPos(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException { return ((Coordinates)context.getArgument(name, Coordinates.class)).getBlockPos((CommandSourceStack)context.getSource()); }
/*    */ 
/*    */   
/*    */   public static BlockPos getSpawnablePos(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 55 */     BlockPos pos = getBlockPos(context, name);
/* 56 */     if (!Level.isInSpawnableBounds(pos)) {
/* 57 */       throw ERROR_OUT_OF_BOUNDS.create();
/*    */     }
/* 59 */     return pos;
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader reader) throws CommandSyntaxException {
/* 64 */     if (reader.canRead() && reader.peek() == '^') {
/* 65 */       return LocalCoordinates.parse(reader);
/*    */     }
/* 67 */     return WorldCoordinates.parseInt(reader);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 73 */     if (context.getSource() instanceof SharedSuggestionProvider) {
/* 74 */       Collection<SharedSuggestionProvider.TextCoordinates> suggestedCoordinates; String remainder = builder.getRemaining();
/*    */ 
/*    */ 
/*    */       
/* 78 */       if (!remainder.isEmpty() && remainder.charAt(0) == '^') {
/* 79 */         suggestedCoordinates = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
/*    */       } else {
/* 81 */         suggestedCoordinates = ((SharedSuggestionProvider)context.getSource()).getRelevantCoordinates();
/*    */       } 
/*    */       
/* 84 */       return SharedSuggestionProvider.suggestCoordinates(remainder, suggestedCoordinates, builder, Commands.createValidator(this::parse));
/*    */     } 
/* 86 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 92 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\coordinates\BlockPosArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */