/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Template
/*    */   extends Object
/*    */   implements ArgumentTypeInfo.Template<IntegerArgumentType>
/*    */ {
/*    */   private final int min;
/*    */   private final int max;
/*    */   
/*    */   private Template(int min, int max) {
/* 19 */     this.min = min;
/* 20 */     this.max = max;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public IntegerArgumentType instantiate(CommandBuildContext context) { return IntegerArgumentType.integer(this.min, this.max); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public ArgumentTypeInfo<IntegerArgumentType, ?> type() { return IntegerArgumentInfo.this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\IntegerArgumentInfo$Template.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */