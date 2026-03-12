/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.Direction;
/*     */ import org.joml.Vector3i;
/*     */ 
/*     */ public abstract class DiscreteVoxelShape {
/*   9 */   private static final Direction.Axis[] AXIS_VALUES = Direction.Axis.values();
/*     */   
/*     */   protected final int xSize;
/*     */   protected final int ySize;
/*     */   protected final int zSize;
/*     */   
/*     */   protected DiscreteVoxelShape(int xSize, int ySize, int zSize) {
/*  16 */     if (xSize < 0 || ySize < 0 || zSize < 0) {
/*  17 */       throw new IllegalArgumentException("Need all positive sizes: x: " + xSize + ", y: " + ySize + ", z: " + zSize);
/*     */     }
/*  19 */     this.xSize = xSize;
/*  20 */     this.ySize = ySize;
/*  21 */     this.zSize = zSize;
/*     */   }
/*     */   
/*     */   public DiscreteVoxelShape rotate(OctahedralGroup rotation) {
/*  25 */     if (rotation == OctahedralGroup.IDENTITY) {
/*  26 */       return this;
/*     */     }
/*     */     
/*  29 */     Vector3i v = rotation.rotate(new Vector3i(this.xSize, this.ySize, this.zSize));
/*     */ 
/*     */     
/*  32 */     int shiftX = fixupCoordinate(v, 0);
/*  33 */     int shiftY = fixupCoordinate(v, 1);
/*  34 */     int shiftZ = fixupCoordinate(v, 2);
/*     */     
/*  36 */     DiscreteVoxelShape newShape = new BitSetDiscreteVoxelShape(v.x, v.y, v.z);
/*  37 */     for (int x = 0; x < this.xSize; x++) {
/*  38 */       for (int y = 0; y < this.ySize; y++) {
/*  39 */         for (int z = 0; z < this.zSize; z++) {
/*  40 */           if (isFull(x, y, z)) {
/*  41 */             Vector3i newPos = rotation.rotate(v.set(x, y, z));
/*  42 */             int newX = shiftX + newPos.x;
/*  43 */             int newY = shiftY + newPos.y;
/*  44 */             int newZ = shiftZ + newPos.z;
/*  45 */             newShape.fill(newX, newY, newZ);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  50 */     return newShape;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int fixupCoordinate(Vector3i v, int index) {
/*  55 */     int value = v.get(index);
/*  56 */     if (value < 0) {
/*  57 */       v.setComponent(index, -value);
/*  58 */       return -value - 1;
/*     */     } 
/*  60 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isFullWide(AxisCycle transform, int x, int y, int z) {
/*  64 */     return isFullWide(transform
/*  65 */         .cycle(x, y, z, Direction.Axis.X), transform
/*  66 */         .cycle(x, y, z, Direction.Axis.Y), transform
/*  67 */         .cycle(x, y, z, Direction.Axis.Z));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullWide(int x, int y, int z) {
/*  72 */     if (x < 0 || y < 0 || z < 0) {
/*  73 */       return false;
/*     */     }
/*  75 */     if (x >= this.xSize || y >= this.ySize || z >= this.zSize) {
/*  76 */       return false;
/*     */     }
/*  78 */     return isFull(x, y, z);
/*     */   }
/*     */   
/*     */   public boolean isFull(AxisCycle transform, int x, int y, int z) {
/*  82 */     return isFull(transform
/*  83 */         .cycle(x, y, z, Direction.Axis.X), transform
/*  84 */         .cycle(x, y, z, Direction.Axis.Y), transform
/*  85 */         .cycle(x, y, z, Direction.Axis.Z));
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean isFull(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public abstract void fill(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public boolean isEmpty() {
/*  94 */     for (Direction.Axis axis : AXIS_VALUES) {
/*  95 */       if (firstFull(axis) >= lastFull(axis)) {
/*  96 */         return true;
/*     */       }
/*     */     } 
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract int firstFull(Direction.Axis paramAxis);
/*     */   
/*     */   public abstract int lastFull(Direction.Axis paramAxis);
/*     */   
/*     */   public int firstFull(Direction.Axis aAxis, int b, int c) {
/* 108 */     int aSize = getSize(aAxis);
/* 109 */     if (b < 0 || c < 0) {
/* 110 */       return aSize;
/*     */     }
/* 112 */     Direction.Axis bAxis = AxisCycle.FORWARD.cycle(aAxis);
/* 113 */     Direction.Axis cAxis = AxisCycle.BACKWARD.cycle(aAxis);
/* 114 */     if (b >= getSize(bAxis) || c >= getSize(cAxis)) {
/* 115 */       return aSize;
/*     */     }
/* 117 */     AxisCycle transform = AxisCycle.between(Direction.Axis.X, aAxis);
/* 118 */     for (int a = 0; a < aSize; a++) {
/* 119 */       if (isFull(transform, a, b, c)) {
/* 120 */         return a;
/*     */       }
/*     */     } 
/* 123 */     return aSize;
/*     */   }
/*     */   
/*     */   public int lastFull(Direction.Axis aAxis, int b, int c) {
/* 127 */     if (b < 0 || c < 0) {
/* 128 */       return 0;
/*     */     }
/* 130 */     Direction.Axis bAxis = AxisCycle.FORWARD.cycle(aAxis);
/* 131 */     Direction.Axis cAxis = AxisCycle.BACKWARD.cycle(aAxis);
/* 132 */     if (b >= getSize(bAxis) || c >= getSize(cAxis)) {
/* 133 */       return 0;
/*     */     }
/* 135 */     int aSize = getSize(aAxis);
/* 136 */     AxisCycle transform = AxisCycle.between(Direction.Axis.X, aAxis);
/* 137 */     for (int a = aSize - 1; a >= 0; a--) {
/* 138 */       if (isFull(transform, a, b, c)) {
/* 139 */         return a + 1;
/*     */       }
/*     */     } 
/* 142 */     return 0;
/*     */   }
/*     */ 
/*     */   
/* 146 */   public int getSize(Direction.Axis axis) { return axis.choose(this.xSize, this.ySize, this.zSize); }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public int getXSize() { return getSize(Direction.Axis.X); }
/*     */ 
/*     */ 
/*     */   
/* 154 */   public int getYSize() { return getSize(Direction.Axis.Y); }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public int getZSize() { return getSize(Direction.Axis.Z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forAllEdges(IntLineConsumer consumer, boolean mergeNeighbors) {
/* 170 */     forAllAxisEdges(consumer, AxisCycle.NONE, mergeNeighbors);
/* 171 */     forAllAxisEdges(consumer, AxisCycle.FORWARD, mergeNeighbors);
/* 172 */     forAllAxisEdges(consumer, AxisCycle.BACKWARD, mergeNeighbors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void forAllAxisEdges(IntLineConsumer consumer, AxisCycle transform, boolean mergeNeighbors) {
/* 180 */     AxisCycle inverse = transform.inverse();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     int aSize = getSize(inverse.cycle(Direction.Axis.X));
/* 186 */     int bSize = getSize(inverse.cycle(Direction.Axis.Y));
/* 187 */     int cSize = getSize(inverse.cycle(Direction.Axis.Z));
/*     */     
/* 189 */     for (int a = 0; a <= aSize; a++) {
/* 190 */       for (int b = 0; b <= bSize; b++) {
/* 191 */         int lastStart = -1;
/* 192 */         for (int c = 0; c <= cSize; c++) {
/* 193 */           int fullSectors = 0;
/*     */           
/* 195 */           int oddSectors = 0;
/* 196 */           for (int da = 0; da <= 1; da++) {
/* 197 */             for (int db = 0; db <= 1; db++) {
/* 198 */               if (isFullWide(inverse, a + da - 1, b + db - 1, c)) {
/* 199 */                 fullSectors++;
/* 200 */                 oddSectors ^= da ^ db;
/*     */               } 
/*     */             } 
/*     */           } 
/* 204 */           if (fullSectors == 1 || fullSectors == 3 || (fullSectors == 2 && (oddSectors & true) == 0)) {
/* 205 */             if (mergeNeighbors) {
/*     */               
/* 207 */               if (lastStart == -1) {
/* 208 */                 lastStart = c;
/*     */               }
/*     */             } else {
/* 211 */               consumer.consume(inverse
/* 212 */                   .cycle(a, b, c, Direction.Axis.X), inverse
/* 213 */                   .cycle(a, b, c, Direction.Axis.Y), inverse
/* 214 */                   .cycle(a, b, c, Direction.Axis.Z), inverse
/* 215 */                   .cycle(a, b, c + 1, Direction.Axis.X), inverse
/* 216 */                   .cycle(a, b, c + 1, Direction.Axis.Y), inverse
/* 217 */                   .cycle(a, b, c + 1, Direction.Axis.Z));
/*     */             }
/*     */           
/* 220 */           } else if (lastStart != -1) {
/*     */             
/* 222 */             consumer.consume(inverse
/* 223 */                 .cycle(a, b, lastStart, Direction.Axis.X), inverse
/* 224 */                 .cycle(a, b, lastStart, Direction.Axis.Y), inverse
/* 225 */                 .cycle(a, b, lastStart, Direction.Axis.Z), inverse
/* 226 */                 .cycle(a, b, c, Direction.Axis.X), inverse
/* 227 */                 .cycle(a, b, c, Direction.Axis.Y), inverse
/* 228 */                 .cycle(a, b, c, Direction.Axis.Z));
/*     */             
/* 230 */             lastStart = -1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 238 */   public void forAllBoxes(IntLineConsumer consumer, boolean mergeNeighbors) { BitSetDiscreteVoxelShape.forAllBoxes(this, consumer, mergeNeighbors); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void forAllFaces(IntFaceConsumer consumer) {
/* 243 */     forAllAxisFaces(consumer, AxisCycle.NONE);
/* 244 */     forAllAxisFaces(consumer, AxisCycle.FORWARD);
/* 245 */     forAllAxisFaces(consumer, AxisCycle.BACKWARD);
/*     */   }
/*     */   
/*     */   private void forAllAxisFaces(IntFaceConsumer consumer, AxisCycle transform) {
/* 249 */     AxisCycle inverse = transform.inverse();
/*     */     
/* 251 */     Direction.Axis cAxis = inverse.cycle(Direction.Axis.Z);
/*     */     
/* 253 */     int aSize = getSize(inverse.cycle(Direction.Axis.X));
/* 254 */     int bSize = getSize(inverse.cycle(Direction.Axis.Y));
/* 255 */     int cSize = getSize(cAxis);
/*     */     
/* 257 */     Direction negative = Direction.fromAxisAndDirection(cAxis, Direction.AxisDirection.NEGATIVE);
/* 258 */     Direction positive = Direction.fromAxisAndDirection(cAxis, Direction.AxisDirection.POSITIVE);
/*     */     
/* 260 */     for (int a = 0; a < aSize; a++) {
/* 261 */       for (int b = 0; b < bSize; b++) {
/* 262 */         boolean lastFull = false;
/* 263 */         for (int c = 0; c <= cSize; c++) {
/* 264 */           boolean full = (c != cSize && isFull(inverse, a, b, c));
/* 265 */           if (!lastFull && full) {
/* 266 */             consumer.consume(negative, inverse
/*     */                 
/* 268 */                 .cycle(a, b, c, Direction.Axis.X), inverse
/* 269 */                 .cycle(a, b, c, Direction.Axis.Y), inverse
/* 270 */                 .cycle(a, b, c, Direction.Axis.Z));
/*     */           }
/*     */           
/* 273 */           if (lastFull && !full) {
/* 274 */             consumer.consume(positive, inverse
/*     */                 
/* 276 */                 .cycle(a, b, c - 1, Direction.Axis.X), inverse
/* 277 */                 .cycle(a, b, c - 1, Direction.Axis.Y), inverse
/* 278 */                 .cycle(a, b, c - 1, Direction.Axis.Z));
/*     */           }
/*     */           
/* 281 */           lastFull = full;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface IntLineConsumer {
/*     */     void consume(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6);
/*     */   }
/*     */   
/*     */   public static interface IntFaceConsumer {
/*     */     void consume(Direction param1Direction, int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\DiscreteVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */