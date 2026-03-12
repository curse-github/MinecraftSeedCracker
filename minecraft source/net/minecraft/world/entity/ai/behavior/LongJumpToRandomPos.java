/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.random.WeightedRandom;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LongJumpToRandomPos<E extends Mob> extends Behavior<E> {
/*     */   protected static final int FIND_JUMP_TRIES = 20;
/*     */   private static final int PREPARE_JUMP_DURATION = 40;
/*     */   protected static final int MIN_PATHFIND_DISTANCE_TO_VALID_JUMP = 8;
/*     */   private static final int TIME_OUT_DURATION = 200;
/*  37 */   private static final List<Integer> ALLOWED_ANGLES = Lists.newArrayList(new Integer[] { null, null, null, (new Integer[4][2] = (new Integer[4][1] = (new Integer[4][0] = Integer.valueOf(65)).valueOf(70)).valueOf(75)).valueOf(80) });
/*     */   
/*     */   private final UniformInt timeBetweenLongJumps;
/*     */   
/*     */   protected final int maxLongJumpHeight;
/*     */   protected final int maxLongJumpWidth;
/*     */   protected final float maxJumpVelocityMultiplier;
/*  44 */   protected List<PossibleJump> jumpCandidates = Lists.newArrayList();
/*  45 */   protected Optional<Vec3> initialPosition = Optional.empty();
/*     */   
/*     */   protected Vec3 chosenJump;
/*     */   protected int findJumpTries;
/*     */   protected long prepareJumpStart;
/*     */   private final Function<E, SoundEvent> getJumpSound;
/*     */   private final BiPredicate<E, BlockPos> acceptableLandingSpot;
/*     */   
/*  53 */   public LongJumpToRandomPos(UniformInt timeBetweenLongJumps, int maxLongJumpHeight, int maxLongJumpWidth, float maxJumpVelocityMultiplier, Function<E, SoundEvent> getJumpSound) { this(timeBetweenLongJumps, maxLongJumpHeight, maxLongJumpWidth, maxJumpVelocityMultiplier, getJumpSound, LongJumpToRandomPos::defaultAcceptableLandingSpot); }
/*     */ 
/*     */   
/*     */   public static <E extends Mob> boolean defaultAcceptableLandingSpot(E body, BlockPos targetPos) {
/*  57 */     Level level = body.level();
/*  58 */     BlockPos below = targetPos.below();
/*  59 */     return (level.getBlockState(below).isSolidRender() && body
/*  60 */       .getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic(body, targetPos)) == 0.0F);
/*     */   }
/*     */   
/*     */   public LongJumpToRandomPos(UniformInt timeBetweenLongJumps, int maxLongJumpHeight, int maxLongJumpWidth, float maxJumpVelocityMultiplier, Function<E, SoundEvent> getJumpSound, BiPredicate<E, BlockPos> acceptableLandingSpot) {
/*  64 */     super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 200);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.timeBetweenLongJumps = timeBetweenLongJumps;
/*  71 */     this.maxLongJumpHeight = maxLongJumpHeight;
/*  72 */     this.maxLongJumpWidth = maxLongJumpWidth;
/*  73 */     this.maxJumpVelocityMultiplier = maxJumpVelocityMultiplier;
/*  74 */     this.getJumpSound = getJumpSound;
/*  75 */     this.acceptableLandingSpot = acceptableLandingSpot;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/*  80 */     boolean canStart = (body.onGround() && !body.isInWater() && !body.isInLava() && !level.getBlockState(body.blockPosition()).is(Blocks.HONEY_BLOCK));
/*  81 */     if (!canStart) {
/*  82 */       body.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, Integer.valueOf(this.timeBetweenLongJumps.sample(level.random) / 2));
/*     */     }
/*  84 */     return canStart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/*  93 */     boolean isValid = (this.initialPosition.isPresent() && ((Vec3)this.initialPosition.get()).equals(body.position()) && this.findJumpTries > 0 && !body.isInWater() && (this.chosenJump != null || !this.jumpCandidates.isEmpty()));
/*     */     
/*  95 */     if (!isValid && body.getBrain().getMemory(MemoryModuleType.LONG_JUMP_MID_JUMP).isEmpty()) {
/*  96 */       body.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, Integer.valueOf(this.timeBetweenLongJumps.sample(level.random) / 2));
/*  97 */       body.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*     */     } 
/*  99 */     return isValid;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, E body, long timestamp) {
/* 104 */     this.chosenJump = null;
/* 105 */     this.findJumpTries = 20;
/* 106 */     this.initialPosition = Optional.of(body.position());
/*     */     
/* 108 */     BlockPos mobPos = body.blockPosition();
/* 109 */     int mobX = mobPos.getX();
/* 110 */     int mobY = mobPos.getY();
/* 111 */     int mobZ = mobPos.getZ();
/*     */     
/* 113 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       .jumpCandidates = (List)BlockPos.betweenClosedStream(mobX - this.maxLongJumpWidth, mobY - this.maxLongJumpHeight, mobZ - this.maxLongJumpWidth, mobX + this.maxLongJumpWidth, mobY + this.maxLongJumpHeight, mobZ + this.maxLongJumpWidth).filter(pos -> !pos.equals(mobPos)).map(pos -> new PossibleJump(pos.immutable(), Mth.ceil(mobPos.distSqr(pos)))).collect(Collectors.toCollection(Lists::newArrayList));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {
/* 128 */     if (this.chosenJump != null) {
/* 129 */       if (timestamp - this.prepareJumpStart >= 40L) {
/* 130 */         body.setYRot(body.yBodyRot);
/* 131 */         body.setDiscardFriction(true);
/* 132 */         double orgLength = this.chosenJump.length();
/*     */ 
/*     */         
/* 135 */         double lengthWithJumpBoost = orgLength + body.getJumpBoostPower();
/* 136 */         body.setDeltaMovement(this.chosenJump.scale(lengthWithJumpBoost / orgLength));
/*     */         
/* 138 */         body.getBrain().setMemory(MemoryModuleType.LONG_JUMP_MID_JUMP, Boolean.valueOf(true));
/* 139 */         level.playSound(null, body, (SoundEvent)this.getJumpSound.apply(body), SoundSource.NEUTRAL, 1.0F, 1.0F);
/*     */       } 
/*     */     } else {
/* 142 */       this.findJumpTries--;
/* 143 */       pickCandidate(level, body, timestamp);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void pickCandidate(ServerLevel level, E body, long timestamp) {
/* 148 */     while (!this.jumpCandidates.isEmpty()) {
/* 149 */       Optional<PossibleJump> optionalPosition = getJumpCandidate(level);
/* 150 */       if (optionalPosition.isEmpty()) {
/*     */         continue;
/*     */       }
/*     */       
/* 154 */       PossibleJump position = (PossibleJump)optionalPosition.get();
/*     */       
/* 156 */       BlockPos targetPos = position.targetPos();
/* 157 */       if (!isAcceptableLandingPosition(level, body, targetPos)) {
/*     */         continue;
/*     */       }
/*     */       
/* 161 */       Vec3 targetPosition = Vec3.atCenterOf(targetPos);
/* 162 */       Vec3 jumpVector = calculateOptimalJumpVector(body, targetPosition);
/* 163 */       if (jumpVector == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 167 */       body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(targetPos));
/*     */ 
/*     */       
/* 170 */       PathNavigation navigation = body.getNavigation();
/* 171 */       Path path = navigation.createPath(targetPos, 0, 8);
/* 172 */       if (path == null || !path.canReach()) {
/* 173 */         this.chosenJump = jumpVector;
/* 174 */         this.prepareJumpStart = timestamp;
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Optional<PossibleJump> getJumpCandidate(ServerLevel level) {
/* 181 */     Optional<PossibleJump> randomItem = WeightedRandom.getRandomItem(level.random, this.jumpCandidates, PossibleJump::weight);
/* 182 */     Objects.requireNonNull(this.jumpCandidates); randomItem.ifPresent(this.jumpCandidates::remove);
/* 183 */     return randomItem;
/*     */   }
/*     */   
/*     */   private boolean isAcceptableLandingPosition(ServerLevel level, E body, BlockPos targetPos) {
/* 187 */     BlockPos bodyPos = body.blockPosition();
/* 188 */     int mobX = bodyPos.getX();
/* 189 */     int mobZ = bodyPos.getZ();
/*     */     
/* 191 */     if (mobX == targetPos.getX() && mobZ == targetPos.getZ()) {
/* 192 */       return false;
/*     */     }
/* 194 */     return this.acceptableLandingSpot.test(body, targetPos);
/*     */   }
/*     */   
/*     */   protected Vec3 calculateOptimalJumpVector(Mob body, Vec3 targetPos) {
/* 198 */     List<Integer> allowedAngles = Lists.newArrayList(ALLOWED_ANGLES);
/* 199 */     Collections.shuffle(allowedAngles);
/*     */     
/* 201 */     float maxJumpVelocity = (float)(body.getAttributeValue(Attributes.JUMP_STRENGTH) * this.maxJumpVelocityMultiplier);
/*     */     
/* 203 */     for (Iterator iterator = allowedAngles.iterator(); iterator.hasNext(); ) { int angle = ((Integer)iterator.next()).intValue();
/*     */       
/* 205 */       Optional<Vec3> velocityVector = LongJumpUtil.calculateJumpVectorForAngle(body, targetPos, maxJumpVelocity, angle, true);
/* 206 */       if (velocityVector.isPresent()) {
/* 207 */         return (Vec3)velocityVector.get();
/*     */       } }
/*     */ 
/*     */     
/* 211 */     return null;
/*     */   }
/*     */   public static final class PossibleJump extends Record { private final BlockPos targetPos; private final int weight;
/* 214 */     public PossibleJump(BlockPos targetPos, int weight) { this.targetPos = targetPos; this.weight = weight; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #214	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 214 */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump; } public BlockPos targetPos() { return this.targetPos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #214	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #214	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/behavior/LongJumpToRandomPos$PossibleJump;
/* 214 */       //   0	8	1	o	Ljava/lang/Object; } public int weight() { return this.weight; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LongJumpToRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */