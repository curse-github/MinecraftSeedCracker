/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class CompoundContainer implements Container {
/*     */   private final Container container1;
/*     */   private final Container container2;
/*     */   
/*     */   public CompoundContainer(Container container1, Container container2) {
/*  12 */     this.container1 = container1;
/*  13 */     this.container2 = container2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  18 */   public int getContainerSize() { return this.container1.getContainerSize() + this.container2.getContainerSize(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   public boolean isEmpty() { return (this.container1.isEmpty() && this.container2.isEmpty()); }
/*     */ 
/*     */ 
/*     */   
/*  27 */   public boolean contains(Container container) { return (this.container1 == container || this.container2 == container); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  32 */     if (slot >= this.container1.getContainerSize()) {
/*  33 */       return this.container2.getItem(slot - this.container1.getContainerSize());
/*     */     }
/*  35 */     return this.container1.getItem(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  41 */     if (slot >= this.container1.getContainerSize()) {
/*  42 */       return this.container2.removeItem(slot - this.container1.getContainerSize(), count);
/*     */     }
/*  44 */     return this.container1.removeItem(slot, count);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/*  50 */     if (slot >= this.container1.getContainerSize()) {
/*  51 */       return this.container2.removeItemNoUpdate(slot - this.container1.getContainerSize());
/*     */     }
/*  53 */     return this.container1.removeItemNoUpdate(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/*  59 */     if (slot >= this.container1.getContainerSize()) {
/*  60 */       this.container2.setItem(slot - this.container1.getContainerSize(), itemStack);
/*     */     } else {
/*  62 */       this.container1.setItem(slot, itemStack);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public int getMaxStackSize() { return this.container1.getMaxStackSize(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {
/*  73 */     this.container1.setChanged();
/*  74 */     this.container2.setChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public boolean stillValid(Player player) { return (this.container1.stillValid(player) && this.container2.stillValid(player)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startOpen(ContainerUser containerUser) {
/*  84 */     this.container1.startOpen(containerUser);
/*  85 */     this.container2.startOpen(containerUser);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(ContainerUser containerUser) {
/*  90 */     this.container1.stopOpen(containerUser);
/*  91 */     this.container2.stopOpen(containerUser);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/*  96 */     if (slot >= this.container1.getContainerSize()) {
/*  97 */       return this.container2.canPlaceItem(slot - this.container1.getContainerSize(), itemStack);
/*     */     }
/*  99 */     return this.container1.canPlaceItem(slot, itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 105 */     this.container1.clearContent();
/* 106 */     this.container2.clearContent();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\CompoundContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */