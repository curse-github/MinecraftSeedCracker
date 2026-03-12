/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Column
/*     */ {
/*  23 */   public static Range around(int lowest, int highest) { return new Range(lowest - 1, highest + 1); }
/*     */ 
/*     */ 
/*     */   
/*  27 */   public static Range inside(int floor, int ceiling) { return new Range(floor, ceiling); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static Column below(int ceiling) { return new Ray(ceiling, false); }
/*     */ 
/*     */ 
/*     */   
/*  38 */   public static Column fromHighest(int highest) { return new Ray(highest + 1, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public static Column above(int floor) { return new Ray(floor, true); }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static Column fromLowest(int lowest) { return new Ray(lowest - 1, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static Column line() { return Line.INSTANCE; }
/*     */ 
/*     */   
/*     */   public static Column create(OptionalInt floor, OptionalInt ceiling) {
/*  60 */     if (floor.isPresent() && ceiling.isPresent()) {
/*  61 */       return inside(floor.getAsInt(), ceiling.getAsInt());
/*     */     }
/*     */     
/*  64 */     if (floor.isPresent()) {
/*  65 */       return above(floor.getAsInt());
/*     */     }
/*     */     
/*  68 */     if (ceiling.isPresent()) {
/*  69 */       return below(ceiling.getAsInt());
/*     */     }
/*     */     
/*  72 */     return line();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getCeiling();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getFloor();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract OptionalInt getHeight();
/*     */ 
/*     */   
/*  88 */   public Column withFloor(OptionalInt floor) { return create(floor, getCeiling()); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public Column withCeiling(OptionalInt ceiling) { return create(getFloor(), ceiling); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<Column> scan(LevelSimulatedReader level, BlockPos pos, int searchRange, Predicate<BlockState> insideColumn, Predicate<BlockState> validEdge) {
/* 102 */     BlockPos.MutableBlockPos mutablePos = pos.mutable();
/* 103 */     if (!level.isStateAtPosition(pos, insideColumn)) {
/* 104 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 108 */     int nearestEmptyY = pos.getY();
/* 109 */     OptionalInt ceiling = scanDirection(level, searchRange, insideColumn, validEdge, mutablePos, nearestEmptyY, Direction.UP);
/* 110 */     OptionalInt floor = scanDirection(level, searchRange, insideColumn, validEdge, mutablePos, nearestEmptyY, Direction.DOWN);
/*     */     
/* 112 */     return Optional.of(create(floor, ceiling));
/*     */   }
/*     */   
/*     */   private static OptionalInt scanDirection(LevelSimulatedReader level, int searchRange, Predicate<BlockState> insideColumn, Predicate<BlockState> validEdge, BlockPos.MutableBlockPos mutablePos, int nearestEmptyY, Direction direction) {
/* 116 */     mutablePos.setY(nearestEmptyY);
/* 117 */     for (int i = 1; i < searchRange && 
/* 118 */       level.isStateAtPosition(mutablePos, insideColumn); i++) {
/* 119 */       mutablePos.move(direction);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     return level.isStateAtPosition(mutablePos, validEdge) ? OptionalInt.of(mutablePos.getY()) : OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Range
/*     */     extends Column
/*     */   {
/*     */     private final int floor;
/*     */     private final int ceiling;
/*     */     
/*     */     protected Range(int floor, int ceiling) {
/* 136 */       this.floor = floor;
/* 137 */       this.ceiling = ceiling;
/* 138 */       if (height() < 0) {
/* 139 */         throw new IllegalArgumentException("Column of negative height: " + String.valueOf(this));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 145 */     public OptionalInt getCeiling() { return OptionalInt.of(this.ceiling); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     public OptionalInt getFloor() { return OptionalInt.of(this.floor); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     public OptionalInt getHeight() { return OptionalInt.of(height()); }
/*     */ 
/*     */ 
/*     */     
/* 159 */     public int ceiling() { return this.ceiling; }
/*     */ 
/*     */ 
/*     */     
/* 163 */     public int floor() { return this.floor; }
/*     */ 
/*     */ 
/*     */     
/* 167 */     public int height() { return this.ceiling - this.floor - 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     public String toString() { return "C(" + this.ceiling + "-" + this.floor + ")"; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Line
/*     */     extends Column
/*     */   {
/* 180 */     private static final Line INSTANCE = new Line();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     public OptionalInt getCeiling() { return OptionalInt.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     public OptionalInt getFloor() { return OptionalInt.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     public OptionalInt getHeight() { return OptionalInt.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     public String toString() { return "C(-)"; }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Ray
/*     */     extends Column
/*     */   {
/*     */     private final int edge;
/*     */     
/*     */     private final boolean pointingUp;
/*     */     
/*     */     public Ray(int edge, boolean pointingUp) {
/* 214 */       this.edge = edge;
/* 215 */       this.pointingUp = pointingUp;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 220 */     public OptionalInt getCeiling() { return this.pointingUp ? OptionalInt.empty() : OptionalInt.of(this.edge); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     public OptionalInt getFloor() { return this.pointingUp ? OptionalInt.of(this.edge) : OptionalInt.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     public OptionalInt getHeight() { return OptionalInt.empty(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 235 */       return this.pointingUp ? ("C(" + this.edge + "-)") : ("C(-" + this.edge + ")");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\Column.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */