/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class AxisAlignedLinearPosTest extends PosRuleTest {
/* 12 */   public static final MapCodec<AxisAlignedLinearPosTest> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 13 */         .fieldOf("min_chance").orElse(Float.valueOf(0.0F)).forGetter(()), Codec.FLOAT
/* 14 */         .fieldOf("max_chance").orElse(Float.valueOf(0.0F)).forGetter(()), Codec.INT
/* 15 */         .fieldOf("min_dist").orElse(Integer.valueOf(0)).forGetter(()), Codec.INT
/* 16 */         .fieldOf("max_dist").orElse(Integer.valueOf(0)).forGetter(()), Direction.Axis.CODEC
/* 17 */         .fieldOf("axis").orElse(Direction.Axis.Y).forGetter(()))
/* 18 */       .apply(i, AxisAlignedLinearPosTest::new));
/*    */   
/*    */   private final float minChance;
/*    */   private final float maxChance;
/*    */   private final int minDist;
/*    */   private final int maxDist;
/*    */   private final Direction.Axis axis;
/*    */   
/*    */   public AxisAlignedLinearPosTest(float minChance, float maxChance, int minDist, int maxDist, Direction.Axis axis) {
/* 27 */     if (minDist >= maxDist) {
/* 28 */       throw new IllegalArgumentException("Invalid range: [" + minDist + "," + maxDist + "]");
/*    */     }
/* 30 */     this.minChance = minChance;
/* 31 */     this.maxChance = maxChance;
/* 32 */     this.minDist = minDist;
/* 33 */     this.maxDist = maxDist;
/* 34 */     this.axis = axis;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos inTemplatePos, BlockPos worldPos, BlockPos worldReference, RandomSource random) {
/* 39 */     Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, this.axis);
/* 40 */     float xd = Math.abs((worldPos.getX() - worldReference.getX()) * direction.getStepX());
/* 41 */     float yd = Math.abs((worldPos.getY() - worldReference.getY()) * direction.getStepY());
/* 42 */     float zd = Math.abs((worldPos.getZ() - worldReference.getZ()) * direction.getStepZ());
/* 43 */     int dist = (int)(xd + yd + zd);
/*    */     
/* 45 */     float rnd = random.nextFloat();
/* 46 */     return (rnd <= Mth.clampedLerp(Mth.inverseLerp(dist, this.minDist, this.maxDist), this.minChance, this.maxChance));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   protected PosRuleTestType<?> getType() { return PosRuleTestType.AXIS_ALIGNED_LINEAR_POS_TEST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\AxisAlignedLinearPosTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */