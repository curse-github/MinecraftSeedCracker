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
/*    */ import net.minecraft.server.level.ColumnPos;
/*    */ 
/*    */ public class ColumnPosArgument
/*    */   extends Object implements ArgumentType<Coordinates> {
/* 23 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0", "~ ~", "~1 ~-2", "^ ^", "^-1 ^0" });
/* 24 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.pos2d.incomplete"));
/*    */ 
/*    */   
/* 27 */   public static ColumnPosArgument columnPos() { return new ColumnPosArgument(); }
/*    */ 
/*    */   
/*    */   public static ColumnPos getColumnPos(CommandContext<CommandSourceStack> context, String name) {
/* 31 */     BlockPos pos = ((Coordinates)context.getArgument(name, Coordinates.class)).getBlockPos((CommandSourceStack)context.getSource());
/* 32 */     return new ColumnPos(pos.getX(), pos.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader reader) throws CommandSyntaxException {
/* 37 */     int start = reader.getCursor();
/* 38 */     if (!reader.canRead()) {
/* 39 */       throw ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     }
/* 41 */     WorldCoordinate x = WorldCoordinate.parseInt(reader);
/* 42 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 43 */       reader.setCursor(start);
/* 44 */       throw ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 46 */     reader.skip();
/* 47 */     WorldCoordinate z = WorldCoordinate.parseInt(reader);
/* 48 */     return new WorldCoordinates(x, new WorldCoordinate(true, 0.0D), z);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 53 */     if (context.getSource() instanceof SharedSuggestionProvider) {
/* 54 */       Collection<SharedSuggestionProvider.TextCoordinates> suggestedCoordinates; String remainder = builder.getRemaining();
/*    */ 
/*    */ 
/*    */       
/* 58 */       if (!remainder.isEmpty() && remainder.charAt(0) == '^') {
/* 59 */         suggestedCoordinates = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
/*    */       } else {
/* 61 */         suggestedCoordinates = ((SharedSuggestionProvider)context.getSource()).getRelevantCoordinates();
/*    */       } 
/*    */       
/* 64 */       return SharedSuggestionProvider.suggest2DCoordinates(remainder, suggestedCoordinates, builder, Commands.createValidator(this::parse));
/*    */     } 
/* 66 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\coordinates\ColumnPosArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */