/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DriedGhastBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
/*  39 */   public static final MapCodec<DriedGhastBlock> CODEC = simpleCodec(DriedGhastBlock::new);
/*     */   
/*     */   public static final int MAX_HYDRATION_LEVEL = 3;
/*     */   
/*  43 */   public MapCodec<DriedGhastBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final IntegerProperty HYDRATION_LEVEL = BlockStateProperties.DRIED_GHAST_HYDRATION_LEVELS;
/*  48 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   public static final int HYDRATION_TICK_DELAY = 5000;
/*  51 */   private static final VoxelShape SHAPE = Block.column(10.0D, 10.0D, 0.0D, 10.0D);
/*     */   
/*     */   public DriedGhastBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(HYDRATION_LEVEL, Integer.valueOf(0))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  60 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, HYDRATION_LEVEL, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  65 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  66 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  69 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public int getHydrationLevel(BlockState state) { return ((Integer)state.getValue(HYDRATION_LEVEL)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   private boolean isReadyToSpawn(BlockState state) { return (getHydrationLevel(state) == 3); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos position, RandomSource random) {
/*  87 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  88 */       tickWaterlogged(state, level, position, random);
/*     */       return;
/*     */     } 
/*  91 */     int hydrationLevel = getHydrationLevel(state);
/*  92 */     if (hydrationLevel > 0) {
/*  93 */       level.setBlock(position, (BlockState)state.setValue(HYDRATION_LEVEL, Integer.valueOf(hydrationLevel - 1)), 2);
/*  94 */       level.gameEvent(GameEvent.BLOCK_CHANGE, position, GameEvent.Context.of(state));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickWaterlogged(BlockState state, ServerLevel level, BlockPos position, RandomSource random) {
/*  99 */     if (!isReadyToSpawn(state)) {
/* 100 */       level.playSound(null, position, SoundEvents.DRIED_GHAST_TRANSITION, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 101 */       level.setBlock(position, (BlockState)state.setValue(HYDRATION_LEVEL, Integer.valueOf(getHydrationLevel(state) + 1)), 2);
/* 102 */       level.gameEvent(GameEvent.BLOCK_CHANGE, position, GameEvent.Context.of(state));
/*     */     } else {
/* 104 */       spawnGhastling(level, position, state);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnGhastling(ServerLevel level, BlockPos position, BlockState state) {
/* 109 */     level.removeBlock(position, false);
/*     */     
/* 111 */     HappyGhast ghastling = (HappyGhast)EntityType.HAPPY_GHAST.create(level, EntitySpawnReason.BREEDING);
/* 112 */     if (ghastling != null) {
/* 113 */       Vec3 spawnAt = position.getBottomCenter();
/* 114 */       ghastling.setBaby(true);
/* 115 */       float blockRotation = Direction.getYRot((Direction)state.getValue(FACING));
/* 116 */       ghastling.setYHeadRot(blockRotation);
/* 117 */       ghastling.snapTo(spawnAt.x(), spawnAt.y(), spawnAt.z(), blockRotation, 0.0F);
/* 118 */       level.addFreshEntity(ghastling);
/* 119 */       level.playSound(null, ghastling, SoundEvents.GHASTLING_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 126 */     double x = pos.getX() + 0.5D;
/* 127 */     double y = pos.getY() + 0.5D;
/* 128 */     double z = pos.getZ() + 0.5D;
/*     */     
/* 130 */     if (!((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 131 */       if (random.nextInt(40) == 0 && level.getBlockState(pos.below()).is(BlockTags.TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS)) {
/* 132 */         level.playLocalSound(x, y, z, SoundEvents.DRIED_GHAST_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */       }
/* 134 */       if (random.nextInt(6) == 0) {
/* 135 */         level.addParticle(ParticleTypes.WHITE_SMOKE, x, y, z, 0.0D, 0.02D, 0.0D);
/*     */       }
/*     */     } else {
/* 138 */       if (random.nextInt(40) == 0) {
/* 139 */         level.playLocalSound(x, y, z, SoundEvents.DRIED_GHAST_AMBIENT_WATER, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */       }
/* 141 */       if (random.nextInt(6) == 0) {
/* 142 */         level.addParticle(ParticleTypes.HAPPY_VILLAGER, x + ((random.nextFloat() * 2.0F - 1.0F) / 3.0F), y + 0.4D, z + ((random.nextFloat() * 2.0F - 1.0F) / 3.0F), 0.0D, random.nextFloat(), 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 149 */     if ((((Boolean)state.getValue(WATERLOGGED)).booleanValue() || ((Integer)state.getValue(HYDRATION_LEVEL)).intValue() > 0) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 150 */       level.scheduleTick(pos, this, 5000);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 156 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 157 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/* 158 */     return (BlockState)((BlockState)super.getStateForPlacement(context).setValue(WATERLOGGED, Boolean.valueOf(isWaterSource))).setValue(FACING, context.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 163 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 164 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 166 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 171 */     if (((Boolean)state.getValue(BlockStateProperties.WATERLOGGED)).booleanValue() || fluidState.getType() != Fluids.WATER) {
/* 172 */       return false;
/*     */     }
/* 174 */     if (!level.isClientSide()) {
/* 175 */       level.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
/* 176 */       level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
/* 177 */       level.playSound(null, pos, SoundEvents.DRIED_GHAST_PLACE_IN_WATER, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 185 */     super.setPlacedBy(level, pos, state, by, itemStack);
/* 186 */     level.playSound(null, pos, ((Boolean)state.getValue(WATERLOGGED)).booleanValue() ? SoundEvents.DRIED_GHAST_PLACE_IN_WATER : SoundEvents.DRIED_GHAST_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 191 */   public boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DriedGhastBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */