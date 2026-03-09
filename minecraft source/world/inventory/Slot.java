/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Slot
/*     */ {
/*     */   private final int slot;
/*     */   public final Container container;
/*     */   public int index;
/*     */   public final int x;
/*     */   public final int y;
/*     */   
/*     */   public Slot(Container container, int slot, int x, int y) {
/*  20 */     this.container = container;
/*  21 */     this.slot = slot;
/*  22 */     this.x = x;
/*  23 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void onQuickCraft(ItemStack picked, ItemStack original) {
/*  27 */     int count = original.getCount() - picked.getCount();
/*  28 */     if (count > 0) {
/*  29 */       onQuickCraft(original, count);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onQuickCraft(ItemStack picked, int count) {}
/*     */ 
/*     */   
/*     */   protected void onSwapCraft(int count) {}
/*     */ 
/*     */   
/*     */   protected void checkTakeAchievements(ItemStack carried) {}
/*     */ 
/*     */   
/*  44 */   public void onTake(Player player, ItemStack carried) { setChanged(); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public boolean mayPlace(ItemStack itemStack) { return true; }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public ItemStack getItem() { return this.container.getItem(this.slot); }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public boolean hasItem() { return !getItem().isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public void setByPlayer(ItemStack itemStack) { setByPlayer(itemStack, getItem()); }
/*     */ 
/*     */   
/*  65 */   public void setByPlayer(ItemStack itemStack, ItemStack previous) { set(itemStack); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(ItemStack itemStack) {
/*  74 */     this.container.setItem(this.slot, itemStack);
/*  75 */     setChanged();
/*     */   }
/*     */ 
/*     */   
/*  79 */   public void setChanged() { this.container.setChanged(); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public int getMaxStackSize() { return this.container.getMaxStackSize(); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public int getMaxStackSize(ItemStack itemStack) { return Math.min(getMaxStackSize(), itemStack.getMaxStackSize()); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Identifier getNoItemIcon() { return null; }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public ItemStack remove(int amount) { return this.container.removeItem(this.slot, amount); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public boolean mayPickup(Player player) { return true; }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean isActive() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<ItemStack> tryRemove(int amount, int maxAmount, Player player) {
/* 108 */     if (!mayPickup(player)) {
/* 109 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 113 */     if (!allowModification(player) && maxAmount < getItem().getCount()) {
/* 114 */       return Optional.empty();
/*     */     }
/*     */     
/* 117 */     amount = Math.min(amount, maxAmount);
/* 118 */     ItemStack result = remove(amount);
/* 119 */     if (result.isEmpty()) {
/* 120 */       return Optional.empty();
/*     */     }
/* 122 */     if (getItem().isEmpty()) {
/* 123 */       setByPlayer(ItemStack.EMPTY, result);
/*     */     }
/* 125 */     return Optional.of(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack safeTake(int amount, int maxAmount, Player player) {
/* 134 */     Optional<ItemStack> result = tryRemove(amount, maxAmount, player);
/* 135 */     result.ifPresent(item -> onTake(player, item));
/* 136 */     return (ItemStack)result.orElse(ItemStack.EMPTY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public ItemStack safeInsert(ItemStack stack) { return safeInsert(stack, stack.getCount()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack safeInsert(ItemStack inputStack, int inputAmount) {
/* 150 */     if (inputStack.isEmpty() || !mayPlace(inputStack)) {
/* 151 */       return inputStack;
/*     */     }
/*     */     
/* 154 */     ItemStack slotStack = getItem();
/* 155 */     int transferableItemCount = Math.min(Math.min(inputAmount, inputStack.getCount()), getMaxStackSize(inputStack) - slotStack.getCount());
/*     */     
/* 157 */     if (transferableItemCount <= 0)
/* 158 */       return inputStack; 
/* 159 */     if (slotStack.isEmpty()) {
/* 160 */       setByPlayer(inputStack.split(transferableItemCount));
/* 161 */     } else if (ItemStack.isSameItemSameComponents(slotStack, inputStack)) {
/* 162 */       inputStack.shrink(transferableItemCount);
/* 163 */       slotStack.grow(transferableItemCount);
/*     */       
/* 165 */       setByPlayer(slotStack);
/*     */     } 
/* 167 */     return inputStack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public boolean allowModification(Player player) { return (mayPickup(player) && mayPlace(getItem())); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public int getContainerSlot() { return this.slot; }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public boolean isHighlightable() { return true; }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public boolean isFake() { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\Slot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */