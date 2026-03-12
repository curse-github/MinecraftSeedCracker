/*   */ package net.minecraft.world.entity.ai.control;
/*   */ 
/*   */ import net.minecraft.util.Mth;
/*   */ 
/*   */ public interface Control {
/*   */   default float rotateTowards(float fromAngle, float toAngle, float maxRot) {
/* 7 */     float diff = Mth.degreesDifference(fromAngle, toAngle);
/* 8 */     float diffClamped = Mth.clamp(diff, -maxRot, maxRot);
/* 9 */     return fromAngle + diffClamped;
/*   */   }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\control\Control.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */