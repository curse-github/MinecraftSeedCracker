/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
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
/*    */ public abstract class ContainerOpenersCounter
/*    */ {
/*    */   private static final int CHECK_TICK_DELAY = 5;
/*    */   private int openCount;
/*    */   private double maxInteractionRange;
/*    */   
/*    */   public void incrementOpeners(LivingEntity entity, Level level, BlockPos pos, BlockState blockState, double maxInteractionRange) {
/* 32 */     int previous = this.openCount++;
/* 33 */     if (previous == 0) {
/* 34 */       onOpen(level, pos, blockState);
/* 35 */       level.gameEvent(entity, GameEvent.CONTAINER_OPEN, pos);
/* 36 */       scheduleRecheck(level, pos, blockState);
/*    */     } 
/* 38 */     openerCountChanged(level, pos, blockState, previous, this.openCount);
/* 39 */     this.maxInteractionRange = Math.max(maxInteractionRange, this.maxInteractionRange);
/*    */   }
/*    */   
/*    */   public void decrementOpeners(LivingEntity entity, Level level, BlockPos pos, BlockState blockState) {
/* 43 */     int previous = this.openCount--;
/* 44 */     if (this.openCount == 0) {
/* 45 */       onClose(level, pos, blockState);
/* 46 */       level.gameEvent(entity, GameEvent.CONTAINER_CLOSE, pos);
/* 47 */       this.maxInteractionRange = 0.0D;
/*    */     } 
/* 49 */     openerCountChanged(level, pos, blockState, previous, this.openCount);
/*    */   }
/*    */   
/*    */   public List<ContainerUser> getEntitiesWithContainerOpen(Level level, BlockPos pos) {
/* 53 */     double range = this.maxInteractionRange + 4.0D;
/* 54 */     AABB searchBox = (new AABB(pos)).inflate(range);
/* 55 */     return (List)level.getEntities((Entity)null, searchBox, entity -> hasContainerOpen(entity, pos)).stream()
/* 56 */       .map(entity -> (ContainerUser)entity)
/* 57 */       .collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   private boolean hasContainerOpen(Entity entity, BlockPos blockPos) {
/* 61 */     if (entity instanceof ContainerUser) { ContainerUser containerUser = (ContainerUser)entity; if (!containerUser.getLivingEntity().isSpectator())
/* 62 */         return containerUser.hasContainerOpen(this, blockPos);  }
/*    */     
/* 64 */     return false;
/*    */   }
/*    */   
/*    */   public void recheckOpeners(Level level, BlockPos pos, BlockState blockState) {
/* 68 */     List<ContainerUser> containerUsers = getEntitiesWithContainerOpen(level, pos);
/*    */     
/* 70 */     this.maxInteractionRange = 0.0D;
/* 71 */     for (ContainerUser containerUser : containerUsers) {
/* 72 */       this.maxInteractionRange = Math.max(containerUser.getContainerInteractionRange(), this.maxInteractionRange);
/*    */     }
/* 74 */     int openCount = containerUsers.size();
/* 75 */     int prevCount = this.openCount;
/* 76 */     if (prevCount != openCount) {
/* 77 */       boolean isOpen = (openCount != 0);
/* 78 */       boolean wasOpen = (prevCount != 0);
/* 79 */       if (isOpen && !wasOpen) {
/* 80 */         onOpen(level, pos, blockState);
/* 81 */         level.gameEvent(null, GameEvent.CONTAINER_OPEN, pos);
/* 82 */       } else if (!isOpen) {
/* 83 */         onClose(level, pos, blockState);
/* 84 */         level.gameEvent(null, GameEvent.CONTAINER_CLOSE, pos);
/*    */       } 
/* 86 */       this.openCount = openCount;
/*    */     } 
/* 88 */     openerCountChanged(level, pos, blockState, prevCount, openCount);
/* 89 */     if (openCount > 0) {
/* 90 */       scheduleRecheck(level, pos, blockState);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 95 */   public int getOpenerCount() { return this.openCount; }
/*    */ 
/*    */ 
/*    */   
/* 99 */   private static void scheduleRecheck(Level level, BlockPos blockPos, BlockState blockState) { level.scheduleTick(blockPos, blockState.getBlock(), 5); }
/*    */   
/*    */   protected abstract void onOpen(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   protected abstract void onClose(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   protected abstract void openerCountChanged(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract boolean isOwnContainer(Player paramPlayer);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ContainerOpenersCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */