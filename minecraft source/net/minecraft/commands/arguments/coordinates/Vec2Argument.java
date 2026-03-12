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
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Vec2Argument
/*    */   extends Object implements ArgumentType<Coordinates> {
/* 23 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0", "~ ~", "0.1 -0.5", "~1 ~-2" });
/* 24 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.pos2d.incomplete"));
/*    */   
/*    */   private final boolean centerCorrect;
/*    */ 
/*    */   
/* 29 */   public Vec2Argument(boolean centerCorrect) { this.centerCorrect = centerCorrect; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static Vec2Argument vec2() { return new Vec2Argument(true); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static Vec2Argument vec2(boolean centerCorrect) { return new Vec2Argument(centerCorrect); }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Vec2 getVec2(CommandContext<CommandSourceStack> context, String name) {
/* 42 */     Vec3 vec3 = ((Coordinates)context.getArgument(name, Coordinates.class)).getPosition((CommandSourceStack)context.getSource());
/* 43 */     return new Vec2((float)vec3.x, (float)vec3.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader reader) throws CommandSyntaxException {
/* 48 */     int start = reader.getCursor();
/* 49 */     if (!reader.canRead()) {
/* 50 */       throw ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     }
/* 52 */     WorldCoordinate x = WorldCoordinate.parseDouble(reader, this.centerCorrect);
/* 53 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 54 */       reader.setCursor(start);
/* 55 */       throw ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 57 */     reader.skip();
/* 58 */     WorldCoordinate z = WorldCoordinate.parseDouble(reader, this.centerCorrect);
/* 59 */     return new WorldCoordinates(x, new WorldCoordinate(true, 0.0D), z);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 64 */     if (context.getSource() instanceof SharedSuggestionProvider) {
/* 65 */       Collection<SharedSuggestionProvider.TextCoordinates> suggestedCoordinates; String remainder = builder.getRemaining();
/*    */ 
/*    */ 
/*    */       
/* 69 */       if (!remainder.isEmpty() && remainder.charAt(0) == '^') {
/* 70 */         suggestedCoordinates = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
/*    */       } else {
/* 72 */         suggestedCoordinates = ((SharedSuggestionProvider)context.getSource()).getAbsoluteCoordinates();
/*    */       } 
/*    */       
/* 75 */       return SharedSuggestionProvider.suggest2DCoordinates(remainder, suggestedCoordinates, builder, Commands.createValidator(this::parse));
/*    */     } 
/* 77 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\coordinates\Vec2Argument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */