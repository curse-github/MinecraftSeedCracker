/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
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
/*     */   implements SlotAccess
/*     */ {
/* 129 */   public ItemStack get() { return AbstractChestedHorse.this.hasChest() ? new ItemStack(Items.CHEST) : ItemStack.EMPTY; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean set(ItemStack itemStack) {
/* 134 */     if (itemStack.isEmpty()) {
/* 135 */       if (AbstractChestedHorse.this.hasChest()) {
/* 136 */         AbstractChestedHorse.this.setChest(false);
/* 137 */         AbstractChestedHorse.this.createInventory();
/*     */       } 
/* 139 */       return true;
/*     */     } 
/* 141 */     if (itemStack.is(Items.CHEST)) {
/* 142 */       if (!AbstractChestedHorse.this.hasChest()) {
/* 143 */         AbstractChestedHorse.this.setChest(true);
/* 144 */         AbstractChestedHorse.this.createInventory();
/*     */       } 
/* 146 */       return true;
/*     */     } 
/* 148 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\AbstractChestedHorse$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */