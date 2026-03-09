/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class GrowingPlantBlock extends Block {
/*    */   protected final Direction growthDirection;
/*    */   protected final boolean scheduleFluidTicks;
/*    */   protected final VoxelShape shape;
/*    */   
/*    */   protected GrowingPlantBlock(BlockBehaviour.Properties properties, Direction growthDirection, VoxelShape shape, boolean scheduleFluidTicks) {
/* 22 */     super(properties);
/* 23 */     this.growthDirection = growthDirection;
/* 24 */     this.shape = shape;
/* 25 */     this.scheduleFluidTicks = scheduleFluidTicks;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends GrowingPlantBlock> codec();
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 33 */     BlockState growthDirectionState = context.getLevel().getBlockState(context.getClickedPos().relative(this.growthDirection));
/* 34 */     if (growthDirectionState.is(getHeadBlock()) || growthDirectionState.is(getBodyBlock())) {
/* 35 */       return getBodyBlock().defaultBlockState();
/*    */     }
/* 37 */     return getStateForPlacement((context.getLevel()).random);
/*    */   }
/*    */ 
/*    */   
/* 41 */   public BlockState getStateForPlacement(RandomSource random) { return defaultBlockState(); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 46 */     BlockPos attachedToPos = pos.relative(this.growthDirection.getOpposite());
/* 47 */     BlockState attachedToState = level.getBlockState(attachedToPos);
/* 48 */     if (!canAttachTo(attachedToState)) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     return (attachedToState.is(getHeadBlock()) || attachedToState.is(getBodyBlock()) || attachedToState.isFaceSturdy(level, attachedToPos, this.growthDirection));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 57 */     if (!state.canSurvive(level, pos)) {
/* 58 */       level.destroyBlock(pos, true);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 63 */   protected boolean canAttachTo(BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return this.shape; }
/*    */   
/*    */   protected abstract GrowingPlantHeadBlock getHeadBlock();
/*    */   
/*    */   protected abstract Block getBodyBlock();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\GrowingPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */