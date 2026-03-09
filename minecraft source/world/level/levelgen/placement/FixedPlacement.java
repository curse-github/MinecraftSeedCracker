/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FixedPlacement
/*    */   extends PlacementModifier
/*    */ {
/* 16 */   public static final MapCodec<FixedPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockPos.CODEC
/* 17 */         .listOf().fieldOf("positions").forGetter(()))
/* 18 */       .apply(i, FixedPlacement::new));
/*    */   
/*    */   private final List<BlockPos> positions;
/*    */ 
/*    */   
/* 23 */   public static FixedPlacement of(BlockPos... pos) { return new FixedPlacement(List.of(pos)); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   private FixedPlacement(List<BlockPos> positions) { this.positions = positions; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 32 */     int chunkX = SectionPos.blockToSectionCoord(origin.getX());
/* 33 */     int chunkZ = SectionPos.blockToSectionCoord(origin.getZ());
/* 34 */     boolean hasPositions = false;
/* 35 */     for (BlockPos position : this.positions) {
/* 36 */       if (isSameChunk(chunkX, chunkZ, position)) {
/* 37 */         hasPositions = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 41 */     if (!hasPositions) {
/* 42 */       return Stream.empty();
/*    */     }
/* 44 */     return this.positions.stream().filter(pos -> isSameChunk(chunkX, chunkZ, pos));
/*    */   }
/*    */ 
/*    */   
/* 48 */   private static boolean isSameChunk(int chunkX, int chunkZ, BlockPos position) { return (chunkX == SectionPos.blockToSectionCoord(position.getX()) && chunkZ == SectionPos.blockToSectionCoord(position.getZ())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public PlacementModifierType<?> type() { return PlacementModifierType.FIXED_PLACEMENT; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\FixedPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */