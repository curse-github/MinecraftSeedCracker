/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializer<T extends CraftingRecipe>
/*    */   extends Object
/*    */   implements RecipeSerializer<T>
/*    */ {
/*    */   private final MapCodec<T> codec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */   
/*    */   public Serializer(Factory<T> constructor) {
/* 38 */     this.codec = RecordCodecBuilder.mapCodec(r -> {
/*    */           
/* 40 */           Objects.requireNonNull(constructor); return r.group(CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(CraftingRecipe::category)).apply(r, constructor::create);
/*    */         });
/*    */ 
/*    */     
/* 44 */     Objects.requireNonNull(constructor); this.streamCodec = StreamCodec.composite(CraftingBookCategory.STREAM_CODEC, CraftingRecipe::category, constructor::create);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public MapCodec<T> codec() { return this.codec; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends CraftingRecipe> {
/*    */     T create(CraftingBookCategory param2CraftingBookCategory);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\CustomRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */