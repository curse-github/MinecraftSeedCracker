/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.ContainerHelper;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public interface ListBackedContainer
/*    */   extends Container
/*    */ {
/*    */   NonNullList<ItemStack> getItems();
/*    */   
/* 15 */   default int count() { return (int)getItems().stream().filter(Predicate.not(ItemStack::isEmpty)).count(); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   default int getContainerSize() { return getItems().size(); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   default void clearContent() { getItems().clear(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   default boolean isEmpty() { return getItems().stream().allMatch(ItemStack::isEmpty); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   default ItemStack getItem(int slot) { return (ItemStack)getItems().get(slot); }
/*    */ 
/*    */ 
/*    */   
/*    */   default ItemStack removeItem(int slot, int count) {
/* 38 */     ItemStack result = ContainerHelper.removeItem(getItems(), slot, count);
/* 39 */     if (!result.isEmpty()) {
/* 40 */       setChanged();
/*    */     }
/* 42 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 47 */   default ItemStack removeItemNoUpdate(int slot) { return ContainerHelper.removeItem(getItems(), slot, getMaxStackSize()); }
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 52 */     return (acceptsItemType(itemStack) && (
/* 53 */       getItem(slot).isEmpty() || getItem(slot).getCount() < getMaxStackSize(itemStack)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   default boolean acceptsItemType(ItemStack itemStack) { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   default void setItem(int slot, ItemStack itemStack) {
/* 63 */     setItemNoUpdate(slot, itemStack);
/* 64 */     setChanged();
/*    */   }
/*    */   
/*    */   default void setItemNoUpdate(int slot, ItemStack itemStack) {
/* 68 */     getItems().set(slot, itemStack);
/* 69 */     itemStack.limitSize(getMaxStackSize(itemStack));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ListBackedContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */