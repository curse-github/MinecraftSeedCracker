/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ExperienceOrb;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class ThrownExperienceBottle
/*    */   extends ThrowableItemProjectile
/*    */ {
/* 19 */   public ThrownExperienceBottle(EntityType<? extends ThrownExperienceBottle> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public ThrownExperienceBottle(Level level, LivingEntity mob, ItemStack itemStack) { super(EntityType.EXPERIENCE_BOTTLE, mob, level, itemStack); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public ThrownExperienceBottle(Level level, double x, double y, double z, ItemStack itemStack) { super(EntityType.EXPERIENCE_BOTTLE, x, y, z, level, itemStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected Item getDefaultItem() { return Items.EXPERIENCE_BOTTLE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected double getDefaultGravity() { return 0.07D; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult hitResult) {
/* 42 */     super.onHit(hitResult);
/*    */     
/* 44 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 45 */       level.levelEvent(2002, blockPosition(), -13083194);
/*    */       
/* 47 */       int xpCount = 3 + level.random.nextInt(5) + level.random.nextInt(5);
/* 48 */       if (hitResult instanceof BlockHitResult) { BlockHitResult blockHitResult = (BlockHitResult)hitResult;
/* 49 */         Vec3 blockNormalHit = blockHitResult.getDirection().getUnitVec3();
/* 50 */         ExperienceOrb.awardWithDirection(level, hitResult.getLocation(), blockNormalHit, xpCount); }
/*    */       else
/* 52 */       { ExperienceOrb.awardWithDirection(level, hitResult.getLocation(), getDeltaMovement().scale(-1.0D), xpCount); }
/*    */       
/* 54 */       discard(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownExperienceBottle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */