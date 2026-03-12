/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TraderLlamaDefendWanderingTraderGoal
/*     */   extends TargetGoal
/*     */ {
/*     */   private final Llama llama;
/*     */   private LivingEntity ownerLastHurtBy;
/*     */   private int timestamp;
/*     */   
/*     */   public TraderLlamaDefendWanderingTraderGoal(Llama tameAnimal) {
/* 137 */     super(tameAnimal, false);
/* 138 */     this.llama = tameAnimal;
/* 139 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 144 */     if (!this.llama.isLeashed()) {
/* 145 */       return false;
/*     */     }
/* 147 */     Entity leashHolder = this.llama.getLeashHolder();
/* 148 */     if (!(leashHolder instanceof WanderingTrader)) {
/* 149 */       return false;
/*     */     }
/*     */     
/* 152 */     WanderingTrader owner = (WanderingTrader)leashHolder;
/* 153 */     this.ownerLastHurtBy = owner.getLastHurtByMob();
/* 154 */     int timeStamp = owner.getLastHurtByMobTimestamp();
/* 155 */     return (timeStamp != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 160 */     this.mob.setTarget(this.ownerLastHurtBy);
/*     */     
/* 162 */     Entity leashHolder = this.llama.getLeashHolder();
/* 163 */     if (leashHolder instanceof WanderingTrader) {
/* 164 */       this.timestamp = ((WanderingTrader)leashHolder).getLastHurtByMobTimestamp();
/*     */     }
/*     */     
/* 167 */     super.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\TraderLlama$TraderLlamaDefendWanderingTraderGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */