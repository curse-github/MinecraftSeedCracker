/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ static enum null
/*    */ {
/*    */   protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {
/* 19 */     sharedData.setDisplayItem(ItemStack.EMPTY);
/* 20 */     serverLevel.levelEvent(3016, pos, isOminous ? 1 : 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultState$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */