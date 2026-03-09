/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ShapelessRecipe
/*     */   implements CraftingRecipe {
/*     */   private final String group;
/*     */   private final CraftingBookCategory category;
/*     */   private final ItemStack result;
/*     */   private final List<Ingredient> ingredients;
/*     */   private PlacementInfo placementInfo;
/*     */   
/*     */   public ShapelessRecipe(String group, CraftingBookCategory category, ItemStack result, List<Ingredient> ingredients) {
/*  29 */     this.group = group;
/*  30 */     this.category = category;
/*  31 */     this.result = result;
/*  32 */     this.ingredients = ingredients;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  37 */   public RecipeSerializer<ShapelessRecipe> getSerializer() { return RecipeSerializer.SHAPELESS_RECIPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public String group() { return this.group; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public CraftingBookCategory category() { return this.category; }
/*     */ 
/*     */ 
/*     */   
/*     */   public PlacementInfo placementInfo() {
/*  52 */     if (this.placementInfo == null) {
/*  53 */       this.placementInfo = PlacementInfo.create(this.ingredients);
/*     */     }
/*  55 */     return this.placementInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  60 */     if (input.ingredientCount() != this.ingredients.size()) {
/*  61 */       return false;
/*     */     }
/*  63 */     if (input.size() == 1 && this.ingredients.size() == 1) {
/*  64 */       return ((Ingredient)this.ingredients.getFirst()).test(input.getItem(0));
/*     */     }
/*  66 */     return input.stackedContents().canCraft(this, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) { return this.result.copy(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  76 */     return List.of(new ShapelessCraftingRecipeDisplay(this.ingredients
/*  77 */           .stream().map(Ingredient::display).toList(), new SlotDisplay.ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends Object
/*     */     implements RecipeSerializer<ShapelessRecipe>
/*     */   {
/*  84 */     private static final MapCodec<ShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(r -> r.group(Codec.STRING
/*  85 */           .optionalFieldOf("group", "").forGetter(()), CraftingBookCategory.CODEC
/*  86 */           .fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), ItemStack.STRICT_CODEC
/*  87 */           .fieldOf("result").forGetter(()), Ingredient.CODEC
/*  88 */           .listOf(1, 9).fieldOf("ingredients").forGetter(()))
/*  89 */         .apply(r, ShapelessRecipe::new));
/*     */     
/*  91 */     public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, r -> 
/*  92 */         r.group, CraftingBookCategory.STREAM_CODEC, r -> 
/*  93 */         r.category, ItemStack.STREAM_CODEC, r -> 
/*  94 */         r.result, Ingredient.CONTENTS_STREAM_CODEC
/*  95 */         .apply(ByteBufCodecs.list()), r -> r.ingredients, ShapelessRecipe::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     public MapCodec<ShapelessRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     public StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> streamCodec() { return STREAM_CODEC; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\ShapelessRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */