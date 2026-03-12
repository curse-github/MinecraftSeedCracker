/*     */ package net.minecraft.world.level.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Callback
/*     */   implements EntityInLevelCallback
/*     */ {
/*     */   private final T entity;
/*     */   private long currentSectionKey;
/*     */   private EntitySection<T> currentSection;
/*     */   
/*     */   private Callback(T entity, long currentSectionKey, EntitySection<T> currentSection) {
/*  47 */     this.entity = entity;
/*  48 */     this.currentSectionKey = currentSectionKey;
/*  49 */     this.currentSection = currentSection;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMove() {
/*  54 */     BlockPos pos = this.entity.blockPosition();
/*  55 */     long newSectionPos = SectionPos.asLong(pos);
/*  56 */     if (newSectionPos != this.currentSectionKey) {
/*  57 */       Visibility previousStatus = this.currentSection.getStatus();
/*  58 */       if (!this.currentSection.remove(this.entity)) {
/*  59 */         PersistentEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (moving to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), Long.valueOf(newSectionPos) });
/*     */       }
/*  61 */       PersistentEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
/*     */       
/*  63 */       EntitySection<T> newSection = PersistentEntitySectionManager.this.sectionStorage.getOrCreateSection(newSectionPos);
/*  64 */       newSection.add(this.entity);
/*  65 */       this.currentSection = newSection;
/*  66 */       this.currentSectionKey = newSectionPos;
/*     */       
/*  68 */       updateStatus(previousStatus, newSection.getStatus());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateStatus(Visibility previousStatus, Visibility newStatus) {
/*  73 */     Visibility effectivePreviousStatus = PersistentEntitySectionManager.getEffectiveStatus(this.entity, previousStatus);
/*  74 */     Visibility effectiveNewStatus = PersistentEntitySectionManager.getEffectiveStatus(this.entity, newStatus);
/*     */     
/*  76 */     if (effectivePreviousStatus == effectiveNewStatus) {
/*  77 */       if (effectiveNewStatus.isAccessible()) {
/*  78 */         PersistentEntitySectionManager.this.callbacks.onSectionChange(this.entity);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  83 */     boolean wasAccessible = effectivePreviousStatus.isAccessible();
/*  84 */     boolean isAccessible = effectiveNewStatus.isAccessible();
/*  85 */     if (wasAccessible && !isAccessible) {
/*  86 */       PersistentEntitySectionManager.this.stopTracking(this.entity);
/*  87 */     } else if (!wasAccessible && isAccessible) {
/*  88 */       PersistentEntitySectionManager.this.startTracking(this.entity);
/*     */     } 
/*     */     
/*  91 */     boolean wasTicking = effectivePreviousStatus.isTicking();
/*  92 */     boolean isTicking = effectiveNewStatus.isTicking();
/*  93 */     if (wasTicking && !isTicking) {
/*  94 */       PersistentEntitySectionManager.this.stopTicking(this.entity);
/*  95 */     } else if (!wasTicking && isTicking) {
/*  96 */       PersistentEntitySectionManager.this.startTicking(this.entity);
/*     */     } 
/*     */     
/*  99 */     if (isAccessible) {
/* 100 */       PersistentEntitySectionManager.this.callbacks.onSectionChange(this.entity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(Entity.RemovalReason reason) {
/* 106 */     if (!this.currentSection.remove(this.entity)) {
/* 107 */       PersistentEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (destroying due to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), reason });
/*     */     }
/*     */     
/* 110 */     Visibility status = PersistentEntitySectionManager.getEffectiveStatus(this.entity, this.currentSection.getStatus());
/* 111 */     if (status.isTicking()) {
/* 112 */       PersistentEntitySectionManager.this.stopTicking(this.entity);
/*     */     }
/* 114 */     if (status.isAccessible()) {
/* 115 */       PersistentEntitySectionManager.this.stopTracking(this.entity);
/*     */     }
/* 117 */     if (reason.shouldDestroy()) {
/* 118 */       PersistentEntitySectionManager.this.callbacks.onDestroyed(this.entity);
/*     */     }
/* 120 */     PersistentEntitySectionManager.this.knownUuids.remove(this.entity.getUUID());
/* 121 */     this.entity.setLevelCallback(NULL);
/*     */     
/* 123 */     PersistentEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\PersistentEntitySectionManager$Callback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */