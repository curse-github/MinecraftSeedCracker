/*     */ package net.minecraft.network.syncher;
/*     */ 
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DataValue<T>
/*     */   extends Record
/*     */ {
/*     */   private final int id;
/*     */   private final EntityDataSerializer<T> serializer;
/*     */   private final T value;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #129	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue<TT;>; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #129	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue<TT;>; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #129	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/network/syncher/SynchedEntityData$DataValue<TT;>; }
/*     */   
/* 129 */   public DataValue(int id, EntityDataSerializer<T> serializer, T value) { this.id = id; this.serializer = serializer; this.value = value; } public int id() { return this.id; } public EntityDataSerializer<T> serializer() { return this.serializer; } public T value() { return (T)this.value; }
/*     */   public static <T> DataValue<T> create(EntityDataAccessor<T> accessor, T value) {
/* 131 */     EntityDataSerializer<T> serializer = accessor.serializer();
/* 132 */     return new DataValue(accessor.id(), serializer, serializer.copy(value));
/*     */   }
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {
/* 136 */     int serializerId = EntityDataSerializers.getSerializedId(this.serializer);
/* 137 */     if (serializerId < 0) {
/* 138 */       throw new EncoderException("Unknown serializer type " + String.valueOf(this.serializer));
/*     */     }
/* 140 */     output.writeByte(this.id);
/* 141 */     output.writeVarInt(serializerId);
/* 142 */     this.serializer.codec().encode(output, this.value);
/*     */   }
/*     */   
/*     */   public static DataValue<?> read(RegistryFriendlyByteBuf input, int id) {
/* 146 */     int type = input.readVarInt();
/* 147 */     EntityDataSerializer<?> serializer = EntityDataSerializers.getSerializer(type);
/* 148 */     if (serializer == null) {
/* 149 */       throw new DecoderException("Unknown serializer type " + type);
/*     */     }
/*     */     
/* 152 */     return read(input, id, serializer);
/*     */   }
/*     */ 
/*     */   
/* 156 */   private static <T> DataValue<T> read(RegistryFriendlyByteBuf input, int id, EntityDataSerializer<T> serializer) { return new DataValue(id, serializer, serializer.codec().decode(input)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\syncher\SynchedEntityData$DataValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */