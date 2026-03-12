/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ 
/*     */ public class IceSpikeFeature
/*     */   extends Feature<NoneFeatureConfiguration> {
/*  14 */   public IceSpikeFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/*  19 */     BlockPos origin = context.origin();
/*  20 */     RandomSource random = context.random();
/*  21 */     WorldGenLevel level = context.level();
/*  22 */     while (level.isEmptyBlock(origin) && origin.getY() > level.getMinY() + 2) {
/*  23 */       origin = origin.below();
/*     */     }
/*     */     
/*  26 */     if (!level.getBlockState(origin).is(Blocks.SNOW_BLOCK)) {
/*  27 */       return false;
/*     */     }
/*  29 */     origin = origin.above(random.nextInt(4));
/*     */     
/*  31 */     int height = random.nextInt(4) + 7;
/*  32 */     int width = height / 4 + random.nextInt(2);
/*     */     
/*  34 */     if (width > 1 && random.nextInt(60) == 0) {
/*  35 */       origin = origin.above(10 + random.nextInt(30));
/*     */     }
/*     */     
/*  38 */     for (int yOff = 0; yOff < height; yOff++) {
/*  39 */       float scale = (1.0F - yOff / height) * width;
/*  40 */       int newWidth = Mth.ceil(scale);
/*     */       
/*  42 */       for (int xo = -newWidth; xo <= newWidth; xo++) {
/*  43 */         float dx = Mth.abs(xo) - 0.25F;
/*  44 */         for (int zo = -newWidth; zo <= newWidth; zo++) {
/*  45 */           float dz = Mth.abs(zo) - 0.25F;
/*  46 */           if ((xo == 0 && zo == 0) || dx * dx + dz * dz <= scale * scale)
/*     */           {
/*     */             
/*  49 */             if ((xo != -newWidth && xo != newWidth && zo != -newWidth && zo != newWidth) || 
/*  50 */               random.nextFloat() <= 0.75F) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  55 */               BlockState state = level.getBlockState(origin.offset(xo, yOff, zo));
/*     */               
/*  57 */               if (state.isAir() || isDirt(state) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.ICE)) {
/*  58 */                 setBlock(level, origin.offset(xo, yOff, zo), Blocks.PACKED_ICE.defaultBlockState());
/*     */               }
/*     */               
/*  61 */               if (yOff != 0 && newWidth > 1) {
/*  62 */                 state = level.getBlockState(origin.offset(xo, -yOff, zo));
/*     */                 
/*  64 */                 if (state.isAir() || isDirt(state) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.ICE))
/*  65 */                   setBlock(level, origin.offset(xo, -yOff, zo), Blocks.PACKED_ICE.defaultBlockState()); 
/*     */               } 
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  71 */     int pillarWidth = width - 1;
/*  72 */     if (pillarWidth < 0) {
/*  73 */       pillarWidth = 0;
/*  74 */     } else if (pillarWidth > 1) {
/*  75 */       pillarWidth = 1;
/*     */     } 
/*  77 */     for (int xo = -pillarWidth; xo <= pillarWidth; xo++) {
/*  78 */       for (int zo = -pillarWidth; zo <= pillarWidth; zo++) {
/*  79 */         BlockPos iceBlock = origin.offset(xo, -1, zo);
/*  80 */         int runLength = 50;
/*  81 */         if (Math.abs(xo) == 1 && Math.abs(zo) == 1) {
/*  82 */           runLength = random.nextInt(5);
/*     */         }
/*  84 */         while (iceBlock.getY() > 50) {
/*  85 */           BlockState state = level.getBlockState(iceBlock);
/*     */           
/*  87 */           if (state.isAir() || isDirt(state) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.ICE) || state.is(Blocks.PACKED_ICE)) {
/*  88 */             setBlock(level, iceBlock, Blocks.PACKED_ICE.defaultBlockState());
/*     */ 
/*     */ 
/*     */             
/*  92 */             iceBlock = iceBlock.below();
/*  93 */             runLength--;
/*  94 */             if (runLength <= 0) {
/*  95 */               iceBlock = iceBlock.below(random.nextInt(5) + 1);
/*  96 */               runLength = random.nextInt(5);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 102 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\IceSpikeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */