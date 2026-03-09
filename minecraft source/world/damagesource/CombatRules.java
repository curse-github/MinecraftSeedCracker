/*    */ package net.minecraft.world.damagesource;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class CombatRules {
/*    */   public static final float MAX_ARMOR = 20.0F;
/*    */   public static final float ARMOR_PROTECTION_DIVIDER = 25.0F;
/*    */   public static final float BASE_ARMOR_TOUGHNESS = 2.0F;
/*    */   public static final float MIN_ARMOR_RATIO = 0.2F;
/*    */   private static final int NUM_ARMOR_ITEMS = 4;
/*    */   
/*    */   public static float getDamageAfterAbsorb(LivingEntity victim, float damage, DamageSource source, float totalArmor, float armorToughness) {
/* 17 */     float toughness = 2.0F + armorToughness / 4.0F;
/* 18 */     float realArmor = Mth.clamp(totalArmor - damage / toughness, totalArmor * 0.2F, 20.0F);
/* 19 */     float armorFraction = realArmor / 25.0F;
/*    */     
/* 21 */     ItemStack weaponItem = source.getWeaponItem();
/* 22 */     if (weaponItem != null) { Level level1 = victim.level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 23 */         float modifiedArmorFraction = Mth.clamp(EnchantmentHelper.modifyArmorEffectiveness(level, weaponItem, victim, source, armorFraction), 0.0F, 1.0F);
/*    */ 
/*    */ 
/*    */         
/* 27 */         float damageMultiplier = 1.0F - modifiedArmorFraction;
/* 28 */         return damage * damageMultiplier; }  }  float modifiedArmorFraction = armorFraction; float f = 1.0F - modifiedArmorFraction; return damage * f;
/*    */   }
/*    */   
/*    */   public static float getDamageAfterMagicAbsorb(float damage, float totalMagicArmor) {
/* 32 */     float realArmor = Mth.clamp(totalMagicArmor, 0.0F, 20.0F);
/* 33 */     return damage * (1.0F - realArmor / 25.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\CombatRules.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */