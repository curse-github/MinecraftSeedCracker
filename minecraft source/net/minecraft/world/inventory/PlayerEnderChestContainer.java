/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.ItemStackWithSlot;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class PlayerEnderChestContainer
/*    */   extends SimpleContainer
/*    */ {
/*    */   private EnderChestBlockEntity activeChest;
/*    */   
/* 17 */   public PlayerEnderChestContainer() { super(27); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public void setActiveChest(EnderChestBlockEntity activeChest) { this.activeChest = activeChest; }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public boolean isActiveChest(EnderChestBlockEntity chest) { return (this.activeChest == chest); }
/*    */ 
/*    */   
/*    */   public void fromSlots(ValueInput.TypedInputList<ItemStackWithSlot> list) {
/* 29 */     for (int i = 0; i < getContainerSize(); i++) {
/* 30 */       setItem(i, ItemStack.EMPTY);
/*    */     }
/*    */     
/* 33 */     for (ItemStackWithSlot item : list) {
/* 34 */       if (item.isValidInContainer(getContainerSize())) {
/* 35 */         setItem(item.slot(), item.stack());
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void storeAsSlots(ValueOutput.TypedOutputList<ItemStackWithSlot> output) {
/* 41 */     for (int i = 0; i < getContainerSize(); i++) {
/* 42 */       ItemStack itemStack = getItem(i);
/* 43 */       if (!itemStack.isEmpty()) {
/* 44 */         output.add(new ItemStackWithSlot(i, itemStack));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player player) {
/* 51 */     if (this.activeChest != null && !this.activeChest.stillValid(player)) {
/* 52 */       return false;
/*    */     }
/* 54 */     return super.stillValid(player);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startOpen(ContainerUser containerUser) {
/* 59 */     if (this.activeChest != null) {
/* 60 */       this.activeChest.startOpen(containerUser);
/*    */     }
/* 62 */     super.startOpen(containerUser);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopOpen(ContainerUser containerUser) {
/* 67 */     if (this.activeChest != null) {
/* 68 */       this.activeChest.stopOpen(containerUser);
/*    */     }
/* 70 */     super.stopOpen(containerUser);
/* 71 */     this.activeChest = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\PlayerEnderChestContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */