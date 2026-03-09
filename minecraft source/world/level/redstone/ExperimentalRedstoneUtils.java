/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public class ExperimentalRedstoneUtils
/*    */ {
/*    */   public static Orientation initialOrientation(Level level, Direction front, Direction up) {
/* 11 */     if (level.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS)) {
/* 12 */       Orientation orientation = Orientation.random(level.random).withSideBias(Orientation.SideBias.LEFT);
/* 13 */       if (up != null) {
/* 14 */         orientation = orientation.withUp(up);
/*    */       }
/* 16 */       if (front != null) {
/* 17 */         orientation = orientation.withFront(front);
/*    */       }
/* 19 */       return orientation;
/*    */     } 
/* 21 */     return null;
/*    */   }
/*    */ 
/*    */   
/* 25 */   public static Orientation withFront(Orientation orientation, Direction front) { return (orientation == null) ? null : orientation.withFront(front); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\ExperimentalRedstoneUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */