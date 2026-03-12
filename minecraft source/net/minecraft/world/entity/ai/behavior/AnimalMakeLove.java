/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnimalMakeLove
/*     */   extends Behavior<Animal>
/*     */ {
/*     */   private static final int BREED_RANGE = 3;
/*     */   private static final int MIN_DURATION = 60;
/*     */   private static final int MAX_DURATION = 110;
/*     */   private final EntityType<? extends Animal> partnerType;
/*     */   private final float speedModifier;
/*     */   private final int closeEnoughDistance;
/*     */   private static final int DEFAULT_CLOSE_ENOUGH_DISTANCE = 2;
/*     */   private long spawnChildAtTime;
/*     */   
/*  31 */   public AnimalMakeLove(EntityType<? extends Animal> partnerType) { this(partnerType, 1.0F, 2); }
/*     */   
/*     */   public AnimalMakeLove(EntityType<? extends Animal> partnerType, float speedModifier, int closeEnoughDistance) {
/*  34 */     super(
/*  35 */         ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), 110);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     this.partnerType = partnerType;
/*  44 */     this.speedModifier = speedModifier;
/*  45 */     this.closeEnoughDistance = closeEnoughDistance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected boolean checkExtraStartConditions(ServerLevel level, Animal body) { return (body.isInLove() && findValidBreedPartner(body).isPresent()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Animal body, long timestamp) {
/*  55 */     Animal partner = (Animal)findValidBreedPartner(body).get();
/*     */     
/*  57 */     body.getBrain().setMemory(MemoryModuleType.BREED_TARGET, partner);
/*  58 */     partner.getBrain().setMemory(MemoryModuleType.BREED_TARGET, body);
/*     */     
/*  60 */     BehaviorUtils.lockGazeAndWalkToEachOther(body, partner, this.speedModifier, this.closeEnoughDistance);
/*     */     
/*  62 */     int duration = 60 + body.getRandom().nextInt(50);
/*  63 */     this.spawnChildAtTime = timestamp + duration;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Animal body, long timestamp) {
/*  68 */     if (!hasBreedTargetOfRightType(body)) {
/*  69 */       return false;
/*     */     }
/*  71 */     Animal partner = getBreedTarget(body);
/*     */     
/*  73 */     return (partner.isAlive() && body
/*  74 */       .canMate(partner) && 
/*  75 */       BehaviorUtils.entityIsVisible(body.getBrain(), partner) && timestamp <= this.spawnChildAtTime && 
/*     */       
/*  77 */       !body.isPanicking() && !partner.isPanicking());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Animal body, long timestamp) {
/*  82 */     Animal partner = getBreedTarget(body);
/*     */     
/*  84 */     BehaviorUtils.lockGazeAndWalkToEachOther(body, partner, this.speedModifier, this.closeEnoughDistance);
/*  85 */     if (!body.closerThan(partner, 3.0D)) {
/*     */       return;
/*     */     }
/*  88 */     if (timestamp >= this.spawnChildAtTime) {
/*  89 */       body.spawnChildFromBreeding(level, partner);
/*  90 */       body.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*  91 */       partner.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Animal body, long timestamp) {
/*  97 */     body.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*  98 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*  99 */     body.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/* 100 */     this.spawnChildAtTime = 0L;
/*     */   }
/*     */ 
/*     */   
/* 104 */   private Animal getBreedTarget(Animal body) { return (Animal)body.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get(); }
/*     */ 
/*     */   
/*     */   private boolean hasBreedTargetOfRightType(Animal body) {
/* 108 */     Brain<?> brain = body.getBrain();
/* 109 */     return (brain.hasMemoryValue(MemoryModuleType.BREED_TARGET) && ((AgeableMob)brain
/* 110 */       .getMemory(MemoryModuleType.BREED_TARGET).get()).getType() == this.partnerType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   private Optional<? extends Animal> findValidBreedPartner(Animal body) { Objects.requireNonNull(Animal.class); return ((NearestVisibleLivingEntities)body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get()).findClosest(entity -> { // Byte code:
/*     */           //   0: aload_2
/*     */           //   1: invokevirtual getType : ()Lnet/minecraft/world/entity/EntityType;
/*     */           //   4: aload_0
/*     */           //   5: getfield partnerType : Lnet/minecraft/world/entity/EntityType;
/*     */           //   8: if_acmpne -> 42
/*     */           //   11: aload_2
/*     */           //   12: instanceof net/minecraft/world/entity/animal/Animal
/*     */           //   15: ifeq -> 42
/*     */           //   18: aload_2
/*     */           //   19: checkcast net/minecraft/world/entity/animal/Animal
/*     */           //   22: astore_3
/*     */           //   23: aload_1
/*     */           //   24: aload_3
/*     */           //   25: invokevirtual canMate : (Lnet/minecraft/world/entity/animal/Animal;)Z
/*     */           //   28: ifeq -> 42
/*     */           //   31: aload_3
/*     */           //   32: invokevirtual isPanicking : ()Z
/*     */           //   35: ifne -> 42
/*     */           //   38: iconst_1
/*     */           //   39: goto -> 43
/*     */           //   42: iconst_0
/*     */           //   43: ireturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #116	-> 0
/*     */           //   #115	-> 1
/*     */           //   #116	-> 25
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   23	19	3	animal	Lnet/minecraft/world/entity/animal/Animal;
/*     */           //   0	44	0	this	Lnet/minecraft/world/entity/ai/behavior/AnimalMakeLove;
/*     */           //   0	44	1	body	Lnet/minecraft/world/entity/animal/Animal;
/* 117 */           //   0	44	2	entity	Lnet/minecraft/world/entity/LivingEntity; }).map(Animal.class::cast); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\AnimalMakeLove.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */