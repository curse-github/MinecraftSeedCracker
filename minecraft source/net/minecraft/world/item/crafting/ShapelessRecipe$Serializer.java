/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   implements RecipeSerializer<ShapelessRecipe>
/*     */ {
/*  84 */   private static final MapCodec<ShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(r -> r.group(Codec.STRING
/*  85 */         .optionalFieldOf("group", "").forGetter(()), CraftingBookCategory.CODEC
/*  86 */         .fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), ItemStack.STRICT_CODEC
/*  87 */         .fieldOf("result").forGetter(()), Ingredient.CODEC
/*  88 */         .listOf(1, 9).fieldOf("ingredients").forGetter(()))
/*  89 */       .apply(r, ShapelessRecipe::new));
/*     */   
/*  91 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, r -> 
/*  92 */       r.group, CraftingBookCategory.STREAM_CODEC, r -> 
/*  93 */       r.category, ItemStack.STREAM_CODEC, r -> 
/*  94 */       r.result, Ingredient.CONTENTS_STREAM_CODEC
/*  95 */       .apply(ByteBufCodecs.list()), r -> r.ingredients, ShapelessRecipe::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public MapCodec<ShapelessRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> streamCodec() { return STREAM_CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\ShapelessRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */