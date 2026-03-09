/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class RandomOffsetPlacement
/*    */   extends PlacementModifier
/*    */ {
/* 16 */   public static final MapCodec<RandomOffsetPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 17 */         IntProvider.codec(-16, 16).fieldOf("xz_spread").forGetter(()), 
/* 18 */         IntProvider.codec(-16, 16).fieldOf("y_spread").forGetter(()))
/* 19 */       .apply(i, RandomOffsetPlacement::new));
/*    */   
/*    */   private final IntProvider xzSpread;
/*    */   
/*    */   private final IntProvider ySpread;
/*    */   
/* 25 */   public static RandomOffsetPlacement of(IntProvider xzSpread, IntProvider ySpread) { return new RandomOffsetPlacement(xzSpread, ySpread); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static RandomOffsetPlacement vertical(IntProvider ySpread) { return new RandomOffsetPlacement(ConstantInt.of(0), ySpread); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static RandomOffsetPlacement horizontal(IntProvider xzSpread) { return new RandomOffsetPlacement(xzSpread, ConstantInt.of(0)); }
/*    */ 
/*    */   
/*    */   private RandomOffsetPlacement(IntProvider xzSpread, IntProvider ySpread) {
/* 37 */     this.xzSpread = xzSpread;
/* 38 */     this.ySpread = ySpread;
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 43 */     int scatterX = origin.getX() + this.xzSpread.sample(random);
/* 44 */     int scatterY = origin.getY() + this.ySpread.sample(random);
/* 45 */     int scatterZ = origin.getZ() + this.xzSpread.sample(random);
/* 46 */     return Stream.of(new BlockPos(scatterX, scatterY, scatterZ));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public PlacementModifierType<?> type() { return PlacementModifierType.RANDOM_OFFSET; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\RandomOffsetPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */