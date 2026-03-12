/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DirectionalPlaceContext extends BlockPlaceContext {
/*    */   private final Direction direction;
/*    */   
/*    */   public DirectionalPlaceContext(Level level, BlockPos pos, Direction direction, ItemStack dispensed, Direction clickedFace) {
/* 15 */     super(level, null, InteractionHand.MAIN_HAND, dispensed, new BlockHitResult(Vec3.atBottomCenterOf(pos), clickedFace, pos, false));
/*    */     
/* 17 */     this.direction = direction;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public BlockPos getClickedPos() { return getHitResult().getBlockPos(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean canPlace() { return getLevel().getBlockState(getHitResult().getBlockPos()).canBeReplaced(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public boolean replacingClickedOnBlock() { return canPlace(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public Direction getNearestLookingDirection() { return Direction.DOWN; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Direction[] getNearestLookingDirections() {
/* 42 */     switch (this.direction)
/*    */     
/*    */     { default:
/* 45 */         return new Direction[] { Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP };
/*    */       case UP:
/* 47 */         return new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
/*    */       case NORTH:
/* 49 */         return new Direction[] { Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.SOUTH };
/*    */       case SOUTH:
/* 51 */         return new Direction[] { Direction.DOWN, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.NORTH };
/*    */       case WEST:
/* 53 */         return new Direction[] { Direction.DOWN, Direction.WEST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.EAST };
/*    */       case EAST:
/* 55 */         break; }  return new Direction[] { Direction.DOWN, Direction.EAST, Direction.SOUTH, Direction.UP, Direction.NORTH, Direction.WEST };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public Direction getHorizontalDirection() { return (this.direction.getAxis() == Direction.Axis.Y) ? Direction.NORTH : this.direction; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public boolean isSecondaryUseActive() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public float getRotation() { return (this.direction.get2DDataValue() * 90); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\context\DirectionalPlaceContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */