/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Serializer
/*     */   extends Object
/*     */   implements RecipeSerializer<TransmuteRecipe>
/*     */ {
/* 114 */   private static final MapCodec<TransmuteRecipe> CODEC = RecordCodecBuilder.mapCodec(r -> r.group(Codec.STRING
/* 115 */         .optionalFieldOf("group", "").forGetter(()), CraftingBookCategory.CODEC
/* 116 */         .fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), Ingredient.CODEC
/* 117 */         .fieldOf("input").forGetter(()), Ingredient.CODEC
/* 118 */         .fieldOf("material").forGetter(()), TransmuteResult.CODEC
/* 119 */         .fieldOf("result").forGetter(()))
/* 120 */       .apply(r, TransmuteRecipe::new));
/*     */   
/* 122 */   public static final StreamCodec<RegistryFriendlyByteBuf, TransmuteRecipe> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, r -> 
/* 123 */       r.group, CraftingBookCategory.STREAM_CODEC, r -> 
/* 124 */       r.category, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 125 */       r.input, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 126 */       r.material, TransmuteResult.STREAM_CODEC, r -> 
/* 127 */       r.result, TransmuteRecipe::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public MapCodec<TransmuteRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public StreamCodec<RegistryFriendlyByteBuf, TransmuteRecipe> streamCodec() { return STREAM_CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\TransmuteRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */