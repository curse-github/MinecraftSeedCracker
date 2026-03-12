/*     */ package net.minecraft.network.syncher;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.util.ClassTreeIdRegistry;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SynchedEntityData
/*     */ {
/*  19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_ID_VALUE = 254;
/*  22 */   private static final ClassTreeIdRegistry ID_REGISTRY = new ClassTreeIdRegistry();
/*     */   
/*     */   private final SyncedDataHolder entity;
/*     */   
/*     */   private final DataItem<?>[] itemsById;
/*     */   private boolean isDirty;
/*     */   
/*     */   private SynchedEntityData(SyncedDataHolder entity, DataItem[] itemsById) {
/*  30 */     this.entity = entity;
/*  31 */     this.itemsById = itemsById;
/*     */   }
/*     */   
/*     */   public static <T> EntityDataAccessor<T> defineId(Class<? extends SyncedDataHolder> clazz, EntityDataSerializer<T> type) {
/*  35 */     if (LOGGER.isDebugEnabled()) {
/*     */       try {
/*  37 */         Class<?> aClass = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
/*  38 */         if (!aClass.equals(clazz)) {
/*  39 */           LOGGER.debug("defineId called for: {} from {}", new Object[] { clazz, aClass, new RuntimeException() });
/*     */         }
/*  41 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */     
/*  44 */     int id = ID_REGISTRY.define(clazz);
/*  45 */     if (id > 254) {
/*  46 */       throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is 254)");
/*     */     }
/*  48 */     return type.createAccessor(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  53 */   private <T> DataItem<T> getItem(EntityDataAccessor<T> accessor) { return this.itemsById[accessor.id()]; }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public <T> T get(EntityDataAccessor<T> accessor) { return (T)getItem(accessor).getValue(); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public <T> void set(EntityDataAccessor<T> accessor, T value) { set(accessor, value, false); }
/*     */ 
/*     */   
/*     */   public <T> void set(EntityDataAccessor<T> accessor, T value, boolean forceDirty) {
/*  65 */     DataItem<T> dataItem = getItem(accessor);
/*     */     
/*  67 */     if (forceDirty || ObjectUtils.notEqual(value, dataItem.getValue())) {
/*  68 */       dataItem.setValue(value);
/*  69 */       this.entity.onSyncedDataUpdated(accessor);
/*  70 */       dataItem.setDirty(true);
/*  71 */       this.isDirty = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  76 */   public boolean isDirty() { return this.isDirty; }
/*     */ 
/*     */   
/*     */   public List<DataValue<?>> packDirty() {
/*  80 */     if (!this.isDirty) {
/*  81 */       return null;
/*     */     }
/*  83 */     this.isDirty = false;
/*     */     
/*  85 */     List<DataValue<?>> result = new ArrayList<DataValue<?>>();
/*  86 */     for (DataItem<?> dataItem : this.itemsById) {
/*  87 */       if (dataItem.isDirty()) {
/*  88 */         dataItem.setDirty(false);
/*  89 */         result.add(dataItem.value());
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     return result;
/*     */   }
/*     */   
/*     */   public List<DataValue<?>> getNonDefaultValues() {
/*  97 */     List<DataValue<?>> result = null;
/*     */     
/*  99 */     for (DataItem<?> dataItem : this.itemsById) {
/* 100 */       if (!dataItem.isSetToDefault()) {
/*     */ 
/*     */         
/* 103 */         if (result == null) {
/* 104 */           result = new ArrayList<DataValue<?>>();
/*     */         }
/* 106 */         result.add(dataItem.value());
/*     */       } 
/*     */     } 
/* 109 */     return result;
/*     */   }
/*     */   
/*     */   public void assignValues(List<DataValue<?>> items) {
/* 113 */     for (DataValue<?> item : items) {
/* 114 */       DataItem<?> dataItem = this.itemsById[item.id];
/* 115 */       assignValue(dataItem, item);
/* 116 */       this.entity.onSyncedDataUpdated(dataItem.getAccessor());
/*     */     } 
/* 118 */     this.entity.onSyncedDataUpdated(items);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> void assignValue(DataItem<T> dataItem, DataValue<?> item) {
/* 123 */     if (!Objects.equals(item.serializer(), dataItem.accessor.serializer())) {
/* 124 */       throw new IllegalStateException(String.format(Locale.ROOT, "Invalid entity data item type for field %d on entity %s: old=%s(%s), new=%s(%s)", new Object[] { Integer.valueOf(dataItem.accessor.id()), this.entity, dataItem.value, dataItem.value.getClass(), item.value, item.value.getClass() }));
/*     */     }
/* 126 */     dataItem.setValue(item.value);
/*     */   }
/*     */   public static final class DataValue<T> extends Record { private final int id; private final EntityDataSerializer<T> serializer; private final T value;
/* 129 */     public DataValue(int id, EntityDataSerializer<T> serializer, T value) { this.id = id; this.serializer = serializer; this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #129	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 129 */       //   0	7	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue<TT;>; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #129	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #129	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 129 */       //   0	8	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue<TT;>; } public EntityDataSerializer<T> serializer() { return this.serializer; } public T value() { return (T)this.value; }
/*     */     public static <T> DataValue<T> create(EntityDataAccessor<T> accessor, T value) {
/* 131 */       EntityDataSerializer<T> serializer = accessor.serializer();
/* 132 */       return new DataValue(accessor.id(), serializer, serializer.copy(value));
/*     */     }
/*     */     
/*     */     public void write(RegistryFriendlyByteBuf output) {
/* 136 */       int serializerId = EntityDataSerializers.getSerializedId(this.serializer);
/* 137 */       if (serializerId < 0) {
/* 138 */         throw new EncoderException("Unknown serializer type " + String.valueOf(this.serializer));
/*     */       }
/* 140 */       output.writeByte(this.id);
/* 141 */       output.writeVarInt(serializerId);
/* 142 */       this.serializer.codec().encode(output, this.value);
/*     */     }
/*     */     
/*     */     public static DataValue<?> read(RegistryFriendlyByteBuf input, int id) {
/* 146 */       int type = input.readVarInt();
/* 147 */       EntityDataSerializer<?> serializer = EntityDataSerializers.getSerializer(type);
/* 148 */       if (serializer == null) {
/* 149 */         throw new DecoderException("Unknown serializer type " + type);
/*     */       }
/*     */       
/* 152 */       return read(input, id, serializer);
/*     */     }
/*     */ 
/*     */     
/* 156 */     private static <T> DataValue<T> read(RegistryFriendlyByteBuf input, int id, EntityDataSerializer<T> serializer) { return new DataValue(id, serializer, serializer.codec().decode(input)); } }
/*     */ 
/*     */   
/*     */   public static class DataItem<T>
/*     */     extends Object {
/*     */     private final EntityDataAccessor<T> accessor;
/*     */     private T value;
/*     */     private final T initialValue;
/*     */     private boolean dirty;
/*     */     
/*     */     public DataItem(EntityDataAccessor<T> accessor, T initialValue) {
/* 167 */       this.accessor = accessor;
/* 168 */       this.initialValue = initialValue;
/* 169 */       this.value = initialValue;
/*     */     }
/*     */ 
/*     */     
/* 173 */     public EntityDataAccessor<T> getAccessor() { return this.accessor; }
/*     */ 
/*     */ 
/*     */     
/* 177 */     public void setValue(T value) { this.value = value; }
/*     */ 
/*     */ 
/*     */     
/* 181 */     public T getValue() { return (T)this.value; }
/*     */ 
/*     */ 
/*     */     
/* 185 */     public boolean isDirty() { return this.dirty; }
/*     */ 
/*     */ 
/*     */     
/* 189 */     public void setDirty(boolean dirty) { this.dirty = dirty; }
/*     */ 
/*     */ 
/*     */     
/* 193 */     public boolean isSetToDefault() { return this.initialValue.equals(this.value); }
/*     */ 
/*     */ 
/*     */     
/* 197 */     public SynchedEntityData.DataValue<T> value() { return SynchedEntityData.DataValue.create(this.accessor, this.value); }
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final SyncedDataHolder entity;
/*     */     private final SynchedEntityData.DataItem<?>[] itemsById;
/*     */     
/*     */     public Builder(SyncedDataHolder entity) {
/* 206 */       this.entity = entity;
/* 207 */       this.itemsById = new SynchedEntityData.DataItem[SynchedEntityData.ID_REGISTRY.getCount(entity.getClass())];
/*     */     }
/*     */     
/*     */     public <T> Builder define(EntityDataAccessor<T> accessor, T value) {
/* 211 */       int id = accessor.id();
/* 212 */       if (id > this.itemsById.length) {
/* 213 */         throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + this.itemsById.length + ")");
/*     */       }
/* 215 */       if (this.itemsById[id] != null) {
/* 216 */         throw new IllegalArgumentException("Duplicate id value for " + id + "!");
/*     */       }
/* 218 */       if (EntityDataSerializers.getSerializedId(accessor.serializer()) < 0) {
/* 219 */         throw new IllegalArgumentException("Unregistered serializer " + String.valueOf(accessor.serializer()) + " for " + id + "!");
/*     */       }
/* 221 */       this.itemsById[accessor.id()] = new SynchedEntityData.DataItem(accessor, value);
/* 222 */       return this;
/*     */     }
/*     */     
/*     */     public SynchedEntityData build() {
/* 226 */       for (int i = 0; i < this.itemsById.length; i++) {
/* 227 */         if (this.itemsById[i] == null)
/*     */         {
/* 229 */           throw new IllegalStateException("Entity " + String.valueOf(this.entity.getClass()) + " has not defined synched data value " + i);
/*     */         }
/*     */       } 
/* 232 */       return new SynchedEntityData(this.entity, this.itemsById);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\SynchedEntityData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */