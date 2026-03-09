/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.Map;
/*    */ import net.minecraft.world.entity.SlotAccess;
/*    */ import net.minecraft.world.entity.SlotProvider;
/*    */ import net.minecraft.world.inventory.SlotRange;
/*    */ 
/*    */ public final class SlotsPredicate extends Record {
/*    */   private final Map<SlotRange, ItemPredicate> slots;
/*    */   
/* 12 */   public SlotsPredicate(Map<SlotRange, ItemPredicate> slots) { this.slots = slots; } public Map<SlotRange, ItemPredicate> slots() { return this.slots; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/SlotsPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SlotsPredicate; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/SlotsPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SlotsPredicate; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/SlotsPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/SlotsPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 15 */   public static final Codec<SlotsPredicate> CODEC = Codec.unboundedMap(SlotRanges.CODEC, ItemPredicate.CODEC).xmap(SlotsPredicate::new, SlotsPredicate::slots);
/*    */   
/*    */   public boolean matches(SlotProvider slotProvider) {
/* 18 */     for (Map.Entry<SlotRange, ItemPredicate> entry : this.slots.entrySet()) {
/* 19 */       if (!matchSlots(slotProvider, (ItemPredicate)entry.getValue(), ((SlotRange)entry.getKey()).slots())) {
/* 20 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   private static boolean matchSlots(SlotProvider slotProvider, ItemPredicate test, IntList slots) {
/* 28 */     for (int i = 0; i < slots.size(); i++) {
/* 29 */       int slotId = slots.getInt(i);
/* 30 */       SlotAccess slot = slotProvider.getSlot(slotId);
/* 31 */       if (slot != null && test.test(slot.get())) {
/* 32 */         return true;
/*    */       }
/*    */     } 
/* 35 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\SlotsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */