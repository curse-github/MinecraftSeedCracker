/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum Brightness
/*     */ {
/*     */   private static final Brightness[] VALUES;
/* 115 */   LOW(0, 180),
/* 116 */   NORMAL(1, 220),
/* 117 */   HIGH(2, 255),
/* 118 */   LOWEST(3, 135);
/*     */   
/*     */   static  {
/* 121 */     VALUES = new Brightness[] { LOW, NORMAL, HIGH, LOWEST };
/*     */   }
/*     */   
/*     */   public final int id;
/*     */   
/*     */   Brightness(int id, int modifier) {
/* 127 */     this.id = id;
/* 128 */     this.modifier = modifier;
/*     */   }
/*     */   public final int modifier;
/*     */   public static Brightness byId(int id) {
/* 132 */     Preconditions.checkPositionIndex(id, VALUES.length, "brightness id");
/* 133 */     return byIdUnsafe(id);
/*     */   }
/*     */ 
/*     */   
/* 137 */   private static Brightness byIdUnsafe(int id) { return VALUES[id]; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\material\MapColor$Brightness.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */