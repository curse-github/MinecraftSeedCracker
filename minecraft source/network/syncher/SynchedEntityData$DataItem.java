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
/*     */ public class DataItem<T>
/*     */   extends Object
/*     */ {
/*     */   private final EntityDataAccessor<T> accessor;
/*     */   private T value;
/*     */   private final T initialValue;
/*     */   private boolean dirty;
/*     */   
/*     */   public DataItem(EntityDataAccessor<T> accessor, T initialValue) {
/* 167 */     this.accessor = accessor;
/* 168 */     this.initialValue = initialValue;
/* 169 */     this.value = initialValue;
/*     */   }
/*     */ 
/*     */   
/* 173 */   public EntityDataAccessor<T> getAccessor() { return this.accessor; }
/*     */ 
/*     */ 
/*     */   
/* 177 */   public void setValue(T value) { this.value = value; }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public T getValue() { return (T)this.value; }
/*     */ 
/*     */ 
/*     */   
/* 185 */   public boolean isDirty() { return this.dirty; }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public void setDirty(boolean dirty) { this.dirty = dirty; }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public boolean isSetToDefault() { return this.initialValue.equals(this.value); }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public SynchedEntityData.DataValue<T> value() { return SynchedEntityData.DataValue.create(this.accessor, this.value); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\SynchedEntityData$DataItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */