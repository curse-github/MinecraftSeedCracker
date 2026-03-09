/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
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
/*    */ class null
/*    */   extends ArmorSlot
/*    */ {
/* 25 */   null(NautilusInventoryMenu this$0, Container inventory, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, Identifier emptyIcon) { super(inventory, owner, slot, slotIndex, x, y, emptyIcon); }
/*    */ 
/*    */   
/* 28 */   public boolean isActive() { return nautilus.canUseSlot(EquipmentSlot.BODY); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\NautilusInventoryMenu$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */