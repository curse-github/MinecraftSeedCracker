/*    */ package net.minecraft.network;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.TypedDataComponent;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class HashedPatchMap extends Record {
/*    */   private final Map<DataComponentType<?>, Integer> addedComponents;
/*    */   private final Set<DataComponentType<?>> removedComponents;
/*    */   
/* 17 */   public HashedPatchMap(Map<DataComponentType<?>, Integer> addedComponents, Set<DataComponentType<?>> removedComponents) { this.addedComponents = addedComponents; this.removedComponents = removedComponents; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/HashedPatchMap;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/network/HashedPatchMap; } public Map<DataComponentType<?>, Integer> addedComponents() { return this.addedComponents; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/HashedPatchMap;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/HashedPatchMap; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/HashedPatchMap;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/HashedPatchMap;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public Set<DataComponentType<?>> removedComponents() { return this.removedComponents; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, HashedPatchMap> STREAM_CODEC = StreamCodec.composite(
/* 23 */       ByteBufCodecs.map(java.util.HashMap::new, ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE), ByteBufCodecs.INT, 256), HashedPatchMap::addedComponents, 
/* 24 */       ByteBufCodecs.collection(java.util.HashSet::new, ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE), 256), HashedPatchMap::removedComponents, HashedPatchMap::new);
/*    */ 
/*    */ 
/*    */   
/*    */   public static HashedPatchMap create(DataComponentPatch patch, HashGenerator hasher) {
/* 29 */     DataComponentPatch.SplitResult split = patch.split();
/*    */     
/* 31 */     Map<DataComponentType<?>, Integer> setComponentHashes = new IdentityHashMap<DataComponentType<?>, Integer>(split.added().size());
/* 32 */     split.added().forEach(e -> setComponentHashes.put(e.type(), (Integer)hasher.apply(e)));
/*    */     
/* 34 */     return new HashedPatchMap(setComponentHashes, split
/*    */         
/* 36 */         .removed());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(DataComponentPatch patch, HashGenerator hasher) {
/* 41 */     DataComponentPatch.SplitResult split = patch.split();
/* 42 */     if (!split.removed().equals(this.removedComponents)) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     if (this.addedComponents.size() != split.added().size()) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     for (TypedDataComponent<?> typedDataComponent : split.added()) {
/* 51 */       Integer expectedHash = (Integer)this.addedComponents.get(typedDataComponent.type());
/* 52 */       if (expectedHash == null) {
/* 53 */         return false;
/*    */       }
/* 55 */       Integer actualHash = (Integer)hasher.apply(typedDataComponent);
/* 56 */       if (!actualHash.equals(expectedHash)) {
/* 57 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 61 */     return true;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface HashGenerator extends Function<TypedDataComponent<?>, Integer> {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\HashedPatchMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */