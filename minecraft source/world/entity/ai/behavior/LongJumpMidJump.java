/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class LongJumpMidJump
/*    */   extends Behavior<Mob> {
/*    */   public static final int TIME_OUT_DURATION = 100;
/*    */   private final UniformInt timeBetweenLongJumps;
/*    */   private final SoundEvent landingSound;
/*    */   
/*    */   public LongJumpMidJump(UniformInt timeBetweenLongJumps, SoundEvent landingSound) {
/* 21 */     super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_PRESENT), 100);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     this.timeBetweenLongJumps = timeBetweenLongJumps;
/* 27 */     this.landingSound = landingSound;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) { return !body.onGround(); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Mob body, long timestamp) {
/* 37 */     body.setDiscardFriction(true);
/* 38 */     body.setPose(Pose.LONG_JUMPING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, Mob body, long timestamp) {
/* 43 */     if (body.onGround()) {
/* 44 */       body.setDeltaMovement(body.getDeltaMovement().multiply(0.10000000149011612D, 1.0D, 0.10000000149011612D));
/* 45 */       level.playSound(null, body, this.landingSound, SoundSource.NEUTRAL, 2.0F, 1.0F);
/*    */     } 
/*    */     
/* 48 */     body.setDiscardFriction(false);
/* 49 */     body.setPose(Pose.STANDING);
/*    */     
/* 51 */     body.getBrain().eraseMemory(MemoryModuleType.LONG_JUMP_MID_JUMP);
/* 52 */     body.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, Integer.valueOf(this.timeBetweenLongJumps.sample(level.random)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LongJumpMidJump.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */