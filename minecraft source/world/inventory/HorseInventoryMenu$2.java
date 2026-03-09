/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
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
/*    */ class null
/*    */   extends ArmorSlot
/*    */ {
/* 30 */   null(HorseInventoryMenu this$0, Container inventory, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, Identifier emptyIcon) { super(inventory, owner, slot, slotIndex, x, y, emptyIcon); }
/*    */ 
/*    */   
/* 33 */   public boolean isActive() { return (horse.canUseSlot(EquipmentSlot.BODY) && (horse.getType().is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR) || isLlama)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\HorseInventoryMenu$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */