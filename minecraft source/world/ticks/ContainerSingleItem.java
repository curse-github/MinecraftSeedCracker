/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public interface ContainerSingleItem
/*    */   extends Container {
/*    */   ItemStack getTheItem();
/*    */   
/* 12 */   default ItemStack splitTheItem(int count) { return getTheItem().split(count); }
/*    */ 
/*    */   
/*    */   void setTheItem(ItemStack paramItemStack);
/*    */ 
/*    */   
/* 18 */   default ItemStack removeTheItem() { return splitTheItem(getMaxStackSize()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   default int getContainerSize() { return 1; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   default boolean isEmpty() { return getTheItem().isEmpty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   default void clearContent() { removeTheItem(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   default ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, getMaxStackSize()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   default ItemStack getItem(int slot) { return (slot == 0) ? getTheItem() : ItemStack.EMPTY; }
/*    */ 
/*    */ 
/*    */   
/*    */   default ItemStack removeItem(int slot, int count) {
/* 48 */     if (slot != 0) {
/* 49 */       return ItemStack.EMPTY;
/*    */     }
/* 51 */     return splitTheItem(count);
/*    */   }
/*    */ 
/*    */   
/*    */   default void setItem(int slot, ItemStack itemStack) {
/* 56 */     if (slot == 0) {
/* 57 */       setTheItem(itemStack);
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface BlockContainerSingleItem
/*    */     extends ContainerSingleItem
/*    */   {
/*    */     BlockEntity getContainerBlockEntity();
/*    */     
/* 66 */     default boolean stillValid(Player player) { return Container.stillValidBlockEntity(getContainerBlockEntity(), player); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\ContainerSingleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */