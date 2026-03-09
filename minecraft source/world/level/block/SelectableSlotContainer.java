/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SelectableSlotContainer
/*    */ {
/*    */   default OptionalInt getHitSlot(BlockHitResult hitResult, Direction blockFacing) {
/* 19 */     return (OptionalInt)getRelativeHitCoordinatesForBlockFace(hitResult, blockFacing)
/* 20 */       .map(hitCoords -> {
/* 21 */           int row = getSection(1.0F - hitCoords.y, getRows());
/* 22 */           int column = getSection(hitCoords.x, getColumns());
/* 23 */           return OptionalInt.of(column + row * getColumns());
/* 24 */         }).orElseGet(OptionalInt::empty);
/*    */   }
/*    */   
/*    */   private static Optional<Vec2> getRelativeHitCoordinatesForBlockFace(BlockHitResult hitResult, Direction blockFacing) {
/* 28 */     Direction hitDirection = hitResult.getDirection();
/*    */     
/* 30 */     if (blockFacing != hitDirection) {
/* 31 */       return Optional.empty();
/*    */     }
/*    */     
/* 34 */     BlockPos hitBlockPos = hitResult.getBlockPos().relative(hitDirection);
/* 35 */     Vec3 relativeHit = hitResult.getLocation().subtract(hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
/*    */     
/* 37 */     double relativeX = relativeHit.x();
/* 38 */     double relativeY = relativeHit.y();
/* 39 */     double relativeZ = relativeHit.z();
/*    */     
/* 41 */     switch (hitDirection) { default: throw new MatchException(null, null);case NORTH: case SOUTH: case WEST: case EAST: case DOWN: case UP: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 46 */       Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   private static int getSection(float relativeCoordinate, int maxSections) {
/* 51 */     float targetedPixel = relativeCoordinate * 16.0F;
/* 52 */     float sectionSize = 16.0F / maxSections;
/* 53 */     return Mth.clamp(Mth.floor(targetedPixel / sectionSize), 0, maxSections - 1);
/*    */   }
/*    */   
/*    */   int getRows();
/*    */   
/*    */   int getColumns();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SelectableSlotContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */