/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements EndCityPieces.SectionGenerator
/*     */ {
/*     */   public void init() {}
/*     */   
/*     */   public boolean generate(StructureTemplateManager structureTemplateManager, int genDepth, EndCityPieces.EndCityPiece parent, BlockPos offset, List<StructurePiece> pieces, RandomSource random) {
/* 198 */     Rotation rotation = parent.placeSettings().getRotation();
/* 199 */     EndCityPieces.EndCityPiece lastPiece = parent;
/* 200 */     lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(3 + random.nextInt(2), -3, 3 + random.nextInt(2)), "tower_base", rotation, true));
/* 201 */     lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 7, 0), "tower_piece", rotation, true));
/*     */     
/* 203 */     EndCityPieces.EndCityPiece bridgePiece = (random.nextInt(3) == 0) ? lastPiece : null;
/*     */     
/* 205 */     int towerHeight = 1 + random.nextInt(3);
/* 206 */     for (int i = 0; i < towerHeight; i++) {
/* 207 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, 4, 0), "tower_piece", rotation, true));
/* 208 */       if (i < towerHeight - 1 && random.nextBoolean()) {
/* 209 */         bridgePiece = lastPiece;
/*     */       }
/*     */     } 
/*     */     
/* 213 */     if (bridgePiece != null) {
/* 214 */       for (Tuple<Rotation, BlockPos> bridge : EndCityPieces.TOWER_BRIDGES) {
/* 215 */         if (random.nextBoolean()) {
/*     */           
/* 217 */           EndCityPieces.EndCityPiece bridgeStart = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, bridgePiece, (BlockPos)bridge.getB(), "bridge_end", rotation.getRotated((Rotation)bridge.getA()), true));
/* 218 */           EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_BRIDGE_GENERATOR, genDepth + 1, bridgeStart, null, pieces, random);
/*     */         } 
/*     */       } 
/*     */       
/* 222 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
/*     */     }
/* 224 */     else if (genDepth == 7) {
/* 225 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "tower_top", rotation, true));
/*     */     } else {
/* 227 */       return EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.FAT_TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
/*     */     } 
/*     */     
/* 230 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */