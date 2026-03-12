/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public final class BundleContents
/*     */   implements TooltipComponent
/*     */ {
/*  24 */   public static final BundleContents EMPTY = new BundleContents(List.of());
/*     */   
/*  26 */   public static final Codec<BundleContents> CODEC = ItemStack.CODEC.listOf().flatXmap(BundleContents::checkAndCreate, contents -> DataResult.success(contents.items));
/*  27 */   public static final StreamCodec<RegistryFriendlyByteBuf, BundleContents> STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(BundleContents::new, contents -> contents.items);
/*     */   
/*  29 */   private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
/*     */   
/*     */   private static final int NO_STACK_INDEX = -1;
/*     */   public static final int NO_SELECTED_ITEM_INDEX = -1;
/*     */   private final List<ItemStack> items;
/*     */   private final Fraction weight;
/*     */   private final int selectedItem;
/*     */   
/*     */   private BundleContents(List<ItemStack> items, Fraction weight, int selectedItem) {
/*  38 */     this.items = items;
/*  39 */     this.weight = weight;
/*  40 */     this.selectedItem = selectedItem;
/*     */   }
/*     */   
/*     */   private static DataResult<BundleContents> checkAndCreate(List<ItemStack> items) {
/*     */     try {
/*  45 */       Fraction weight = computeContentWeight(items);
/*  46 */       return DataResult.success(new BundleContents(items, weight, -1));
/*  47 */     } catch (ArithmeticException exception) {
/*  48 */       return DataResult.error(() -> "Excessive total bundle weight");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  53 */   public BundleContents(List<ItemStack> items) { this(items, computeContentWeight(items), -1); }
/*     */ 
/*     */   
/*     */   private static Fraction computeContentWeight(List<ItemStack> items) {
/*  57 */     Fraction weight = Fraction.ZERO;
/*  58 */     for (ItemStack stack : items) {
/*  59 */       weight = weight.add(getWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
/*     */     }
/*  61 */     return weight;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Fraction getWeight(ItemStack stack) {
/*  66 */     BundleContents bundle = (BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS);
/*  67 */     if (bundle != null) {
/*  68 */       return BUNDLE_IN_BUNDLE_WEIGHT.add(bundle.weight());
/*     */     }
/*  70 */     List<BeehiveBlockEntity.Occupant> bees = ((Bees)stack.getOrDefault(DataComponents.BEES, Bees.EMPTY)).bees();
/*  71 */     if (!bees.isEmpty()) {
/*  72 */       return Fraction.ONE;
/*     */     }
/*  74 */     return Fraction.getFraction(1, stack.getMaxStackSize());
/*     */   }
/*     */ 
/*     */   
/*  78 */   public static boolean canItemBeInBundle(ItemStack itemsToAdd) { return (!itemsToAdd.isEmpty() && itemsToAdd.getItem().canFitInsideContainerItems()); }
/*     */ 
/*     */   
/*     */   public int getNumberOfItemsToShow() {
/*  82 */     int numberOfItemStacks = size();
/*  83 */     int availableItemsToShow = (numberOfItemStacks > 12) ? 11 : 12;
/*  84 */     int itemsOnNonFullRow = numberOfItemStacks % 4;
/*  85 */     int emptySpaceOnNonFullRow = (itemsOnNonFullRow == 0) ? 0 : (4 - itemsOnNonFullRow);
/*  86 */     return Math.min(numberOfItemStacks, availableItemsToShow - emptySpaceOnNonFullRow);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public ItemStack getItemUnsafe(int index) { return (ItemStack)this.items.get(index); }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public Stream<ItemStack> itemCopyStream() { return this.items.stream().map(ItemStack::copy); }
/*     */ 
/*     */ 
/*     */   
/* 102 */   public Iterable<ItemStack> items() { return this.items; }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public Iterable<ItemStack> itemsCopy() { return Lists.transform(this.items, ItemStack::copy); }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public int size() { return this.items.size(); }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public Fraction weight() { return this.weight; }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public boolean isEmpty() { return this.items.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public int getSelectedItem() { return this.selectedItem; }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public boolean hasSelectedItem() { return (this.selectedItem != -1); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 131 */     if (this == obj) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (obj instanceof BundleContents) { BundleContents contents = (BundleContents)obj;
/* 135 */       return (this.weight.equals(contents.weight) && ItemStack.listMatches(this.items, contents.items)); }
/*     */     
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public int hashCode() { return ItemStack.hashStackList(this.items); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public String toString() { return "BundleContents" + String.valueOf(this.items); }
/*     */   
/*     */   public static class Mutable
/*     */   {
/*     */     private final List<ItemStack> items;
/*     */     private Fraction weight;
/*     */     private int selectedItem;
/*     */     
/*     */     public Mutable(BundleContents contents) {
/* 156 */       this.items = new ArrayList(contents.items);
/* 157 */       this.weight = contents.weight;
/* 158 */       this.selectedItem = contents.selectedItem;
/*     */     }
/*     */     
/*     */     public Mutable clearItems() {
/* 162 */       this.items.clear();
/* 163 */       this.weight = Fraction.ZERO;
/* 164 */       this.selectedItem = -1;
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     private int findStackIndex(ItemStack itemsToAdd) {
/* 169 */       if (!itemsToAdd.isStackable()) {
/* 170 */         return -1;
/*     */       }
/* 172 */       for (int i = 0; i < this.items.size(); i++) {
/* 173 */         if (ItemStack.isSameItemSameComponents((ItemStack)this.items.get(i), itemsToAdd)) {
/* 174 */           return i;
/*     */         }
/*     */       } 
/* 177 */       return -1;
/*     */     }
/*     */     
/*     */     private int getMaxAmountToAdd(ItemStack item) {
/* 181 */       Fraction remainingWeight = Fraction.ONE.subtract(this.weight);
/* 182 */       return Math.max(remainingWeight.divideBy(BundleContents.getWeight(item)).intValue(), 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int tryInsert(ItemStack itemsToAdd) {
/* 189 */       if (!BundleContents.canItemBeInBundle(itemsToAdd)) {
/* 190 */         return 0;
/*     */       }
/*     */       
/* 193 */       int amountToAdd = Math.min(itemsToAdd.getCount(), getMaxAmountToAdd(itemsToAdd));
/* 194 */       if (amountToAdd == 0) {
/* 195 */         return 0;
/*     */       }
/*     */       
/* 198 */       this.weight = this.weight.add(BundleContents.getWeight(itemsToAdd).multiplyBy(Fraction.getFraction(amountToAdd, 1)));
/*     */       
/* 200 */       int stackIndex = findStackIndex(itemsToAdd);
/* 201 */       if (stackIndex != -1) {
/* 202 */         ItemStack removedStack = (ItemStack)this.items.remove(stackIndex);
/* 203 */         ItemStack mergedStack = removedStack.copyWithCount(removedStack.getCount() + amountToAdd);
/* 204 */         itemsToAdd.shrink(amountToAdd);
/*     */         
/* 206 */         this.items.add(0, mergedStack);
/*     */       } else {
/* 208 */         this.items.add(0, itemsToAdd.split(amountToAdd));
/*     */       } 
/*     */       
/* 211 */       return amountToAdd;
/*     */     }
/*     */     
/*     */     public int tryTransfer(Slot slot, Player player) {
/* 215 */       ItemStack other = slot.getItem();
/* 216 */       int maxAmount = getMaxAmountToAdd(other);
/* 217 */       return BundleContents.canItemBeInBundle(other) ? tryInsert(slot.safeTake(other.getCount(), maxAmount, player)) : 0;
/*     */     }
/*     */ 
/*     */     
/* 221 */     public void toggleSelectedItem(int selectedItem) { this.selectedItem = (this.selectedItem == selectedItem || indexIsOutsideAllowedBounds(selectedItem)) ? -1 : selectedItem; }
/*     */ 
/*     */ 
/*     */     
/* 225 */     private boolean indexIsOutsideAllowedBounds(int selectedItem) { return (selectedItem < 0 || selectedItem >= this.items.size()); }
/*     */ 
/*     */     
/*     */     public ItemStack removeOne() {
/* 229 */       if (this.items.isEmpty()) {
/* 230 */         return null;
/*     */       }
/* 232 */       int removeIndex = indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
/* 233 */       ItemStack stack = ((ItemStack)this.items.remove(removeIndex)).copy();
/* 234 */       this.weight = this.weight.subtract(BundleContents.getWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
/* 235 */       toggleSelectedItem(-1);
/* 236 */       return stack;
/*     */     }
/*     */ 
/*     */     
/* 240 */     public Fraction weight() { return this.weight; }
/*     */ 
/*     */ 
/*     */     
/* 244 */     public BundleContents toImmutable() { return new BundleContents(List.copyOf(this.items), this.weight, this.selectedItem); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\BundleContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */