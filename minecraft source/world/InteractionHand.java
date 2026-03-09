/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public static enum InteractionHand {
/*  6 */   MAIN_HAND,
/*  7 */   OFF_HAND;
/*    */ 
/*    */ 
/*    */   
/* 11 */   public EquipmentSlot asEquipmentSlot() { return (this == MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\InteractionHand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */