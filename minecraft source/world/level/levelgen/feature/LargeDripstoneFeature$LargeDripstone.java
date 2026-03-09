/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
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
/*     */ 
/*     */ 
/*     */ final class LargeDripstone
/*     */ {
/*     */   private BlockPos root;
/*     */   private final boolean pointingUp;
/*     */   private int radius;
/*     */   private final double bluntness;
/*     */   private final double scale;
/*     */   
/*     */   private LargeDripstone(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale) {
/* 116 */     this.root = root;
/* 117 */     this.pointingUp = pointingUp;
/* 118 */     this.radius = radius;
/* 119 */     this.bluntness = bluntness;
/* 120 */     this.scale = scale;
/*     */   }
/*     */ 
/*     */   
/* 124 */   private int getHeight() { return getHeightAtRadius(0.0F); }
/*     */ 
/*     */   
/*     */   private int getMinY() {
/* 128 */     if (this.pointingUp) {
/* 129 */       return this.root.getY();
/*     */     }
/* 131 */     return this.root.getY() - getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   private int getMaxY() {
/* 136 */     if (!this.pointingUp) {
/* 137 */       return this.root.getY();
/*     */     }
/* 139 */     return this.root.getY() + getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, LargeDripstoneFeature.WindOffsetter wind) {
/* 144 */     while (this.radius > 1) {
/* 145 */       BlockPos.MutableBlockPos newRoot = this.root.mutable();
/* 146 */       int maxTries = Math.min(10, getHeight());
/* 147 */       for (int i = 0; i < maxTries; i++) {
/* 148 */         if (level.getBlockState(newRoot).is(Blocks.LAVA)) {
/* 149 */           return false;
/*     */         }
/* 151 */         if (DripstoneUtils.isCircleMostlyEmbeddedInStone(level, wind.offset(newRoot), this.radius)) {
/* 152 */           this.root = newRoot;
/* 153 */           return true;
/*     */         } 
/* 155 */         newRoot.move(this.pointingUp ? Direction.DOWN : Direction.UP);
/*     */       } 
/* 157 */       this.radius /= 2;
/*     */     } 
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 163 */   private int getHeightAtRadius(float checkRadius) { return (int)DripstoneUtils.getDripstoneHeight(checkRadius, this.radius, this.scale, this.bluntness); }
/*     */ 
/*     */   
/*     */   private void placeBlocks(WorldGenLevel level, RandomSource random, LargeDripstoneFeature.WindOffsetter wind) {
/* 167 */     for (int dx = -this.radius; dx <= this.radius; dx++) {
/* 168 */       for (int dz = -this.radius; dz <= this.radius; dz++) {
/* 169 */         float currentRadius = Mth.sqrt((dx * dx + dz * dz));
/* 170 */         if (currentRadius <= this.radius) {
/*     */ 
/*     */ 
/*     */           
/* 174 */           int height = getHeightAtRadius(currentRadius);
/* 175 */           if (height > 0) {
/*     */ 
/*     */             
/* 178 */             if (random.nextFloat() < 0.2D)
/*     */             {
/* 180 */               height = (int)(height * Mth.randomBetween(random, 0.8F, 1.0F));
/*     */             }
/*     */             
/* 183 */             BlockPos.MutableBlockPos pos = this.root.offset(dx, 0, dz).mutable();
/* 184 */             boolean hasBeenOutOfStone = false;
/*     */             
/* 186 */             int maxY = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) : Integer.MAX_VALUE;
/* 187 */             for (int i = 0; i < height && 
/* 188 */               pos.getY() < maxY; i++) {
/*     */ 
/*     */               
/* 191 */               BlockPos windAdjustedPos = wind.offset(pos);
/* 192 */               if (DripstoneUtils.isEmptyOrWaterOrLava(level, windAdjustedPos)) {
/* 193 */                 hasBeenOutOfStone = true;
/* 194 */                 Block block = SharedConstants.DEBUG_LARGE_DRIPSTONE ? Blocks.GLASS : Blocks.DRIPSTONE_BLOCK;
/* 195 */                 level.setBlock(windAdjustedPos, block.defaultBlockState(), 2);
/* 196 */               } else if (hasBeenOutOfStone && level.getBlockState(windAdjustedPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
/*     */                 break;
/*     */               } 
/* 199 */               pos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 209 */   private boolean isSuitableForWind(LargeDripstoneConfiguration config) { return (this.radius >= config.minRadiusForWind && this.bluntness >= config.minBluntnessForWind); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\LargeDripstoneFeature$LargeDripstone.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */