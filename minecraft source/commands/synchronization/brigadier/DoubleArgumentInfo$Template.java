/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Template
/*    */   extends Object
/*    */   implements ArgumentTypeInfo.Template<DoubleArgumentType>
/*    */ {
/*    */   private final double min;
/*    */   private final double max;
/*    */   
/*    */   private Template(double min, double max) {
/* 19 */     this.min = min;
/* 20 */     this.max = max;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public DoubleArgumentType instantiate(CommandBuildContext context) { return DoubleArgumentType.doubleArg(this.min, this.max); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public ArgumentTypeInfo<DoubleArgumentType, ?> type() { return DoubleArgumentInfo.this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\DoubleArgumentInfo$Template.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */