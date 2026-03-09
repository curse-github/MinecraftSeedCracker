/*     */ package net.minecraft.network.syncher;
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
/*     */ public class Builder
/*     */ {
/*     */   private final SyncedDataHolder entity;
/*     */   private final SynchedEntityData.DataItem<?>[] itemsById;
/*     */   
/*     */   public Builder(SyncedDataHolder entity) {
/* 206 */     this.entity = entity;
/* 207 */     this.itemsById = new SynchedEntityData.DataItem[SynchedEntityData.ID_REGISTRY.getCount(entity.getClass())];
/*     */   }
/*     */   
/*     */   public <T> Builder define(EntityDataAccessor<T> accessor, T value) {
/* 211 */     int id = accessor.id();
/* 212 */     if (id > this.itemsById.length) {
/* 213 */       throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + this.itemsById.length + ")");
/*     */     }
/* 215 */     if (this.itemsById[id] != null) {
/* 216 */       throw new IllegalArgumentException("Duplicate id value for " + id + "!");
/*     */     }
/* 218 */     if (EntityDataSerializers.getSerializedId(accessor.serializer()) < 0) {
/* 219 */       throw new IllegalArgumentException("Unregistered serializer " + String.valueOf(accessor.serializer()) + " for " + id + "!");
/*     */     }
/* 221 */     this.itemsById[accessor.id()] = new SynchedEntityData.DataItem(accessor, value);
/* 222 */     return this;
/*     */   }
/*     */   
/*     */   public SynchedEntityData build() {
/* 226 */     for (int i = 0; i < this.itemsById.length; i++) {
/* 227 */       if (this.itemsById[i] == null)
/*     */       {
/* 229 */         throw new IllegalStateException("Entity " + String.valueOf(this.entity.getClass()) + " has not defined synched data value " + i);
/*     */       }
/*     */     } 
/* 232 */     return new SynchedEntityData(this.entity, this.itemsById);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\SynchedEntityData$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */