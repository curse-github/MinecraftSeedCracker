/*     */ package net.minecraft.world.scores.criteria;
/*     */ 
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum RenderType
/*     */   implements StringRepresentable
/*     */ {
/* 119 */   INTEGER("integer"),
/* 120 */   HEARTS("hearts");
/*     */   
/*     */   private final String id;
/*     */   
/*     */   public static final StringRepresentable.EnumCodec<RenderType> CODEC;
/*     */   
/* 126 */   RenderType(String id) { this.id = id; }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public String getId() { return this.id; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public String getSerializedName() { return this.id; }
/*     */   
/*     */   static  {
/* 138 */     CODEC = StringRepresentable.fromEnum(RenderType::values);
/*     */   }
/*     */   
/* 141 */   public static RenderType byId(String key) { return (RenderType)CODEC.byName(key, INTEGER); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\criteria\ObjectiveCriteria$RenderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */