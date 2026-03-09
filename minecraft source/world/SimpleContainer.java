/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.StackedContentsCompatible;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SimpleContainer
/*     */   implements Container, StackedContentsCompatible
/*     */ {
/*     */   private final int size;
/*     */   private final NonNullList<ItemStack> items;
/*     */   private List<ContainerListener> listeners;
/*     */   
/*     */   public SimpleContainer(int size) {
/*  23 */     this.size = size;
/*  24 */     this.items = NonNullList.withSize(size, ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public SimpleContainer(ItemStack... itemstacks) {
/*  28 */     this.size = itemstacks.length;
/*  29 */     this.items = NonNullList.of(ItemStack.EMPTY, itemstacks);
/*     */   }
/*     */   
/*     */   public void addListener(ContainerListener listener) {
/*  33 */     if (this.listeners == null) {
/*  34 */       this.listeners = Lists.newArrayList();
/*     */     }
/*  36 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeListener(ContainerListener listener) {
/*  40 */     if (this.listeners != null) {
/*  41 */       this.listeners.remove(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  47 */     if (slot < 0 || slot >= this.items.size()) {
/*  48 */       return ItemStack.EMPTY;
/*     */     }
/*  50 */     return (ItemStack)this.items.get(slot);
/*     */   }
/*     */   
/*     */   public List<ItemStack> removeAllItems() {
/*  54 */     List<ItemStack> itemsRemoved = (List)this.items.stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());
/*  55 */     clearContent();
/*  56 */     return itemsRemoved;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  61 */     ItemStack result = ContainerHelper.removeItem(this.items, slot, count);
/*  62 */     if (!result.isEmpty()) {
/*  63 */       setChanged();
/*     */     }
/*  65 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItemType(Item itemType, int count) {
/*  73 */     ItemStack removed = new ItemStack(itemType, 0);
/*     */     
/*  75 */     for (int slot = this.size - 1; slot >= 0; slot--) {
/*  76 */       ItemStack current = getItem(slot);
/*  77 */       if (current.getItem().equals(itemType)) {
/*  78 */         int stillNeeded = count - removed.getCount();
/*  79 */         ItemStack removedFromThisSlot = current.split(stillNeeded);
/*  80 */         removed.grow(removedFromThisSlot.getCount());
/*  81 */         if (removed.getCount() == count) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*  86 */     if (!removed.isEmpty()) {
/*  87 */       setChanged();
/*     */     }
/*  89 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack addItem(ItemStack itemStack) {
/*  97 */     if (itemStack.isEmpty()) {
/*  98 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 101 */     ItemStack remainingItems = itemStack.copy();
/*     */     
/* 103 */     moveItemToOccupiedSlotsWithSameType(remainingItems);
/* 104 */     if (remainingItems.isEmpty()) {
/* 105 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 108 */     moveItemToEmptySlots(remainingItems);
/* 109 */     if (remainingItems.isEmpty()) {
/* 110 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 113 */     return remainingItems;
/*     */   }
/*     */   
/*     */   public boolean canAddItem(ItemStack itemStack) {
/* 117 */     boolean hasSpace = false;
/* 118 */     for (ItemStack targetStack : this.items) {
/* 119 */       if (targetStack.isEmpty() || (ItemStack.isSameItemSameComponents(targetStack, itemStack) && targetStack.getCount() < targetStack.getMaxStackSize())) {
/* 120 */         hasSpace = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 124 */     return hasSpace;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/* 129 */     ItemStack itemStack = (ItemStack)this.items.get(slot);
/* 130 */     if (itemStack.isEmpty()) {
/* 131 */       return ItemStack.EMPTY;
/*     */     }
/* 133 */     this.items.set(slot, ItemStack.EMPTY);
/* 134 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 139 */     this.items.set(slot, itemStack);
/* 140 */     itemStack.limitSize(getMaxStackSize(itemStack));
/* 141 */     setChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public int getContainerSize() { return this.size; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 151 */     for (ItemStack itemStack : this.items) {
/* 152 */       if (!itemStack.isEmpty()) {
/* 153 */         return false;
/*     */       }
/*     */     } 
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/* 161 */     if (this.listeners != null) {
/* 162 */       for (ContainerListener listener : this.listeners) {
/* 163 */         listener.containerChanged(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 170 */   public boolean stillValid(Player player) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 175 */     this.items.clear();
/* 176 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedItemContents contents) {
/* 181 */     for (ItemStack itemStack : this.items) {
/* 182 */       contents.accountStack(itemStack);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public String toString() { return ((List)this.items.stream()
/* 189 */       .filter(item -> !item.isEmpty())
/* 190 */       .collect(Collectors.toList()))
/* 191 */       .toString(); }
/*     */ 
/*     */   
/*     */   private void moveItemToEmptySlots(ItemStack sourceStack) {
/* 195 */     for (int slot = 0; slot < this.size; slot++) {
/* 196 */       ItemStack targetStack = getItem(slot);
/* 197 */       if (targetStack.isEmpty()) {
/* 198 */         setItem(slot, sourceStack.copyAndClear());
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveItemToOccupiedSlotsWithSameType(ItemStack sourceStack) {
/* 205 */     for (int slot = 0; slot < this.size; slot++) {
/* 206 */       ItemStack targetStack = getItem(slot);
/* 207 */       if (ItemStack.isSameItemSameComponents(targetStack, sourceStack)) {
/* 208 */         moveItemsBetweenStacks(sourceStack, targetStack);
/* 209 */         if (sourceStack.isEmpty()) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void moveItemsBetweenStacks(ItemStack sourceStack, ItemStack targetStack) {
/* 220 */     int maxCount = getMaxStackSize(targetStack);
/* 221 */     int diff = Math.min(sourceStack.getCount(), maxCount - targetStack.getCount());
/* 222 */     if (diff > 0) {
/* 223 */       targetStack.grow(diff);
/* 224 */       sourceStack.shrink(diff);
/* 225 */       setChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fromItemList(ValueInput.TypedInputList<ItemStack> items) {
/* 230 */     clearContent();
/*     */     
/* 232 */     for (ItemStack stack : items) {
/* 233 */       addItem(stack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void storeAsItemList(ValueOutput.TypedOutputList<ItemStack> output) {
/* 238 */     for (int i = 0; i < getContainerSize(); i++) {
/* 239 */       ItemStack itemStack = getItem(i);
/* 240 */       if (!itemStack.isEmpty()) {
/* 241 */         output.add(itemStack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 247 */   public NonNullList<ItemStack> getItems() { return this.items; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\SimpleContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */