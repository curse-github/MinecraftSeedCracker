/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.OptionalInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Range
/*     */   extends Column
/*     */ {
/*     */   private final int floor;
/*     */   private final int ceiling;
/*     */   
/*     */   protected Range(int floor, int ceiling) {
/* 136 */     this.floor = floor;
/* 137 */     this.ceiling = ceiling;
/* 138 */     if (height() < 0) {
/* 139 */       throw new IllegalArgumentException("Column of negative height: " + String.valueOf(this));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public OptionalInt getCeiling() { return OptionalInt.of(this.ceiling); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   public OptionalInt getFloor() { return OptionalInt.of(this.floor); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public OptionalInt getHeight() { return OptionalInt.of(height()); }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public int ceiling() { return this.ceiling; }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public int floor() { return this.floor; }
/*     */ 
/*     */ 
/*     */   
/* 167 */   public int height() { return this.ceiling - this.floor - 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public String toString() { return "C(" + this.ceiling + "-" + this.floor + ")"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\Column$Range.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */