/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class KelpBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {
/* 20 */   public static final MapCodec<KelpBlock> CODEC = simpleCodec(KelpBlock::new);
/*    */   
/*    */   private static final double GROW_PER_TICK_PROBABILITY = 0.14D;
/*    */   
/* 24 */   public MapCodec<KelpBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 9.0D);
/*    */ 
/*    */   
/* 32 */   protected KelpBlock(BlockBehaviour.Properties properties) { super(properties, Direction.UP, SHAPE, true, 0.14D); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected boolean canGrowInto(BlockState state) { return state.is(Blocks.WATER); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   protected Block getBodyBlock() { return Blocks.KELP_PLANT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   protected boolean canAttachTo(BlockState state) { return !state.is(Blocks.MAGMA_BLOCK); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   protected int getBlocksToGrowWhenBonemealed(RandomSource random) { return 1; }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 67 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 68 */     if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) {
/* 69 */       return super.getStateForPlacement(context);
/*    */     }
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 76 */   protected FluidState getFluidState(BlockState state) { return Fluids.WATER.getSource(false); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\KelpBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */