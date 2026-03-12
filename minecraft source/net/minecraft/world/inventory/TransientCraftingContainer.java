/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class TransientCraftingContainer
/*     */   implements CraftingContainer
/*     */ {
/*     */   private final NonNullList<ItemStack> items;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final AbstractContainerMenu menu;
/*     */   
/*  18 */   public TransientCraftingContainer(AbstractContainerMenu menu, int width, int height) { this(menu, width, height, NonNullList.withSize(width * height, ItemStack.EMPTY)); }
/*     */ 
/*     */   
/*     */   private TransientCraftingContainer(AbstractContainerMenu menu, int width, int height, NonNullList<ItemStack> items) {
/*  22 */     this.items = items;
/*  23 */     this.menu = menu;
/*  24 */     this.width = width;
/*  25 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  30 */   public int getContainerSize() { return this.items.size(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  35 */     for (ItemStack itemStack : this.items) {
/*  36 */       if (!itemStack.isEmpty()) {
/*  37 */         return false;
/*     */       }
/*     */     } 
/*  40 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  45 */     if (slot >= getContainerSize()) {
/*  46 */       return ItemStack.EMPTY;
/*     */     }
/*  48 */     return (ItemStack)this.items.get(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public ItemStack removeItemNoUpdate(int slot) { return ContainerHelper.takeItem(this.items, slot); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  58 */     ItemStack result = ContainerHelper.removeItem(this.items, slot, count);
/*  59 */     if (!result.isEmpty()) {
/*  60 */       this.menu.slotsChanged(this);
/*     */     }
/*  62 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/*  67 */     this.items.set(slot, itemStack);
/*  68 */     this.menu.slotsChanged(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */ 
/*     */   
/*  77 */   public boolean stillValid(Player player) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public void clearContent() { this.items.clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public int getHeight() { return this.height; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public int getWidth() { return this.width; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public List<ItemStack> getItems() { return List.copyOf(this.items); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedItemContents contents) {
/* 102 */     for (ItemStack itemStack : this.items)
/* 103 */       contents.accountSimpleStack(itemStack); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\TransientCraftingContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */