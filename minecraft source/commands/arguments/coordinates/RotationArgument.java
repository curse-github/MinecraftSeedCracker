/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class RotationArgument
/*    */   extends Object
/*    */   implements ArgumentType<Coordinates> {
/* 16 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0 0", "~ ~", "~-5 ~5" });
/* 17 */   public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.rotation.incomplete"));
/*    */ 
/*    */   
/* 20 */   public static RotationArgument rotation() { return new RotationArgument(); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static Coordinates getRotation(CommandContext<CommandSourceStack> context, String name) { return (Coordinates)context.getArgument(name, Coordinates.class); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Coordinates parse(StringReader reader) throws CommandSyntaxException {
/* 29 */     int start = reader.getCursor();
/* 30 */     if (!reader.canRead()) {
/* 31 */       throw ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     }
/* 33 */     WorldCoordinate y = WorldCoordinate.parseDouble(reader, false);
/* 34 */     if (!reader.canRead() || reader.peek() != ' ') {
/* 35 */       reader.setCursor(start);
/* 36 */       throw ERROR_NOT_COMPLETE.createWithContext(reader);
/*    */     } 
/* 38 */     reader.skip();
/* 39 */     WorldCoordinate x = WorldCoordinate.parseDouble(reader, false);
/* 40 */     return new WorldCoordinates(x, y, new WorldCoordinate(true, 0.0D));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\coordinates\RotationArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */