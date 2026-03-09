/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends Object
/*     */   implements StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch>
/*     */ {
/*     */   public DataComponentPatch decode(RegistryFriendlyByteBuf input) {
/*  87 */     int positiveCount = input.readVarInt();
/*  88 */     int negativeCount = input.readVarInt();
/*  89 */     if (positiveCount == 0 && negativeCount == 0) {
/*  90 */       return DataComponentPatch.EMPTY;
/*     */     }
/*     */     
/*  93 */     int expectedSize = positiveCount + negativeCount;
/*  94 */     Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(Math.min(expectedSize, 65536));
/*  95 */     for (int i = 0; i < positiveCount; i++) {
/*  96 */       DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/*  97 */       Object value = codecGetter.apply(type).decode(input);
/*  98 */       reference2ObjectArrayMap.put(type, Optional.of(value));
/*     */     } 
/*     */     
/* 101 */     for (int i = 0; i < negativeCount; i++) {
/* 102 */       DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/* 103 */       reference2ObjectArrayMap.put(type, Optional.empty());
/*     */     } 
/*     */     
/* 106 */     return new DataComponentPatch(reference2ObjectArrayMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, DataComponentPatch patch) {
/* 111 */     if (patch.isEmpty()) {
/* 112 */       output.writeVarInt(0);
/* 113 */       output.writeVarInt(0);
/*     */       
/*     */       return;
/*     */     } 
/* 117 */     int positiveCount = 0;
/* 118 */     int negativeCount = 0; ObjectIterator objectIterator;
/* 119 */     for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 120 */       if (((Optional)entry.getValue()).isPresent()) {
/* 121 */         positiveCount++; continue;
/*     */       } 
/* 123 */       negativeCount++; }
/*     */ 
/*     */ 
/*     */     
/* 127 */     output.writeVarInt(positiveCount);
/* 128 */     output.writeVarInt(negativeCount);
/* 129 */     for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 130 */       Optional<?> value = (Optional)entry.getValue();
/* 131 */       if (value.isPresent()) {
/* 132 */         DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 133 */         DataComponentType.STREAM_CODEC.encode(output, type);
/* 134 */         encodeComponent(output, type, value.get());
/*     */       }  }
/*     */ 
/*     */     
/* 138 */     for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) { Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = (Reference2ObjectMap.Entry)objectIterator.next();
/* 139 */       if (((Optional)entry.getValue()).isEmpty()) {
/* 140 */         DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 141 */         DataComponentType.STREAM_CODEC.encode(output, type);
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 148 */   private <T> void encodeComponent(RegistryFriendlyByteBuf output, DataComponentType<T> type, Object value) { codecGetter.apply(type).encode(output, value); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\component\DataComponentPatch$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */