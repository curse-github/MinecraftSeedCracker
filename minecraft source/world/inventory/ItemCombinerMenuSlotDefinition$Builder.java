/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
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
/*    */ 
/*    */ public class Builder
/*    */ {
/* 47 */   private final List<ItemCombinerMenuSlotDefinition.SlotDefinition> inputSlots = new ArrayList();
/* 48 */   private ItemCombinerMenuSlotDefinition.SlotDefinition resultSlot = ItemCombinerMenuSlotDefinition.SlotDefinition.EMPTY;
/*    */   
/*    */   public Builder withSlot(int slotIndex, int xPlacement, int yPlacement, Predicate<ItemStack> mayPlace) {
/* 51 */     this.inputSlots.add(new ItemCombinerMenuSlotDefinition.SlotDefinition(slotIndex, xPlacement, yPlacement, mayPlace));
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withResultSlot(int slotIndex, int xPlacement, int yPlacement) {
/* 56 */     this.resultSlot = new ItemCombinerMenuSlotDefinition.SlotDefinition(slotIndex, xPlacement, yPlacement, itemStack -> false);
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemCombinerMenuSlotDefinition build() {
/* 63 */     int inputCount = this.inputSlots.size();
/* 64 */     for (int i = 0; i < inputCount; i++) {
/* 65 */       ItemCombinerMenuSlotDefinition.SlotDefinition inputDefinition = (ItemCombinerMenuSlotDefinition.SlotDefinition)this.inputSlots.get(i);
/* 66 */       if (inputDefinition.slotIndex != i) {
/* 67 */         throw new IllegalArgumentException("Expected input slots to have continous indexes");
/*    */       }
/*    */     } 
/* 70 */     if (this.resultSlot.slotIndex != inputCount) {
/* 71 */       throw new IllegalArgumentException("Expected result slot index to follow last input slot");
/*    */     }
/*    */     
/* 74 */     return new ItemCombinerMenuSlotDefinition(this.inputSlots, this.resultSlot);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ItemCombinerMenuSlotDefinition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */