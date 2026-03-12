/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.util.TimeUtil;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class TickRateManager
/*    */ {
/*    */   public static final float MIN_TICKRATE = 1.0F;
/* 10 */   protected float tickrate = 20.0F;
/* 11 */   protected long nanosecondsPerTick = TimeUtil.NANOSECONDS_PER_SECOND / 20L;
/* 12 */   protected int frozenTicksToRun = 0;
/*    */   protected boolean runGameElements = true;
/*    */   protected boolean isFrozen = false;
/*    */   
/*    */   public void setTickRate(float rate) {
/* 17 */     this.tickrate = Math.max(rate, 1.0F);
/* 18 */     this.nanosecondsPerTick = (long)(TimeUtil.NANOSECONDS_PER_SECOND / this.tickrate);
/*    */   }
/*    */ 
/*    */   
/* 22 */   public float tickrate() { return this.tickrate; }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public float millisecondsPerTick() { return (float)this.nanosecondsPerTick / (float)TimeUtil.NANOSECONDS_PER_MILLISECOND; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public long nanosecondsPerTick() { return this.nanosecondsPerTick; }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public boolean runsNormally() { return this.runGameElements; }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public boolean isSteppingForward() { return (this.frozenTicksToRun > 0); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void setFrozenTicksToRun(int timeout) { this.frozenTicksToRun = timeout; }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public int frozenTicksToRun() { return this.frozenTicksToRun; }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public void setFrozen(boolean state) { this.isFrozen = state; }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public boolean isFrozen() { return this.isFrozen; }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 58 */     this.runGameElements = (!this.isFrozen || this.frozenTicksToRun > 0);
/* 59 */     if (this.frozenTicksToRun > 0) {
/* 60 */       this.frozenTicksToRun--;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 65 */   public boolean isEntityFrozen(Entity entity) { return (!runsNormally() && !(entity instanceof net.minecraft.world.entity.player.Player) && entity.countPlayerPassengers() <= 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\TickRateManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */