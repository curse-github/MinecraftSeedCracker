/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function5;
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
/*     */ public class TransmuteRecipe
/*     */   implements CraftingRecipe
/*     */ {
/*     */   private final String group;
/*     */   private final CraftingBookCategory category;
/*     */   private final Ingredient input;
/*     */   private final Ingredient material;
/*     */   private final TransmuteResult result;
/*     */   private PlacementInfo placementInfo;
/*     */   
/*     */   public TransmuteRecipe(String group, CraftingBookCategory category, Ingredient input, Ingredient material, TransmuteResult result) {
/*  31 */     this.group = group;
/*  32 */     this.category = category;
/*  33 */     this.input = input;
/*  34 */     this.material = material;
/*  35 */     this.result = result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  41 */     if (input.ingredientCount() != 2) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     boolean foundInput = false;
/*  46 */     boolean foundMaterial = false;
/*     */     
/*  48 */     for (int slot = 0; slot < input.size(); slot++) {
/*  49 */       ItemStack stack = input.getItem(slot);
/*     */       
/*  51 */       if (!stack.isEmpty())
/*     */       {
/*     */ 
/*     */         
/*  55 */         if (!foundInput && this.input.test(stack)) {
/*  56 */           if (this.result.isResultUnchanged(stack)) {
/*  57 */             return false;
/*     */           }
/*  59 */           foundInput = true;
/*  60 */         } else if (!foundMaterial && this.material.test(stack)) {
/*  61 */           foundMaterial = true;
/*     */         } else {
/*  63 */           return false;
/*     */         }  } 
/*     */     } 
/*  66 */     return (foundInput && foundMaterial);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/*  71 */     for (int slot = 0; slot < input.size(); slot++) {
/*  72 */       ItemStack itemStack = input.getItem(slot);
/*     */       
/*  74 */       if (!itemStack.isEmpty() && this.input.test(itemStack)) {
/*  75 */         return this.result.apply(itemStack);
/*     */       }
/*     */     } 
/*  78 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  83 */     return List.of(new ShapelessCraftingRecipeDisplay(
/*  84 */           List.of(this.input.display(), this.material.display()), this.result
/*  85 */           .display(), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public RecipeSerializer<TransmuteRecipe> getSerializer() { return RecipeSerializer.TRANSMUTE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public String group() { return this.group; }
/*     */ 
/*     */ 
/*     */   
/*     */   public PlacementInfo placementInfo() {
/* 102 */     if (this.placementInfo == null) {
/* 103 */       this.placementInfo = PlacementInfo.create(List.of(this.input, this.material));
/*     */     }
/* 105 */     return this.placementInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public CraftingBookCategory category() { return this.category; }
/*     */   
/*     */   public static class Serializer
/*     */     extends Object implements RecipeSerializer<TransmuteRecipe> {
/* 114 */     private static final MapCodec<TransmuteRecipe> CODEC = RecordCodecBuilder.mapCodec(r -> r.group(Codec.STRING
/* 115 */           .optionalFieldOf("group", "").forGetter(()), CraftingBookCategory.CODEC
/* 116 */           .fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), Ingredient.CODEC
/* 117 */           .fieldOf("input").forGetter(()), Ingredient.CODEC
/* 118 */           .fieldOf("material").forGetter(()), TransmuteResult.CODEC
/* 119 */           .fieldOf("result").forGetter(()))
/* 120 */         .apply(r, TransmuteRecipe::new));
/*     */     
/* 122 */     public static final StreamCodec<RegistryFriendlyByteBuf, TransmuteRecipe> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, r -> 
/* 123 */         r.group, CraftingBookCategory.STREAM_CODEC, r -> 
/* 124 */         r.category, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 125 */         r.input, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 126 */         r.material, TransmuteResult.STREAM_CODEC, r -> 
/* 127 */         r.result, TransmuteRecipe::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     public MapCodec<TransmuteRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     public StreamCodec<RegistryFriendlyByteBuf, TransmuteRecipe> streamCodec() { return STREAM_CODEC; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\TransmuteRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */