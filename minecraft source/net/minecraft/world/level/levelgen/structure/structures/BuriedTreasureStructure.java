/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class BuriedTreasureStructure extends Structure {
/* 13 */   public static final MapCodec<BuriedTreasureStructure> CODEC = simpleCodec(BuriedTreasureStructure::new);
/*    */ 
/*    */   
/* 16 */   public BuriedTreasureStructure(Structure.StructureSettings settings) { super(settings); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) { return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, builder -> generatePieces(builder, context)); }
/*    */ 
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 25 */     BlockPos offset = new BlockPos(context.chunkPos().getBlockX(9), 90, context.chunkPos().getBlockZ(9));
/* 26 */     builder.addPiece(new BuriedTreasurePieces.BuriedTreasurePiece(offset));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public StructureType<?> type() { return StructureType.BURIED_TREASURE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\BuriedTreasureStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */