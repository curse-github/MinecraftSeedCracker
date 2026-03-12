/*     */ package net.minecraft.world.item.crafting;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.HolderSetCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public final class Ingredient extends Object implements Predicate<ItemStack>, StackedContents.IngredientInfo<Holder<Item>> {
/*  26 */   public static final StreamCodec<RegistryFriendlyByteBuf, Ingredient> CONTENTS_STREAM_CODEC = ByteBufCodecs.holderSet(Registries.ITEM)
/*  27 */     .map(Ingredient::new, i -> i.values);
/*     */ 
/*     */   
/*  30 */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Ingredient>> OPTIONAL_CONTENTS_STREAM_CODEC = ByteBufCodecs.holderSet(Registries.ITEM).map(ingredient -> 
/*  31 */       (ingredient.size() == 0) ? Optional.empty() : Optional.of(new Ingredient(ingredient)), ingredient -> 
/*  32 */       (HolderSet)ingredient.map(()).orElse(HolderSet.direct(new Holder[0])));
/*     */ 
/*     */   
/*  35 */   public static final Codec<HolderSet<Item>> NON_AIR_HOLDER_SET_CODEC = HolderSetCodec.create(Registries.ITEM, Item.CODEC, false);
/*  36 */   public static final Codec<Ingredient> CODEC = ExtraCodecs.nonEmptyHolderSet(NON_AIR_HOLDER_SET_CODEC).xmap(Ingredient::new, i -> i.values);
/*     */   
/*     */   private final HolderSet<Item> values;
/*     */   
/*     */   private Ingredient(HolderSet<Item> values) {
/*  41 */     values.unwrap().ifRight(directValues -> {
/*  42 */           if (directValues.isEmpty()) {
/*  43 */             throw new UnsupportedOperationException("Ingredients can't be empty");
/*     */           }
/*     */           
/*  46 */           if (directValues.contains(Items.AIR.builtInRegistryHolder())) {
/*  47 */             throw new UnsupportedOperationException("Ingredient can't contain air");
/*     */           }
/*     */         });
/*  50 */     this.values = values;
/*     */   }
/*     */ 
/*     */   
/*  54 */   public static boolean testOptionalIngredient(Optional<Ingredient> ingredient, ItemStack stack) { Objects.requireNonNull(stack); return ((Boolean)ingredient.map(value -> Boolean.valueOf(value.test(stack))).orElseGet(stack::isEmpty)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  62 */   public Stream<Holder<Item>> items() { return this.values.stream(); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public boolean isEmpty() { return (this.values.size() == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public boolean test(ItemStack input) { return input.is(this.values); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public boolean acceptsItem(Holder<Item> item) { return this.values.contains(item); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  81 */     if (o instanceof Ingredient) { Ingredient other = (Ingredient)o;
/*  82 */       return Objects.equals(this.values, other.values); }
/*     */     
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*  88 */   public static Ingredient of(ItemLike itemLike) { return new Ingredient(HolderSet.direct(new Holder[] { itemLike.asItem().builtInRegistryHolder() })); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static Ingredient of(ItemLike... items) { return of(Arrays.stream(items)); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public static Ingredient of(Stream<? extends ItemLike> stream) { return new Ingredient(HolderSet.direct(stream.map(e -> e.asItem().builtInRegistryHolder()).toList())); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static Ingredient of(HolderSet<Item> tag) { return new Ingredient(tag); }
/*     */ 
/*     */   
/*     */   public SlotDisplay display() {
/* 104 */     return (SlotDisplay)this.values.unwrap().map(net.minecraft.world.item.crafting.display.SlotDisplay.TagSlotDisplay::new, l -> 
/*     */         
/* 106 */         new SlotDisplay.Composite(l.stream().map(Ingredient::displayForSingleItem).toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static SlotDisplay optionalIngredientToDisplay(Optional<Ingredient> ingredient) { return (SlotDisplay)ingredient.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE); }
/*     */ 
/*     */   
/*     */   private static SlotDisplay displayForSingleItem(Holder<Item> item) {
/* 116 */     SlotDisplay.ItemSlotDisplay itemSlotDisplay = new SlotDisplay.ItemSlotDisplay(item);
/*     */     
/* 118 */     ItemStack remainderStack = ((Item)item.value()).getCraftingRemainder();
/* 119 */     if (!remainderStack.isEmpty()) {
/* 120 */       SlotDisplay.ItemStackSlotDisplay itemStackSlotDisplay = new SlotDisplay.ItemStackSlotDisplay(remainderStack);
/* 121 */       return new SlotDisplay.WithRemainder(itemSlotDisplay, itemStackSlotDisplay);
/*     */     } 
/* 123 */     return itemSlotDisplay;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\Ingredient.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */