/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Callback
/*    */   implements EntityInLevelCallback
/*    */ {
/*    */   private final T entity;
/*    */   private long currentSectionKey;
/*    */   private EntitySection<T> currentSection;
/*    */   
/*    */   private Callback(T entity, long currentSectionKey, EntitySection<T> currentSection) {
/* 22 */     this.entity = entity;
/* 23 */     this.currentSectionKey = currentSectionKey;
/* 24 */     this.currentSection = currentSection;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMove() {
/* 29 */     BlockPos pos = this.entity.blockPosition();
/* 30 */     long newSectionPos = SectionPos.asLong(pos);
/* 31 */     if (newSectionPos != this.currentSectionKey) {
/* 32 */       Visibility previousStatus = this.currentSection.getStatus();
/* 33 */       if (!this.currentSection.remove(this.entity)) {
/* 34 */         TransientEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (moving to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), Long.valueOf(newSectionPos) });
/*    */       }
/* 36 */       TransientEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
/*    */       
/* 38 */       EntitySection<T> newSection = TransientEntitySectionManager.this.sectionStorage.getOrCreateSection(newSectionPos);
/* 39 */       newSection.add(this.entity);
/* 40 */       this.currentSection = newSection;
/* 41 */       this.currentSectionKey = newSectionPos;
/*    */       
/* 43 */       TransientEntitySectionManager.this.callbacks.onSectionChange(this.entity);
/*    */       
/* 45 */       if (!this.entity.isAlwaysTicking()) {
/* 46 */         boolean wasTicking = previousStatus.isTicking();
/* 47 */         boolean isTicking = newSection.getStatus().isTicking();
/* 48 */         if (wasTicking && !isTicking) {
/* 49 */           TransientEntitySectionManager.this.callbacks.onTickingEnd(this.entity);
/* 50 */         } else if (!wasTicking && isTicking) {
/* 51 */           TransientEntitySectionManager.this.callbacks.onTickingStart(this.entity);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRemove(Entity.RemovalReason reason) {
/* 59 */     if (!this.currentSection.remove(this.entity)) {
/* 60 */       TransientEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (destroying due to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), reason });
/*    */     }
/* 62 */     Visibility status = this.currentSection.getStatus();
/* 63 */     if (status.isTicking() || this.entity.isAlwaysTicking()) {
/* 64 */       TransientEntitySectionManager.this.callbacks.onTickingEnd(this.entity);
/*    */     }
/* 66 */     TransientEntitySectionManager.this.callbacks.onTrackingEnd(this.entity);
/* 67 */     TransientEntitySectionManager.this.callbacks.onDestroyed(this.entity);
/* 68 */     TransientEntitySectionManager.this.entityStorage.remove(this.entity);
/* 69 */     this.entity.setLevelCallback(NULL);
/*    */     
/* 71 */     TransientEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\TransientEntitySectionManager$Callback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */