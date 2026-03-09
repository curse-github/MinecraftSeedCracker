/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
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
/*     */ public final class OreVeinifier
/*     */ {
/*     */   private static final float VEININESS_THRESHOLD = 0.4F;
/*     */   private static final int EDGE_ROUNDOFF_BEGIN = 20;
/*     */   private static final double MAX_EDGE_ROUNDOFF = 0.2D;
/*     */   private static final float VEIN_SOLIDNESS = 0.7F;
/*     */   private static final float MIN_RICHNESS = 0.1F;
/*     */   private static final float MAX_RICHNESS = 0.3F;
/*     */   private static final float MAX_RICHNESS_THRESHOLD = 0.6F;
/*     */   private static final float CHANCE_OF_RAW_ORE_BLOCK = 0.02F;
/*     */   private static final float SKIP_ORE_IF_GAP_NOISE_IS_BELOW = -0.3F;
/*     */   
/*     */   protected static NoiseChunk.BlockStateFiller create(DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, PositionalRandomFactory oreVeinsPositionalRandomFactory) {
/*  42 */     BlockState defaultState = SharedConstants.DEBUG_ORE_VEINS ? Blocks.AIR.defaultBlockState() : null;
/*     */     
/*  44 */     return context -> {
/*  45 */         double oreVeininessNoiseValue = veinToggle.compute(context);
/*     */         
/*  47 */         int posY = context.blockY();
/*     */         
/*  49 */         VeinType veinType = (oreVeininessNoiseValue > 0.0D) ? VeinType.COPPER : VeinType.IRON;
/*  50 */         double veininessRidged = Math.abs(oreVeininessNoiseValue);
/*     */         
/*  52 */         int distanceFromTop = veinType.maxY - posY;
/*  53 */         int distanceFromBottom = posY - veinType.minY;
/*  54 */         if (distanceFromBottom < 0 || distanceFromTop < 0) {
/*  55 */           return defaultState;
/*     */         }
/*  57 */         int distanceFromEdge = Math.min(distanceFromTop, distanceFromBottom);
/*  58 */         double edgeRoundoff = Mth.clampedMap(distanceFromEdge, 0.0D, 20.0D, -0.2D, 0.0D);
/*  59 */         if (veininessRidged + edgeRoundoff < 0.4000000059604645D) {
/*  60 */           return defaultState;
/*     */         }
/*     */         
/*  63 */         RandomSource positionalRandom = oreVeinsPositionalRandomFactory.at(context.blockX(), posY, context.blockZ());
/*     */         
/*  65 */         if (positionalRandom.nextFloat() > 0.7F)
/*     */         {
/*  67 */           return defaultState;
/*     */         }
/*     */         
/*  70 */         if (veinRidged.compute(context) >= 0.0D) {
/*  71 */           return defaultState;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  76 */         double richness = Mth.clampedMap(veininessRidged, 0.4000000059604645D, 0.6000000238418579D, 0.10000000149011612D, 0.30000001192092896D);
/*  77 */         if (positionalRandom.nextFloat() < richness && veinGap.compute(context) > -0.30000001192092896D) {
/*  78 */           return (positionalRandom.nextFloat() < 0.02F) ? veinType.rawOreBlock : veinType.ore;
/*     */         }
/*  80 */         return SharedConstants.DEBUG_ORE_VEINS ? Blocks.OAK_BUTTON.defaultBlockState() : veinType.filler;
/*     */       };
/*     */   }
/*     */   
/*     */   protected enum VeinType {
/*  85 */     COPPER(Blocks.COPPER_ORE.defaultBlockState(), Blocks.RAW_COPPER_BLOCK.defaultBlockState(), Blocks.GRANITE.defaultBlockState(), 0, 50),
/*  86 */     IRON(Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), Blocks.RAW_IRON_BLOCK.defaultBlockState(), Blocks.TUFF.defaultBlockState(), -60, -8);
/*     */     
/*     */     private final BlockState ore;
/*     */     
/*     */     private final BlockState rawOreBlock;
/*     */     private final BlockState filler;
/*     */     protected final int minY;
/*     */     protected final int maxY;
/*     */     
/*     */     VeinType(BlockState ore, BlockState rawOreBlock, BlockState filler, int minY, int maxY) {
/*  96 */       this.ore = ore;
/*  97 */       this.rawOreBlock = rawOreBlock;
/*  98 */       this.filler = filler;
/*  99 */       this.minY = minY;
/* 100 */       this.maxY = maxY;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\OreVeinifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */