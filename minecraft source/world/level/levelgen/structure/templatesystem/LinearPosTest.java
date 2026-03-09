/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class LinearPosTest extends PosRuleTest {
/* 11 */   public static final MapCodec<LinearPosTest> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 12 */         .fieldOf("min_chance").orElse(Float.valueOf(0.0F)).forGetter(()), Codec.FLOAT
/* 13 */         .fieldOf("max_chance").orElse(Float.valueOf(0.0F)).forGetter(()), Codec.INT
/* 14 */         .fieldOf("min_dist").orElse(Integer.valueOf(0)).forGetter(()), Codec.INT
/* 15 */         .fieldOf("max_dist").orElse(Integer.valueOf(0)).forGetter(()))
/* 16 */       .apply(i, LinearPosTest::new));
/*    */   
/*    */   private final float minChance;
/*    */   private final float maxChance;
/*    */   private final int minDist;
/*    */   private final int maxDist;
/*    */   
/*    */   public LinearPosTest(float minChance, float maxChance, int minDist, int maxDist) {
/* 24 */     if (minDist >= maxDist) {
/* 25 */       throw new IllegalArgumentException("Invalid range: [" + minDist + "," + maxDist + "]");
/*    */     }
/*    */     
/* 28 */     this.minChance = minChance;
/* 29 */     this.maxChance = maxChance;
/* 30 */     this.minDist = minDist;
/* 31 */     this.maxDist = maxDist;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos inTemplatePos, BlockPos worldPos, BlockPos worldReference, RandomSource random) {
/* 36 */     int dist = worldPos.distManhattan(worldReference);
/*    */     
/* 38 */     float rnd = random.nextFloat();
/* 39 */     return (rnd <= Mth.clampedLerp(Mth.inverseLerp(dist, this.minDist, this.maxDist), this.minChance, this.maxChance));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected PosRuleTestType<?> getType() { return PosRuleTestType.LINEAR_POS_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LinearPosTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */