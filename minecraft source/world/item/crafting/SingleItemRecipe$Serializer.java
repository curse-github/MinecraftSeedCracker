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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializer<T extends SingleItemRecipe>
/*    */   extends Object
/*    */   implements RecipeSerializer<T>
/*    */ {
/*    */   private final MapCodec<T> codec;
/*    */   private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */   
/*    */   protected Serializer(SingleItemRecipe.Factory<T> factory) {
/* 69 */     this.codec = RecordCodecBuilder.mapCodec(r -> {
/*    */ 
/*    */ 
/*    */           
/* 73 */           Objects.requireNonNull(factory); return r.group(Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group), Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SingleItemRecipe::result)).apply(r, factory::create);
/*    */         });
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 79 */     Objects.requireNonNull(factory); this.streamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStack.STREAM_CODEC, SingleItemRecipe::result, factory::create);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 85 */   public MapCodec<T> codec() { return this.codec; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 90 */   public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SingleItemRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */