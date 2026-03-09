/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public interface CrossbowAttackMob extends RangedAttackMob {
/*    */   void setChargingCrossbow(boolean paramBoolean);
/*    */   
/*    */   LivingEntity getTarget();
/*    */   
/*    */   void onCrossbowAttackPerformed();
/*    */   
/*    */   default void performCrossbowAttack(LivingEntity body, float crossbowPower) {
/* 19 */     InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(body, Items.CROSSBOW);
/* 20 */     ItemStack usedItem = body.getItemInHand(hand);
/* 21 */     Item item = usedItem.getItem(); if (item instanceof CrossbowItem) { CrossbowItem crossbow = (CrossbowItem)item;
/* 22 */       crossbow.performShooting(body.level(), body, hand, usedItem, crossbowPower, (14 - body.level().getDifficulty().getId() * 4), getTarget()); }
/*    */     
/* 24 */     onCrossbowAttackPerformed();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\CrossbowAttackMob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */