/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public class Ints
/*    */   extends Object
/*    */   implements RangeArgument<MinMaxBounds.Ints>
/*    */ {
/* 15 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0..5", "0", "-5", "-100..", "..100" });
/*    */ 
/*    */   
/* 18 */   public static MinMaxBounds.Ints getRange(CommandContext<CommandSourceStack> context, String name) { return (MinMaxBounds.Ints)context.getArgument(name, MinMaxBounds.Ints.class); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public MinMaxBounds.Ints parse(StringReader reader) throws CommandSyntaxException { return MinMaxBounds.Ints.fromReader(reader); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public Collection<String> getExamples() { return EXAMPLES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\RangeArgument$Ints.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */