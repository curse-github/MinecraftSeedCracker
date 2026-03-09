/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Mule
/*    */   extends AbstractChestedHorse
/*    */ {
/* 15 */   public Mule(EntityType<? extends Mule> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected SoundEvent getAmbientSound() { return SoundEvents.MULE_AMBIENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected SoundEvent getAngrySound() { return SoundEvents.MULE_ANGRY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   protected SoundEvent getDeathSound() { return SoundEvents.MULE_DEATH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected SoundEvent getEatingSound() { return SoundEvents.MULE_EAT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.MULE_HURT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   protected void playJumpSound() { playSound(SoundEvents.MULE_JUMP, 0.4F, 1.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   protected void playChestEquipsSound() { playSound(SoundEvents.MULE_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) { return (AgeableMob)EntityType.MULE.create(level, EntitySpawnReason.BREEDING); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\Mule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */