/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.animal.frog.Tadpole;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FrogspawnBlock extends Block {
/*  29 */   public static final MapCodec<FrogspawnBlock> CODEC = simpleCodec(FrogspawnBlock::new);
/*     */   private static final int MIN_TADPOLES_SPAWN = 2;
/*     */   private static final int MAX_TADPOLES_SPAWN = 5;
/*     */   
/*  33 */   public MapCodec<FrogspawnBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_MIN_HATCH_TICK_DELAY = 3600;
/*     */   
/*     */   private static final int DEFAULT_MAX_HATCH_TICK_DELAY = 12000;
/*     */   
/*  41 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 1.5D);
/*     */   
/*  43 */   private static int minHatchTickDelay = 3600;
/*  44 */   private static int maxHatchTickDelay = 12000;
/*     */ 
/*     */   
/*  47 */   public FrogspawnBlock(BlockBehaviour.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return mayPlaceOn(level, pos.below()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { level.scheduleTick(pos, this, getFrogspawnHatchDelay(level.getRandom())); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   private static int getFrogspawnHatchDelay(RandomSource random) { return random.nextInt(minHatchTickDelay, maxHatchTickDelay); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  71 */     if (!canSurvive(state, level, pos)) {
/*  72 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  74 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  79 */     if (!canSurvive(state, level, pos)) {
/*  80 */       destroyBlock(level, pos);
/*     */       return;
/*     */     } 
/*  83 */     hatchFrogspawn(level, pos, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  88 */     if (entity.getType().equals(EntityType.FALLING_BLOCK)) {
/*  89 */       destroyBlock(level, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean mayPlaceOn(BlockGetter level, BlockPos pos) {
/*  94 */     FluidState fluidState = level.getFluidState(pos);
/*  95 */     FluidState fluidAbove = level.getFluidState(pos.above());
/*  96 */     return (fluidState.getType() == Fluids.WATER && fluidAbove.getType() == Fluids.EMPTY);
/*     */   }
/*     */   
/*     */   private void hatchFrogspawn(ServerLevel level, BlockPos pos, RandomSource random) {
/* 100 */     destroyBlock(level, pos);
/* 101 */     level.playSound(null, pos, SoundEvents.FROGSPAWN_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 102 */     spawnTadpoles(level, pos, random);
/*     */   }
/*     */ 
/*     */   
/* 106 */   private void destroyBlock(Level level, BlockPos pos) { level.destroyBlock(pos, false); }
/*     */ 
/*     */   
/*     */   private void spawnTadpoles(ServerLevel level, BlockPos pos, RandomSource random) {
/* 110 */     int tadpoleAmount = random.nextInt(2, 6);
/* 111 */     for (int i = 1; i <= tadpoleAmount; i++) {
/* 112 */       Tadpole tadpole = (Tadpole)EntityType.TADPOLE.create(level, EntitySpawnReason.BREEDING);
/* 113 */       if (tadpole != null) {
/* 114 */         double xPos = pos.getX() + getRandomTadpolePositionOffset(random);
/* 115 */         double zPos = pos.getZ() + getRandomTadpolePositionOffset(random);
/* 116 */         int yRot = random.nextInt(1, 361);
/* 117 */         tadpole.snapTo(xPos, pos.getY() - 0.5D, zPos, yRot, 0.0F);
/* 118 */         tadpole.setPersistenceRequired();
/* 119 */         level.addFreshEntity(tadpole);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private double getRandomTadpolePositionOffset(RandomSource random) {
/* 125 */     double tadpoleHitboxCenter = 0.20000000298023224D;
/* 126 */     return Mth.clamp(random.nextDouble(), 0.20000000298023224D, 0.7999999970197678D);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void setHatchDelay(int minDelay, int maxDelay) {
/* 131 */     minHatchTickDelay = minDelay;
/* 132 */     maxHatchTickDelay = maxDelay;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void setDefaultHatchDelay() {
/* 137 */     minHatchTickDelay = 3600;
/* 138 */     maxHatchTickDelay = 12000;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FrogspawnBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */