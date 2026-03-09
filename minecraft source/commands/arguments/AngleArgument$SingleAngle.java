/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SingleAngle
/*    */ {
/*    */   private final float angle;
/*    */   private final boolean isRelative;
/*    */   
/*    */   private SingleAngle(float angle, boolean isRelative) {
/* 53 */     this.angle = angle;
/* 54 */     this.isRelative = isRelative;
/*    */   }
/*    */ 
/*    */   
/* 58 */   public float getAngle(CommandSourceStack sender) { return Mth.wrapDegrees(this.isRelative ? (this.angle + (sender.getRotation()).y) : this.angle); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\AngleArgument$SingleAngle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */