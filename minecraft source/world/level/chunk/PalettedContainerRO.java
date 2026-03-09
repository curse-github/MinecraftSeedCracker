/*    */ package net.minecraft.world.level.chunk;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.LongStream;
/*    */ 
/*    */ public interface PalettedContainerRO<T> {
/*    */   T get(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   void getAll(Consumer<T> paramConsumer);
/*    */   
/*    */   void write(FriendlyByteBuf paramFriendlyByteBuf);
/*    */   
/*    */   int getSerializedSize();
/*    */   
/*    */   @VisibleForTesting
/*    */   int bitsPerEntry();
/*    */   
/*    */   boolean maybeHas(Predicate<T> paramPredicate);
/*    */   
/*    */   void count(PalettedContainer.CountConsumer<T> paramCountConsumer);
/*    */   
/*    */   PalettedContainer<T> copy();
/*    */   
/*    */   PalettedContainer<T> recreate();
/*    */   
/*    */   PackedData<T> pack(Strategy<T> paramStrategy);
/*    */   
/*    */   public static interface Unpacker<T, C extends PalettedContainerRO<T>> {
/*    */     DataResult<C> read(Strategy<T> param1Strategy, PalettedContainerRO.PackedData<T> param1PackedData);
/*    */   }
/*    */   
/*    */   public static final class PackedData<T> extends Record {
/*    */     private final List<T> paletteEntries;
/*    */     private final Optional<LongStream> storage;
/*    */     
/* 35 */     public int bitsPerEntry() { return this.bitsPerEntry; } private final int bitsPerEntry; public static final int UNKNOWN_BITS_PER_ENTRY = -1; public Optional<LongStream> storage() { return this.storage; } public List<T> paletteEntries() { return this.paletteEntries; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 35 */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>; } public PackedData(List<T> paletteEntries, Optional<LongStream> storage, int bitsPerEntry) { this.paletteEntries = paletteEntries; this.storage = storage; this.bitsPerEntry = bitsPerEntry; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 44 */     public PackedData(List<T> paletteEntries, Optional<LongStream> storage) { this(paletteEntries, storage, -1); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\PalettedContainerRO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */