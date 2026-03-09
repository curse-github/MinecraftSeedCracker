/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class RandomLookAround
/*    */   extends Behavior<Mob>
/*    */ {
/*    */   private final IntProvider interval;
/*    */   private final float maxYaw;
/*    */   private final float minPitch;
/*    */   private final float pitchRange;
/*    */   
/*    */   public RandomLookAround(IntProvider interval, float maxYaw, float minPitch, float maxPitch) {
/* 24 */     super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.GAZE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT));
/* 25 */     if (minPitch > maxPitch) {
/* 26 */       throw new IllegalArgumentException("Minimum pitch is larger than maximum pitch! " + minPitch + " > " + maxPitch);
/*    */     }
/* 28 */     this.interval = interval;
/* 29 */     this.maxYaw = maxYaw;
/* 30 */     this.minPitch = minPitch;
/* 31 */     this.pitchRange = maxPitch - minPitch;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Mob body, long timestamp) {
/* 36 */     RandomSource random = body.getRandom();
/*    */     
/* 38 */     float pitch = Mth.clamp(random.nextFloat() * this.pitchRange + this.minPitch, -90.0F, 90.0F);
/* 39 */     float rotation = Mth.wrapDegrees(body.getYRot() + 2.0F * random.nextFloat() * this.maxYaw - this.maxYaw);
/* 40 */     Vec3 newLookVec = Vec3.directionFromRotation(pitch, rotation);
/*    */     
/* 42 */     body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(body.getEyePosition().add(newLookVec)));
/* 43 */     body.getBrain().setMemory(MemoryModuleType.GAZE_COOLDOWN_TICKS, Integer.valueOf(this.interval.sample(random)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RandomLookAround.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */