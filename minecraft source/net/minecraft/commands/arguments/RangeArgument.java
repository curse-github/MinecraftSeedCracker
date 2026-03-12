/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public interface RangeArgument<T extends MinMaxBounds<?>>
/*    */   extends ArgumentType<T> {
/*    */   public static class Ints extends Object implements RangeArgument<MinMaxBounds.Ints> {
/* 15 */     private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0..5", "0", "-5", "-100..", "..100" });
/*    */ 
/*    */     
/* 18 */     public static MinMaxBounds.Ints getRange(CommandContext<CommandSourceStack> context, String name) { return (MinMaxBounds.Ints)context.getArgument(name, MinMaxBounds.Ints.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     public MinMaxBounds.Ints parse(StringReader reader) throws CommandSyntaxException { return MinMaxBounds.Ints.fromReader(reader); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     public Collection<String> getExamples() { return EXAMPLES; }
/*    */   }
/*    */   
/*    */   public static class Floats
/*    */     extends Object implements RangeArgument<MinMaxBounds.Doubles> {
/* 33 */     private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0..5.2", "0", "-5.4", "-100.76..", "..100" });
/*    */ 
/*    */     
/* 36 */     public static MinMaxBounds.Doubles getRange(CommandContext<CommandSourceStack> context, String name) { return (MinMaxBounds.Doubles)context.getArgument(name, MinMaxBounds.Doubles.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     public MinMaxBounds.Doubles parse(StringReader reader) throws CommandSyntaxException { return MinMaxBounds.Doubles.fromReader(reader); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     public Collection<String> getExamples() { return EXAMPLES; }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   static Ints intRange() { return new Ints(); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   static Floats floatRange() { return new Floats(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\RangeArgument.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */