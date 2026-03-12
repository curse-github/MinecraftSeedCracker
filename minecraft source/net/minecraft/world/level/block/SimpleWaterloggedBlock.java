/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SimpleWaterloggedBlock
/*    */   extends BucketPickup, LiquidBlockContainer
/*    */ {
/* 22 */   default boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) { return (type == Fluids.WATER); }
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 27 */     if (!((Boolean)state.getValue(BlockStateProperties.WATERLOGGED)).booleanValue() && fluidState.getType() == Fluids.WATER) {
/* 28 */       if (!level.isClientSide()) {
/* 29 */         level.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
/* 30 */         level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
/*    */       } 
/* 32 */       return true;
/*    */     } 
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   default ItemStack pickupBlock(LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
/* 39 */     if (((Boolean)state.getValue(BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 40 */       level.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)), 3);
/* 41 */       if (!state.canSurvive(level, pos)) {
/* 42 */         level.destroyBlock(pos, true);
/*    */       }
/* 44 */       return new ItemStack(Items.WATER_BUCKET);
/*    */     } 
/* 46 */     return ItemStack.EMPTY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   default Optional<SoundEvent> getPickupSound() { return Fluids.WATER.getPickupSound(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SimpleWaterloggedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */