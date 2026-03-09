/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
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
/*     */ public static enum Action
/*     */   implements StringRepresentable
/*     */ {
/*     */   public static final Codec<Action> UNSAFE_CODEC;
/*     */   public static final Codec<Action> CODEC;
/* 127 */   SHOW_TEXT("show_text", true, HoverEvent.ShowText.CODEC),
/* 128 */   SHOW_ITEM("show_item", true, HoverEvent.ShowItem.CODEC),
/* 129 */   SHOW_ENTITY("show_entity", true, HoverEvent.ShowEntity.CODEC);
/*     */   
/*     */   static  {
/* 132 */     UNSAFE_CODEC = StringRepresentable.fromValues(Action::values);
/* 133 */     CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */   }
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   
/*     */   Action(String name, boolean allowFromServer, MapCodec<? extends HoverEvent> codec) {
/* 140 */     this.name = name;
/* 141 */     this.allowFromServer = allowFromServer;
/* 142 */     this.codec = codec;
/*     */   }
/*     */   private final boolean allowFromServer; private final MapCodec<? extends HoverEvent> codec;
/*     */   
/* 146 */   public boolean isAllowedFromServer() { return this.allowFromServer; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public String toString() { return "<action " + this.name + ">"; }
/*     */ 
/*     */   
/*     */   private static DataResult<Action> filterForSerialization(Action action) {
/* 160 */     if (!action.isAllowedFromServer()) {
/* 161 */       return DataResult.error(() -> "Action not allowed: " + String.valueOf(action));
/*     */     }
/* 163 */     return DataResult.success(action, Lifecycle.stable());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\HoverEvent$Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */