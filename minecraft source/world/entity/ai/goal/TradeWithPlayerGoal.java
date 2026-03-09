/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class TradeWithPlayerGoal
/*    */   extends Goal {
/*    */   private final AbstractVillager mob;
/*    */   
/*    */   public TradeWithPlayerGoal(AbstractVillager mob) {
/* 12 */     this.mob = mob;
/* 13 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 18 */     if (!this.mob.isAlive()) {
/* 19 */       return false;
/*    */     }
/* 21 */     if (this.mob.isInWater()) {
/* 22 */       return false;
/*    */     }
/* 24 */     if (!this.mob.onGround()) {
/* 25 */       return false;
/*    */     }
/* 27 */     if (this.mob.hurtMarked) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     Player trader = this.mob.getTradingPlayer();
/* 32 */     if (trader == null)
/*    */     {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (this.mob.distanceToSqr(trader) > 16.0D)
/*    */     {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public void start() { this.mob.getNavigation().stop(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public void stop() { this.mob.setTradingPlayer(null); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\TradeWithPlayerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */