/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FarmBlock
/*     */   extends Block {
/*  31 */   public static final MapCodec<FarmBlock> CODEC = simpleCodec(FarmBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  35 */   public MapCodec<FarmBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  38 */   public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE;
/*     */   
/*  40 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 15.0D);
/*     */   
/*     */   public static final int MAX_MOISTURE = 7;
/*     */   
/*     */   protected FarmBlock(BlockBehaviour.Properties properties) {
/*  45 */     super(properties);
/*  46 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(MOISTURE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  51 */     if (directionToNeighbour == Direction.UP && !state.canSurvive(level, pos)) {
/*  52 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*  54 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  59 */     BlockState aboveState = level.getBlockState(pos.above());
/*  60 */     return (!aboveState.isSolid() || aboveState.getBlock() instanceof FenceGateBlock || aboveState.getBlock() instanceof net.minecraft.world.level.block.piston.MovingPistonBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  65 */     if (!defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos())) {
/*  66 */       return Blocks.DIRT.defaultBlockState();
/*     */     }
/*  68 */     return super.getStateForPlacement(context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected boolean useShapeForLightOcclusion(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  83 */     if (!state.canSurvive(level, pos)) {
/*  84 */       turnToDirt(null, state, level, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  90 */     int moisture = ((Integer)state.getValue(MOISTURE)).intValue();
/*  91 */     if (isNearWater(level, pos) || level.isRainingAt(pos.above())) {
/*  92 */       if (moisture < 7) {
/*  93 */         level.setBlock(pos, (BlockState)state.setValue(MOISTURE, Integer.valueOf(7)), 2);
/*     */       }
/*  95 */     } else if (moisture > 0) {
/*  96 */       level.setBlock(pos, (BlockState)state.setValue(MOISTURE, Integer.valueOf(moisture - 1)), 2);
/*  97 */     } else if (!shouldMaintainFarmland(level, pos)) {
/*  98 */       turnToDirt(null, state, level, pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/* 104 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (level.random.nextFloat() < fallDistance - 0.5D && entity instanceof net.minecraft.world.entity.LivingEntity && (
/* 105 */         entity instanceof net.minecraft.world.entity.player.Player || ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()))
/*     */       {
/* 107 */         if (entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > 0.512F) {
/* 108 */           turnToDirt(entity, state, level, pos);
/*     */         }
/*     */       } }
/*     */     
/* 112 */     super.fallOn(level, state, pos, entity, fallDistance);
/*     */   }
/*     */   
/*     */   public static void turnToDirt(Entity sourceEntity, BlockState state, Level level, BlockPos pos) {
/* 116 */     BlockState newState = pushEntitiesUp(state, Blocks.DIRT.defaultBlockState(), level, pos);
/* 117 */     level.setBlockAndUpdate(pos, newState);
/* 118 */     level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(sourceEntity, newState));
/*     */   }
/*     */ 
/*     */   
/* 122 */   private static boolean shouldMaintainFarmland(BlockGetter level, BlockPos pos) { return level.getBlockState(pos.above()).is(BlockTags.MAINTAINS_FARMLAND); }
/*     */ 
/*     */   
/*     */   private static boolean isNearWater(LevelReader level, BlockPos pos) {
/* 126 */     for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
/* 127 */       if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
/* 128 */         return true;
/*     */       }
/*     */     } 
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 136 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { MOISTURE }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FarmBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */