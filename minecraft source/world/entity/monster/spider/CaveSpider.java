/*    */ package net.minecraft.world.entity.monster.spider;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.SpawnGroupData;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class CaveSpider
/*    */   extends Spider
/*    */ {
/* 23 */   public CaveSpider(EntityType<? extends CaveSpider> type, Level level) { super(type, level); }
/*    */ 
/*    */   
/*    */   public static AttributeSupplier.Builder createCaveSpider() {
/* 27 */     return Spider.createAttributes()
/* 28 */       .add(Attributes.MAX_HEALTH, 12.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 33 */     if (super.doHurtTarget(level, target)) {
/* 34 */       if (target instanceof LivingEntity) {
/* 35 */         int poisonTime = 0;
/* 36 */         if (level().getDifficulty() == Difficulty.NORMAL) {
/* 37 */           poisonTime = 7;
/* 38 */         } else if (level().getDifficulty() == Difficulty.HARD) {
/* 39 */           poisonTime = 15;
/*    */         } 
/*    */         
/* 42 */         if (poisonTime > 0) {
/* 43 */           ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.POISON, poisonTime * 20, 0), this);
/*    */         }
/*    */       } 
/*    */       
/* 47 */       return true;
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) { return groupData; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
/* 60 */     if (vehicle.getBbWidth() <= getBbWidth()) {
/* 61 */       return new Vec3(0.0D, 0.21875D * getScale(), 0.0D);
/*    */     }
/* 63 */     return super.getVehicleAttachmentPoint(vehicle);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\spider\CaveSpider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */