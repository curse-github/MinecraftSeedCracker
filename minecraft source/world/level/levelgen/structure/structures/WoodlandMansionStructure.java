/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class WoodlandMansionStructure extends Structure {
/* 24 */   public static final MapCodec<WoodlandMansionStructure> CODEC = simpleCodec(WoodlandMansionStructure::new);
/*    */ 
/*    */   
/* 27 */   public WoodlandMansionStructure(Structure.StructureSettings settings) { super(settings); }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 32 */     Rotation rotation = Rotation.getRandom(context.random());
/*    */ 
/*    */     
/* 35 */     BlockPos startPos = getLowestYIn5by5BoxOffset7Blocks(context, rotation);
/*    */ 
/*    */     
/* 38 */     if (startPos.getY() < 60) {
/* 39 */       return Optional.empty();
/*    */     }
/*    */     
/* 42 */     return Optional.of(new Structure.GenerationStub(startPos, builder -> generatePieces(builder, context, startPos, rotation)));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context, BlockPos startPos, Rotation rotation) {
/* 46 */     List<WoodlandMansionPieces.WoodlandMansionPiece> wmPieces = Lists.newLinkedList();
/* 47 */     WoodlandMansionPieces.generateMansion(context.structureTemplateManager(), startPos, rotation, wmPieces, context.random());
/* 48 */     Objects.requireNonNull(builder); wmPieces.forEach(builder::addPiece);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, PiecesContainer pieces) {
/* 54 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 55 */     int minY = level.getMinY();
/* 56 */     BoundingBox boundingBox = pieces.calculateBoundingBox();
/*    */     
/* 58 */     int yStart = boundingBox.minY();
/* 59 */     for (int x = chunkBB.minX(); x <= chunkBB.maxX(); x++) {
/* 60 */       for (int z = chunkBB.minZ(); z <= chunkBB.maxZ(); z++) {
/* 61 */         pos.set(x, yStart, z);
/*    */         
/* 63 */         if (!level.isEmptyBlock(pos) && boundingBox.isInside(pos) && pieces.isInsidePiece(pos)) {
/* 64 */           for (int y = yStart - 1; y > minY; ) {
/* 65 */             pos.setY(y);
/* 66 */             if (level.isEmptyBlock(pos) || level.getBlockState(pos).liquid()) {
/* 67 */               level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 2);
/*    */               y--;
/*    */             } 
/*    */           } 
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public StructureType<?> type() { return StructureType.WOODLAND_MANSION; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */