/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.item.slot.SlotCollection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SlotProvider
/*    */ {
/*    */   SlotAccess getSlot(int paramInt);
/*    */   
/*    */   default SlotCollection getSlotsFromRange(IntList slots) {
/* 17 */     List<SlotAccess> slotList = slots.intStream().mapToObj(this::getSlot).filter(Objects::nonNull).toList();
/* 18 */     return SlotCollection.of(slotList);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\SlotProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */