/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.FloatProvider;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Column;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LargeDripstoneFeature
/*     */   extends Feature<LargeDripstoneConfiguration>
/*     */ {
/*  28 */   public LargeDripstoneFeature(Codec<LargeDripstoneConfiguration> codec) { super(codec); }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<LargeDripstoneConfiguration> context) {
/*     */     WindOffsetter wind;
/*  33 */     WorldGenLevel level = context.level();
/*  34 */     BlockPos origin = context.origin();
/*  35 */     LargeDripstoneConfiguration config = (LargeDripstoneConfiguration)context.config();
/*  36 */     RandomSource random = context.random();
/*     */     
/*  38 */     if (!DripstoneUtils.isEmptyOrWater(level, origin)) {
/*  39 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  43 */     Optional<Column> column = Column.scan(level, origin, config.floorToCeilingSearchRange, DripstoneUtils::isEmptyOrWater, DripstoneUtils::isDripstoneBaseOrLava);
/*  44 */     if (column.isEmpty() || !(column.get() instanceof Column.Range))
/*     */     {
/*     */ 
/*     */       
/*  48 */       return false;
/*     */     }
/*     */     
/*  51 */     Column.Range columnRange = (Column.Range)column.get();
/*     */     
/*  53 */     if (columnRange.height() < 4)
/*     */     {
/*  55 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  59 */     int maxColumnRadiusBasedOnColumnHeight = (int)(columnRange.height() * config.maxColumnRadiusToCaveHeightRatio);
/*  60 */     int maxColumnRadius = Mth.clamp(maxColumnRadiusBasedOnColumnHeight, config.columnRadius.getMinValue(), config.columnRadius.getMaxValue());
/*  61 */     int radius = Mth.randomBetweenInclusive(random, config.columnRadius.getMinValue(), maxColumnRadius);
/*     */     
/*  63 */     LargeDripstone stalactite = makeDripstone(origin.atY(columnRange.ceiling() - 1), false, random, radius, config.stalactiteBluntness, config.heightScale);
/*  64 */     LargeDripstone stalagmite = makeDripstone(origin.atY(columnRange.floor() + 1), true, random, radius, config.stalagmiteBluntness, config.heightScale);
/*     */ 
/*     */     
/*  67 */     if (stalactite.isSuitableForWind(config) && stalagmite.isSuitableForWind(config)) {
/*  68 */       wind = new WindOffsetter(origin.getY(), random, config.windSpeed);
/*     */     } else {
/*  70 */       wind = WindOffsetter.noWind();
/*     */     } 
/*     */     
/*  73 */     boolean stalactiteBaseEmbeddedInStone = stalactite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
/*  74 */     boolean stalagmiteBaseEmbeddedInStone = stalagmite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
/*     */     
/*  76 */     if (stalactiteBaseEmbeddedInStone) {
/*  77 */       stalactite.placeBlocks(level, random, wind);
/*     */     }
/*     */     
/*  80 */     if (stalagmiteBaseEmbeddedInStone) {
/*  81 */       stalagmite.placeBlocks(level, random, wind);
/*     */     }
/*     */     
/*  84 */     if (SharedConstants.DEBUG_LARGE_DRIPSTONE) {
/*  85 */       placeDebugMarkers(level, origin, columnRange, wind);
/*     */     }
/*     */     
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  92 */   private static LargeDripstone makeDripstone(BlockPos root, boolean pointingUp, RandomSource random, int radius, FloatProvider bluntness, FloatProvider heightScale) { return new LargeDripstone(root, pointingUp, radius, bluntness.sample(random), heightScale.sample(random)); }
/*     */ 
/*     */   
/*     */   private void placeDebugMarkers(WorldGenLevel level, BlockPos origin, Column.Range range, WindOffsetter wind) {
/*  96 */     level.setBlock(wind.offset(origin.atY(range.ceiling() - 1)), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
/*  97 */     level.setBlock(wind.offset(origin.atY(range.floor() + 1)), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
/*  98 */     BlockPos.MutableBlockPos pos = origin.atY(range.floor() + 2).mutable();
/*  99 */     while (pos.getY() < range.ceiling() - 1) {
/* 100 */       BlockPos windAdjustedPos = wind.offset(pos);
/* 101 */       if (DripstoneUtils.isEmptyOrWater(level, windAdjustedPos) || level.getBlockState(windAdjustedPos).is(Blocks.DRIPSTONE_BLOCK)) {
/* 102 */         level.setBlock(windAdjustedPos, Blocks.CREEPER_HEAD.defaultBlockState(), 2);
/*     */       }
/* 104 */       pos.move(Direction.UP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class LargeDripstone {
/*     */     private BlockPos root;
/*     */     private final boolean pointingUp;
/*     */     private int radius;
/*     */     private final double bluntness;
/*     */     private final double scale;
/*     */     
/*     */     private LargeDripstone(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale) {
/* 116 */       this.root = root;
/* 117 */       this.pointingUp = pointingUp;
/* 118 */       this.radius = radius;
/* 119 */       this.bluntness = bluntness;
/* 120 */       this.scale = scale;
/*     */     }
/*     */ 
/*     */     
/* 124 */     private int getHeight() { return getHeightAtRadius(0.0F); }
/*     */ 
/*     */     
/*     */     private int getMinY() {
/* 128 */       if (this.pointingUp) {
/* 129 */         return this.root.getY();
/*     */       }
/* 131 */       return this.root.getY() - getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     private int getMaxY() {
/* 136 */       if (!this.pointingUp) {
/* 137 */         return this.root.getY();
/*     */       }
/* 139 */       return this.root.getY() + getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, LargeDripstoneFeature.WindOffsetter wind) {
/* 144 */       while (this.radius > 1) {
/* 145 */         BlockPos.MutableBlockPos newRoot = this.root.mutable();
/* 146 */         int maxTries = Math.min(10, getHeight());
/* 147 */         for (int i = 0; i < maxTries; i++) {
/* 148 */           if (level.getBlockState(newRoot).is(Blocks.LAVA)) {
/* 149 */             return false;
/*     */           }
/* 151 */           if (DripstoneUtils.isCircleMostlyEmbeddedInStone(level, wind.offset(newRoot), this.radius)) {
/* 152 */             this.root = newRoot;
/* 153 */             return true;
/*     */           } 
/* 155 */           newRoot.move(this.pointingUp ? Direction.DOWN : Direction.UP);
/*     */         } 
/* 157 */         this.radius /= 2;
/*     */       } 
/* 159 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 163 */     private int getHeightAtRadius(float checkRadius) { return (int)DripstoneUtils.getDripstoneHeight(checkRadius, this.radius, this.scale, this.bluntness); }
/*     */ 
/*     */     
/*     */     private void placeBlocks(WorldGenLevel level, RandomSource random, LargeDripstoneFeature.WindOffsetter wind) {
/* 167 */       for (int dx = -this.radius; dx <= this.radius; dx++) {
/* 168 */         for (int dz = -this.radius; dz <= this.radius; dz++) {
/* 169 */           float currentRadius = Mth.sqrt((dx * dx + dz * dz));
/* 170 */           if (currentRadius <= this.radius) {
/*     */ 
/*     */ 
/*     */             
/* 174 */             int height = getHeightAtRadius(currentRadius);
/* 175 */             if (height > 0) {
/*     */ 
/*     */               
/* 178 */               if (random.nextFloat() < 0.2D)
/*     */               {
/* 180 */                 height = (int)(height * Mth.randomBetween(random, 0.8F, 1.0F));
/*     */               }
/*     */               
/* 183 */               BlockPos.MutableBlockPos pos = this.root.offset(dx, 0, dz).mutable();
/* 184 */               boolean hasBeenOutOfStone = false;
/*     */               
/* 186 */               int maxY = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) : Integer.MAX_VALUE;
/* 187 */               for (int i = 0; i < height && 
/* 188 */                 pos.getY() < maxY; i++) {
/*     */ 
/*     */                 
/* 191 */                 BlockPos windAdjustedPos = wind.offset(pos);
/* 192 */                 if (DripstoneUtils.isEmptyOrWaterOrLava(level, windAdjustedPos)) {
/* 193 */                   hasBeenOutOfStone = true;
/* 194 */                   Block block = SharedConstants.DEBUG_LARGE_DRIPSTONE ? Blocks.GLASS : Blocks.DRIPSTONE_BLOCK;
/* 195 */                   level.setBlock(windAdjustedPos, block.defaultBlockState(), 2);
/* 196 */                 } else if (hasBeenOutOfStone && level.getBlockState(windAdjustedPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
/*     */                   break;
/*     */                 } 
/* 199 */                 pos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 209 */     private boolean isSuitableForWind(LargeDripstoneConfiguration config) { return (this.radius >= config.minRadiusForWind && this.bluntness >= config.minBluntnessForWind); }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class WindOffsetter
/*     */   {
/*     */     private final int originY;
/*     */     
/*     */     private final Vec3 windSpeed;
/*     */ 
/*     */     
/*     */     private WindOffsetter(int originY, RandomSource random, FloatProvider windSpeedRange) {
/* 221 */       this.originY = originY;
/*     */       
/* 223 */       float speed = windSpeedRange.sample(random);
/*     */       
/* 225 */       float direction = Mth.randomBetween(random, 0.0F, 3.1415927F);
/* 226 */       this.windSpeed = new Vec3((Mth.cos(direction) * speed), 0.0D, (Mth.sin(direction) * speed));
/*     */     }
/*     */     
/*     */     private WindOffsetter() {
/* 230 */       this.originY = 0;
/* 231 */       this.windSpeed = null;
/*     */     }
/*     */ 
/*     */     
/* 235 */     private static WindOffsetter noWind() { return new WindOffsetter(); }
/*     */ 
/*     */     
/*     */     private BlockPos offset(BlockPos pos) {
/* 239 */       if (this.windSpeed == null) {
/* 240 */         return pos;
/*     */       }
/* 242 */       int dy = this.originY - pos.getY();
/* 243 */       Vec3 totalWindAdjust = this.windSpeed.scale(dy);
/* 244 */       return pos.offset(Mth.floor(totalWindAdjust.x), 0, Mth.floor(totalWindAdjust.z));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\LargeDripstoneFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */