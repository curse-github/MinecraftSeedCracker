/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Orientation
/*     */ {
/*  34 */   public static final StreamCodec<ByteBuf, Orientation> STREAM_CODEC = ByteBufCodecs.idMapper(Orientation::fromIndex, Orientation::getIndex);
/*     */   
/*  36 */   private static final Orientation[] ORIENTATIONS = (Orientation[])Util.make(() -> {
/*  37 */         orientations = new Orientation[48];
/*  38 */         generateContext(new Orientation(Direction.UP, Direction.NORTH, SideBias.LEFT), orientations);
/*  39 */         return orientations;
/*     */       });
/*     */   
/*     */   private final Direction up;
/*     */   
/*     */   private final Direction front;
/*     */   
/*     */   private final Direction side;
/*     */   private final SideBias sideBias;
/*     */   private final int index;
/*     */   private final List<Direction> neighbors;
/*     */   private final List<Direction> horizontalNeighbors;
/*     */   private final List<Direction> verticalNeighbors;
/*     */   private final Map<Direction, Orientation> withFront;
/*     */   private final Map<Direction, Orientation> withUp;
/*     */   private final Map<SideBias, Orientation> withSideBias;
/*     */   
/*     */   private Orientation(Direction up, Direction front, SideBias sideBias) {
/*  57 */     this.withFront = new EnumMap(Direction.class);
/*  58 */     this.withUp = new EnumMap(Direction.class);
/*  59 */     this.withSideBias = new EnumMap(SideBias.class);
/*     */ 
/*     */     
/*  62 */     this.up = up;
/*  63 */     this.front = front;
/*  64 */     this.sideBias = sideBias;
/*  65 */     this.index = generateIndex(up, front, sideBias);
/*     */     
/*  67 */     Vec3i rightVector = front.getUnitVec3i().cross(up.getUnitVec3i());
/*  68 */     Direction side = Direction.getNearest(rightVector, null);
/*  69 */     Objects.requireNonNull(side);
/*  70 */     if (this.sideBias == SideBias.RIGHT) {
/*  71 */       this.side = side;
/*     */     } else {
/*  73 */       this.side = side.getOpposite();
/*     */     } 
/*  75 */     this.neighbors = List.of(this.front
/*  76 */         .getOpposite(), this.front, this.side, this.side
/*     */ 
/*     */         
/*  79 */         .getOpposite(), this.up
/*  80 */         .getOpposite(), this.up);
/*     */ 
/*     */     
/*  83 */     this.horizontalNeighbors = this.neighbors.stream().filter(d -> (d.getAxis() != this.up.getAxis())).toList();
/*  84 */     this.verticalNeighbors = this.neighbors.stream().filter(d -> (d.getAxis() == this.up.getAxis())).toList();
/*     */   }
/*     */ 
/*     */   
/*  88 */   public static Orientation of(Direction up, Direction front, SideBias sideBias) { return ORIENTATIONS[generateIndex(up, front, sideBias)]; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public Orientation withUp(Direction up) { return (Orientation)this.withUp.get(up); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public Orientation withFront(Direction front) { return (Orientation)this.withFront.get(front); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation withFrontPreserveUp(Direction front) {
/* 117 */     if (front.getAxis() == this.up.getAxis()) {
/* 118 */       return this;
/*     */     }
/* 120 */     return (Orientation)this.withFront.get(front);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Orientation withFrontAdjustSideBias(Direction front) {
/* 128 */     Orientation withFront = withFront(front);
/* 129 */     if (this.front == withFront.side) {
/* 130 */       return withFront.withMirror();
/*     */     }
/* 132 */     return withFront;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public Orientation withSideBias(SideBias sideBias) { return (Orientation)this.withSideBias.get(sideBias); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public Orientation withMirror() { return withSideBias(this.sideBias.getOpposite()); }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public Direction getFront() { return this.front; }
/*     */ 
/*     */ 
/*     */   
/* 154 */   public Direction getUp() { return this.up; }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public Direction getSide() { return this.side; }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public SideBias getSideBias() { return this.sideBias; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public List<Direction> getDirections() { return this.neighbors; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public List<Direction> getHorizontalDirections() { return this.horizontalNeighbors; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   public List<Direction> getVerticalDirections() { return this.verticalNeighbors; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public String toString() { return "[up=" + String.valueOf(this.up) + ",front=" + String.valueOf(this.front) + ",sideBias=" + String.valueOf(this.sideBias) + "]"; }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public int getIndex() { return this.index; }
/*     */ 
/*     */ 
/*     */   
/* 201 */   public static Orientation fromIndex(int index) { return ORIENTATIONS[index]; }
/*     */ 
/*     */ 
/*     */   
/* 205 */   public static Orientation random(RandomSource rand) { return (Orientation)Util.getRandom(ORIENTATIONS, rand); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Orientation generateContext(Orientation self, Orientation[] lookup) {
/* 210 */     if (lookup[self.getIndex()] != null) {
/* 211 */       return lookup[self.getIndex()];
/*     */     }
/* 213 */     lookup[self.getIndex()] = self;
/*     */     
/* 215 */     for (SideBias sideBias : SideBias.values()) {
/* 216 */       self.withSideBias.put(sideBias, generateContext(new Orientation(self.up, self.front, sideBias), lookup));
/*     */     }
/*     */     
/* 219 */     for (Direction facing : Direction.values()) {
/* 220 */       Direction up = self.up;
/*     */       
/* 222 */       if (facing == self.up) {
/* 223 */         up = self.front.getOpposite();
/*     */       }
/* 225 */       if (facing == self.up.getOpposite()) {
/* 226 */         up = self.front;
/*     */       }
/* 228 */       self.withFront.put(facing, generateContext(new Orientation(up, facing, self.sideBias), lookup));
/*     */     } 
/*     */     
/* 231 */     for (Direction facing : Direction.values()) {
/* 232 */       Direction front = self.front;
/*     */       
/* 234 */       if (facing == self.front) {
/* 235 */         front = self.up.getOpposite();
/*     */       }
/* 237 */       if (facing == self.front.getOpposite()) {
/* 238 */         front = self.up;
/*     */       }
/* 240 */       self.withUp.put(facing, generateContext(new Orientation(facing, front, self.sideBias), lookup));
/*     */     } 
/* 242 */     return self;
/*     */   }
/*     */   @VisibleForTesting
/*     */   protected static int generateIndex(Direction up, Direction front, SideBias sideBias) {
/*     */     int frontAxisKey;
/* 247 */     if (up.getAxis() == front.getAxis()) {
/* 248 */       throw new IllegalStateException("Up-vector and front-vector can not be on the same axis");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 253 */     if (up.getAxis() == Direction.Axis.Y) {
/*     */       
/* 255 */       frontAxisKey = (front.getAxis() == Direction.Axis.X) ? 1 : 0;
/*     */     } else {
/*     */       
/* 258 */       frontAxisKey = (front.getAxis() == Direction.Axis.Y) ? 1 : 0;
/*     */     } 
/* 260 */     int frontKey = frontAxisKey << 1 | front.getAxisDirection().ordinal();
/* 261 */     return ((up.ordinal() << 2) + frontKey << 1) + sideBias.ordinal();
/*     */   }
/*     */   
/*     */   public enum SideBias {
/* 265 */     LEFT("left"),
/* 266 */     RIGHT("right");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/* 271 */     SideBias(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */     
/* 275 */     public SideBias getOpposite() { return (this == LEFT) ? RIGHT : LEFT; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 280 */     public String toString() { return this.name; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\Orientation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */