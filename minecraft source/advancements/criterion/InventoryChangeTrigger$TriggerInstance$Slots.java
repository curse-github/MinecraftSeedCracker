/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Slots
/*     */   extends Record
/*     */ {
/*     */   private final MinMaxBounds.Ints occupied;
/*     */   private final MinMaxBounds.Ints full;
/*     */   private final MinMaxBounds.Ints empty;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #105	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 105 */   public Slots(MinMaxBounds.Ints occupied, MinMaxBounds.Ints full, MinMaxBounds.Ints empty) { this.occupied = occupied; this.full = full; this.empty = empty; } public MinMaxBounds.Ints occupied() { return this.occupied; } public MinMaxBounds.Ints full() { return this.full; } public MinMaxBounds.Ints empty() { return this.empty; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public static final Codec<Slots> CODEC = RecordCodecBuilder.create(i -> i.group(MinMaxBounds.Ints.CODEC
/* 111 */         .optionalFieldOf("occupied", MinMaxBounds.Ints.ANY).forGetter(Slots::occupied), MinMaxBounds.Ints.CODEC
/* 112 */         .optionalFieldOf("full", MinMaxBounds.Ints.ANY).forGetter(Slots::full), MinMaxBounds.Ints.CODEC
/* 113 */         .optionalFieldOf("empty", MinMaxBounds.Ints.ANY).forGetter(Slots::empty))
/* 114 */       .apply(i, Slots::new));
/*     */   
/* 116 */   public static final Slots ANY = new Slots(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY);
/*     */   
/*     */   public boolean matches(int slotsFull, int slotsEmpty, int slotsOccupied) {
/* 119 */     if (!this.full.matches(slotsFull)) {
/* 120 */       return false;
/*     */     }
/* 122 */     if (!this.empty.matches(slotsEmpty)) {
/* 123 */       return false;
/*     */     }
/* 125 */     if (!this.occupied.matches(slotsOccupied)) {
/* 126 */       return false;
/*     */     }
/* 128 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\InventoryChangeTrigger$TriggerInstance$Slots.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */