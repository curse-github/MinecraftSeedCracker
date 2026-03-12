/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ 
/*    */ public interface InventoryCarrier
/*    */ {
/*    */   public static final String TAG_INVENTORY = "Inventory";
/*    */   
/*    */   static void pickUpItem(ServerLevel level, Mob mob, InventoryCarrier inventoryCarrier, ItemEntity itemEntity) {
/* 17 */     ItemStack itemStack = itemEntity.getItem();
/* 18 */     if (mob.wantsToPickUp(level, itemStack)) {
/* 19 */       SimpleContainer inventory = inventoryCarrier.getInventory();
/* 20 */       boolean hasSpace = inventory.canAddItem(itemStack);
/* 21 */       if (!hasSpace) {
/*    */         return;
/*    */       }
/*    */       
/* 25 */       mob.onItemPickup(itemEntity);
/* 26 */       int count = itemStack.getCount();
/* 27 */       ItemStack remainder = inventory.addItem(itemStack);
/* 28 */       mob.take(itemEntity, count - remainder.getCount());
/* 29 */       if (remainder.isEmpty()) {
/* 30 */         itemEntity.discard();
/*    */       } else {
/* 32 */         itemStack.setCount(remainder.getCount());
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   default void readInventoryFromTag(ValueInput input) {
/* 38 */     input.list("Inventory", ItemStack.CODEC).ifPresent(list -> 
/* 39 */         getInventory().fromItemList(list));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   default void writeInventoryToTag(ValueOutput output) { getInventory().storeAsItemList(output.list("Inventory", ItemStack.CODEC)); }
/*    */   
/*    */   SimpleContainer getInventory();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\InventoryCarrier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */