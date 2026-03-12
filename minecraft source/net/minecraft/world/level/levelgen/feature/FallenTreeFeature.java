/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FallenTreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ 
/*     */ 
/*     */ public class FallenTreeFeature
/*     */   extends Feature<FallenTreeConfiguration>
/*     */ {
/*     */   private static final int STUMP_HEIGHT = 1;
/*     */   private static final int STUMP_HEIGHT_PLUS_EMPTY_SPACE = 2;
/*     */   private static final int FALLEN_LOG_MAX_FALL_HEIGHT_TO_GROUND = 5;
/*     */   private static final int FALLEN_LOG_MAX_GROUND_GAP = 2;
/*     */   private static final int FALLEN_LOG_MAX_SPACE_FROM_STUMP = 2;
/*     */   
/*  29 */   public FallenTreeFeature(Codec<FallenTreeConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<FallenTreeConfiguration> context) {
/*  34 */     placeFallenTree((FallenTreeConfiguration)context.config(), context.origin(), context.level(), context.random());
/*  35 */     return true;
/*     */   }
/*     */   
/*     */   private void placeFallenTree(FallenTreeConfiguration config, BlockPos origin, WorldGenLevel level, RandomSource random) {
/*  39 */     placeStump(config, level, random, origin.mutable());
/*     */     
/*  41 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/*  42 */     int logLength = config.logLength.sample(random) - 2;
/*  43 */     BlockPos.MutableBlockPos logStartPos = origin.relative(direction, 2 + random.nextInt(2)).mutable();
/*  44 */     setGroundHeightForFallenLogStartPos(level, logStartPos);
/*  45 */     if (canPlaceEntireFallenLog(level, logLength, logStartPos, direction)) {
/*  46 */       placeFallenLog(config, level, random, logLength, logStartPos, direction);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setGroundHeightForFallenLogStartPos(WorldGenLevel level, BlockPos.MutableBlockPos logStartPos) {
/*  51 */     logStartPos.move(Direction.UP, 1);
/*  52 */     for (int i = 0; i < 6; i++) {
/*  53 */       if (mayPlaceOn(level, logStartPos)) {
/*     */         return;
/*     */       }
/*  56 */       logStartPos.move(Direction.DOWN);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeStump(FallenTreeConfiguration config, WorldGenLevel level, RandomSource random, BlockPos.MutableBlockPos stumpPos) {
/*  61 */     BlockPos stump = placeLogBlock(config, level, random, stumpPos, Function.identity());
/*  62 */     decorateLogs(level, random, Set.of(stump), config.stumpDecorators);
/*     */   }
/*     */   
/*     */   private boolean canPlaceEntireFallenLog(WorldGenLevel level, int logLength, BlockPos.MutableBlockPos logStartPos, Direction direction) {
/*  66 */     int gapInGround = 0;
/*  67 */     for (int i = 0; i < logLength; i++) {
/*  68 */       if (!TreeFeature.validTreePos(level, logStartPos)) {
/*  69 */         return false;
/*     */       }
/*     */       
/*  72 */       if (!isOverSolidGround(level, logStartPos)) {
/*  73 */         gapInGround++;
/*  74 */         if (gapInGround > 2) {
/*  75 */           return false;
/*     */         }
/*     */       } else {
/*  78 */         gapInGround = 0;
/*     */       } 
/*     */       
/*  81 */       logStartPos.move(direction);
/*     */     } 
/*  83 */     logStartPos.move(direction.getOpposite(), logLength);
/*  84 */     return true;
/*     */   }
/*     */   
/*     */   private void placeFallenLog(FallenTreeConfiguration config, WorldGenLevel level, RandomSource random, int logLength, BlockPos.MutableBlockPos logStartPos, Direction direction) {
/*  88 */     Set<BlockPos> fallenLog = new HashSet<BlockPos>();
/*  89 */     for (int i = 0; i < logLength; i++) {
/*  90 */       fallenLog.add(placeLogBlock(config, level, random, logStartPos, getSidewaysStateModifier(direction)));
/*  91 */       logStartPos.move(direction);
/*     */     } 
/*     */     
/*  94 */     decorateLogs(level, random, fallenLog, config.logDecorators);
/*     */   }
/*     */ 
/*     */   
/*  98 */   private boolean mayPlaceOn(LevelAccessor level, BlockPos blockPos) { return (TreeFeature.validTreePos(level, blockPos) && isOverSolidGround(level, blockPos)); }
/*     */ 
/*     */ 
/*     */   
/* 102 */   private boolean isOverSolidGround(LevelAccessor level, BlockPos blockPos) { return level.getBlockState(blockPos.below()).isFaceSturdy(level, blockPos, Direction.UP); }
/*     */ 
/*     */   
/*     */   private BlockPos placeLogBlock(FallenTreeConfiguration config, WorldGenLevel level, RandomSource random, BlockPos.MutableBlockPos blockPos, Function<BlockState, BlockState> sidewaysStateModifier) {
/* 106 */     level.setBlock(blockPos, (BlockState)sidewaysStateModifier.apply(config.trunkProvider.getState(random, blockPos)), 3);
/* 107 */     markAboveForPostProcessing(level, blockPos);
/* 108 */     return blockPos.immutable();
/*     */   }
/*     */   
/*     */   private void decorateLogs(WorldGenLevel level, RandomSource random, Set<BlockPos> logs, List<TreeDecorator> decorators) {
/* 112 */     if (!decorators.isEmpty()) {
/* 113 */       TreeDecorator.Context decoratorContext = new TreeDecorator.Context(level, getDecorationSetter(level), random, logs, Set.of(), Set.of());
/* 114 */       decorators.forEach(decorator -> decorator.place(decoratorContext));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 119 */   private BiConsumer<BlockPos, BlockState> getDecorationSetter(WorldGenLevel level) { return (pos, state) -> level.setBlock(pos, state, 19); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   private static Function<BlockState, BlockState> getSidewaysStateModifier(Direction direction) { return state -> (BlockState)state.trySetValue(RotatedPillarBlock.AXIS, direction.getAxis()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FallenTreeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */