/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
/*     */ 
/*     */ public class SmithingTransformRecipe
/*     */   implements SmithingRecipe {
/*     */   private final Optional<Ingredient> template;
/*     */   private final Ingredient base;
/*     */   private final Optional<Ingredient> addition;
/*     */   private final TransmuteResult result;
/*     */   private PlacementInfo placementInfo;
/*     */   
/*     */   public SmithingTransformRecipe(Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition, TransmuteResult result) {
/*  27 */     this.template = template;
/*  28 */     this.base = base;
/*  29 */     this.addition = addition;
/*  30 */     this.result = result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  35 */   public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) { return this.result.apply(input.base()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   public Optional<Ingredient> templateIngredient() { return this.template; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public Ingredient baseIngredient() { return this.base; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public Optional<Ingredient> additionIngredient() { return this.addition; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public RecipeSerializer<SmithingTransformRecipe> getSerializer() { return RecipeSerializer.SMITHING_TRANSFORM; }
/*     */ 
/*     */ 
/*     */   
/*     */   public PlacementInfo placementInfo() {
/*  60 */     if (this.placementInfo == null) {
/*  61 */       this.placementInfo = PlacementInfo.createFromOptionals(List.of(this.template, Optional.of(this.base), this.addition));
/*     */     }
/*  63 */     return this.placementInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  69 */     return List.of(new SmithingRecipeDisplay(
/*     */           
/*  71 */           Ingredient.optionalIngredientToDisplay(this.template), this.base
/*  72 */           .display(), 
/*  73 */           Ingredient.optionalIngredientToDisplay(this.addition), this.result
/*  74 */           .display(), new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)));
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends Object
/*     */     implements RecipeSerializer<SmithingTransformRecipe>
/*     */   {
/*  81 */     private static final MapCodec<SmithingTransformRecipe> CODEC = RecordCodecBuilder.mapCodec(r -> r.group(Ingredient.CODEC
/*  82 */           .optionalFieldOf("template").forGetter(()), Ingredient.CODEC
/*  83 */           .fieldOf("base").forGetter(()), Ingredient.CODEC
/*  84 */           .optionalFieldOf("addition").forGetter(()), TransmuteResult.CODEC
/*  85 */           .fieldOf("result").forGetter(()))
/*  86 */         .apply(r, SmithingTransformRecipe::new));
/*     */     
/*  88 */     public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> STREAM_CODEC = StreamCodec.composite(Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, r -> 
/*  89 */         r.template, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/*  90 */         r.base, Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, r -> 
/*  91 */         r.addition, TransmuteResult.STREAM_CODEC, r -> 
/*  92 */         r.result, SmithingTransformRecipe::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     public MapCodec<SmithingTransformRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     public StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> streamCodec() { return STREAM_CODEC; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SmithingTransformRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */