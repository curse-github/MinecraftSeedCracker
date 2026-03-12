/*    */ package net.minecraft.world.item.slot;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.entity.SlotAccess;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface SlotCollection
/*    */ {
/* 13 */   public static final SlotCollection EMPTY = Stream::empty;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   default SlotCollection filter(Predicate<ItemStack> predicate) { return new Filtered(this, predicate); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   default SlotCollection flatMap(Function<ItemStack, ? extends SlotCollection> mapper) { return new FlatMapped(this, mapper); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   default SlotCollection limit(int limit) { return new Limited(this, limit); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   static SlotCollection of(SlotAccess slotAccess) { return () -> Stream.of(slotAccess.get().copy()); }
/*    */ 
/*    */   
/*    */   static SlotCollection of(Collection<? extends SlotAccess> slots) {
/* 34 */     switch (slots.size()) { case 0: case 1:  }  return () -> 
/*    */ 
/*    */       
/* 37 */       slots.stream()
/* 38 */       .map(SlotAccess::get)
/* 39 */       .map(ItemStack::copy);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   static SlotCollection concat(SlotCollection first, SlotCollection second) { return () -> Stream.concat(first.itemCopies(), second.itemCopies()); }
/*    */ 
/*    */   
/*    */   static SlotCollection concat(List<? extends SlotCollection> terms) {
/* 48 */     switch (terms.size()) { case 0: case 1: case 2:  }  return () -> 
/*    */ 
/*    */ 
/*    */       
/* 52 */       terms.stream().flatMap(SlotCollection::itemCopies);
/*    */   }
/*    */   Stream<ItemStack> itemCopies();
/*    */   public static final class Filtered extends Record implements SlotCollection { private final SlotCollection slots; private final Predicate<ItemStack> filter;
/* 56 */     public Filtered(SlotCollection slots, Predicate<ItemStack> filter) { this.slots = slots; this.filter = filter; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$Filtered;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Filtered;
/* 56 */       //   0	8	1	o	Ljava/lang/Object; } public SlotCollection slots() { return this.slots; } public Predicate<ItemStack> filter() { return this.filter; }
/*    */ 
/*    */     
/* 59 */     public Stream<ItemStack> itemCopies() { return this.slots.itemCopies().filter(this.filter); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     public SlotCollection filter(Predicate<ItemStack> predicate) { return new Filtered(this.slots, this.filter.and(predicate)); } }
/*    */   public static final class FlatMapped extends Record implements SlotCollection { private final SlotCollection slots;
/*    */     private final Function<ItemStack, ? extends SlotCollection> mapper;
/*    */     
/* 68 */     public FlatMapped(SlotCollection slots, Function<ItemStack, ? extends SlotCollection> mapper) { this.slots = slots; this.mapper = mapper; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$FlatMapped;
/* 68 */       //   0	8	1	o	Ljava/lang/Object; } public SlotCollection slots() { return this.slots; } public Function<ItemStack, ? extends SlotCollection> mapper() { return this.mapper; }
/*    */     
/*    */     public Stream<ItemStack> itemCopies() {
/* 71 */       return this.slots.itemCopies().map(this.mapper)
/* 72 */         .flatMap(SlotCollection::itemCopies);
/*    */     } }
/*    */   public static final class Limited extends Record implements SlotCollection { private final SlotCollection slots; private final int limit;
/*    */     
/* 76 */     public Limited(SlotCollection slots, int limit) { this.slots = slots; this.limit = limit; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #76	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #76	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/slot/SlotCollection$Limited;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #76	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/slot/SlotCollection$Limited;
/* 76 */       //   0	8	1	o	Ljava/lang/Object; } public SlotCollection slots() { return this.slots; } public int limit() { return this.limit; }
/*    */ 
/*    */     
/* 79 */     public Stream<ItemStack> itemCopies() { return this.slots.itemCopies().limit(this.limit); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 84 */     public SlotCollection limit(int limit) { return new Limited(this.slots, Math.min(this.limit, limit)); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\SlotCollection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */