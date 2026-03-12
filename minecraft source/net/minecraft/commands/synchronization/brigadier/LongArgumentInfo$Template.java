/*    */ package net.minecraft.commands.synchronization.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.LongArgumentType;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Template
/*    */   extends Object
/*    */   implements ArgumentTypeInfo.Template<LongArgumentType>
/*    */ {
/*    */   private final long min;
/*    */   private final long max;
/*    */   
/*    */   private Template(long min, long max) {
/* 19 */     this.min = min;
/* 20 */     this.max = max;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public LongArgumentType instantiate(CommandBuildContext context) { return LongArgumentType.longArg(this.min, this.max); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public ArgumentTypeInfo<LongArgumentType, ?> type() { return LongArgumentInfo.this; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\brigadier\LongArgumentInfo$Template.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */