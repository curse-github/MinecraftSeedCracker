/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public class VegetationPatchFeature
/*     */   extends Feature<VegetationPatchConfiguration>
/*     */ {
/*  20 */   public VegetationPatchFeature(Codec<VegetationPatchConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<VegetationPatchConfiguration> context) {
/*  25 */     WorldGenLevel level = context.level();
/*  26 */     VegetationPatchConfiguration config = (VegetationPatchConfiguration)context.config();
/*  27 */     RandomSource random = context.random();
/*  28 */     BlockPos origin = context.origin();
/*  29 */     Predicate<BlockState> replaceable = s -> s.is(config.replaceable);
/*     */     
/*  31 */     int xRadius = config.xzRadius.sample(random) + 1;
/*  32 */     int zRadius = config.xzRadius.sample(random) + 1;
/*     */     
/*  34 */     Set<BlockPos> surface = placeGroundPatch(level, config, random, origin, replaceable, xRadius, zRadius);
/*  35 */     distributeVegetation(context, level, config, random, surface, xRadius, zRadius);
/*     */     
/*  37 */     return !surface.isEmpty();
/*     */   }
/*     */   
/*     */   protected Set<BlockPos> placeGroundPatch(WorldGenLevel level, VegetationPatchConfiguration config, RandomSource random, BlockPos origin, Predicate<BlockState> replaceable, int xRadius, int zRadius) {
/*  41 */     BlockPos.MutableBlockPos pos = origin.mutable();
/*  42 */     BlockPos.MutableBlockPos belowPos = pos.mutable();
/*  43 */     Direction inwards = config.surface.getDirection();
/*  44 */     Direction outwards = inwards.getOpposite();
/*  45 */     Set<BlockPos> surface = new HashSet<BlockPos>();
/*  46 */     for (int dx = -xRadius; dx <= xRadius; dx++) {
/*  47 */       boolean isXEdge = (dx == -xRadius || dx == xRadius);
/*  48 */       for (int dz = -zRadius; dz <= zRadius; dz++) {
/*  49 */         boolean isZEdge = (dz == -zRadius || dz == zRadius);
/*  50 */         boolean isEdge = (isXEdge || isZEdge);
/*  51 */         boolean isCorner = (isXEdge && isZEdge);
/*  52 */         boolean isEdgeButNotCorner = (isEdge && !isCorner);
/*  53 */         if (!isCorner && (!isEdgeButNotCorner || (config.extraEdgeColumnChance != 0.0F && random.nextFloat() <= config.extraEdgeColumnChance))) {
/*     */ 
/*     */           
/*  56 */           pos.setWithOffset(origin, dx, 0, dz);
/*  57 */           int offset = 0;
/*  58 */           while (level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir) && offset < config.verticalRange) {
/*  59 */             pos.move(inwards);
/*  60 */             offset++;
/*     */           } 
/*  62 */           offset = 0;
/*  63 */           while (level.isStateAtPosition(pos, s -> !s.isAir()) && offset < config.verticalRange) {
/*  64 */             pos.move(outwards);
/*  65 */             offset++;
/*     */           } 
/*     */           
/*  68 */           belowPos.setWithOffset(pos, config.surface.getDirection());
/*  69 */           BlockState belowState = level.getBlockState(belowPos);
/*  70 */           if (level.isEmptyBlock(pos) && belowState.isFaceSturdy(level, belowPos, config.surface.getDirection().getOpposite())) {
/*  71 */             int depth = config.depth.sample(random) + ((config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance) ? 1 : 0);
/*  72 */             BlockPos groundPos = belowPos.immutable();
/*  73 */             boolean groundPlaced = placeGround(level, config, replaceable, random, belowPos, depth);
/*  74 */             if (groundPlaced)
/*  75 */               surface.add(groundPos); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  80 */     return surface;
/*     */   }
/*     */   
/*     */   protected void distributeVegetation(FeaturePlaceContext<VegetationPatchConfiguration> context, WorldGenLevel level, VegetationPatchConfiguration config, RandomSource random, Set<BlockPos> surface, int xRadius, int zRadius) {
/*  84 */     for (BlockPos surfacePos : surface) {
/*  85 */       if (config.vegetationChance > 0.0F && random.nextFloat() < config.vegetationChance) {
/*  86 */         placeVegetation(level, config, context.chunkGenerator(), random, surfacePos);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  92 */   protected boolean placeVegetation(WorldGenLevel level, VegetationPatchConfiguration config, ChunkGenerator generator, RandomSource random, BlockPos vegetationPos) { return ((PlacedFeature)config.vegetationFeature.value()).place(level, generator, random, vegetationPos.relative(config.surface.getDirection().getOpposite())); }
/*     */ 
/*     */   
/*     */   protected boolean placeGround(WorldGenLevel level, VegetationPatchConfiguration config, Predicate<BlockState> replaceable, RandomSource random, BlockPos.MutableBlockPos belowPos, int depth) {
/*  96 */     for (int i = 0; i < depth; i++) {
/*  97 */       BlockState stateToPlace = config.groundState.getState(random, belowPos);
/*  98 */       BlockState belowState = level.getBlockState(belowPos);
/*  99 */       if (!stateToPlace.is(belowState.getBlock())) {
/*     */ 
/*     */ 
/*     */         
/* 103 */         if (!replaceable.test(belowState)) {
/* 104 */           return (i != 0);
/*     */         }
/*     */         
/* 107 */         level.setBlock(belowPos, stateToPlace, 2);
/* 108 */         belowPos.move(config.surface.getDirection());
/*     */       } 
/* 110 */     }  return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\VegetationPatchFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */