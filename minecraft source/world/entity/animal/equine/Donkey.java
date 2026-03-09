/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Donkey
/*    */   extends AbstractChestedHorse
/*    */ {
/* 16 */   public Donkey(EntityType<? extends Donkey> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   protected SoundEvent getAmbientSound() { return SoundEvents.DONKEY_AMBIENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected SoundEvent getAngrySound() { return SoundEvents.DONKEY_ANGRY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected SoundEvent getDeathSound() { return SoundEvents.DONKEY_DEATH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   protected SoundEvent getEatingSound() { return SoundEvents.DONKEY_EAT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.DONKEY_HURT; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canMate(Animal partner) {
/* 46 */     if (partner == this) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     if (partner instanceof Donkey || partner instanceof Horse) {
/* 51 */       return (canParent() && ((AbstractHorse)partner).canParent());
/*    */     }
/*    */     
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected void playJumpSound() { playSound(SoundEvents.DONKEY_JUMP, 0.4F, 1.0F); }
/*    */ 
/*    */ 
/*    */   
/*    */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 64 */     EntityType<? extends AbstractHorse> babyType = (partner instanceof Horse) ? EntityType.MULE : EntityType.DONKEY;
/* 65 */     AbstractHorse baby = (AbstractHorse)babyType.create(level, EntitySpawnReason.BREEDING);
/*    */     
/* 67 */     if (baby != null) {
/* 68 */       setOffspringAttributes(partner, baby);
/*    */     }
/*    */     
/* 71 */     return baby;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\Donkey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */