/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializer<T extends AbstractCookingRecipe>
/*    */   extends Object
/*    */   implements RecipeSerializer<T>
/*    */ {
/*    */   private final MapCodec<T> codec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */   
/*    */   public Serializer(AbstractCookingRecipe.Factory<T> factory, int defaultCookingTime) {
/* 66 */     this.codec = RecordCodecBuilder.mapCodec(r -> {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 73 */           Objects.requireNonNull(factory); return r.group(Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group), CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter(AbstractCookingRecipe::category), Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("result").forGetter(SingleItemRecipe::result), Codec.FLOAT.fieldOf("experience").orElse(Float.valueOf(0.0F)).forGetter(AbstractCookingRecipe::experience), Codec.INT.fieldOf("cookingtime").orElse(Integer.valueOf(defaultCookingTime)).forGetter(AbstractCookingRecipe::cookingTime)).apply(r, factory::create);
/*    */         });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 82 */     Objects.requireNonNull(factory); this.streamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group, CookingBookCategory.STREAM_CODEC, AbstractCookingRecipe::category, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStack.STREAM_CODEC, SingleItemRecipe::result, ByteBufCodecs.FLOAT, AbstractCookingRecipe::experience, ByteBufCodecs.INT, AbstractCookingRecipe::cookingTime, factory::create);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   public MapCodec<T> codec() { return this.codec; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 93 */   public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */