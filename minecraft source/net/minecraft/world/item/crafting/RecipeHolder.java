/*    */ package net.minecraft.world.item.crafting;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class RecipeHolder<T extends Recipe<?>> extends Record {
/*    */   private final ResourceKey<Recipe<?>> id;
/*    */   
/*  9 */   public RecipeHolder(ResourceKey<Recipe<?>> id, T value) { this.id = id; this.value = value; } private final T value; public ResourceKey<Recipe<?>> id() { return this.id; } public T value() { return (T)this.value; }
/* 10 */   public static final StreamCodec<RegistryFriendlyByteBuf, RecipeHolder<?>> STREAM_CODEC = StreamCodec.composite(
/* 11 */       ResourceKey.streamCodec(Registries.RECIPE), RecipeHolder::id, Recipe.STREAM_CODEC, RecipeHolder::value, RecipeHolder::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 18 */     if (this == obj) {
/* 19 */       return true;
/*    */     }
/* 21 */     if (obj instanceof RecipeHolder) { RecipeHolder<?> holder = (RecipeHolder)obj; if (this.id == holder.id); }  return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public int hashCode() { return this.id.hashCode(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public String toString() { return this.id.toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */