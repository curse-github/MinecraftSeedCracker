/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import net.minecraft.world.entity.EntityEquipment;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class PlayerEquipment
/*    */   extends EntityEquipment {
/*    */   private final Player player;
/*    */   
/* 11 */   public PlayerEquipment(Player player) { this.player = player; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack set(EquipmentSlot slot, ItemStack itemStack) {
/* 16 */     if (slot == EquipmentSlot.MAINHAND) {
/* 17 */       return this.player.getInventory().setSelectedItem(itemStack);
/*    */     }
/* 19 */     return super.set(slot, itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack get(EquipmentSlot slot) {
/* 24 */     if (slot == EquipmentSlot.MAINHAND) {
/* 25 */       return this.player.getInventory().getSelectedItem();
/*    */     }
/* 27 */     return super.get(slot);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public boolean isEmpty() { return (this.player.getInventory().getSelectedItem().isEmpty() && super.isEmpty()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\PlayerEquipment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */