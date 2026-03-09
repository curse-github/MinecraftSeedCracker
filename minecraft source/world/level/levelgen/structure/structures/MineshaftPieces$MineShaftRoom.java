/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MineShaftRoom
/*     */   extends MineshaftPieces.MineShaftPiece
/*     */ {
/* 193 */   private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();
/*     */   
/*     */   public MineShaftRoom(int genDepth, RandomSource random, int west, int north, MineshaftStructure.Type type) {
/* 196 */     super(StructurePieceType.MINE_SHAFT_ROOM, genDepth, type, new BoundingBox(west, 50, north, west + 7 + random.nextInt(6), 54 + random.nextInt(6), north + 7 + random.nextInt(6)));
/* 197 */     this.type = type;
/*     */   }
/*     */   
/*     */   public MineShaftRoom(CompoundTag tag) {
/* 201 */     super(StructurePieceType.MINE_SHAFT_ROOM, tag);
/* 202 */     this.childEntranceBoxes.addAll((Collection)tag.read("Entrances", BoundingBox.CODEC.listOf()).orElse(List.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 207 */     int depth = getGenDepth();
/*     */ 
/*     */ 
/*     */     
/* 211 */     int heightSpace = this.boundingBox.getYSpan() - 3 - 1;
/* 212 */     if (heightSpace <= 0) {
/* 213 */       heightSpace = 1;
/*     */     }
/*     */ 
/*     */     
/* 217 */     int pos = 0;
/* 218 */     while (pos < this.boundingBox.getXSpan()) {
/* 219 */       pos += random.nextInt(this.boundingBox.getXSpan());
/* 220 */       if (pos + 3 > this.boundingBox.getXSpan()) {
/*     */         break;
/*     */       }
/* 223 */       MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + pos, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() - 1, Direction.NORTH, depth);
/* 224 */       if (child != null) {
/* 225 */         BoundingBox childBox = child.getBoundingBox();
/* 226 */         this.childEntranceBoxes.add(new BoundingBox(childBox.minX(), childBox.minY(), this.boundingBox.minZ(), childBox.maxX(), childBox.maxY(), this.boundingBox.minZ() + 1));
/*     */       } 
/* 228 */       pos += 4;
/*     */     } 
/*     */     
/* 231 */     pos = 0;
/* 232 */     while (pos < this.boundingBox.getXSpan()) {
/* 233 */       pos += random.nextInt(this.boundingBox.getXSpan());
/* 234 */       if (pos + 3 > this.boundingBox.getXSpan()) {
/*     */         break;
/*     */       }
/* 237 */       MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + pos, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, depth);
/* 238 */       if (child != null) {
/* 239 */         BoundingBox childBox = child.getBoundingBox();
/* 240 */         this.childEntranceBoxes.add(new BoundingBox(childBox.minX(), childBox.minY(), this.boundingBox.maxZ() - 1, childBox.maxX(), childBox.maxY(), this.boundingBox.maxZ()));
/*     */       } 
/* 242 */       pos += 4;
/*     */     } 
/*     */     
/* 245 */     pos = 0;
/* 246 */     while (pos < this.boundingBox.getZSpan()) {
/* 247 */       pos += random.nextInt(this.boundingBox.getZSpan());
/* 248 */       if (pos + 3 > this.boundingBox.getZSpan()) {
/*     */         break;
/*     */       }
/* 251 */       MineshaftPieces.MineShaftPiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() + pos, Direction.WEST, depth);
/* 252 */       if (child != null) {
/* 253 */         BoundingBox childBox = child.getBoundingBox();
/* 254 */         this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.minX(), childBox.minY(), childBox.minZ(), this.boundingBox.minX() + 1, childBox.maxY(), childBox.maxZ()));
/*     */       } 
/* 256 */       pos += 4;
/*     */     } 
/*     */     
/* 259 */     pos = 0;
/* 260 */     while (pos < this.boundingBox.getZSpan()) {
/* 261 */       pos += random.nextInt(this.boundingBox.getZSpan());
/* 262 */       if (pos + 3 > this.boundingBox.getZSpan()) {
/*     */         break;
/*     */       }
/* 265 */       StructurePiece child = MineshaftPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + random.nextInt(heightSpace) + 1, this.boundingBox.minZ() + pos, Direction.EAST, depth);
/* 266 */       if (child != null) {
/* 267 */         BoundingBox childBox = child.getBoundingBox();
/* 268 */         this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.maxX() - 1, childBox.minY(), childBox.minZ(), this.boundingBox.maxX(), childBox.maxY(), childBox.maxZ()));
/*     */       } 
/* 270 */       pos += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 276 */     if (isInInvalidLocation(level, chunkBB)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 281 */     generateBox(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 282 */     for (BoundingBox entranceBox : this.childEntranceBoxes) {
/* 283 */       generateBox(level, chunkBB, entranceBox.minX(), entranceBox.maxY() - 2, entranceBox.minZ(), entranceBox.maxX(), entranceBox.maxY(), entranceBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/*     */     }
/* 285 */     generateUpperHalfSphere(level, chunkBB, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(int dx, int dy, int dz) {
/* 290 */     super.move(dx, dy, dz);
/* 291 */     for (BoundingBox bb : this.childEntranceBoxes) {
/* 292 */       bb.move(dx, dy, dz);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 298 */     super.addAdditionalSaveData(context, tag);
/* 299 */     tag.store("Entrances", BoundingBox.CODEC.listOf(), this.childEntranceBoxes);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces$MineShaftRoom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */