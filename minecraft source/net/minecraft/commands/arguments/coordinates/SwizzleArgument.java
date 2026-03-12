/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class SwizzleArgument
/*    */   extends Object implements ArgumentType<EnumSet<Direction.Axis>> {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "xyz", "x" });
/* 18 */   private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(Component.translatable("arguments.swizzle.invalid"));
/*    */ 
/*    */   
/* 21 */   public static SwizzleArgument swizzle() { return new SwizzleArgument(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static EnumSet<Direction.Axis> getSwizzle(CommandContext<CommandSourceStack> context, String name) { return (EnumSet)context.getArgument(name, EnumSet.class); }
/*    */ 
/*    */ 
/*    */   
/*    */   public EnumSet<Direction.Axis> parse(StringReader reader) throws CommandSyntaxException {
/* 31 */     EnumSet<Direction.Axis> result = EnumSet.noneOf(Direction.Axis.class);
/*    */     
/* 33 */     while (reader.canRead() && reader.peek() != ' ') {
/* 34 */       Direction.Axis axis, axis, axis; char c = reader.read();
/*    */ 
/*    */       
/* 37 */       switch (c) {
/*    */         case 'x':
/* 39 */           axis = Direction.Axis.X;
/*    */           break;
/*    */         case 'y':
/* 42 */           axis = Direction.Axis.Y;
/*    */           break;
/*    */         case 'z':
/* 45 */           axis = Direction.Axis.Z;
/*    */           break;
/*    */         default:
/* 48 */           throw ERROR_INVALID.createWithContext(reader);
/*    */       } 
/*    */       
/* 51 */       if (result.contains(axis)) {
/* 52 */         throw ERROR_INVALID.createWithContext(reader);
/*    */       }
/* 54 */       result.add(axis);
/*    */     } 
/*    */     
/* 57 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\coordinates\SwizzleArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */