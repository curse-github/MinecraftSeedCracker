/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class IglooStructure extends Structure {
/* 16 */   public static final MapCodec<IglooStructure> CODEC = simpleCodec(IglooStructure::new);
/*    */ 
/*    */   
/* 19 */   public IglooStructure(Structure.StructureSettings settings) { super(settings); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) { return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context)); }
/*    */ 
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 28 */     ChunkPos chunkPos = context.chunkPos();
/* 29 */     WorldgenRandom random = context.random();
/*    */     
/* 31 */     BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
/* 32 */     Rotation rotation = Rotation.getRandom(random);
/* 33 */     IglooPieces.addPieces(context.structureTemplateManager(), startPos, rotation, builder, random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public StructureType<?> type() { return StructureType.IGLOO; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\IglooStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */