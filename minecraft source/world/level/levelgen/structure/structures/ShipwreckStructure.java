/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class ShipwreckStructure extends Structure {
/* 18 */   public static final MapCodec<ShipwreckStructure> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 19 */         settingsCodec(i), Codec.BOOL
/* 20 */         .fieldOf("is_beached").forGetter(()))
/* 21 */       .apply(i, ShipwreckStructure::new));
/*    */   
/*    */   public final boolean isBeached;
/*    */   
/*    */   public ShipwreckStructure(Structure.StructureSettings settings, boolean isBeached) {
/* 26 */     super(settings);
/* 27 */     this.isBeached = isBeached;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 32 */     Heightmap.Types type = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
/* 33 */     return onTopOfChunkCenter(context, type, builder -> generatePieces(builder, context));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 37 */     Rotation rotation = Rotation.getRandom(context.random());
/* 38 */     BlockPos offset = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
/* 39 */     ShipwreckPieces.ShipwreckPiece piece = ShipwreckPieces.addRandomPiece(context.structureTemplateManager(), offset, rotation, builder, context.random(), this.isBeached);
/* 40 */     if (piece.isTooBigToFitInWorldGenRegion()) {
/* 41 */       int height; BoundingBox bb = piece.getBoundingBox();
/*    */       
/* 43 */       if (this.isBeached) {
/* 44 */         int minY = Structure.getLowestY(context, bb.minX(), bb.getXSpan(), bb.minZ(), bb.getZSpan());
/* 45 */         height = piece.calculateBeachedPosition(minY, context.random());
/*    */       } else {
/* 47 */         height = Structure.getMeanFirstOccupiedHeight(context, bb.minX(), bb.getXSpan(), bb.minZ(), bb.getZSpan());
/*    */       } 
/* 49 */       piece.adjustPositionHeight(height);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public StructureType<?> type() { return StructureType.SHIPWRECK; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\ShipwreckStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */