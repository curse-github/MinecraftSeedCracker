/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.RotatedPillarBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class FancyTrunkPlacer extends TrunkPlacer {
/*  21 */   public static final MapCodec<FancyTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply(i, FancyTrunkPlacer::new));
/*     */   
/*     */   private static final double TRUNK_HEIGHT_SCALE = 0.618D;
/*     */   
/*     */   private static final double CLUSTER_DENSITY_MAGIC = 1.382D;
/*     */   private static final double BRANCH_SLOPE = 0.381D;
/*     */   private static final double BRANCH_LENGTH_MAGIC = 0.328D;
/*     */   
/*  29 */   public FancyTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) { super(baseHeight, heightRandA, heightRandB); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   protected TrunkPlacerType<?> type() { return TrunkPlacerType.FANCY_TRUNK_PLACER; }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int treeHeight, BlockPos origin, TreeConfiguration config) {
/*  39 */     int assumedFoliageHeight = 5;
/*  40 */     int height = treeHeight + 2;
/*  41 */     int trunkHeight = Mth.floor(height * 0.618D);
/*     */     
/*  43 */     setDirtAt(level, trunkSetter, random, origin.below(), config);
/*     */ 
/*     */     
/*  46 */     double foliageDensity = 1.0D;
/*  47 */     int clustersPerY = Math.min(1, Mth.floor(1.382D + Math.pow(1.0D * height / 13.0D, 2.0D)));
/*     */     
/*  49 */     int trunkTop = origin.getY() + trunkHeight;
/*  50 */     int relativeY = height - 5;
/*     */     
/*  52 */     List<FoliageCoords> foliageCoords = Lists.newArrayList();
/*  53 */     foliageCoords.add(new FoliageCoords(origin.above(relativeY), trunkTop));
/*     */     
/*  55 */     for (; relativeY >= 0; relativeY--) {
/*  56 */       float treeShape = treeShape(height, relativeY);
/*  57 */       if (treeShape >= 0.0F)
/*     */       {
/*     */ 
/*     */         
/*  61 */         for (int i = 0; i < clustersPerY; i++) {
/*  62 */           double widthScale = 1.0D;
/*  63 */           double radius = 1.0D * treeShape * (random.nextFloat() + 0.328D);
/*  64 */           double angle = (random.nextFloat() * 2.0F) * Math.PI;
/*     */           
/*  66 */           double x = radius * Math.sin(angle) + 0.5D;
/*  67 */           double z = radius * Math.cos(angle) + 0.5D;
/*     */           
/*  69 */           BlockPos checkStart = origin.offset(Mth.floor(x), relativeY - 1, Mth.floor(z));
/*  70 */           BlockPos checkEnd = checkStart.above(5);
/*     */ 
/*     */           
/*  73 */           if (makeLimb(level, trunkSetter, random, checkStart, checkEnd, false, config)) {
/*     */             
/*  75 */             int dx = origin.getX() - checkStart.getX();
/*  76 */             int dz = origin.getZ() - checkStart.getZ();
/*     */             
/*  78 */             double branchHeight = checkStart.getY() - Math.sqrt((dx * dx + dz * dz)) * 0.381D;
/*  79 */             int branchTop = (branchHeight > trunkTop) ? trunkTop : (int)branchHeight;
/*  80 */             BlockPos checkBranchBase = new BlockPos(origin.getX(), branchTop, origin.getZ());
/*     */ 
/*     */             
/*  83 */             if (makeLimb(level, trunkSetter, random, checkBranchBase, checkStart, false, config))
/*     */             {
/*  85 */               foliageCoords.add(new FoliageCoords(checkStart, checkBranchBase.getY())); } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*  90 */     makeLimb(level, trunkSetter, random, origin, origin.above(trunkHeight), true, config);
/*  91 */     makeBranches(level, trunkSetter, random, height, origin, foliageCoords, config);
/*     */     
/*  93 */     List<FoliagePlacer.FoliageAttachment> attachments = Lists.newArrayList();
/*  94 */     for (FoliageCoords foliageCoord : foliageCoords) {
/*  95 */       if (trimBranches(height, foliageCoord.getBranchBase() - origin.getY())) {
/*  96 */         attachments.add(foliageCoord.attachment);
/*     */       }
/*     */     } 
/*  99 */     return attachments;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean makeLimb(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos startPos, BlockPos endPos, boolean doPlace, TreeConfiguration config) {
/* 104 */     if (!doPlace && Objects.equals(startPos, endPos)) {
/* 105 */       return true;
/*     */     }
/*     */     
/* 108 */     BlockPos delta = endPos.offset(-startPos.getX(), -startPos.getY(), -startPos.getZ());
/*     */     
/* 110 */     int steps = getSteps(delta);
/*     */     
/* 112 */     float dx = delta.getX() / steps;
/* 113 */     float dy = delta.getY() / steps;
/* 114 */     float dz = delta.getZ() / steps;
/*     */     
/* 116 */     for (int i = 0; i <= steps; i++) {
/* 117 */       BlockPos blockPos = startPos.offset(Mth.floor(0.5F + i * dx), Mth.floor(0.5F + i * dy), Mth.floor(0.5F + i * dz));
/* 118 */       if (doPlace) {
/* 119 */         placeLog(level, trunkSetter, random, blockPos, config, state -> (BlockState)state.trySetValue(RotatedPillarBlock.AXIS, getLogAxis(startPos, blockPos)));
/*     */       
/*     */       }
/* 122 */       else if (!isFree(level, blockPos)) {
/* 123 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     return true;
/*     */   }
/*     */   
/*     */   private int getSteps(BlockPos pos) {
/* 131 */     int absX = Mth.abs(pos.getX());
/* 132 */     int absY = Mth.abs(pos.getY());
/* 133 */     int absZ = Mth.abs(pos.getZ());
/*     */     
/* 135 */     return Math.max(absX, Math.max(absY, absZ));
/*     */   }
/*     */   
/*     */   private Direction.Axis getLogAxis(BlockPos startPos, BlockPos blockPos) {
/* 139 */     Direction.Axis axis = Direction.Axis.Y;
/* 140 */     int xdiff = Math.abs(blockPos.getX() - startPos.getX());
/* 141 */     int zdiff = Math.abs(blockPos.getZ() - startPos.getZ());
/* 142 */     int maxdiff = Math.max(xdiff, zdiff);
/*     */     
/* 144 */     if (maxdiff > 0) {
/* 145 */       if (xdiff == maxdiff) {
/* 146 */         axis = Direction.Axis.X;
/*     */       } else {
/* 148 */         axis = Direction.Axis.Z;
/*     */       } 
/*     */     }
/* 151 */     return axis;
/*     */   }
/*     */ 
/*     */   
/* 155 */   private boolean trimBranches(int height, int localY) { return (localY >= height * 0.2D); }
/*     */ 
/*     */   
/*     */   private void makeBranches(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, int height, BlockPos origin, List<FoliageCoords> foliageCoords, TreeConfiguration config) {
/* 159 */     for (FoliageCoords endCoord : foliageCoords) {
/* 160 */       int branchBase = endCoord.getBranchBase();
/* 161 */       BlockPos baseCoord = new BlockPos(origin.getX(), branchBase, origin.getZ());
/*     */       
/* 163 */       if (!baseCoord.equals(endCoord.attachment.pos()) && trimBranches(height, branchBase - origin.getY())) {
/* 164 */         makeLimb(level, trunkSetter, random, baseCoord, endCoord.attachment.pos(), true, config);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static float treeShape(int height, int y) {
/* 171 */     if (y < height * 0.3F) {
/* 172 */       return -1.0F;
/*     */     }
/*     */     
/* 175 */     float radius = height / 2.0F;
/* 176 */     float adjacent = radius - y;
/*     */     
/* 178 */     float distance = Mth.sqrt(radius * radius - adjacent * adjacent);
/* 179 */     if (adjacent == 0.0F) {
/* 180 */       distance = radius;
/* 181 */     } else if (Math.abs(adjacent) >= radius) {
/* 182 */       return 0.0F;
/*     */     } 
/*     */     
/* 185 */     return distance * 0.5F;
/*     */   }
/*     */   
/*     */   private static class FoliageCoords {
/*     */     private final FoliagePlacer.FoliageAttachment attachment;
/*     */     private final int branchBase;
/*     */     
/*     */     public FoliageCoords(BlockPos pos, int branchBase) {
/* 193 */       this.attachment = new FoliagePlacer.FoliageAttachment(pos, 0, false);
/* 194 */       this.branchBase = branchBase;
/*     */     }
/*     */ 
/*     */     
/* 198 */     public int getBranchBase() { return this.branchBase; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\FancyTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */