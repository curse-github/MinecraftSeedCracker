/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ 
/*    */ public abstract class AbstractDragonSittingPhase
/*    */   extends AbstractDragonPhaseInstance
/*    */ {
/* 10 */   public AbstractDragonSittingPhase(EnderDragon dragon) { super(dragon); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public boolean isSitting() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public float onHurt(DamageSource source, float damage) {
/* 20 */     if (source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow || source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge) {
/* 21 */       source.getDirectEntity().igniteForSeconds(1.0F);
/* 22 */       return 0.0F;
/*    */     } 
/* 24 */     return super.onHurt(source, damage);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\AbstractDragonSittingPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */