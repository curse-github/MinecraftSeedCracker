/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Template
/*    */   extends Object
/*    */   implements ArgumentTypeInfo.Template<FloatArgumentType>
/*    */ {
/*    */   private final float min;
/*    */   private final float max;
/*    */   
/*    */   private Template(float min, float max) {
/* 19 */     this.min = min;
/* 20 */     this.max = max;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public FloatArgumentType instantiate(CommandBuildContext context) { return FloatArgumentType.floatArg(this.min, this.max); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public ArgumentTypeInfo<FloatArgumentType, ?> type() { return FloatArgumentInfo.this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\FloatArgumentInfo$Template.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */