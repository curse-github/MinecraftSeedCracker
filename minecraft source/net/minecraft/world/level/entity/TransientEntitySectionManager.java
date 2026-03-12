/*     */ package net.minecraft.world.level.entity;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TransientEntitySectionManager<T extends EntityAccess> extends Object {
/*  14 */   private static final Logger LOGGER = LogUtils.getLogger(); private final LevelCallback<T> callbacks; private final EntityLookup<T> entityStorage;
/*     */   private final EntitySectionStorage<T> sectionStorage;
/*     */   private final LongSet tickingChunks;
/*     */   private final LevelEntityGetter<T> entityGetter;
/*     */   
/*     */   private class Callback implements EntityInLevelCallback { private final T entity;
/*     */     
/*     */     private Callback(T entity, long currentSectionKey, EntitySection<T> currentSection) {
/*  22 */       this.entity = entity;
/*  23 */       this.currentSectionKey = currentSectionKey;
/*  24 */       this.currentSection = currentSection;
/*     */     }
/*     */     private long currentSectionKey; private EntitySection<T> currentSection;
/*     */     
/*     */     public void onMove() {
/*  29 */       BlockPos pos = this.entity.blockPosition();
/*  30 */       long newSectionPos = SectionPos.asLong(pos);
/*  31 */       if (newSectionPos != this.currentSectionKey) {
/*  32 */         Visibility previousStatus = this.currentSection.getStatus();
/*  33 */         if (!this.currentSection.remove(this.entity)) {
/*  34 */           TransientEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (moving to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), Long.valueOf(newSectionPos) });
/*     */         }
/*  36 */         TransientEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
/*     */         
/*  38 */         EntitySection<T> newSection = TransientEntitySectionManager.this.sectionStorage.getOrCreateSection(newSectionPos);
/*  39 */         newSection.add(this.entity);
/*  40 */         this.currentSection = newSection;
/*  41 */         this.currentSectionKey = newSectionPos;
/*     */         
/*  43 */         TransientEntitySectionManager.this.callbacks.onSectionChange(this.entity);
/*     */         
/*  45 */         if (!this.entity.isAlwaysTicking()) {
/*  46 */           boolean wasTicking = previousStatus.isTicking();
/*  47 */           boolean isTicking = newSection.getStatus().isTicking();
/*  48 */           if (wasTicking && !isTicking) {
/*  49 */             TransientEntitySectionManager.this.callbacks.onTickingEnd(this.entity);
/*  50 */           } else if (!wasTicking && isTicking) {
/*  51 */             TransientEntitySectionManager.this.callbacks.onTickingStart(this.entity);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onRemove(Entity.RemovalReason reason) {
/*  59 */       if (!this.currentSection.remove(this.entity)) {
/*  60 */         TransientEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (destroying due to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), reason });
/*     */       }
/*  62 */       Visibility status = this.currentSection.getStatus();
/*  63 */       if (status.isTicking() || this.entity.isAlwaysTicking()) {
/*  64 */         TransientEntitySectionManager.this.callbacks.onTickingEnd(this.entity);
/*     */       }
/*  66 */       TransientEntitySectionManager.this.callbacks.onTrackingEnd(this.entity);
/*  67 */       TransientEntitySectionManager.this.callbacks.onDestroyed(this.entity);
/*  68 */       TransientEntitySectionManager.this.entityStorage.remove(this.entity);
/*  69 */       this.entity.setLevelCallback(NULL);
/*     */       
/*  71 */       TransientEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransientEntitySectionManager(Class<T> entityClass, LevelCallback<T> callbacks) {
/*  78 */     this.tickingChunks = new LongOpenHashSet();
/*     */ 
/*     */ 
/*     */     
/*  82 */     this.entityStorage = new EntityLookup();
/*     */     
/*  84 */     this.sectionStorage = new EntitySectionStorage(entityClass, key -> this.tickingChunks.contains(key) ? Visibility.TICKING : Visibility.TRACKED);
/*  85 */     this.callbacks = callbacks;
/*  86 */     this.entityGetter = new LevelEntityGetterAdapter(this.entityStorage, this.sectionStorage);
/*     */   }
/*     */   
/*     */   public void startTicking(ChunkPos pos) {
/*  90 */     long chunkKey = pos.toLong();
/*  91 */     this.tickingChunks.add(chunkKey);
/*  92 */     this.sectionStorage.getExistingSectionsInChunk(chunkKey).forEach(section -> {
/*  93 */           Visibility previousStatus = section.updateChunkStatus(Visibility.TICKING);
/*     */           
/*  95 */           if (!previousStatus.isTicking()) {
/*  96 */             Objects.requireNonNull(this.callbacks); section.getEntities().filter(()).forEach(this.callbacks::onTickingStart);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void stopTicking(ChunkPos pos) {
/* 102 */     long chunkKey = pos.toLong();
/* 103 */     this.tickingChunks.remove(chunkKey);
/* 104 */     this.sectionStorage.getExistingSectionsInChunk(chunkKey).forEach(section -> {
/* 105 */           Visibility previousStatus = section.updateChunkStatus(Visibility.TRACKED);
/*     */           
/* 107 */           if (previousStatus.isTicking()) {
/* 108 */             Objects.requireNonNull(this.callbacks); section.getEntities().filter(()).forEach(this.callbacks::onTickingEnd);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 114 */   public LevelEntityGetter<T> getEntityGetter() { return this.entityGetter; }
/*     */ 
/*     */   
/*     */   public void addEntity(T entity) {
/* 118 */     this.entityStorage.add(entity);
/*     */     
/* 120 */     long sectionKey = SectionPos.asLong(entity.blockPosition());
/* 121 */     EntitySection<T> entitySection = this.sectionStorage.getOrCreateSection(sectionKey);
/* 122 */     entitySection.add(entity);
/*     */     
/* 124 */     entity.setLevelCallback(new Callback(entity, sectionKey, entitySection));
/* 125 */     this.callbacks.onCreated(entity);
/* 126 */     this.callbacks.onTrackingStart(entity);
/* 127 */     if (entity.isAlwaysTicking() || entitySection.getStatus().isTicking()) {
/* 128 */       this.callbacks.onTickingStart(entity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 134 */   public int count() { return this.entityStorage.count(); }
/*     */ 
/*     */   
/*     */   private void removeSectionIfEmpty(long sectionPos, EntitySection<T> section) {
/* 138 */     if (section.isEmpty()) {
/* 139 */       this.sectionStorage.remove(sectionPos);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public String gatherStats() {
/* 145 */     return "" + this.entityStorage.count() + "," + this.entityStorage.count() + "," + this.sectionStorage
/* 146 */       .count();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\TransientEntitySectionManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */