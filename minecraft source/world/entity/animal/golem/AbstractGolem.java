/*    */ package net.minecraft.world.entity.animal.golem;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AbstractGolem
/*    */   extends PathfinderMob
/*    */ {
/* 12 */   protected AbstractGolem(EntityType<? extends AbstractGolem> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   protected SoundEvent getAmbientSound() { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected SoundEvent getHurtSound(DamageSource source) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   protected SoundEvent getDeathSound() { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public int getAmbientSoundInterval() { return 120; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public boolean removeWhenFarAway(double distSqr) { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\golem\AbstractGolem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */