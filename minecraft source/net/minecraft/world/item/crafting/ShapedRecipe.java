/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
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
/*     */ import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ShapedRecipe
/*     */   implements CraftingRecipe {
/*     */   private final ShapedRecipePattern pattern;
/*     */   private final ItemStack result;
/*     */   private final String group;
/*     */   private final CraftingBookCategory category;
/*     */   private final boolean showNotification;
/*     */   private PlacementInfo placementInfo;
/*     */   
/*     */   public ShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
/*  31 */     this.group = group;
/*  32 */     this.category = category;
/*  33 */     this.pattern = pattern;
/*  34 */     this.result = result;
/*  35 */     this.showNotification = showNotification;
/*     */   }
/*     */ 
/*     */   
/*  39 */   public ShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result) { this(group, category, pattern, result, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public RecipeSerializer<? extends ShapedRecipe> getSerializer() { return RecipeSerializer.SHAPED_RECIPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public String group() { return this.group; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public CraftingBookCategory category() { return this.category; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  59 */   public List<Optional<Ingredient>> getIngredients() { return this.pattern.ingredients(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public PlacementInfo placementInfo() {
/*  64 */     if (this.placementInfo == null) {
/*  65 */       this.placementInfo = PlacementInfo.createFromOptionals(this.pattern.ingredients());
/*     */     }
/*  67 */     return this.placementInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public boolean showNotification() { return this.showNotification; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public boolean matches(CraftingInput input, Level level) { return this.pattern.matches(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) { return this.result.copy(); }
/*     */ 
/*     */ 
/*     */   
/*  86 */   public int getWidth() { return this.pattern.width(); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public int getHeight() { return this.pattern.height(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  95 */     return List.of(new ShapedCraftingRecipeDisplay(this.pattern
/*  96 */           .width(), this.pattern
/*  97 */           .height(), this.pattern
/*  98 */           .ingredients().stream().map(e -> (SlotDisplay)e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(), new SlotDisplay.ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*     */   }
/*     */   
/*     */   public static class Serializer
/*     */     extends Object
/*     */     implements RecipeSerializer<ShapedRecipe>
/*     */   {
/* 105 */     public static final MapCodec<ShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(r -> r.group(Codec.STRING
/* 106 */           .optionalFieldOf("group", "").forGetter(()), CraftingBookCategory.CODEC
/* 107 */           .fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), ShapedRecipePattern.MAP_CODEC
/* 108 */           .forGetter(()), ItemStack.STRICT_CODEC
/* 109 */           .fieldOf("result").forGetter(()), Codec.BOOL
/* 110 */           .optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(()))
/* 111 */         .apply(r, ShapedRecipe::new));
/*     */     
/* 113 */     public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
/*     */ 
/*     */ 
/*     */     
/* 117 */     public MapCodec<ShapedRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     public StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> streamCodec() { return STREAM_CODEC; }
/*     */ 
/*     */     
/*     */     private static ShapedRecipe fromNetwork(RegistryFriendlyByteBuf input) {
/* 126 */       String group = input.readUtf();
/* 127 */       CraftingBookCategory category = (CraftingBookCategory)input.readEnum(CraftingBookCategory.class);
/* 128 */       ShapedRecipePattern pattern = (ShapedRecipePattern)ShapedRecipePattern.STREAM_CODEC.decode(input);
/* 129 */       ItemStack result = (ItemStack)ItemStack.STREAM_CODEC.decode(input);
/* 130 */       boolean showNotification = input.readBoolean();
/* 131 */       return new ShapedRecipe(group, category, pattern, result, showNotification);
/*     */     }
/*     */     
/*     */     private static void toNetwork(RegistryFriendlyByteBuf output, ShapedRecipe recipe) {
/* 135 */       output.writeUtf(recipe.group);
/* 136 */       output.writeEnum(recipe.category);
/* 137 */       ShapedRecipePattern.STREAM_CODEC.encode(output, recipe.pattern);
/* 138 */       ItemStack.STREAM_CODEC.encode(output, recipe.result);
/* 139 */       output.writeBoolean(recipe.showNotification);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\ShapedRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */