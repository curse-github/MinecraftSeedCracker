/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.Direction;
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
/*    */ public class SegmentedAnglePrecision
/*    */ {
/*    */   private final int mask;
/*    */   private final int precision;
/*    */   private final float degreeToAngle;
/*    */   private final float angleToDegree;
/*    */   
/*    */   public SegmentedAnglePrecision(int bitPrecision) {
/* 24 */     if (bitPrecision < 2) {
/* 25 */       throw new IllegalArgumentException("Precision cannot be less than 2 bits");
/*    */     }
/* 27 */     if (bitPrecision > 30) {
/* 28 */       throw new IllegalArgumentException("Precision cannot be greater than 30 bits");
/*    */     }
/*    */     
/* 31 */     int twoPi = 1 << bitPrecision;
/* 32 */     this.mask = twoPi - 1;
/* 33 */     this.precision = bitPrecision;
/* 34 */     this.degreeToAngle = twoPi / 360.0F;
/* 35 */     this.angleToDegree = 360.0F / twoPi;
/*    */   }
/*    */   
/*    */   public boolean isSameAxis(int binaryAngleA, int binaryAngleB) {
/* 39 */     int semicircleMask = getMask() >> 1;
/*    */     
/* 41 */     return ((binaryAngleA & semicircleMask) == (binaryAngleB & semicircleMask));
/*    */   }
/*    */   
/*    */   public int fromDirection(Direction direction) {
/* 45 */     if (direction.getAxis().isVertical()) {
/* 46 */       return 0;
/*    */     }
/* 48 */     int segmentedAngle2bit = direction.get2DDataValue();
/* 49 */     return segmentedAngle2bit << this.precision - 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public int fromDegreesWithTurns(float degrees) { return Math.round(degrees * this.degreeToAngle); }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public int fromDegrees(float degrees) { return normalize(fromDegreesWithTurns(degrees)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public float toDegreesWithTurns(int binaryAngle) { return binaryAngle * this.angleToDegree; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float toDegrees(int binaryAngle) {
/* 80 */     float degrees = toDegreesWithTurns(normalize(binaryAngle));
/* 81 */     return (degrees >= 180.0F) ? (degrees - 360.0F) : degrees;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 90 */   public int normalize(int binaryAngle) { return binaryAngle & this.mask; }
/*    */ 
/*    */ 
/*    */   
/* 94 */   public int getMask() { return this.mask; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SegmentedAnglePrecision.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */