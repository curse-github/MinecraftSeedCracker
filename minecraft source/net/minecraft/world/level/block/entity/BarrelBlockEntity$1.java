/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ import net.minecraft.world.level.Level;
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
/*    */ class null
/*    */   extends ContainerOpenersCounter
/*    */ {
/*    */   protected void onOpen(Level level, BlockPos pos, BlockState state) {
/* 33 */     BarrelBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
/* 34 */     BarrelBlockEntity.this.updateBlockState(state, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onClose(Level level, BlockPos pos, BlockState state) {
/* 39 */     BarrelBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
/* 40 */     BarrelBlockEntity.this.updateBlockState(state, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) {}
/*    */ 
/*    */   
/*    */   public boolean isOwnContainer(Player player) {
/* 49 */     if (player.containerMenu instanceof ChestMenu) {
/* 50 */       Container container = ((ChestMenu)player.containerMenu).getContainer();
/* 51 */       return (container == BarrelBlockEntity.this);
/*    */     } 
/* 53 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BarrelBlockEntity$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */