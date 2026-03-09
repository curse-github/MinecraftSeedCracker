/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class UpwardsBranchingTrunkPlacer extends TrunkPlacer {
/*  24 */   public static final MapCodec<UpwardsBranchingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).and(i
/*  25 */         .group(IntProvider.POSITIVE_CODEC
/*  26 */           .fieldOf("extra_branch_steps").forGetter(()), 
/*  27 */           Codec.floatRange(0.0F, 1.0F).fieldOf("place_branch_per_log_probability").forGetter(()), IntProvider.NON_NEGATIVE_CODEC
/*  28 */           .fieldOf("extra_branch_length").forGetter(()), 
/*  29 */           RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter(())))
/*     */       
/*  31 */       .apply(i, UpwardsBranchingTrunkPlacer::new));
/*     */   
/*     */   private final IntProvider extraBranchSteps;
/*     */   private final float placeBranchPerLogProbability;
/*     */   private final IntProvider extraBranchLength;
/*     */   private final HolderSet<Block> canGrowThrough;
/*     */   
/*     */   public UpwardsBranchingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider extraBranchSteps, float placeBranchPerLogProbability, IntProvider extraBranchLength, HolderSet<Block> canGrowThrough) {
/*  39 */     super(baseHeight, heightRandA, heightRandB);
/*  40 */     this.extraBranchSteps = extraBranchSteps;
/*  41 */     this.placeBranchPerLogProbability = placeBranchPerLogProbability;
/*  42 */     this.extraBranchLength = extraBranchLength;
/*  43 */     this.canGrowThrough = canGrowThrough;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  48 */   protected TrunkPlacerType<?> type() { return TrunkPlacerType.UPWARDS_BRANCHING_TRUNK_PLACER; }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/*  53 */     List<FoliagePlacer.FoliageAttachment> attachments = Lists.newArrayList();
/*     */     
/*  55 */     BlockPos.MutableBlockPos logPos = new BlockPos.MutableBlockPos();
/*  56 */     for (int heightPos = 0; heightPos < treeHeight; heightPos++) {
/*  57 */       int currentHeight = origin.getY() + heightPos;
/*  58 */       if (placeLog(level, trunkSetter, random, logPos.set(origin.getX(), currentHeight, origin.getZ()), config) && 
/*  59 */         heightPos < treeHeight - 1 && random.nextFloat() < this.placeBranchPerLogProbability) {
/*  60 */         Direction branchDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/*  61 */         int branchLen = this.extraBranchLength.sample(random);
/*  62 */         int branchPos = Math.max(0, branchLen - this.extraBranchLength.sample(random) - 1);
/*  63 */         int branchSteps = this.extraBranchSteps.sample(random);
/*  64 */         placeBranch(level, trunkSetter, random, treeHeight, config, attachments, logPos, currentHeight, branchDir, branchPos, branchSteps);
/*     */       } 
/*     */ 
/*     */       
/*  68 */       if (heightPos == treeHeight - 1) {
/*  69 */         attachments.add(new FoliagePlacer.FoliageAttachment(logPos.set(origin.getX(), currentHeight + 1, origin.getZ()), 0, false));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return attachments;
/*     */   }
/*     */   
/*     */   private void placeBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, TreeConfiguration config, List<FoliagePlacer.FoliageAttachment> attachments, BlockPos.MutableBlockPos logPos, int currentHeight, Direction branchDir, int branchPos, int branchSteps) {
/*  78 */     int heightAlongBranch = currentHeight + branchPos;
/*  79 */     int logX = logPos.getX();
/*  80 */     int logZ = logPos.getZ();
/*  81 */     for (int branchPlacementIndex = branchPos; branchPlacementIndex < treeHeight && branchSteps > 0; branchPlacementIndex++, branchSteps--) {
/*  82 */       if (branchPlacementIndex >= 1) {
/*     */ 
/*     */         
/*  85 */         int placementHeight = currentHeight + branchPlacementIndex;
/*  86 */         logX += branchDir.getStepX();
/*  87 */         logZ += branchDir.getStepZ();
/*  88 */         heightAlongBranch = placementHeight;
/*     */         
/*  90 */         if (placeLog(level, trunkSetter, random, logPos.set(logX, placementHeight, logZ), config)) {
/*  91 */           heightAlongBranch++;
/*     */         }
/*     */         
/*  94 */         attachments.add(new FoliagePlacer.FoliageAttachment(logPos.immutable(), 0, false));
/*     */       } 
/*  96 */     }  if (heightAlongBranch - currentHeight > 1) {
/*  97 */       BlockPos foliagePos = new BlockPos(logX, heightAlongBranch, logZ);
/*  98 */       attachments.add(new FoliagePlacer.FoliageAttachment(foliagePos, 0, false));
/*  99 */       attachments.add(new FoliagePlacer.FoliageAttachment(foliagePos.below(2), 0, false));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected boolean validTreePos(LevelSimulatedReader level, BlockPos pos) { return (super.validTreePos(level, pos) || level.isStateAtPosition(pos, s -> s.is(this.canGrowThrough))); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\UpwardsBranchingTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */