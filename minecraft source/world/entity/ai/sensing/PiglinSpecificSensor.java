/*     */ package net.minecraft.world.entity.ai.sensing;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinBrute;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinSpecificSensor
/*     */   extends Sensor<LivingEntity>
/*     */ {
/*  36 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, new MemoryModuleType[] { MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doTick(ServerLevel level, LivingEntity body) {
/*  57 */     Brain<?> brain = body.getBrain();
/*     */     
/*  59 */     brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, body));
/*     */     
/*  61 */     Optional<Mob> nemesis = Optional.empty();
/*  62 */     Optional<Hoglin> huntableHoglin = Optional.empty();
/*  63 */     Optional<Hoglin> babyHoglin = Optional.empty();
/*  64 */     Optional<Piglin> babyPiglin = Optional.empty();
/*  65 */     Optional<LivingEntity> zombified = Optional.empty();
/*  66 */     Optional<Player> playerNotWearingGold = Optional.empty();
/*  67 */     Optional<Player> playerHoldingWantedItem = Optional.empty();
/*  68 */     int visibleAdultHoglinCount = 0;
/*     */     
/*  70 */     List<AbstractPiglin> visibleAdultPiglins = Lists.newArrayList();
/*  71 */     List<AbstractPiglin> adultPiglins = Lists.newArrayList();
/*     */ 
/*     */     
/*  74 */     NearestVisibleLivingEntities visibleLivingEntities = (NearestVisibleLivingEntities)brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
/*  75 */     for (LivingEntity entity : visibleLivingEntities.findAll(ignored -> true)) {
/*  76 */       if (entity instanceof Hoglin) { Hoglin hoglin = (Hoglin)entity;
/*  77 */         if (hoglin.isBaby() && babyHoglin.isEmpty()) {
/*  78 */           babyHoglin = Optional.of(hoglin); continue;
/*  79 */         }  if (hoglin.isAdult()) {
/*  80 */           visibleAdultHoglinCount++;
/*  81 */           if (huntableHoglin.isEmpty() && hoglin.canBeHunted())
/*  82 */             huntableHoglin = Optional.of(hoglin); 
/*     */         }  continue; }
/*     */       
/*  85 */       if (entity instanceof PiglinBrute) { PiglinBrute piglinBrute = (PiglinBrute)entity;
/*  86 */         visibleAdultPiglins.add(piglinBrute); continue; }
/*  87 */        if (entity instanceof Piglin) { Piglin piglin = (Piglin)entity;
/*  88 */         if (piglin.isBaby() && babyPiglin.isEmpty()) {
/*  89 */           babyPiglin = Optional.of(piglin); continue;
/*  90 */         }  if (piglin.isAdult())
/*  91 */           visibleAdultPiglins.add(piglin);  continue; }
/*     */       
/*  93 */       if (entity instanceof Player) { Player player = (Player)entity;
/*  94 */         if (playerNotWearingGold.isEmpty() && !PiglinAi.isWearingSafeArmor(player) && body.canAttack(entity)) {
/*  95 */           playerNotWearingGold = Optional.of(player);
/*     */         }
/*  97 */         if (playerHoldingWantedItem.isEmpty() && !player.isSpectator() && PiglinAi.isPlayerHoldingLovedItem(player))
/*  98 */           playerHoldingWantedItem = Optional.of(player);  continue; }
/*     */       
/* 100 */       if (nemesis.isEmpty() && (entity instanceof net.minecraft.world.entity.monster.skeleton.WitherSkeleton || entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss)) {
/* 101 */         nemesis = Optional.of((Mob)entity); continue;
/* 102 */       }  if (zombified.isEmpty() && PiglinAi.isZombified(entity.getType())) {
/* 103 */         zombified = Optional.of(entity);
/*     */       }
/*     */     } 
/*     */     
/* 107 */     List<LivingEntity> livingEntities = (List)brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of());
/* 108 */     for (LivingEntity entity : livingEntities) {
/* 109 */       if (entity instanceof AbstractPiglin) { AbstractPiglin piglin = (AbstractPiglin)entity; if (piglin.isAdult()) {
/* 110 */           adultPiglins.add(piglin);
/*     */         } }
/*     */     
/*     */     } 
/* 114 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, nemesis);
/* 115 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, huntableHoglin);
/* 116 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, babyHoglin);
/* 117 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, zombified);
/* 118 */     brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, playerNotWearingGold);
/* 119 */     brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, playerHoldingWantedItem);
/* 120 */     brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, adultPiglins);
/* 121 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, visibleAdultPiglins);
/* 122 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, Integer.valueOf(visibleAdultPiglins.size()));
/* 123 */     brain.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, Integer.valueOf(visibleAdultHoglinCount));
/*     */   }
/*     */   
/*     */   private static Optional<BlockPos> findNearestRepellent(ServerLevel level, LivingEntity body) {
/* 127 */     return BlockPos.findClosestMatch(body
/* 128 */         .blockPosition(), 8, 4, pos -> 
/*     */ 
/*     */         
/* 131 */         isValidRepellent(level, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isValidRepellent(ServerLevel level, BlockPos pos) {
/* 136 */     BlockState blockState = level.getBlockState(pos);
/* 137 */     boolean isRepellent = blockState.is(BlockTags.PIGLIN_REPELLENTS);
/* 138 */     if (isRepellent && blockState.is(Blocks.SOUL_CAMPFIRE)) {
/* 139 */       return CampfireBlock.isLitCampfire(blockState);
/*     */     }
/* 141 */     return isRepellent;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\PiglinSpecificSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */