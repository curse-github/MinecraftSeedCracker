/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArmadilloBallUp
/*     */   extends Behavior<Armadillo>
/*     */ {
/* 156 */   static final int BALL_UP_STAY_IN_STATE = 5 * TimeUtil.SECONDS_PER_MINUTE * 20;
/*     */   
/*     */   static final int TICKS_DELAY_TO_DETERMINE_IF_DANGER_IS_STILL_AROUND = 5;
/*     */   static final int DANGER_DETECTED_RECENTLY_DANGER_THRESHOLD = 75;
/* 160 */   int nextPeekTimer = 0;
/*     */   
/*     */   boolean dangerWasAround;
/*     */   
/* 164 */   public ArmadilloBallUp() { super(Map.of(), BALL_UP_STAY_IN_STATE); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Armadillo body, long timestamp) {
/* 169 */     super.tick(level, body, timestamp);
/* 170 */     if (this.nextPeekTimer > 0) {
/* 171 */       this.nextPeekTimer--;
/*     */     }
/* 173 */     if (body.shouldSwitchToScaredState()) {
/* 174 */       body.switchToState(Armadillo.ArmadilloState.SCARED);
/* 175 */       if (body.onGround()) {
/* 176 */         body.playSound(SoundEvents.ARMADILLO_LAND);
/*     */       }
/*     */       return;
/*     */     } 
/* 180 */     Armadillo.ArmadilloState state = body.getState();
/* 181 */     long dangerTickCounter = body.getBrain().getTimeUntilExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY);
/* 182 */     boolean dangerIsAround = (dangerTickCounter > 75L);
/* 183 */     if (dangerIsAround != this.dangerWasAround) {
/* 184 */       this.nextPeekTimer = pickNextPeekTimer(body);
/*     */     }
/* 186 */     this.dangerWasAround = dangerIsAround;
/* 187 */     if (state == Armadillo.ArmadilloState.SCARED) {
/* 188 */       if (this.nextPeekTimer == 0 && body.onGround() && dangerIsAround) {
/* 189 */         level.broadcastEntityEvent(body, (byte)64);
/* 190 */         this.nextPeekTimer = pickNextPeekTimer(body);
/*     */       } 
/*     */ 
/*     */       
/* 194 */       if (dangerTickCounter < Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
/* 195 */         body.playSound(SoundEvents.ARMADILLO_UNROLL_START);
/* 196 */         body.switchToState(Armadillo.ArmadilloState.UNROLLING);
/*     */       } 
/* 198 */     } else if (state == Armadillo.ArmadilloState.UNROLLING && dangerTickCounter > Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
/* 199 */       body.switchToState(Armadillo.ArmadilloState.SCARED);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 204 */   private int pickNextPeekTimer(Armadillo body) { return Armadillo.ArmadilloState.SCARED.animationDuration() + body.getRandom().nextIntBetweenInclusive(100, 400); }
/*     */ 
/*     */ 
/*     */   
/* 208 */   protected boolean checkExtraStartConditions(ServerLevel level, Armadillo body) { return body.onGround(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   protected boolean canStillUse(ServerLevel level, Armadillo body, long timestamp) { return body.getState().isThreatened(); }
/*     */ 
/*     */ 
/*     */   
/* 217 */   protected void start(ServerLevel level, Armadillo body, long timestamp) { body.rollUp(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Armadillo body, long timestamp) {
/* 222 */     if (!body.canStayRolledUp())
/* 223 */       body.rollOut(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\armadillo\ArmadilloAi$ArmadilloBallUp.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */