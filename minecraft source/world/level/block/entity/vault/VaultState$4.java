/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
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
/*    */ 
/*    */ 
/*    */ static enum null
/*    */ {
/* 41 */   protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) { serverLevel.playSound(null, pos, SoundEvents.VAULT_OPEN_SHUTTER, SoundSource.BLOCKS); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   protected void onExit(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData) { serverLevel.playSound(null, pos, SoundEvents.VAULT_CLOSE_SHUTTER, SoundSource.BLOCKS); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultState$4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */