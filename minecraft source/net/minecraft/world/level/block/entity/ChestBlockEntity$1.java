/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.CompoundContainer;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.ChestBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
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
/*    */ class null
/*    */   extends ContainerOpenersCounter
/*    */ {
/*    */   protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
/* 37 */     Block block = blockState.getBlock(); if (block instanceof ChestBlock) { ChestBlock chestBlock = (ChestBlock)block;
/* 38 */       ChestBlockEntity.playSound(level, pos, blockState, chestBlock.getOpenChestSound()); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onClose(Level level, BlockPos pos, BlockState blockState) {
/* 44 */     Block block = blockState.getBlock(); if (block instanceof ChestBlock) { ChestBlock chestBlock = (ChestBlock)block;
/* 45 */       ChestBlockEntity.playSound(level, pos, blockState, chestBlock.getCloseChestSound()); }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) { ChestBlockEntity.this.signalOpenCount(level, pos, blockState, previous, current); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isOwnContainer(Player player) {
/* 56 */     if (player.containerMenu instanceof ChestMenu) {
/* 57 */       Container container = ((ChestMenu)player.containerMenu).getContainer();
/* 58 */       return (container == ChestBlockEntity.this || (container instanceof CompoundContainer && ((CompoundContainer)container).contains(ChestBlockEntity.this)));
/*    */     } 
/* 60 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ChestBlockEntity$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */