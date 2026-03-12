/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class DoublePlantBlock extends VegetationBlock {
/*  26 */   public static final MapCodec<DoublePlantBlock> CODEC = simpleCodec(DoublePlantBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  30 */   public MapCodec<? extends DoublePlantBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  33 */   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
/*     */   
/*     */   public DoublePlantBlock(BlockBehaviour.Properties properties) {
/*  36 */     super(properties);
/*     */     
/*  38 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(HALF, DoubleBlockHalf.LOWER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  43 */     DoubleBlockHalf half = (DoubleBlockHalf)state.getValue(HALF);
/*  44 */     if (directionToNeighbour.getAxis() == Direction.Axis.Y) if (((half == DoubleBlockHalf.LOWER) ? 1 : 0) == ((directionToNeighbour == Direction.UP) ? 1 : 0) && (
/*  45 */         !neighbourState.is(this) || neighbourState.getValue(HALF) == half)) {
/*  46 */         return Blocks.AIR.defaultBlockState();
/*     */       }
/*     */ 
/*     */     
/*  50 */     if (half == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/*  51 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  54 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  59 */     BlockPos pos = context.getClickedPos();
/*  60 */     Level level = context.getLevel();
/*  61 */     if (pos.getY() < level.getMaxY() && level.getBlockState(pos.above()).canBeReplaced(context)) {
/*  62 */       return super.getStateForPlacement(context);
/*     */     }
/*     */     
/*  65 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/*  70 */     BlockPos abovePos = pos.above();
/*  71 */     level.setBlock(abovePos, copyWaterloggedFrom(level, abovePos, (BlockState)defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  77 */     if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
/*  78 */       BlockState belowState = level.getBlockState(pos.below());
/*  79 */       return (belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER);
/*     */     } 
/*     */     
/*  82 */     return super.canSurvive(state, level, pos);
/*     */   }
/*     */   
/*     */   public static void placeAt(LevelAccessor level, BlockState state, BlockPos lowerPos, @UpdateFlags int updateType) {
/*  86 */     BlockPos upperPos = lowerPos.above();
/*     */     
/*  88 */     level.setBlock(lowerPos, copyWaterloggedFrom(level, lowerPos, (BlockState)state.setValue(HALF, DoubleBlockHalf.LOWER)), updateType);
/*  89 */     level.setBlock(upperPos, copyWaterloggedFrom(level, upperPos, (BlockState)state.setValue(HALF, DoubleBlockHalf.UPPER)), updateType);
/*     */   }
/*     */   
/*     */   public static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
/*  93 */     if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
/*  94 */       return (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(level.isWaterAt(pos)));
/*     */     }
/*  96 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 101 */     if (!level.isClientSide()) {
/* 102 */       if (player.preventsBlockDrops()) {
/* 103 */         preventDropFromBottomPart(level, pos, state, player);
/*     */       } else {
/*     */         
/* 106 */         dropResources(state, level, pos, null, player, player.getMainHandItem());
/*     */       } 
/*     */     }
/*     */     
/* 110 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack destroyedWith) { super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, destroyedWith); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
/* 121 */     DoubleBlockHalf part = (DoubleBlockHalf)state.getValue(HALF);
/* 122 */     if (part == DoubleBlockHalf.UPPER) {
/* 123 */       BlockPos bottomPos = pos.below();
/* 124 */       BlockState bottomState = level.getBlockState(bottomPos);
/* 125 */       if (bottomState.is(state.getBlock()) && bottomState.getValue(HALF) == DoubleBlockHalf.LOWER) {
/*     */         
/* 127 */         BlockState blockState = bottomState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
/* 128 */         level.setBlock(bottomPos, blockState, 35);
/* 129 */         level.levelEvent(player, 2001, bottomPos, Block.getId(bottomState));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 136 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { HALF }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected long getSeed(BlockState state, BlockPos pos) { return Mth.getSeed(pos.getX(), pos.below((state.getValue(HALF) == DoubleBlockHalf.LOWER) ? 0 : 1).getY(), pos.getZ()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DoublePlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */