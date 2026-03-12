/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillagerMakeLove
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   private long birthTimestamp;
/*     */   
/*     */   public VillagerMakeLove() {
/*  30 */     super(
/*  31 */         ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT), 350, 350);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) { return isBreedingPossible(body); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) { return (timestamp <= this.birthTimestamp && isBreedingPossible(body)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Villager body, long timestamp) {
/*  52 */     AgeableMob breedTarget = (AgeableMob)body.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*     */     
/*  54 */     BehaviorUtils.lockGazeAndWalkToEachOther(body, breedTarget, 0.5F, 2);
/*     */     
/*  56 */     level.broadcastEntityEvent(breedTarget, (byte)18);
/*  57 */     level.broadcastEntityEvent(body, (byte)18);
/*     */     
/*  59 */     int duration = 275 + body.getRandom().nextInt(50);
/*  60 */     this.birthTimestamp = timestamp + duration;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/*  65 */     Villager target = (Villager)body.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*     */     
/*  67 */     if (body.distanceToSqr(target) > 5.0D) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     BehaviorUtils.lockGazeAndWalkToEachOther(body, target, 0.5F, 2);
/*     */     
/*  73 */     if (timestamp >= this.birthTimestamp) {
/*     */       
/*  75 */       body.eatAndDigestFood();
/*  76 */       target.eatAndDigestFood();
/*     */       
/*  78 */       tryToGiveBirth(level, body, target);
/*  79 */     } else if (body.getRandom().nextInt(35) == 0) {
/*  80 */       level.broadcastEntityEvent(target, (byte)12);
/*  81 */       level.broadcastEntityEvent(body, (byte)12);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void tryToGiveBirth(ServerLevel level, Villager body, Villager target) {
/*  87 */     Optional<BlockPos> childsBed = takeVacantBed(level, body);
/*  88 */     if (childsBed.isEmpty()) {
/*     */       
/*  90 */       level.broadcastEntityEvent(target, (byte)13);
/*  91 */       level.broadcastEntityEvent(body, (byte)13);
/*     */     } else {
/*  93 */       Optional<Villager> child = breed(level, body, target);
/*     */       
/*  95 */       if (child.isPresent()) {
/*  96 */         giveBedToChild(level, (Villager)child.get(), (BlockPos)childsBed.get());
/*     */       } else {
/*  98 */         level.getPoiManager().release((BlockPos)childsBed.get());
/*  99 */         level.debugSynchronizers().updatePoi((BlockPos)childsBed.get());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected void stop(ServerLevel level, Villager body, long timestamp) { body.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET); }
/*     */ 
/*     */   
/*     */   private boolean isBreedingPossible(Villager myBody) {
/* 110 */     Brain<Villager> brain = myBody.getBrain();
/*     */     
/* 112 */     Optional<AgeableMob> breedTarget = brain.getMemory(MemoryModuleType.BREED_TARGET).filter(entity -> (entity.getType() == EntityType.VILLAGER));
/* 113 */     if (breedTarget.isEmpty()) {
/* 114 */       return false;
/*     */     }
/* 116 */     return (BehaviorUtils.targetIsValid(brain, MemoryModuleType.BREED_TARGET, EntityType.VILLAGER) && myBody
/* 117 */       .canBreed() && ((AgeableMob)breedTarget
/* 118 */       .get()).canBreed());
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> takeVacantBed(ServerLevel level, Villager body) {
/* 122 */     return level.getPoiManager().take(p -> 
/* 123 */         p.is(PoiTypes.HOME), (poiType, poiPos) -> 
/* 124 */         canReach(body, poiPos, poiType), body
/* 125 */         .blockPosition(), 48);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canReach(Villager body, BlockPos poiPos, Holder<PoiType> poiType) {
/* 131 */     Path path = body.getNavigation().createPath(poiPos, ((PoiType)poiType.value()).validRange());
/* 132 */     return (path != null && path.canReach());
/*     */   }
/*     */   
/*     */   private Optional<Villager> breed(ServerLevel level, Villager source, Villager target) {
/* 136 */     Villager child = source.getBreedOffspring(level, target);
/* 137 */     if (child == null) {
/* 138 */       return Optional.empty();
/*     */     }
/* 140 */     source.setAge(6000);
/* 141 */     target.setAge(6000);
/* 142 */     child.setAge(-24000);
/* 143 */     child.snapTo(source.getX(), source.getY(), source.getZ(), 0.0F, 0.0F);
/*     */     
/* 145 */     level.addFreshEntityWithPassengers(child);
/* 146 */     level.broadcastEntityEvent(child, (byte)12);
/*     */     
/* 148 */     return Optional.of(child);
/*     */   }
/*     */   
/*     */   private void giveBedToChild(ServerLevel level, Villager child, BlockPos bedPos) {
/* 152 */     GlobalPos globalBedPos = GlobalPos.of(level.dimension(), bedPos);
/* 153 */     child.getBrain().setMemory(MemoryModuleType.HOME, globalBedPos);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillagerMakeLove.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */