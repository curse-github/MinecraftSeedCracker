/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class HeightmapPlacement
/*    */   extends PlacementModifier
/*    */ {
/* 15 */   public static final MapCodec<HeightmapPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Heightmap.Types.CODEC
/* 16 */         .fieldOf("heightmap").forGetter(()))
/* 17 */       .apply(i, HeightmapPlacement::new));
/*    */   
/*    */   private final Heightmap.Types heightmap;
/*    */ 
/*    */   
/* 22 */   private HeightmapPlacement(Heightmap.Types heightmap) { this.heightmap = heightmap; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static HeightmapPlacement onHeightmap(Heightmap.Types heightmap) { return new HeightmapPlacement(heightmap); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 31 */     int x = origin.getX();
/* 32 */     int z = origin.getZ();
/* 33 */     int height = context.getHeight(this.heightmap, x, z);
/* 34 */     if (height > context.getMinY()) {
/* 35 */       return Stream.of(new BlockPos(x, height, z));
/*    */     }
/* 37 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public PlacementModifierType<?> type() { return PlacementModifierType.HEIGHTMAP; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\HeightmapPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */