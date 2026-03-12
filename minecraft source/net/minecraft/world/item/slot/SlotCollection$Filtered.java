/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Filtered
/*    */   extends Record
/*    */   implements SlotCollection
/*    */ {
/*    */   private final SlotCollection slots;
/*    */   private final Predicate<ItemStack> filter;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 56 */   public Filtered(SlotCollection slots, Predicate<ItemStack> filter) { this.slots = slots; this.filter = filter; } public SlotCollection slots() { return this.slots; } public Predicate<ItemStack> filter() { return this.filter; }
/*    */ 
/*    */   
/* 59 */   public Stream<ItemStack> itemCopies() { return this.slots.itemCopies().filter(this.filter); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public SlotCollection filter(Predicate<ItemStack> predicate) { return new Filtered(this.slots, this.filter.and(predicate)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\SlotCollection$Filtered.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */