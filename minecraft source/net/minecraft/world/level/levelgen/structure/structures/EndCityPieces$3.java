/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
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
/*     */   public boolean shipCreated;
/*     */   
/* 239 */   public void init() { this.shipCreated = false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean generate(StructureTemplateManager structureTemplateManager, int genDepth, EndCityPieces.EndCityPiece parent, BlockPos offset, List<StructurePiece> pieces, RandomSource random) {
/* 244 */     Rotation rotation = parent.placeSettings().getRotation();
/* 245 */     int bridgeLength = random.nextInt(4) + 1;
/*     */     
/* 247 */     EndCityPieces.EndCityPiece lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, parent, new BlockPos(0, 0, -4), "bridge_piece", rotation, true));
/* 248 */     lastPiece.setGenDepth(-1);
/* 249 */     int nextY = 0;
/* 250 */     for (int i = 0; i < bridgeLength; i++) {
/* 251 */       if (random.nextBoolean()) {
/* 252 */         lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -4), "bridge_piece", rotation, true));
/* 253 */         nextY = 0;
/*     */       } else {
/* 255 */         if (random.nextBoolean()) {
/* 256 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -4), "bridge_steep_stairs", rotation, true));
/*     */         } else {
/* 258 */           lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(0, nextY, -8), "bridge_gentle_stairs", rotation, true));
/*     */         } 
/* 260 */         nextY = 4;
/*     */       } 
/*     */     } 
/*     */     
/* 264 */     if (this.shipCreated || random.nextInt(10 - genDepth) != 0) {
/* 265 */       if (!EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.HOUSE_TOWER_GENERATOR, genDepth + 1, lastPiece, new BlockPos(-3, nextY + 1, -11), pieces, random)) {
/* 266 */         return false;
/*     */       }
/*     */     } else {
/*     */       
/* 270 */       EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-8 + random.nextInt(8), nextY, -70 + random.nextInt(10)), "ship", rotation, true));
/* 271 */       this.shipCreated = true;
/*     */     } 
/*     */ 
/*     */     
/* 275 */     lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(4, nextY, 0), "bridge_end", rotation.getRotated(Rotation.CLOCKWISE_180), true));
/* 276 */     lastPiece.setGenDepth(-1);
/*     */     
/* 278 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */