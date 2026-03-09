/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class EquipmentDispenseItemBehavior extends DefaultDispenseItemBehavior {
/* 14 */   public static final EquipmentDispenseItemBehavior INSTANCE = new EquipmentDispenseItemBehavior();
/*    */ 
/*    */ 
/*    */   
/* 18 */   protected ItemStack execute(BlockSource source, ItemStack dispensed) { return dispenseEquipment(source, dispensed) ? dispensed : super.execute(source, dispensed); }
/*    */ 
/*    */   
/*    */   public static boolean dispenseEquipment(BlockSource source, ItemStack dispensed) {
/* 22 */     BlockPos pos = source.pos().relative((Direction)source.state().getValue(DispenserBlock.FACING));
/*    */     
/* 24 */     List<LivingEntity> entities = source.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos), entity -> entity.canEquipWithDispenser(dispensed));
/* 25 */     if (entities.isEmpty()) {
/* 26 */       return false;
/*    */     }
/*    */     
/* 29 */     LivingEntity target = (LivingEntity)entities.getFirst();
/* 30 */     EquipmentSlot slot = target.getEquipmentSlotForItem(dispensed);
/*    */     
/* 32 */     ItemStack equip = dispensed.split(1);
/* 33 */     target.setItemSlot(slot, equip);
/* 34 */     if (target instanceof Mob) { Mob targetMob = (Mob)target;
/* 35 */       targetMob.setGuaranteedDrop(slot);
/* 36 */       targetMob.setPersistenceRequired(); }
/*    */     
/* 38 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\EquipmentDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */