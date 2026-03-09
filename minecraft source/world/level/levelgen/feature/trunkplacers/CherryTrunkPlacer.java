/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class CherryTrunkPlacer extends TrunkPlacer {
/*  24 */   private static final Codec<UniformInt> BRANCH_START_CODEC = UniformInt.CODEC.codec().validate(u -> {
/*  25 */         if (u.getMaxValue() - u.getMinValue() < 1) {
/*  26 */           return DataResult.error(());
/*     */         }
/*  28 */         return DataResult.success(u);
/*     */       });
/*     */   
/*  31 */   public static final MapCodec<CherryTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i.group(
/*  32 */           IntProvider.codec(1, 3).fieldOf("branch_count").forGetter(()), 
/*  33 */           IntProvider.codec(2, 16).fieldOf("branch_horizontal_length").forGetter(()), 
/*  34 */           IntProvider.validateCodec(-16, 0, BRANCH_START_CODEC).fieldOf("branch_start_offset_from_top").forGetter(()), 
/*  35 */           IntProvider.codec(-16, 16).fieldOf("branch_end_offset_from_top").forGetter(())))
/*  36 */       .apply(i, CherryTrunkPlacer::new));
/*     */   
/*     */   private final IntProvider branchCount;
/*     */   
/*     */   private final IntProvider branchHorizontalLength;
/*     */   private final UniformInt branchStartOffsetFromTop;
/*     */   private final UniformInt secondBranchStartOffsetFromTop;
/*     */   private final IntProvider branchEndOffsetFromTop;
/*     */   
/*     */   public CherryTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider branchCount, IntProvider branchHorizontalLength, UniformInt branchStartOffsetFromTop, IntProvider branchEndOffsetFromTop) {
/*  46 */     super(baseHeight, heightRandA, heightRandB);
/*  47 */     this.branchCount = branchCount;
/*  48 */     this.branchHorizontalLength = branchHorizontalLength;
/*  49 */     this.branchStartOffsetFromTop = branchStartOffsetFromTop;
/*  50 */     this.secondBranchStartOffsetFromTop = UniformInt.of(branchStartOffsetFromTop.getMinValue(), branchStartOffsetFromTop.getMaxValue() - 1);
/*  51 */     this.branchEndOffsetFromTop = branchEndOffsetFromTop;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected TrunkPlacerType<?> type() { return TrunkPlacerType.CHERRY_TRUNK_PLACER; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/*     */     int trunkHeight;
/*  68 */     setDirtAt(level, trunkSetter, random, origin.below(), config);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     int firstBranchOffsetFromOrigin = Math.max(0, treeHeight - 1 + this.branchStartOffsetFromTop.sample(random));
/*  80 */     int secondBranchOffsetFromOrigin = Math.max(0, treeHeight - 1 + this.secondBranchStartOffsetFromTop.sample(random));
/*  81 */     if (secondBranchOffsetFromOrigin >= firstBranchOffsetFromOrigin) {
/*  82 */       secondBranchOffsetFromOrigin++;
/*     */     }
/*     */     
/*  85 */     int branchCount = this.branchCount.sample(random);
/*  86 */     boolean hasMiddleBranch = (branchCount == 3);
/*  87 */     boolean hasBothSideBranches = (branchCount >= 2);
/*     */ 
/*     */     
/*  90 */     if (hasMiddleBranch) {
/*  91 */       trunkHeight = treeHeight;
/*  92 */     } else if (hasBothSideBranches) {
/*  93 */       trunkHeight = Math.max(firstBranchOffsetFromOrigin, secondBranchOffsetFromOrigin) + 1;
/*     */     } else {
/*  95 */       trunkHeight = firstBranchOffsetFromOrigin + 1;
/*     */     } 
/*     */     
/*  98 */     for (int y = 0; y < trunkHeight; y++) {
/*  99 */       placeLog(level, trunkSetter, random, origin.above(y), config);
/*     */     }
/*     */     
/* 102 */     List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<FoliagePlacer.FoliageAttachment>();
/*     */     
/* 104 */     if (hasMiddleBranch) {
/* 105 */       attachments.add(new FoliagePlacer.FoliageAttachment(origin.above(trunkHeight), 0, false));
/*     */     }
/*     */     
/* 108 */     BlockPos.MutableBlockPos logPos = new BlockPos.MutableBlockPos();
/* 109 */     Direction treeDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 110 */     Function<BlockState, BlockState> sidewaysStateModifier = state -> (BlockState)state.trySetValue(RotatedPillarBlock.AXIS, treeDirection.getAxis());
/*     */     
/* 112 */     attachments.add(generateBranch(level, trunkSetter, random, treeHeight, origin, config, sidewaysStateModifier, treeDirection, firstBranchOffsetFromOrigin, (firstBranchOffsetFromOrigin < trunkHeight - 1), logPos));
/*     */     
/* 114 */     if (hasBothSideBranches) {
/* 115 */       attachments.add(generateBranch(level, trunkSetter, random, treeHeight, origin, config, sidewaysStateModifier, treeDirection.getOpposite(), secondBranchOffsetFromOrigin, (secondBranchOffsetFromOrigin < trunkHeight - 1), logPos));
/*     */     }
/*     */     
/* 118 */     return attachments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FoliagePlacer.FoliageAttachment generateBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config, Function<BlockState, BlockState> sidewaysStateModifier, Direction branchDirection, int offsetFromOrigin, boolean middleContinuesUpwards, BlockPos.MutableBlockPos logPos) {
/* 134 */     logPos.set(origin).move(Direction.UP, offsetFromOrigin);
/*     */     
/* 136 */     int branchEndPosOffsetFromOrigin = treeHeight - 1 + this.branchEndOffsetFromTop.sample(random);
/*     */     
/* 138 */     boolean extendBranchAwayFromTrunk = (middleContinuesUpwards || branchEndPosOffsetFromOrigin < offsetFromOrigin);
/* 139 */     int distanceToTrunk = this.branchHorizontalLength.sample(random) + (extendBranchAwayFromTrunk ? 1 : 0);
/*     */ 
/*     */     
/* 142 */     BlockPos branchEndPos = origin.relative(branchDirection, distanceToTrunk).above(branchEndPosOffsetFromOrigin);
/*     */     
/* 144 */     int stepsHorizontally = extendBranchAwayFromTrunk ? 2 : 1;
/*     */     
/* 146 */     for (int i = 0; i < stepsHorizontally; i++) {
/* 147 */       placeLog(level, trunkSetter, random, logPos.move(branchDirection), config, sidewaysStateModifier);
/*     */     }
/*     */     
/* 150 */     Direction verticalDirection = (branchEndPos.getY() > logPos.getY()) ? Direction.UP : Direction.DOWN;
/*     */     
/*     */     while (true) {
/* 153 */       int distance = logPos.distManhattan(branchEndPos);
/* 154 */       if (distance == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 158 */       float chanceToGrowVertically = Math.abs(branchEndPos.getY() - logPos.getY()) / distance;
/* 159 */       boolean growVertically = (random.nextFloat() < chanceToGrowVertically);
/*     */       
/* 161 */       logPos.move(growVertically ? verticalDirection : branchDirection);
/* 162 */       placeLog(level, trunkSetter, random, logPos, config, growVertically ? Function.identity() : sidewaysStateModifier);
/*     */     } 
/*     */     
/* 165 */     return new FoliagePlacer.FoliageAttachment(branchEndPos.above(), 0, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\CherryTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */