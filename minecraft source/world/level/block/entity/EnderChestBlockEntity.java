/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class EnderChestBlockEntity
/*    */   extends BlockEntity implements LidBlockEntity {
/* 15 */   private final ChestLidController chestLidController = new ChestLidController();
/* 16 */   private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter()
/*    */     {
/*    */       protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
/* 19 */         level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 24 */       protected void onClose(Level level, BlockPos pos, BlockState blockState) { level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F); }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 29 */       protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) { level.blockEvent(EnderChestBlockEntity.this.worldPosition, Blocks.ENDER_CHEST, 1, current); }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 34 */       public boolean isOwnContainer(Player player) { return player.getEnderChestInventory().isActiveChest(EnderChestBlockEntity.this); }
/*    */     };
/*    */ 
/*    */ 
/*    */   
/* 39 */   public EnderChestBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.ENDER_CHEST, worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, EnderChestBlockEntity entity) { entity.chestLidController.tickLid(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean triggerEvent(int b0, int b1) {
/* 48 */     if (b0 == 1) {
/* 49 */       this.chestLidController.shouldBeOpen((b1 > 0));
/* 50 */       return true;
/*    */     } 
/* 52 */     return super.triggerEvent(b0, b1);
/*    */   }
/*    */   
/*    */   public void startOpen(ContainerUser containerUser) {
/* 56 */     if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
/* 57 */       this.openersCounter.incrementOpeners(containerUser.getLivingEntity(), getLevel(), getBlockPos(), getBlockState(), containerUser.getContainerInteractionRange());
/*    */     }
/*    */   }
/*    */   
/*    */   public void stopOpen(ContainerUser containerUser) {
/* 62 */     if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
/* 63 */       this.openersCounter.decrementOpeners(containerUser.getLivingEntity(), getLevel(), getBlockPos(), getBlockState());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 68 */   public boolean stillValid(Player player) { return Container.stillValidBlockEntity(this, player); }
/*    */ 
/*    */   
/*    */   public void recheckOpen() {
/* 72 */     if (!this.remove) {
/* 73 */       this.openersCounter.recheckOpeners(getLevel(), getBlockPos(), getBlockState());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public float getOpenNess(float a) { return this.chestLidController.getOpenness(a); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\EnderChestBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */