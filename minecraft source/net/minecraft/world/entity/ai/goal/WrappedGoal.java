/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ 
/*     */ public class WrappedGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Goal goal;
/*     */   private final int priority;
/*     */   private boolean isRunning;
/*     */   
/*     */   public WrappedGoal(int priority, Goal goal) {
/*  13 */     this.priority = priority;
/*  14 */     this.goal = goal;
/*     */   }
/*     */ 
/*     */   
/*  18 */   public boolean canBeReplacedBy(WrappedGoal goal) { return (isInterruptable() && goal.getPriority() < getPriority()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   public boolean canUse() { return this.goal.canUse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   public boolean canContinueToUse() { return this.goal.canContinueToUse(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   public boolean isInterruptable() { return this.goal.isInterruptable(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  38 */     if (this.isRunning) {
/*     */       return;
/*     */     }
/*  41 */     this.isRunning = true;
/*  42 */     this.goal.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  47 */     if (!this.isRunning) {
/*     */       return;
/*     */     }
/*  50 */     this.isRunning = false;
/*  51 */     this.goal.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public boolean requiresUpdateEveryTick() { return this.goal.requiresUpdateEveryTick(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   protected int adjustedTickDelay(int ticks) { return this.goal.adjustedTickDelay(ticks); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public void tick() { this.goal.tick(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public void setFlags(EnumSet<Goal.Flag> requiredControlFlags) { this.goal.setFlags(requiredControlFlags); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public EnumSet<Goal.Flag> getFlags() { return this.goal.getFlags(); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public boolean isRunning() { return this.isRunning; }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public int getPriority() { return this.priority; }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public Goal getGoal() { return this.goal; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  93 */     if (this == o) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (o == null || getClass() != o.getClass()) {
/*  97 */       return false;
/*     */     }
/*  99 */     return this.goal.equals(((WrappedGoal)o).goal);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public int hashCode() { return this.goal.hashCode(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\WrappedGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */