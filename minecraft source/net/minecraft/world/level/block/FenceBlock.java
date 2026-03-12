/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.LeadItem;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FenceBlock extends CrossCollisionBlock {
/*  28 */   public static final MapCodec<FenceBlock> CODEC = simpleCodec(FenceBlock::new);
/*     */   
/*     */   private final Function<BlockState, VoxelShape> occlusionShapes;
/*     */   
/*  32 */   public MapCodec<FenceBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FenceBlock(BlockBehaviour.Properties properties) {
/*  38 */     super(4.0F, 16.0F, 4.0F, 16.0F, 24.0F, properties);
/*  39 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(NORTH, Boolean.valueOf(false))).setValue(EAST, Boolean.valueOf(false))).setValue(SOUTH, Boolean.valueOf(false))).setValue(WEST, Boolean.valueOf(false))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */     
/*  41 */     this.occlusionShapes = makeShapes(4.0F, 16.0F, 2.0F, 6.0F, 15.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  46 */   protected VoxelShape getOcclusionShape(BlockState state) { return (VoxelShape)this.occlusionShapes.apply(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return getShape(state, level, pos, context); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */   
/*     */   public boolean connectsTo(BlockState state, boolean faceSolid, Direction direction) {
/*  60 */     Block block = state.getBlock();
/*     */     
/*  62 */     boolean sameFence = isSameFence(state);
/*  63 */     boolean gate = (block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(state, direction));
/*  64 */     return ((!isExceptionForConnection(state) && faceSolid) || sameFence || gate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   private boolean isSameFence(BlockState state) { return (state.is(BlockTags.FENCES) && state.is(BlockTags.WOODEN_FENCES) == defaultBlockState().is(BlockTags.WOODEN_FENCES)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) { return !level.isClientSide() ? LeadItem.bindPlayerMobs(player, level, pos) : InteractionResult.PASS; }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  79 */     Level level1 = context.getLevel();
/*  80 */     BlockPos pos = context.getClickedPos();
/*  81 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */ 
/*     */     
/*  84 */     BlockPos north = pos.north();
/*  85 */     BlockPos east = pos.east();
/*  86 */     BlockPos south = pos.south();
/*  87 */     BlockPos west = pos.west();
/*     */     
/*  89 */     BlockState northState = level1.getBlockState(north);
/*  90 */     BlockState eastState = level1.getBlockState(east);
/*  91 */     BlockState southState = level1.getBlockState(south);
/*  92 */     BlockState westState = level1.getBlockState(west);
/*     */     
/*  94 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)super.getStateForPlacement(context)
/*  95 */       .setValue(NORTH, Boolean.valueOf(connectsTo(northState, northState.isFaceSturdy(level1, north, Direction.SOUTH), Direction.SOUTH))))
/*  96 */       .setValue(EAST, Boolean.valueOf(connectsTo(eastState, eastState.isFaceSturdy(level1, east, Direction.WEST), Direction.WEST))))
/*  97 */       .setValue(SOUTH, Boolean.valueOf(connectsTo(southState, southState.isFaceSturdy(level1, south, Direction.NORTH), Direction.NORTH))))
/*  98 */       .setValue(WEST, Boolean.valueOf(connectsTo(westState, westState.isFaceSturdy(level1, west, Direction.EAST), Direction.EAST))))
/*  99 */       .setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 104 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 105 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 107 */     if (directionToNeighbour.getAxis().isHorizontal()) {
/* 108 */       return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), Boolean.valueOf(connectsTo(neighbourState, neighbourState.isFaceSturdy(level, neighbourPos, directionToNeighbour.getOpposite()), directionToNeighbour.getOpposite())));
/*     */     }
/* 110 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 115 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { NORTH, EAST, WEST, SOUTH, WATERLOGGED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FenceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */