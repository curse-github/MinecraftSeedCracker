/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class NetherFossilPieces
/*    */ {
/*    */   private static final Identifier[] FOSSILS = { 
/* 28 */       Identifier.withDefaultNamespace("nether_fossils/fossil_1"), 
/* 29 */       Identifier.withDefaultNamespace("nether_fossils/fossil_2"), 
/* 30 */       Identifier.withDefaultNamespace("nether_fossils/fossil_3"), 
/* 31 */       Identifier.withDefaultNamespace("nether_fossils/fossil_4"), 
/* 32 */       Identifier.withDefaultNamespace("nether_fossils/fossil_5"), 
/* 33 */       Identifier.withDefaultNamespace("nether_fossils/fossil_6"), 
/* 34 */       Identifier.withDefaultNamespace("nether_fossils/fossil_7"), 
/* 35 */       Identifier.withDefaultNamespace("nether_fossils/fossil_8"), 
/* 36 */       Identifier.withDefaultNamespace("nether_fossils/fossil_9"), 
/* 37 */       Identifier.withDefaultNamespace("nether_fossils/fossil_10"), 
/* 38 */       Identifier.withDefaultNamespace("nether_fossils/fossil_11"), 
/* 39 */       Identifier.withDefaultNamespace("nether_fossils/fossil_12"), 
/* 40 */       Identifier.withDefaultNamespace("nether_fossils/fossil_13"), 
/* 41 */       Identifier.withDefaultNamespace("nether_fossils/fossil_14") };
/*    */ 
/*    */   
/*    */   public static void addPieces(StructureTemplateManager structureTemplateManager, StructurePieceAccessor structurePieceAccessor, RandomSource random, BlockPos position) {
/* 45 */     Rotation nextRotation = Rotation.getRandom(random);
/* 46 */     structurePieceAccessor.addPiece(new NetherFossilPiece(structureTemplateManager, (Identifier)Util.getRandom(FOSSILS, random), position, nextRotation));
/*    */   }
/*    */   
/*    */   public static class NetherFossilPiece
/*    */     extends TemplateStructurePiece {
/* 51 */     public NetherFossilPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation) { super(StructurePieceType.NETHER_FOSSIL, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(rotation), position); }
/*    */ 
/*    */ 
/*    */     
/* 55 */     public NetherFossilPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) { super(StructurePieceType.NETHER_FOSSIL, tag, structureTemplateManager, location -> makeSettings((Rotation)tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow())); }
/*    */ 
/*    */ 
/*    */     
/* 59 */     private static StructurePlaceSettings makeSettings(Rotation rotation) { return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR); }
/*    */ 
/*    */ 
/*    */     
/*    */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 64 */       super.addAdditionalSaveData(context, tag);
/* 65 */       tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {}
/*    */ 
/*    */     
/*    */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 74 */       BoundingBox fossilBB = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/* 75 */       chunkBB.encapsulate(fossilBB);
/* 76 */       super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/* 77 */       placeDriedGhast(level, random, fossilBB, chunkBB);
/*    */     }
/*    */     
/*    */     private void placeDriedGhast(WorldGenLevel level, RandomSource random, BoundingBox fossilBB, BoundingBox chunkBB) {
/* 81 */       RandomSource positionalRandom = RandomSource.create(level.getSeed()).forkPositional().at(fossilBB.getCenter());
/* 82 */       if (positionalRandom.nextFloat() < 0.5F) {
/* 83 */         int x = fossilBB.minX() + positionalRandom.nextInt(fossilBB.getXSpan());
/* 84 */         int y = fossilBB.minY();
/* 85 */         int z = fossilBB.minZ() + positionalRandom.nextInt(fossilBB.getZSpan());
/* 86 */         BlockPos randomPos = new BlockPos(x, y, z);
/*    */         
/* 88 */         if (level.getBlockState(randomPos).isAir() && chunkBB.isInside(randomPos))
/* 89 */           level.setBlock(randomPos, Blocks.DRIED_GHAST.defaultBlockState().rotate(Rotation.getRandom(positionalRandom)), 2); 
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFossilPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */