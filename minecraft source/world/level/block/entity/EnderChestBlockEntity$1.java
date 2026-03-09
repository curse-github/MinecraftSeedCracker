/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends ContainerOpenersCounter
/*    */ {
/* 19 */   protected void onOpen(Level level, BlockPos pos, BlockState blockState) { level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected void onClose(Level level, BlockPos pos, BlockState blockState) { level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) { level.blockEvent(EnderChestBlockEntity.this.worldPosition, Blocks.ENDER_CHEST, 1, current); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public boolean isOwnContainer(Player player) { return player.getEnderChestInventory().isActiveChest(EnderChestBlockEntity.this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\EnderChestBlockEntity$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */