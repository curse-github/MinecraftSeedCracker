/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallSeagrassBlock extends DoublePlantBlock implements LiquidBlockContainer {
/* 24 */   public static final MapCodec<TallSeagrassBlock> CODEC = simpleCodec(TallSeagrassBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 28 */   public MapCodec<TallSeagrassBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 31 */   public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;
/*    */   
/* 33 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 16.0D);
/*    */ 
/*    */   
/* 36 */   public TallSeagrassBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return (state.isFaceSturdy(level, pos, Direction.UP) && !state.is(Blocks.MAGMA_BLOCK)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(Blocks.SEAGRASS); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 56 */     BlockState state = super.getStateForPlacement(context);
/*    */     
/* 58 */     if (state != null) {
/* 59 */       FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos().above());
/* 60 */       if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) {
/* 61 */         return state;
/*    */       }
/*    */     } 
/*    */     
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 70 */     if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
/* 71 */       BlockState belowState = level.getBlockState(pos.below());
/* 72 */       return (belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER);
/*    */     } 
/*    */     
/* 75 */     FluidState fluidState = level.getFluidState(pos);
/* 76 */     return (super.canSurvive(state, level, pos) && fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 81 */   protected FluidState getFluidState(BlockState state) { return Fluids.WATER.getSource(false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 91 */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TallSeagrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */