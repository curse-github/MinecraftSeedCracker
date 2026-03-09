/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class NetherBridgePiece
/*     */   extends StructurePiece
/*     */ {
/* 119 */   protected NetherBridgePiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) { super(type, genDepth, boundingBox); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public NetherBridgePiece(StructurePieceType type, CompoundTag tag) { super(type, tag); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}
/*     */ 
/*     */   
/*     */   private int updatePieceWeight(List<NetherFortressPieces.PieceWeight> currentPieces) {
/* 131 */     boolean hasAnyPieces = false;
/* 132 */     int totalWeight = 0;
/* 133 */     for (NetherFortressPieces.PieceWeight piece : currentPieces) {
/* 134 */       if (piece.maxPlaceCount > 0 && piece.placeCount < piece.maxPlaceCount) {
/* 135 */         hasAnyPieces = true;
/*     */       }
/* 137 */       totalWeight += piece.weight;
/*     */     } 
/* 139 */     return hasAnyPieces ? totalWeight : -1;
/*     */   }
/*     */   
/*     */   private NetherBridgePiece generatePiece(NetherFortressPieces.StartPiece startPiece, List<NetherFortressPieces.PieceWeight> currentPieces, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/* 143 */     int totalWeight = updatePieceWeight(currentPieces);
/* 144 */     boolean doStuff = (totalWeight > 0 && depth <= 30);
/*     */     
/* 146 */     int numAttempts = 0;
/* 147 */     while (numAttempts < 5 && doStuff) {
/* 148 */       numAttempts++;
/*     */       
/* 150 */       int weightSelection = random.nextInt(totalWeight);
/* 151 */       for (NetherFortressPieces.PieceWeight piece : currentPieces) {
/* 152 */         weightSelection -= piece.weight;
/* 153 */         if (weightSelection < 0) {
/* 154 */           if (!piece.doPlace(depth) || (piece == startPiece.previousPiece && !piece.allowInRow)) {
/*     */             break;
/*     */           }
/*     */           
/* 158 */           NetherBridgePiece structurePiece = NetherFortressPieces.findAndCreateBridgePieceFactory(piece, structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/* 159 */           if (structurePiece != null) {
/* 160 */             piece.placeCount++;
/* 161 */             startPiece.previousPiece = piece;
/*     */             
/* 163 */             if (!piece.isValid()) {
/* 164 */               currentPieces.remove(piece);
/*     */             }
/* 166 */             return structurePiece;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 171 */     return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*     */   }
/*     */   
/*     */   private StructurePiece generateAndAddPiece(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth, boolean isCastle) {
/* 175 */     if (Math.abs(footX - startPiece.getBoundingBox().minX()) > 112 || Math.abs(footZ - startPiece.getBoundingBox().minZ()) > 112) {
/* 176 */       return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*     */     }
/* 178 */     List<NetherFortressPieces.PieceWeight> availablePieces = startPiece.availableBridgePieces;
/* 179 */     if (isCastle) {
/* 180 */       availablePieces = startPiece.availableCastlePieces;
/*     */     }
/* 182 */     StructurePiece newPiece = generatePiece(startPiece, availablePieces, structurePieceAccessor, random, footX, footY, footZ, direction, depth + 1);
/* 183 */     if (newPiece != null) {
/* 184 */       structurePieceAccessor.addPiece(newPiece);
/* 185 */       startPiece.pendingChildren.add(newPiece);
/*     */     } 
/* 187 */     return newPiece;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildForward(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int xOff, int yOff, boolean isCastle) {
/* 191 */     Direction orientation = getOrientation();
/* 192 */     if (orientation != null) {
/* 193 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 195 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, orientation, getGenDepth(), isCastle);
/*     */         case 2:
/* 197 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, orientation, getGenDepth(), isCastle);
/*     */         case 3:
/* 199 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth(), isCastle);
/*     */         case 4:
/* 201 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth(), isCastle);
/*     */       } 
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildLeft(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff, boolean isCastle) {
/* 208 */     Direction orientation = getOrientation();
/* 209 */     if (orientation != null) {
/* 210 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 212 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth(), isCastle);
/*     */         case 2:
/* 214 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth(), isCastle);
/*     */         case 3:
/* 216 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), isCastle);
/*     */         case 4:
/* 218 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), isCastle);
/*     */       } 
/*     */     }
/* 221 */     return null;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildRight(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff, boolean isCastle) {
/* 225 */     Direction orientation = getOrientation();
/* 226 */     if (orientation != null) {
/* 227 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 229 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth(), isCastle);
/*     */         case 2:
/* 231 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth(), isCastle);
/*     */         case 3:
/* 233 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), isCastle);
/*     */         case 4:
/* 235 */           return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), isCastle);
/*     */       } 
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 242 */   protected static boolean isOkBox(BoundingBox box) { return (box.minY() > 10); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$NetherBridgePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */