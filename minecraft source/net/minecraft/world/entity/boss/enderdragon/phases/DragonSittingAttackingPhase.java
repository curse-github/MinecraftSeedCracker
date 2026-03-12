/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ 
/*    */ public class DragonSittingAttackingPhase
/*    */   extends AbstractDragonSittingPhase
/*    */ {
/*    */   private static final int ROAR_DURATION = 40;
/*    */   private int attackingTicks;
/*    */   
/* 14 */   public DragonSittingAttackingPhase(EnderDragon dragon) { super(dragon); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public void doClientTick() { this.dragon.level().playLocalSound(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ(), SoundEvents.ENDER_DRAGON_GROWL, this.dragon.getSoundSource(), 2.5F, 0.8F + this.dragon.getRandom().nextFloat() * 0.3F, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 24 */     if (this.attackingTicks++ >= 40) {
/* 25 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_FLAMING);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public void begin() { this.attackingTicks = 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public EnderDragonPhase<DragonSittingAttackingPhase> getPhase() { return EnderDragonPhase.SITTING_ATTACKING; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingAttackingPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */