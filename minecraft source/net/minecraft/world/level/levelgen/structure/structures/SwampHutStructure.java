/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class SwampHutStructure extends Structure {
/* 12 */   public static final MapCodec<SwampHutStructure> CODEC = simpleCodec(SwampHutStructure::new);
/*    */ 
/*    */   
/* 15 */   public SwampHutStructure(Structure.StructureSettings settings) { super(settings); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) { return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context)); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) { builder.addPiece(new SwampHutPiece(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ())); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public StructureType<?> type() { return StructureType.SWAMP_HUT; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\SwampHutStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */