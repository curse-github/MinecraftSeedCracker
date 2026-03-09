/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
/*     */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ 
/*     */ public class SmithingTrimRecipe
/*     */   implements SmithingRecipe {
/*     */   private final Ingredient template;
/*     */   private final Ingredient base;
/*     */   private final Ingredient addition;
/*     */   private final Holder<TrimPattern> pattern;
/*     */   private PlacementInfo placementInfo;
/*     */   
/*     */   public SmithingTrimRecipe(Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern) {
/*  34 */     this.template = template;
/*  35 */     this.base = base;
/*  36 */     this.addition = addition;
/*  37 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) { return applyTrim(registries, input.base(), input.addition(), this.pattern); }
/*     */ 
/*     */   
/*     */   public static ItemStack applyTrim(HolderLookup.Provider registries, ItemStack baseItem, ItemStack materialItem, Holder<TrimPattern> pattern) {
/*  46 */     Optional<Holder<TrimMaterial>> material = TrimMaterials.getFromIngredient(registries, materialItem);
/*  47 */     if (material.isPresent()) {
/*  48 */       ArmorTrim existingTrim = (ArmorTrim)baseItem.get(DataComponents.TRIM);
/*  49 */       ArmorTrim newTrim = new ArmorTrim((Holder)material.get(), pattern);
/*  50 */       if (Objects.equals(existingTrim, newTrim)) {
/*  51 */         return ItemStack.EMPTY;
/*     */       }
/*  53 */       ItemStack trimmedItem = baseItem.copyWithCount(1);
/*  54 */       trimmedItem.set(DataComponents.TRIM, newTrim);
/*  55 */       return trimmedItem;
/*     */     } 
/*  57 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public Optional<Ingredient> templateIngredient() { return Optional.of(this.template); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public Ingredient baseIngredient() { return this.base; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public Optional<Ingredient> additionIngredient() { return Optional.of(this.addition); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public RecipeSerializer<SmithingTrimRecipe> getSerializer() { return RecipeSerializer.SMITHING_TRIM; }
/*     */ 
/*     */ 
/*     */   
/*     */   public PlacementInfo placementInfo() {
/*  82 */     if (this.placementInfo == null) {
/*  83 */       this.placementInfo = PlacementInfo.create(List.of(this.template, this.base, this.addition));
/*     */     }
/*  85 */     return this.placementInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  90 */     SlotDisplay base = this.base.display();
/*  91 */     SlotDisplay material = this.addition.display();
/*  92 */     SlotDisplay template = this.template.display();
/*     */     
/*  94 */     return List.of(new SmithingRecipeDisplay(template, base, material, new SlotDisplay.SmithingTrimDemoSlotDisplay(base, material, this.pattern), new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Serializer
/*     */     extends Object
/*     */     implements RecipeSerializer<SmithingTrimRecipe>
/*     */   {
/* 106 */     private static final MapCodec<SmithingTrimRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Ingredient.CODEC
/* 107 */           .fieldOf("template").forGetter(()), Ingredient.CODEC
/* 108 */           .fieldOf("base").forGetter(()), Ingredient.CODEC
/* 109 */           .fieldOf("addition").forGetter(()), TrimPattern.CODEC
/* 110 */           .fieldOf("pattern").forGetter(()))
/* 111 */         .apply(i, SmithingTrimRecipe::new));
/*     */     
/* 113 */     public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimRecipe> STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 114 */         r.template, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 115 */         r.base, Ingredient.CONTENTS_STREAM_CODEC, r -> 
/* 116 */         r.addition, TrimPattern.STREAM_CODEC, r -> 
/* 117 */         r.pattern, SmithingTrimRecipe::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     public MapCodec<SmithingTrimRecipe> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     public StreamCodec<RegistryFriendlyByteBuf, SmithingTrimRecipe> streamCodec() { return STREAM_CODEC; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SmithingTrimRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */