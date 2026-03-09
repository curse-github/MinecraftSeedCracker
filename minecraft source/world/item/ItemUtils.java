/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemUtils
/*    */ {
/*    */   public static InteractionResult startUsingInstantly(Level level, Player player, InteractionHand hand) {
/* 17 */     player.startUsingItem(hand);
/* 18 */     return InteractionResult.CONSUME;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ItemStack createFilledResult(ItemStack itemStack, Player player, ItemStack newItemStack, boolean limitCreativeStackSize) {
/* 27 */     boolean isCreative = player.hasInfiniteMaterials();
/* 28 */     if (limitCreativeStackSize && isCreative) {
/* 29 */       if (!player.getInventory().contains(newItemStack)) {
/* 30 */         player.getInventory().add(newItemStack);
/*    */       }
/* 32 */       return itemStack;
/*    */     } 
/*    */     
/* 35 */     itemStack.consume(1, player);
/* 36 */     if (itemStack.isEmpty()) {
/* 37 */       return newItemStack;
/*    */     }
/* 39 */     if (!player.getInventory().add(newItemStack)) {
/* 40 */       player.drop(newItemStack, false);
/*    */     }
/* 42 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/* 46 */   public static ItemStack createFilledResult(ItemStack itemStack, Player player, ItemStack newItemStack) { return createFilledResult(itemStack, player, newItemStack, true); }
/*    */ 
/*    */   
/*    */   public static void onContainerDestroyed(ItemEntity container, Iterable<ItemStack> contents) {
/* 50 */     Level level = container.level();
/* 51 */     if (level.isClientSide()) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     contents.forEach(stack -> level.addFreshEntity(new ItemEntity(level, container.getX(), container.getY(), container.getZ(), stack)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */