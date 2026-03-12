/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ 
/*    */ public class SurfaceWaterDepthFilter
/*    */   extends PlacementFilter
/*    */ {
/* 16 */   public static final MapCodec<SurfaceWaterDepthFilter> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 17 */         .fieldOf("max_water_depth").forGetter(()))
/* 18 */       .apply(i, SurfaceWaterDepthFilter::new));
/*    */   
/*    */   private final int maxWaterDepth;
/*    */ 
/*    */   
/* 23 */   private SurfaceWaterDepthFilter(int maxWaterDepth) { this.maxWaterDepth = maxWaterDepth; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static SurfaceWaterDepthFilter forMaxDepth(int maxWaterDepth) { return new SurfaceWaterDepthFilter(maxWaterDepth); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 32 */     int yOceanFloor = context.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX(), origin.getZ());
/* 33 */     int ySurfaceFloor = context.getHeight(Heightmap.Types.WORLD_SURFACE, origin.getX(), origin.getZ());
/*    */     
/* 35 */     return (ySurfaceFloor - yOceanFloor <= this.maxWaterDepth);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public PlacementModifierType<?> type() { return PlacementModifierType.SURFACE_WATER_DEPTH_FILTER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\SurfaceWaterDepthFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */