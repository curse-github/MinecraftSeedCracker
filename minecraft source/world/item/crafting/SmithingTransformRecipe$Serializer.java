/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
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
/*     */ public class Serializer
/*     */   extends Object
/*     */   implements RecipeSerializer<SmithingTransformRecipe>
/*     */ {
/*  81 */   private static final MapCodec<SmithingTransformRecipe> CODEC = RecordCodecBuilder.mapCodec(r -> r.group(Ingredient.CODEC
/*  82 */         .optionalFieldOf("template").forGetter(()), Ingredient.CODEC
/*  83 */         .fieldOf("base").forGetter(()), Ingredient.CODEC
/*  84 */         .optionalFieldOf("addition").forGetter(()), TransmuteResult.CODEC
/*  85 */         .fieldOf("result").forGetter(()))
/*  86 */       .apply(r, SmithingTransformRecipe::new));
/*     */   
/*  88 */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> STREAM_CODEC = StreamCodec.composite(Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, r -> 
/*  89 */       r.template, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/*  90 */       r.base, Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, r -> 
/*  91 */       r.addition, TransmuteResult.STREAM_CODEC, r -> 
/*  92 */       r.result, SmithingTransformRecipe::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public MapCodec<SmithingTransformRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> streamCodec() { return STREAM_CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SmithingTransformRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */