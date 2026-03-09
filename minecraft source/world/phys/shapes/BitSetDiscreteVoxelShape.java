/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public final class BitSetDiscreteVoxelShape
/*     */   extends DiscreteVoxelShape {
/*     */   private final BitSet storage;
/*     */   private int xMin;
/*     */   private int yMin;
/*     */   private int zMin;
/*     */   private int xMax;
/*     */   private int yMax;
/*     */   private int zMax;
/*     */   
/*     */   public BitSetDiscreteVoxelShape(int xSize, int ySize, int zSize) {
/*  17 */     super(xSize, ySize, zSize);
/*  18 */     this.storage = new BitSet(xSize * ySize * zSize);
/*  19 */     this.xMin = xSize;
/*  20 */     this.yMin = ySize;
/*  21 */     this.zMin = zSize;
/*     */   }
/*     */   
/*     */   public static BitSetDiscreteVoxelShape withFilledBounds(int xSize, int ySize, int zSize, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
/*  25 */     BitSetDiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(xSize, ySize, zSize);
/*     */     
/*  27 */     shape.xMin = xMin;
/*  28 */     shape.yMin = yMin;
/*  29 */     shape.zMin = zMin;
/*  30 */     shape.xMax = xMax;
/*  31 */     shape.yMax = yMax;
/*  32 */     shape.zMax = zMax;
/*     */     
/*  34 */     for (int x = xMin; x < xMax; x++) {
/*  35 */       for (int y = yMin; y < yMax; y++) {
/*  36 */         for (int z = zMin; z < zMax; z++) {
/*  37 */           shape.fillUpdateBounds(x, y, z, false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  42 */     return shape;
/*     */   }
/*     */   
/*     */   public BitSetDiscreteVoxelShape(DiscreteVoxelShape voxelShape) {
/*  46 */     super(voxelShape.xSize, voxelShape.ySize, voxelShape.zSize);
/*  47 */     if (voxelShape instanceof BitSetDiscreteVoxelShape) {
/*  48 */       this.storage = (BitSet)((BitSetDiscreteVoxelShape)voxelShape).storage.clone();
/*     */     } else {
/*  50 */       this.storage = new BitSet(this.xSize * this.ySize * this.zSize);
/*  51 */       for (int x = 0; x < this.xSize; x++) {
/*  52 */         for (int y = 0; y < this.ySize; y++) {
/*  53 */           for (int z = 0; z < this.zSize; z++) {
/*  54 */             if (voxelShape.isFull(x, y, z)) {
/*  55 */               this.storage.set(getIndex(x, y, z));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  62 */     this.xMin = voxelShape.firstFull(Direction.Axis.X);
/*  63 */     this.yMin = voxelShape.firstFull(Direction.Axis.Y);
/*  64 */     this.zMin = voxelShape.firstFull(Direction.Axis.Z);
/*     */     
/*  66 */     this.xMax = voxelShape.lastFull(Direction.Axis.X);
/*  67 */     this.yMax = voxelShape.lastFull(Direction.Axis.Y);
/*  68 */     this.zMax = voxelShape.lastFull(Direction.Axis.Z);
/*     */   }
/*     */ 
/*     */   
/*  72 */   protected int getIndex(int x, int y, int z) { return (x * this.ySize + y) * this.zSize + z; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public boolean isFull(int x, int y, int z) { return this.storage.get(getIndex(x, y, z)); }
/*     */ 
/*     */   
/*     */   private void fillUpdateBounds(int x, int y, int z, boolean updateBounds) {
/*  81 */     this.storage.set(getIndex(x, y, z));
/*     */     
/*  83 */     if (updateBounds) {
/*  84 */       this.xMin = Math.min(this.xMin, x);
/*  85 */       this.yMin = Math.min(this.yMin, y);
/*  86 */       this.zMin = Math.min(this.zMin, z);
/*     */       
/*  88 */       this.xMax = Math.max(this.xMax, x + 1);
/*  89 */       this.yMax = Math.max(this.yMax, y + 1);
/*  90 */       this.zMax = Math.max(this.zMax, z + 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public void fill(int x, int y, int z) { fillUpdateBounds(x, y, z, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public boolean isEmpty() { return this.storage.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public int firstFull(Direction.Axis axis) { return axis.choose(this.xMin, this.yMin, this.zMin); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public int lastFull(Direction.Axis axis) { return axis.choose(this.xMax, this.yMax, this.zMax); }
/*     */ 
/*     */   
/*     */   static BitSetDiscreteVoxelShape join(DiscreteVoxelShape first, DiscreteVoxelShape second, IndexMerger xMerger, IndexMerger yMerger, IndexMerger zMerger, BooleanOp op) {
/* 115 */     BitSetDiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(xMerger.size() - 1, yMerger.size() - 1, zMerger.size() - 1);
/* 116 */     int[] bounds = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     xMerger.forMergedIndexes((x1, x2, xr) -> {
/* 126 */           boolean[] updatedSlice = { false };
/* 127 */           yMerger.forMergedIndexes(());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 145 */           if (updatedSlice[0]) {
/* 146 */             bounds[0] = Math.min(bounds[0], xr);
/* 147 */             bounds[3] = Math.max(bounds[3], xr);
/*     */           } 
/* 149 */           return true;
/*     */         });
/* 151 */     shape.xMin = bounds[0];
/* 152 */     shape.yMin = bounds[1];
/* 153 */     shape.zMin = bounds[2];
/* 154 */     shape.xMax = bounds[3] + 1;
/* 155 */     shape.yMax = bounds[4] + 1;
/* 156 */     shape.zMax = bounds[5] + 1;
/* 157 */     return shape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void forAllBoxes(DiscreteVoxelShape voxelShape, DiscreteVoxelShape.IntLineConsumer consumer, boolean mergeNeighbors) {
/* 165 */     BitSetDiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(voxelShape);
/* 166 */     for (int y = 0; y < shape.ySize; y++) {
/* 167 */       for (int x = 0; x < shape.xSize; x++) {
/* 168 */         int lastStartZ = -1;
/* 169 */         for (int z = 0; z <= shape.zSize; z++) {
/* 170 */           if (shape.isFullWide(x, y, z)) {
/* 171 */             if (mergeNeighbors) {
/*     */               
/* 173 */               if (lastStartZ == -1) {
/* 174 */                 lastStartZ = z;
/*     */               }
/*     */             } else {
/* 177 */               consumer.consume(x, y, z, x + 1, y + 1, z + 1);
/*     */             } 
/* 179 */           } else if (lastStartZ != -1) {
/*     */ 
/*     */             
/* 182 */             int endX = x;
/* 183 */             int endY = y;
/*     */ 
/*     */             
/* 186 */             shape.clearZStrip(lastStartZ, z, x, y);
/*     */ 
/*     */             
/* 189 */             while (shape.isZStripFull(lastStartZ, z, endX + 1, y)) {
/* 190 */               shape.clearZStrip(lastStartZ, z, endX + 1, y);
/* 191 */               endX++;
/*     */             } 
/*     */ 
/*     */             
/* 195 */             while (shape.isXZRectangleFull(x, endX + 1, lastStartZ, z, endY + 1)) {
/* 196 */               for (int cx = x; cx <= endX; cx++) {
/* 197 */                 shape.clearZStrip(lastStartZ, z, cx, endY + 1);
/*     */               }
/* 199 */               endY++;
/*     */             } 
/*     */             
/* 202 */             consumer.consume(x, y, lastStartZ, endX + 1, endY + 1, z);
/* 203 */             lastStartZ = -1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isZStripFull(int startZ, int endZ, int x, int y) {
/* 212 */     if (x >= this.xSize || y >= this.ySize) {
/* 213 */       return false;
/*     */     }
/* 215 */     return (this.storage.nextClearBit(getIndex(x, y, startZ)) >= getIndex(x, y, endZ));
/*     */   }
/*     */   
/*     */   private boolean isXZRectangleFull(int startX, int endX, int startZ, int endZ, int y) {
/* 219 */     for (int x = startX; x < endX; x++) {
/* 220 */       if (!isZStripFull(startZ, endZ, x, y)) {
/* 221 */         return false;
/*     */       }
/*     */     } 
/* 224 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 229 */   private void clearZStrip(int startZ, int endZ, int x, int y) { this.storage.clear(getIndex(x, y, startZ), getIndex(x, y, endZ)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterior(int x, int y, int z) {
/* 234 */     boolean isInterior = (x > 0 && x < this.xSize - 1 && y > 0 && y < this.ySize - 1 && z > 0 && z < this.zSize - 1);
/*     */     
/* 236 */     return (isInterior && isFull(x, y, z) && 
/* 237 */       isFull(x - 1, y, z) && 
/* 238 */       isFull(x + 1, y, z) && 
/* 239 */       isFull(x, y - 1, z) && 
/* 240 */       isFull(x, y + 1, z) && 
/* 241 */       isFull(x, y, z - 1) && 
/* 242 */       isFull(x, y, z + 1));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\BitSetDiscreteVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */