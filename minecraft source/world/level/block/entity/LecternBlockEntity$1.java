/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements Container
/*     */ {
/*  43 */   public int getContainerSize() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public boolean isEmpty() { return LecternBlockEntity.this.book.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public ItemStack getItem(int slot) { return (slot == 0) ? LecternBlockEntity.this.book : ItemStack.EMPTY; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  58 */     if (slot == 0) {
/*  59 */       ItemStack result = LecternBlockEntity.this.book.split(count);
/*  60 */       if (LecternBlockEntity.this.book.isEmpty()) {
/*  61 */         LecternBlockEntity.this.onBookItemRemove();
/*     */       }
/*  63 */       return result;
/*     */     } 
/*  65 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/*  70 */     if (slot == 0) {
/*  71 */       ItemStack prev = LecternBlockEntity.this.book;
/*  72 */       LecternBlockEntity.this.book = ItemStack.EMPTY;
/*  73 */       LecternBlockEntity.this.onBookItemRemove();
/*  74 */       return prev;
/*     */     } 
/*  76 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {}
/*     */ 
/*     */ 
/*     */   
/*  86 */   public int getMaxStackSize() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public void setChanged() { LecternBlockEntity.this.setChanged(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public boolean stillValid(Player player) { return (Container.stillValidBlockEntity(LecternBlockEntity.this, player) && LecternBlockEntity.this.hasBook()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public boolean canPlaceItem(int slot, ItemStack itemStack) { return false; }
/*     */   
/*     */   public void clearContent() {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\LecternBlockEntity$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */