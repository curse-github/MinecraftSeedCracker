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
/*     */ class null
/*     */   implements EndCityPieces.SectionGenerator
/*     */ {
/*     */   public void init() {}
/*     */   
/*     */   public boolean generate(StructureTemplateManager structureTemplateManager, int genDepth, EndCityPieces.EndCityPiece parent, BlockPos offset, List<StructurePiece> pieces, RandomSource random) {
/* 158 */     if (genDepth > 8) {
/* 159 */       return false;
/*     */     }
/*     */     
/* 162 */     Rotation rotation = parent.placeSettings().getRotation();
/* 163 */     EndCityPieces.EndCityPiece lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, parent, offset, "base_floor", rotation, true));
/*     */     
/* 165 */     int numFloors = random.nextInt(3);
/* 166 */     if (numFloors == 0) {
/* 167 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "base_roof", rotation, true));
/* 168 */     } else if (numFloors == 1) {
/* 169 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
/* 170 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 8, -1), "second_roof", rotation, false));
/*     */       
/* 172 */       EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
/* 173 */     } else if (numFloors == 2) {
/* 174 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 0, -1), "second_floor_2", rotation, false));
/* 175 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 4, -1), "third_floor_2", rotation, false));
/* 176 */       lastPiece = EndCityPieces.addHelper(pieces, EndCityPieces.addPiece(structureTemplateManager, lastPiece, new BlockPos(-1, 8, -1), "third_roof", rotation, true));
/*     */       
/* 178 */       EndCityPieces.recursiveChildren(structureTemplateManager, EndCityPieces.TOWER_GENERATOR, genDepth + 1, lastPiece, null, pieces, random);
/*     */     } 
/* 180 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */