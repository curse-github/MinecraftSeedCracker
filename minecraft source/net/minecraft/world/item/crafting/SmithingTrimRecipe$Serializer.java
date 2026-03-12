/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   implements RecipeSerializer<SmithingTrimRecipe>
/*     */ {
/* 106 */   private static final MapCodec<SmithingTrimRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Ingredient.CODEC
/* 107 */         .fieldOf("template").forGetter(()), Ingredient.CODEC
/* 108 */         .fieldOf("base").forGetter(()), Ingredient.CODEC
/* 109 */         .fieldOf("addition").forGetter(()), TrimPattern.CODEC
/* 110 */         .fieldOf("pattern").forGetter(()))
/* 111 */       .apply(i, SmithingTrimRecipe::new));
/*     */   
/* 113 */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimRecipe> STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 114 */       r.template, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 115 */       r.base, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 116 */       r.addition, TrimPattern.STREAM_CODEC, r -> 
/* 117 */       r.pattern, SmithingTrimRecipe::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public MapCodec<SmithingTrimRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public StreamCodec<RegistryFriendlyByteBuf, SmithingTrimRecipe> streamCodec() { return STREAM_CODEC; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SmithingTrimRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */