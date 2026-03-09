/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class ItemCombinerMenuSlotDefinition
/*    */ {
/*    */   private final List<SlotDefinition> slots;
/*    */   private final SlotDefinition resultSlot;
/*    */   
/*    */   private ItemCombinerMenuSlotDefinition(List<SlotDefinition> inputSlots, SlotDefinition resultSlot) {
/* 15 */     if (inputSlots.isEmpty() || resultSlot.equals(SlotDefinition.EMPTY)) {
/* 16 */       throw new IllegalArgumentException("Need to define both inputSlots and resultSlot");
/*    */     }
/* 18 */     this.slots = inputSlots;
/* 19 */     this.resultSlot = resultSlot;
/*    */   }
/*    */ 
/*    */   
/* 23 */   public static Builder create() { return new Builder(); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public SlotDefinition getSlot(int index) { return (SlotDefinition)this.slots.get(index); }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public SlotDefinition getResultSlot() { return this.resultSlot; }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public List<SlotDefinition> getSlots() { return this.slots; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public int getNumOfInputSlots() { return this.slots.size(); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int getResultSlotIndex() { return getNumOfInputSlots(); }
/*    */   
/*    */   public static class Builder
/*    */   {
/* 47 */     private final List<ItemCombinerMenuSlotDefinition.SlotDefinition> inputSlots = new ArrayList();
/* 48 */     private ItemCombinerMenuSlotDefinition.SlotDefinition resultSlot = ItemCombinerMenuSlotDefinition.SlotDefinition.EMPTY;
/*    */     
/*    */     public Builder withSlot(int slotIndex, int xPlacement, int yPlacement, Predicate<ItemStack> mayPlace) {
/* 51 */       this.inputSlots.add(new ItemCombinerMenuSlotDefinition.SlotDefinition(slotIndex, xPlacement, yPlacement, mayPlace));
/* 52 */       return this;
/*    */     }
/*    */     
/*    */     public Builder withResultSlot(int slotIndex, int xPlacement, int yPlacement) {
/* 56 */       this.resultSlot = new ItemCombinerMenuSlotDefinition.SlotDefinition(slotIndex, xPlacement, yPlacement, itemStack -> false);
/* 57 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public ItemCombinerMenuSlotDefinition build() {
/* 63 */       int inputCount = this.inputSlots.size();
/* 64 */       for (int i = 0; i < inputCount; i++) {
/* 65 */         ItemCombinerMenuSlotDefinition.SlotDefinition inputDefinition = (ItemCombinerMenuSlotDefinition.SlotDefinition)this.inputSlots.get(i);
/* 66 */         if (inputDefinition.slotIndex != i) {
/* 67 */           throw new IllegalArgumentException("Expected input slots to have continous indexes");
/*    */         }
/*    */       } 
/* 70 */       if (this.resultSlot.slotIndex != inputCount) {
/* 71 */         throw new IllegalArgumentException("Expected result slot index to follow last input slot");
/*    */       }
/*    */       
/* 74 */       return new ItemCombinerMenuSlotDefinition(this.inputSlots, this.resultSlot);
/*    */     } }
/*    */   public static final class SlotDefinition extends Record { private final int slotIndex; private final int x; private final int y; private final Predicate<ItemStack> mayPlace;
/*    */     
/* 78 */     public SlotDefinition(int slotIndex, int x, int y, Predicate<ItemStack> mayPlace) { this.slotIndex = slotIndex; this.x = x; this.y = y; this.mayPlace = mayPlace; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 78 */       //   0	7	0	this	Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition; } public int slotIndex() { return this.slotIndex; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #78	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$SlotDefinition;
/* 78 */       //   0	8	1	o	Ljava/lang/Object; } public int x() { return this.x; } public int y() { return this.y; } public Predicate<ItemStack> mayPlace() { return this.mayPlace; }
/* 79 */     private static final SlotDefinition EMPTY = new SlotDefinition(0, 0, 0, itemStack -> true); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ItemCombinerMenuSlotDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */