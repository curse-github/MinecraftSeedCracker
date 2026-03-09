/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Supplier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ static enum Type
/*     */   implements StringRepresentable
/*     */ {
/* 146 */   PASS_THROUGH("pass_through", () -> FilterMask.PASS_THROUGH_CODEC),
/* 147 */   FULLY_FILTERED("fully_filtered", () -> FilterMask.FULLY_FILTERED_CODEC),
/* 148 */   PARTIALLY_FILTERED("partially_filtered", () -> FilterMask.PARTIALLY_FILTERED_CODEC);
/*     */   
/*     */   private final String serializedName;
/*     */   
/*     */   private final Supplier<MapCodec<FilterMask>> codec;
/*     */   
/*     */   Type(String serializedName, Supplier<MapCodec<FilterMask>> codec) {
/* 155 */     this.serializedName = serializedName;
/* 156 */     this.codec = codec;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public String getSerializedName() { return this.serializedName; }
/*     */ 
/*     */ 
/*     */   
/* 165 */   private MapCodec<FilterMask> codec() { return (MapCodec)this.codec.get(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\FilterMask$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */