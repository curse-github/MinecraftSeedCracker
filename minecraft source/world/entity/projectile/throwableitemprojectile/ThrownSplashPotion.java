/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ 
/*    */ public class ThrownSplashPotion
/*    */   extends AbstractThrownPotion
/*    */ {
/* 25 */   public ThrownSplashPotion(EntityType<? extends ThrownSplashPotion> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public ThrownSplashPotion(Level level, LivingEntity owner, ItemStack itemStack) { super(EntityType.SPLASH_POTION, level, owner, itemStack); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public ThrownSplashPotion(Level level, double x, double y, double z, ItemStack itemStack) { super(EntityType.SPLASH_POTION, level, x, y, z, itemStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected Item getDefaultItem() { return Items.SPLASH_POTION; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onHitAsPotion(ServerLevel level, ItemStack potionItem, HitResult hitResult) {
/* 43 */     PotionContents contents = (PotionContents)potionItem.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 44 */     float durationScale = ((Float)potionItem.getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue();
/*    */     
/* 46 */     Iterable<MobEffectInstance> mobEffects = contents.getAllEffects();
/* 47 */     AABB potionAabb = getBoundingBox().move(hitResult.getLocation().subtract(position()));
/* 48 */     AABB effectAabb = potionAabb.inflate(4.0D, 2.0D, 4.0D);
/* 49 */     List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, effectAabb);
/*    */     
/* 51 */     float margin = ProjectileUtil.computeMargin(this);
/*    */     
/* 53 */     if (!entities.isEmpty()) {
/* 54 */       Entity effectSource = getEffectSource();
/* 55 */       for (LivingEntity entity : entities) {
/* 56 */         if (!entity.isAffectedByPotions()) {
/*    */           continue;
/*    */         }
/*    */         
/* 60 */         double dist = potionAabb.distanceToSqr(entity.getBoundingBox().inflate(margin));
/* 61 */         if (dist < 16.0D) {
/* 62 */           double scale = 1.0D - Math.sqrt(dist) / 4.0D;
/* 63 */           for (MobEffectInstance effectInstance : mobEffects) {
/* 64 */             Holder<MobEffect> effect = effectInstance.getEffect();
/* 65 */             if (((MobEffect)effect.value()).isInstantenous()) {
/* 66 */               ((MobEffect)effect.value()).applyInstantenousEffect(level, this, getOwner(), entity, effectInstance.getAmplifier(), scale); continue;
/*    */             } 
/* 68 */             int duration = effectInstance.mapDuration(d -> (int)(scale * d * durationScale + 0.5D));
/* 69 */             MobEffectInstance newEffect = new MobEffectInstance(effect, duration, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible());
/* 70 */             if (!newEffect.endsWithin(20))
/* 71 */               entity.addEffect(newEffect, effectSource); 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownSplashPotion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */