/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TheEndPortalBlockEntity
/*    */   extends BlockEntity {
/*  9 */   protected TheEndPortalBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) { super(type, worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public TheEndPortalBlockEntity(BlockPos worldPosition, BlockState blockState) { this(BlockEntityType.END_PORTAL, worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean shouldRenderFace(Direction direction) { return (direction.getAxis() == Direction.Axis.Y); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\TheEndPortalBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */