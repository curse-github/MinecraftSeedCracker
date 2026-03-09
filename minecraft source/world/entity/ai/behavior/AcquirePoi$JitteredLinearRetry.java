/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class JitteredLinearRetry
/*     */ {
/*     */   private static final int MIN_INTERVAL_INCREASE = 40;
/*     */   private static final int MAX_INTERVAL_INCREASE = 80;
/*     */   private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
/*     */   private final RandomSource random;
/*     */   private long previousAttemptTimestamp;
/*     */   private long nextScheduledAttemptTimestamp;
/*     */   private int currentDelay;
/*     */   
/*     */   JitteredLinearRetry(RandomSource random, long firstAttemptTimestamp) {
/* 151 */     this.random = random;
/* 152 */     markAttempt(firstAttemptTimestamp);
/*     */   }
/*     */   
/*     */   public void markAttempt(long timestamp) {
/* 156 */     this.previousAttemptTimestamp = timestamp;
/* 157 */     int suggestedDelay = this.currentDelay + this.random.nextInt(40) + 40;
/* 158 */     this.currentDelay = Math.min(suggestedDelay, 400);
/* 159 */     this.nextScheduledAttemptTimestamp = timestamp + this.currentDelay;
/*     */   }
/*     */ 
/*     */   
/* 163 */   public boolean isStillValid(long timestamp) { return (timestamp - this.previousAttemptTimestamp < 400L); }
/*     */ 
/*     */ 
/*     */   
/* 167 */   public boolean shouldRetry(long timestamp) { return (timestamp >= this.nextScheduledAttemptTimestamp); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public String toString() { return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\AcquirePoi$JitteredLinearRetry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */