/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class SurfaceRelativeThresholdFilter
/*    */   extends PlacementFilter
/*    */ {
/* 15 */   public static final MapCodec<SurfaceRelativeThresholdFilter> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Heightmap.Types.CODEC
/* 16 */         .fieldOf("heightmap").forGetter(()), Codec.INT
/* 17 */         .optionalFieldOf("min_inclusive", Integer.valueOf(-2147483648)).forGetter(()), Codec.INT
/* 18 */         .optionalFieldOf("max_inclusive", Integer.valueOf(2147483647)).forGetter(()))
/* 19 */       .apply(i, SurfaceRelativeThresholdFilter::new));
/*    */   
/*    */   private final Heightmap.Types heightmap;
/*    */   private final int minInclusive;
/*    */   private final int maxInclusive;
/*    */   
/*    */   private SurfaceRelativeThresholdFilter(Heightmap.Types heightmap, int minInclusive, int maxInclusive) {
/* 26 */     this.heightmap = heightmap;
/* 27 */     this.minInclusive = minInclusive;
/* 28 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */ 
/*    */   
/* 32 */   public static SurfaceRelativeThresholdFilter of(Heightmap.Types heightmap, int minInclusive, int maxInclusive) { return new SurfaceRelativeThresholdFilter(heightmap, minInclusive, maxInclusive); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 37 */     long surfaceY = context.getHeight(this.heightmap, origin.getX(), origin.getZ());
/*    */     
/* 39 */     long minY = surfaceY + this.minInclusive;
/* 40 */     long maxY = surfaceY + this.maxInclusive;
/*    */     
/* 42 */     return (minY <= origin.getY() && origin.getY() <= maxY);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public PlacementModifierType<?> type() { return PlacementModifierType.SURFACE_RELATIVE_THRESHOLD_FILTER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\SurfaceRelativeThresholdFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */