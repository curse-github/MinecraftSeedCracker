/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*    */ 
/*    */ public class LookAtTradingPlayerGoal
/*    */   extends LookAtPlayerGoal {
/*    */   private final AbstractVillager villager;
/*    */   
/*    */   public LookAtTradingPlayerGoal(AbstractVillager villager) {
/* 10 */     super(villager, net.minecraft.world.entity.player.Player.class, 8.0F);
/* 11 */     this.villager = villager;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 16 */     if (this.villager.isTrading()) {
/* 17 */       this.lookAt = this.villager.getTradingPlayer();
/* 18 */       return true;
/*    */     } 
/* 20 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\LookAtTradingPlayerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */