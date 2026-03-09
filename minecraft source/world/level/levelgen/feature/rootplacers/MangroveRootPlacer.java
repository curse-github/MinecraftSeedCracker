/*     */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ 
/*     */ public class MangroveRootPlacer
/*     */   extends RootPlacer
/*     */ {
/*     */   public static final int ROOT_WIDTH_LIMIT = 8;
/*     */   public static final int ROOT_LENGTH_LIMIT = 15;
/*  25 */   public static final MapCodec<MangroveRootPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> rootPlacerParts(i).and(MangroveRootPlacement.CODEC
/*  26 */         .fieldOf("mangrove_root_placement").forGetter(()))
/*  27 */       .apply(i, MangroveRootPlacer::new));
/*     */   
/*     */   private final MangroveRootPlacement mangroveRootPlacement;
/*     */   
/*     */   public MangroveRootPlacer(IntProvider trunkOffsetY, BlockStateProvider rootProvider, Optional<AboveRootPlacement> aboveRootPlacement, MangroveRootPlacement mangroveRootPlacement) {
/*  32 */     super(trunkOffsetY, rootProvider, aboveRootPlacement);
/*  33 */     this.mangroveRootPlacement = mangroveRootPlacement;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeRoots(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> rootSetter, RandomSource random, BlockPos origin, BlockPos trunkOrigin, TreeConfiguration config) {
/*  38 */     List<BlockPos> rootPositions = Lists.newArrayList();
/*     */     
/*  40 */     BlockPos.MutableBlockPos columnPos = origin.mutable();
/*  41 */     while (columnPos.getY() < trunkOrigin.getY()) {
/*  42 */       if (!canPlaceRoot(level, columnPos)) {
/*  43 */         return false;
/*     */       }
/*  45 */       columnPos.move(Direction.UP);
/*     */     } 
/*     */     
/*  48 */     rootPositions.add(trunkOrigin.below());
/*     */ 
/*     */     
/*  51 */     for (Direction dir : Direction.Plane.HORIZONTAL) {
/*  52 */       BlockPos pos = trunkOrigin.relative(dir);
/*  53 */       List<BlockPos> positionsInDirection = Lists.newArrayList();
/*     */       
/*  55 */       if (!simulateRoots(level, random, pos, dir, trunkOrigin, positionsInDirection, 0)) {
/*  56 */         return false;
/*     */       }
/*     */       
/*  59 */       rootPositions.addAll(positionsInDirection);
/*  60 */       rootPositions.add(trunkOrigin.relative(dir));
/*     */     } 
/*     */     
/*  63 */     for (BlockPos rootPos : rootPositions) {
/*  64 */       placeRoot(level, rootSetter, random, rootPos, config);
/*     */     }
/*     */     
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean simulateRoots(LevelSimulatedReader level, RandomSource random, BlockPos rootPos, Direction dir, BlockPos rootOrigin, List<BlockPos> rootPositions, int layer) {
/*  72 */     int maxRootLength = this.mangroveRootPlacement.maxRootLength();
/*  73 */     if (layer == maxRootLength || rootPositions.size() > maxRootLength) {
/*  74 */       return false;
/*     */     }
/*  76 */     List<BlockPos> potentialRootPositions = potentialRootPositions(rootPos, dir, random, rootOrigin);
/*  77 */     for (BlockPos pos : potentialRootPositions) {
/*  78 */       if (canPlaceRoot(level, pos)) {
/*  79 */         rootPositions.add(pos);
/*  80 */         if (!simulateRoots(level, random, pos, dir, rootOrigin, rootPositions, layer + 1)) {
/*  81 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   protected List<BlockPos> potentialRootPositions(BlockPos pos, Direction prevDir, RandomSource random, BlockPos rootOrigin) {
/*  90 */     BlockPos below = pos.below();
/*  91 */     BlockPos nextTo = pos.relative(prevDir);
/*  92 */     int width = pos.distManhattan(rootOrigin);
/*  93 */     int maxRootWidth = this.mangroveRootPlacement.maxRootWidth();
/*  94 */     float randomSkewChance = this.mangroveRootPlacement.randomSkewChance();
/*     */ 
/*     */     
/*  97 */     if (width > maxRootWidth - 3 && width <= maxRootWidth) {
/*  98 */       return (random.nextFloat() < randomSkewChance) ? List.of(below, nextTo.below()) : List.of(below);
/*     */     }
/*     */ 
/*     */     
/* 102 */     if (width > maxRootWidth) {
/* 103 */       return List.of(below);
/*     */     }
/*     */ 
/*     */     
/* 107 */     if (random.nextFloat() < randomSkewChance) {
/* 108 */       return List.of(below);
/*     */     }
/*     */     
/* 111 */     return random.nextBoolean() ? List.of(nextTo) : List.of(below);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 116 */   protected boolean canPlaceRoot(LevelSimulatedReader level, BlockPos pos) { return (super.canPlaceRoot(level, pos) || level.isStateAtPosition(pos, state -> state.is(this.mangroveRootPlacement.canGrowThrough()))); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void placeRoot(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> rootSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
/* 121 */     if (level.isStateAtPosition(pos, s -> s.is(this.mangroveRootPlacement.muddyRootsIn()))) {
/* 122 */       BlockState muddyRoots = this.mangroveRootPlacement.muddyRootsProvider().getState(random, pos);
/* 123 */       rootSetter.accept(pos, getPotentiallyWaterloggedState(level, pos, muddyRoots));
/*     */     } else {
/* 125 */       super.placeRoot(level, rootSetter, random, pos, config);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 131 */   protected RootPlacerType<?> type() { return RootPlacerType.MANGROVE_ROOT_PLACER; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\MangroveRootPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */