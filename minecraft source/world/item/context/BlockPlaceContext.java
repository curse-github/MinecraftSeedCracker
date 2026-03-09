/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockPlaceContext
/*    */   extends UseOnContext
/*    */ {
/*    */   private final BlockPos relativePos;
/*    */   protected boolean replaceClicked = true;
/*    */   
/* 20 */   public BlockPlaceContext(Player player, InteractionHand hand, ItemStack itemInHand, BlockHitResult hitResult) { this(player.level(), player, hand, itemInHand, hitResult); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public BlockPlaceContext(UseOnContext context) { this(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand(), context.getHitResult()); }
/*    */ 
/*    */   
/*    */   protected BlockPlaceContext(Level level, Player player, InteractionHand hand, ItemStack itemStackInHand, BlockHitResult hitResult) {
/* 28 */     super(level, player, hand, itemStackInHand, hitResult);
/*    */     
/* 30 */     this.relativePos = hitResult.getBlockPos().relative(hitResult.getDirection());
/* 31 */     this.replaceClicked = level.getBlockState(hitResult.getBlockPos()).canBeReplaced(this);
/*    */   }
/*    */   
/*    */   public static BlockPlaceContext at(BlockPlaceContext context, BlockPos pos, Direction direction) {
/* 35 */     return new BlockPlaceContext(context
/* 36 */         .getLevel(), context
/* 37 */         .getPlayer(), context
/* 38 */         .getHand(), context
/* 39 */         .getItemInHand(), new BlockHitResult(new Vec3(pos
/*    */ 
/*    */             
/* 42 */             .getX() + 0.5D + direction.getStepX() * 0.5D, pos
/* 43 */             .getY() + 0.5D + direction.getStepY() * 0.5D, pos
/* 44 */             .getZ() + 0.5D + direction.getStepZ() * 0.5D), direction, pos, false));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public BlockPos getClickedPos() { return this.replaceClicked ? super.getClickedPos() : this.relativePos; }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public boolean canPlace() { return (this.replaceClicked || getLevel().getBlockState(getClickedPos()).canBeReplaced(this)); }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public boolean replacingClickedOnBlock() { return this.replaceClicked; }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public Direction getNearestLookingDirection() { return Direction.orderedByNearest(getPlayer())[0]; }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public Direction getNearestLookingVerticalDirection() { return Direction.getFacingAxis(getPlayer(), Direction.Axis.Y); }
/*    */ 
/*    */   
/*    */   public Direction[] getNearestLookingDirections() {
/* 75 */     Direction[] directions = Direction.orderedByNearest(getPlayer());
/*    */     
/* 77 */     if (this.replaceClicked) {
/* 78 */       return directions;
/*    */     }
/*    */     
/* 81 */     Direction clickedFace = getClickedFace();
/*    */ 
/*    */     
/* 84 */     int index = 0;
/* 85 */     for (; index < directions.length && 
/* 86 */       directions[index] != clickedFace.getOpposite(); index++);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 91 */     if (index > 0) {
/* 92 */       System.arraycopy(directions, 0, directions, 1, index);
/* 93 */       directions[0] = clickedFace.getOpposite();
/*    */     } 
/* 95 */     return directions;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\context\BlockPlaceContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */