/*     */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*     */ 
/*     */ import com.mojang.datafixers.Products;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public abstract class FoliagePlacer {
/*  20 */   public static final Codec<FoliagePlacer> CODEC = BuiltInRegistries.FOLIAGE_PLACER_TYPE.byNameCodec().dispatch(FoliagePlacer::type, FoliagePlacerType::codec);
/*     */   
/*     */   protected final IntProvider radius;
/*     */   protected final IntProvider offset;
/*     */   
/*     */   protected static <P extends FoliagePlacer> Products.P2<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider> foliagePlacerParts(RecordCodecBuilder.Instance<P> instance) {
/*  26 */     return instance.group(
/*  27 */         IntProvider.codec(0, 16).fieldOf("radius").forGetter(p -> p.radius), 
/*  28 */         IntProvider.codec(0, 16).fieldOf("offset").forGetter(p -> p.offset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FoliagePlacer(IntProvider radius, IntProvider offset) {
/*  39 */     this.radius = radius;
/*  40 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public void createFoliage(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int treeHeight, FoliageAttachment foliageAttachment, int foliageHeight, int leafRadius) { createFoliage(level, foliageSetter, random, config, treeHeight, foliageAttachment, foliageHeight, leafRadius, offset(random)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public int foliageRadius(RandomSource random, int trunkHeight) { return this.radius.sample(random); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   private int offset(RandomSource random) { return this.offset.sample(random); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldSkipLocationSigned(RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
/*     */     int minDz;
/*     */     int minDx;
/*  66 */     if (doubleTrunk) {
/*     */ 
/*     */       
/*  69 */       minDx = Math.min(Math.abs(dx), Math.abs(dx - 1));
/*  70 */       minDz = Math.min(Math.abs(dz), Math.abs(dz - 1));
/*     */     }
/*     */     else {
/*     */       
/*  74 */       minDx = Math.abs(dx);
/*  75 */       minDz = Math.abs(dz);
/*     */     } 
/*  77 */     return shouldSkipLocation(random, minDx, y, minDz, currentRadius, doubleTrunk);
/*     */   }
/*     */   
/*     */   protected void placeLeavesRow(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, BlockPos origin, int currentRadius, int y, boolean doubleTrunk) {
/*  81 */     int offset = doubleTrunk ? 1 : 0;
/*  82 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*  83 */     for (int dx = -currentRadius; dx <= currentRadius + offset; dx++) {
/*  84 */       for (int dz = -currentRadius; dz <= currentRadius + offset; dz++) {
/*  85 */         if (!shouldSkipLocationSigned(random, dx, y, dz, currentRadius, doubleTrunk)) {
/*     */ 
/*     */           
/*  88 */           pos.setWithOffset(origin, dx, y, dz);
/*  89 */           tryPlaceLeaf(level, foliageSetter, random, config, pos);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void placeLeavesRowWithHangingLeavesBelow(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, BlockPos origin, int currentRadius, int y, boolean doubleTrunk, float hangingLeavesChance, float hangingLeavesExtensionChance) {
/*  98 */     placeLeavesRow(level, foliageSetter, random, config, origin, currentRadius, y, doubleTrunk);
/*     */     
/* 100 */     int offset = doubleTrunk ? 1 : 0;
/* 101 */     BlockPos logPos = origin.below();
/* 102 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 103 */     for (Direction alongEdge : Direction.Plane.HORIZONTAL) {
/* 104 */       Direction toEdge = alongEdge.getClockWise();
/* 105 */       int offsetToEdge = (toEdge.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (currentRadius + offset) : currentRadius;
/*     */       
/* 107 */       pos.setWithOffset(origin, 0, y - 1, 0)
/* 108 */         .move(toEdge, offsetToEdge)
/* 109 */         .move(alongEdge, -currentRadius);
/*     */       
/* 111 */       for (int offsetAlongEdge = -currentRadius; offsetAlongEdge < currentRadius + offset; offsetAlongEdge++, pos.move(alongEdge)) {
/*     */ 
/*     */         
/* 114 */         boolean leavesAbove = foliageSetter.isSet(pos.move(Direction.UP));
/* 115 */         pos.move(Direction.DOWN);
/*     */         
/* 117 */         if (leavesAbove)
/*     */         {
/*     */           
/* 120 */           if (tryPlaceExtension(level, foliageSetter, random, config, hangingLeavesChance, logPos, pos)) {
/*     */ 
/*     */             
/* 123 */             pos.move(Direction.DOWN);
/* 124 */             tryPlaceExtension(level, foliageSetter, random, config, hangingLeavesExtensionChance, logPos, pos);
/* 125 */             pos.move(Direction.UP);
/*     */           }  } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static boolean tryPlaceExtension(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, float chance, BlockPos logPos, BlockPos.MutableBlockPos pos) {
/* 131 */     if (pos.distManhattan(logPos) >= 7) {
/* 132 */       return false;
/*     */     }
/* 134 */     if (random.nextFloat() > chance) {
/* 135 */       return false;
/*     */     }
/* 137 */     return tryPlaceLeaf(level, foliageSetter, random, config, pos);
/*     */   }
/*     */   
/*     */   protected static boolean tryPlaceLeaf(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, BlockPos pos) {
/* 141 */     boolean isPersistent = level.isStateAtPosition(pos, state -> ((Boolean)state.getValueOrElse(BlockStateProperties.PERSISTENT, Boolean.valueOf(false))).booleanValue());
/* 142 */     if (isPersistent || !TreeFeature.validTreePos(level, pos)) {
/* 143 */       return false;
/*     */     }
/* 145 */     BlockState foliageState = config.foliageProvider.getState(random, pos);
/* 146 */     if (foliageState.hasProperty(BlockStateProperties.WATERLOGGED)) {
/* 147 */       foliageState = (BlockState)foliageState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(level.isFluidAtPosition(pos, fluidState -> fluidState.isSourceOfType(Fluids.WATER))));
/*     */     }
/* 149 */     foliageSetter.set(pos, foliageState);
/* 150 */     return true;
/*     */   } protected abstract FoliagePlacerType<?> type(); protected abstract void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4);
/*     */   public abstract int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration);
/*     */   protected abstract boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
/*     */   public static interface FoliageSetter {
/*     */     void set(BlockPos param1BlockPos, BlockState param1BlockState);
/*     */     boolean isSet(BlockPos param1BlockPos); }
/*     */   public static final class FoliageAttachment { private final BlockPos pos;
/*     */     public FoliageAttachment(BlockPos pos, int radiusOffset, boolean doubleTrunk) {
/* 159 */       this.pos = pos;
/* 160 */       this.radiusOffset = radiusOffset;
/* 161 */       this.doubleTrunk = doubleTrunk;
/*     */     }
/*     */     private final int radiusOffset; private final boolean doubleTrunk;
/*     */     
/* 165 */     public BlockPos pos() { return this.pos; }
/*     */ 
/*     */ 
/*     */     
/* 169 */     public int radiusOffset() { return this.radiusOffset; }
/*     */ 
/*     */ 
/*     */     
/* 173 */     public boolean doubleTrunk() { return this.doubleTrunk; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */