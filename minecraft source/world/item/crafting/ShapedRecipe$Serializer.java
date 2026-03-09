/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   implements RecipeSerializer<ShapedRecipe>
/*     */ {
/* 105 */   public static final MapCodec<ShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(r -> r.group(Codec.STRING
/* 106 */         .optionalFieldOf("group", "").forGetter(()), CraftingBookCategory.CODEC
/* 107 */         .fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), ShapedRecipePattern.MAP_CODEC
/* 108 */         .forGetter(()), ItemStack.STRICT_CODEC
/* 109 */         .fieldOf("result").forGetter(()), Codec.BOOL
/* 110 */         .optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(()))
/* 111 */       .apply(r, ShapedRecipe::new));
/*     */   
/* 113 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
/*     */ 
/*     */ 
/*     */   
/* 117 */   public MapCodec<ShapedRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> streamCodec() { return STREAM_CODEC; }
/*     */ 
/*     */   
/*     */   private static ShapedRecipe fromNetwork(RegistryFriendlyByteBuf input) {
/* 126 */     String group = input.readUtf();
/* 127 */     CraftingBookCategory category = (CraftingBookCategory)input.readEnum(CraftingBookCategory.class);
/* 128 */     ShapedRecipePattern pattern = (ShapedRecipePattern)ShapedRecipePattern.STREAM_CODEC.decode(input);
/* 129 */     ItemStack result = (ItemStack)ItemStack.STREAM_CODEC.decode(input);
/* 130 */     boolean showNotification = input.readBoolean();
/* 131 */     return new ShapedRecipe(group, category, pattern, result, showNotification);
/*     */   }
/*     */   
/*     */   private static void toNetwork(RegistryFriendlyByteBuf output, ShapedRecipe recipe) {
/* 135 */     output.writeUtf(recipe.group);
/* 136 */     output.writeEnum(recipe.category);
/* 137 */     ShapedRecipePattern.STREAM_CODEC.encode(output, recipe.pattern);
/* 138 */     ItemStack.STREAM_CODEC.encode(output, recipe.result);
/* 139 */     output.writeBoolean(recipe.showNotification);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\ShapedRecipe$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */