/*     */ package net.minecraft.world.entity.npc.wanderingtrader;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ class WanderToPositionGoal
/*     */   extends Goal
/*     */ {
/*     */   final WanderingTrader trader;
/*     */   final double stopDistance;
/*     */   final double speedModifier;
/*     */   
/*     */   WanderToPositionGoal(WanderingTrader trader, double stopDistance, double speedModifier) {
/* 230 */     this.trader = trader;
/* 231 */     this.stopDistance = stopDistance;
/* 232 */     this.speedModifier = speedModifier;
/* 233 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 238 */     this.trader.setWanderTarget(null);
/* 239 */     WanderingTrader.access$000(WanderingTrader.this).stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 244 */     BlockPos wanderPosition = this.trader.getWanderTarget();
/* 245 */     return (wanderPosition != null && isTooFarAway(wanderPosition, this.stopDistance));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 250 */     BlockPos wanderPosition = this.trader.getWanderTarget();
/* 251 */     if (wanderPosition != null && WanderingTrader.access$100(WanderingTrader.this).isDone()) {
/* 252 */       if (isTooFarAway(wanderPosition, 10.0D)) {
/*     */         
/* 254 */         Vec3 dir = (new Vec3(wanderPosition.getX() - this.trader.getX(), wanderPosition.getY() - this.trader.getY(), wanderPosition.getZ() - this.trader.getZ())).normalize();
/* 255 */         Vec3 targetPos = dir.scale(10.0D).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
/* 256 */         WanderingTrader.access$200(WanderingTrader.this).moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
/*     */       } else {
/* 258 */         WanderingTrader.access$300(WanderingTrader.this).moveTo(wanderPosition.getX(), wanderPosition.getY(), wanderPosition.getZ(), this.speedModifier);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 264 */   private boolean isTooFarAway(BlockPos pos, double distance) { return !pos.closerToCenterThan(this.trader.position(), distance); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\wanderingtrader\WanderingTrader$WanderToPositionGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */