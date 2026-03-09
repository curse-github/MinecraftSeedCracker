/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public class RootSystemFeature
/*     */   extends Feature<RootSystemConfiguration>
/*     */ {
/*  18 */   public RootSystemFeature(Codec<RootSystemConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<RootSystemConfiguration> context) {
/*  23 */     WorldGenLevel level = context.level();
/*  24 */     BlockPos origin = context.origin();
/*  25 */     if (!level.getBlockState(origin).isAir()) {
/*  26 */       return false;
/*     */     }
/*     */     
/*  29 */     RandomSource random = context.random();
/*  30 */     BlockPos pos = context.origin();
/*  31 */     RootSystemConfiguration config = (RootSystemConfiguration)context.config();
/*  32 */     BlockPos.MutableBlockPos workingPos = pos.mutable();
/*  33 */     if (placeDirtAndTree(level, context.chunkGenerator(), config, random, workingPos, pos)) {
/*  34 */       placeRoots(level, config, random, pos, workingPos);
/*     */     }
/*     */     
/*  37 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean spaceForTree(WorldGenLevel level, RootSystemConfiguration config, BlockPos pos) {
/*  41 */     BlockPos.MutableBlockPos columnUpPos = pos.mutable();
/*  42 */     for (int i = 1; i <= config.requiredVerticalSpaceForTree; i++) {
/*  43 */       columnUpPos.move(Direction.UP);
/*  44 */       BlockState state = level.getBlockState(columnUpPos);
/*  45 */       if (!isAllowedTreeSpace(state, i, config.allowedVerticalWaterForTree)) {
/*  46 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  50 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isAllowedTreeSpace(BlockState state, int blocksAboveOrigin, int allowedVerticalWaterHeight) {
/*  54 */     if (state.isAir()) {
/*  55 */       return true;
/*     */     }
/*  57 */     int blocksAboveGround = blocksAboveOrigin + 1;
/*  58 */     return (blocksAboveGround <= allowedVerticalWaterHeight && state.getFluidState().is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean placeDirtAndTree(WorldGenLevel level, ChunkGenerator generator, RootSystemConfiguration config, RandomSource random, BlockPos.MutableBlockPos workingPos, BlockPos pos) {
/*  65 */     for (int y = 0; y < config.rootColumnMaxHeight; y++) {
/*  66 */       workingPos.move(Direction.UP);
/*     */       
/*  68 */       if (config.allowedTreePosition.test(level, workingPos) && 
/*  69 */         spaceForTree(level, config, workingPos)) {
/*  70 */         BlockPos belowPos = workingPos.below();
/*  71 */         if (level.getFluidState(belowPos).is(FluidTags.LAVA) || !level.getBlockState(belowPos).isSolid()) {
/*  72 */           return false;
/*     */         }
/*     */         
/*  75 */         if (((PlacedFeature)config.treeFeature.value()).place(level, generator, random, workingPos)) {
/*  76 */           placeDirt(pos, pos.getY() + y, level, config, random);
/*  77 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   private static void placeDirt(BlockPos origin, int targetHeight, WorldGenLevel level, RootSystemConfiguration config, RandomSource random) {
/*  86 */     int originX = origin.getX();
/*  87 */     int originZ = origin.getZ();
/*  88 */     BlockPos.MutableBlockPos workingPos = origin.mutable();
/*  89 */     for (int y = origin.getY(); y < targetHeight; y++) {
/*  90 */       placeRootedDirt(level, config, random, originX, originZ, workingPos.set(originX, y, originZ));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void placeRootedDirt(WorldGenLevel level, RootSystemConfiguration config, RandomSource random, int originX, int originZ, BlockPos.MutableBlockPos workingPos) {
/*  95 */     int rootRadius = config.rootRadius;
/*  96 */     Predicate<BlockState> stateTest = s -> s.is(config.rootReplaceable);
/*  97 */     for (int i = 0; i < config.rootPlacementAttempts; i++) {
/*  98 */       workingPos.setWithOffset(workingPos, random.nextInt(rootRadius) - random.nextInt(rootRadius), 0, random.nextInt(rootRadius) - random.nextInt(rootRadius));
/*  99 */       if (stateTest.test(level.getBlockState(workingPos))) {
/* 100 */         level.setBlock(workingPos, config.rootStateProvider.getState(random, workingPos), 2);
/*     */       }
/*     */       
/* 103 */       workingPos.setX(originX);
/* 104 */       workingPos.setZ(originZ);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void placeRoots(WorldGenLevel level, RootSystemConfiguration config, RandomSource random, BlockPos pos, BlockPos.MutableBlockPos workingPos) {
/* 112 */     int rootRadius = config.hangingRootRadius;
/* 113 */     int verticalSpan = config.hangingRootsVerticalSpan;
/* 114 */     for (int i = 0; i < config.hangingRootPlacementAttempts; i++) {
/* 115 */       workingPos.setWithOffset(pos, random.nextInt(rootRadius) - random.nextInt(rootRadius), random.nextInt(verticalSpan) - random.nextInt(verticalSpan), random.nextInt(rootRadius) - random.nextInt(rootRadius));
/* 116 */       if (level.isEmptyBlock(workingPos)) {
/* 117 */         BlockState targetState = config.hangingRootStateProvider.getState(random, workingPos);
/* 118 */         if (targetState.canSurvive(level, workingPos) && level.getBlockState(workingPos.above()).isFaceSturdy(level, workingPos, Direction.DOWN))
/* 119 */           level.setBlock(workingPos, targetState, 2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\RootSystemFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */