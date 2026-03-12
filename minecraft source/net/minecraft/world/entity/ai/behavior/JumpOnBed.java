/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JumpOnBed
/*     */   extends Behavior<Mob>
/*     */ {
/*     */   private static final int MAX_TIME_TO_REACH_BED = 100;
/*     */   private static final int MIN_JUMPS = 3;
/*     */   private static final int MAX_JUMPS = 6;
/*     */   private static final int COOLDOWN_BETWEEN_JUMPS = 5;
/*     */   private final float speedModifier;
/*     */   private BlockPos targetBed;
/*     */   private int remainingTimeToReachBed;
/*     */   private int remainingJumps;
/*     */   private int remainingCooldownUntilNextJump;
/*     */   
/*     */   public JumpOnBed(float speedModifier) {
/*  34 */     super(ImmutableMap.of(MemoryModuleType.NEAREST_BED, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*     */ 
/*     */ 
/*     */     
/*  38 */     this.speedModifier = speedModifier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  43 */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) { return (body.isBaby() && nearBed(level, body)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Mob body, long timestamp) {
/*  48 */     super.start(level, body, timestamp);
/*     */     
/*  50 */     getNearestBed(body).ifPresent(targetBed -> {
/*  51 */           this.targetBed = targetBed;
/*  52 */           this.remainingTimeToReachBed = 100;
/*  53 */           this.remainingJumps = 3 + level.random.nextInt(4);
/*  54 */           this.remainingCooldownUntilNextJump = 0;
/*  55 */           startWalkingTowardsBed(body, targetBed);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Mob body, long timestamp) {
/*  61 */     super.stop(level, body, timestamp);
/*     */     
/*  63 */     this.targetBed = null;
/*  64 */     this.remainingTimeToReachBed = 0;
/*  65 */     this.remainingJumps = 0;
/*  66 */     this.remainingCooldownUntilNextJump = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/*  71 */     return (body.isBaby() && this.targetBed != null && 
/*     */       
/*  73 */       isBed(level, this.targetBed) && 
/*  74 */       !tiredOfWalking(level, body) && 
/*  75 */       !tiredOfJumping(level, body));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected boolean timedOut(long timestamp) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/*  85 */     if (!onOrOverBed(level, body)) {
/*  86 */       this.remainingTimeToReachBed--;
/*     */       
/*     */       return;
/*     */     } 
/*  90 */     if (this.remainingCooldownUntilNextJump > 0) {
/*  91 */       this.remainingCooldownUntilNextJump--;
/*     */       
/*     */       return;
/*     */     } 
/*  95 */     if (onBedSurface(level, body)) {
/*  96 */       body.getJumpControl().jump();
/*  97 */       this.remainingJumps--;
/*  98 */       this.remainingCooldownUntilNextJump = 5;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 103 */   private void startWalkingTowardsBed(Mob body, BlockPos bedPos) { body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(bedPos, this.speedModifier, 0)); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   private boolean nearBed(ServerLevel level, Mob body) { return (onOrOverBed(level, body) || getNearestBed(body).isPresent()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean onOrOverBed(ServerLevel level, Mob body) {
/* 114 */     BlockPos bodyPos = body.blockPosition();
/* 115 */     BlockPos oneBelow = bodyPos.below();
/* 116 */     return (isBed(level, bodyPos) || isBed(level, oneBelow));
/*     */   }
/*     */ 
/*     */   
/* 120 */   private boolean onBedSurface(ServerLevel level, Mob body) { return isBed(level, body.blockPosition()); }
/*     */ 
/*     */ 
/*     */   
/* 124 */   private boolean isBed(ServerLevel level, BlockPos bodyPos) { return level.getBlockState(bodyPos).is(BlockTags.BEDS); }
/*     */ 
/*     */ 
/*     */   
/* 128 */   private Optional<BlockPos> getNearestBed(Mob body) { return body.getBrain().getMemory(MemoryModuleType.NEAREST_BED); }
/*     */ 
/*     */ 
/*     */   
/* 132 */   private boolean tiredOfWalking(ServerLevel level, Mob body) { return (!onOrOverBed(level, body) && this.remainingTimeToReachBed <= 0); }
/*     */ 
/*     */ 
/*     */   
/* 136 */   private boolean tiredOfJumping(ServerLevel level, Mob body) { return (onOrOverBed(level, body) && this.remainingJumps <= 0); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\JumpOnBed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */