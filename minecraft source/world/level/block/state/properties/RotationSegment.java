/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.SegmentedAnglePrecision;
/*    */ 
/*    */ public class RotationSegment
/*    */ {
/*  9 */   private static final SegmentedAnglePrecision SEGMENTED_ANGLE16 = new SegmentedAnglePrecision(4);
/*    */   
/* 11 */   private static final int MAX_SEGMENT_INDEX = SEGMENTED_ANGLE16.getMask();
/*    */   
/*    */   private static final int NORTH_0 = 0;
/*    */   
/*    */   private static final int EAST_90 = 4;
/*    */   private static final int SOUTH_180 = 8;
/*    */   private static final int WEST_270 = 12;
/*    */   
/* 19 */   public static int getMaxSegmentIndex() { return MAX_SEGMENT_INDEX; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static int convertToSegment(Direction direction) { return SEGMENTED_ANGLE16.fromDirection(direction); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static int convertToSegment(float rotDegrees) { return SEGMENTED_ANGLE16.fromDegrees(rotDegrees); }
/*    */ 
/*    */   
/*    */   public static Optional<Direction> convertToDirection(int segment) {
/* 31 */     switch (segment) { case 0: 
/*    */       case 4: 
/*    */       case 8:
/*    */       
/*    */       case 12:
/* 36 */        }  Direction result = null;
/*    */ 
/*    */     
/* 39 */     return Optional.ofNullable(result);
/*    */   }
/*    */ 
/*    */   
/* 43 */   public static float convertToDegrees(int segment) { return SEGMENTED_ANGLE16.toDegrees(segment); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\properties\RotationSegment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */