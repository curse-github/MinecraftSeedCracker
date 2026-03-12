/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GiveGiftToHero
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   private static final int THROW_GIFT_AT_DISTANCE = 5;
/*     */   private static final int MIN_TIME_BETWEEN_GIFTS = 600;
/*     */   private static final int MAX_TIME_BETWEEN_GIFTS = 6600;
/*     */   private static final int TIME_TO_DELAY_FOR_HEAD_TO_FINISH_TURNING = 20;
/*  30 */   private static final Map<ResourceKey<VillagerProfession>, ResourceKey<LootTable>> GIFTS = ImmutableMap.builder()
/*  31 */     .put(VillagerProfession.ARMORER, BuiltInLootTables.ARMORER_GIFT)
/*  32 */     .put(VillagerProfession.BUTCHER, BuiltInLootTables.BUTCHER_GIFT)
/*  33 */     .put(VillagerProfession.CARTOGRAPHER, BuiltInLootTables.CARTOGRAPHER_GIFT)
/*  34 */     .put(VillagerProfession.CLERIC, BuiltInLootTables.CLERIC_GIFT)
/*  35 */     .put(VillagerProfession.FARMER, BuiltInLootTables.FARMER_GIFT)
/*  36 */     .put(VillagerProfession.FISHERMAN, BuiltInLootTables.FISHERMAN_GIFT)
/*  37 */     .put(VillagerProfession.FLETCHER, BuiltInLootTables.FLETCHER_GIFT)
/*  38 */     .put(VillagerProfession.LEATHERWORKER, BuiltInLootTables.LEATHERWORKER_GIFT)
/*  39 */     .put(VillagerProfession.LIBRARIAN, BuiltInLootTables.LIBRARIAN_GIFT)
/*  40 */     .put(VillagerProfession.MASON, BuiltInLootTables.MASON_GIFT)
/*  41 */     .put(VillagerProfession.SHEPHERD, BuiltInLootTables.SHEPHERD_GIFT)
/*  42 */     .put(VillagerProfession.TOOLSMITH, BuiltInLootTables.TOOLSMITH_GIFT)
/*  43 */     .put(VillagerProfession.WEAPONSMITH, BuiltInLootTables.WEAPONSMITH_GIFT)
/*  44 */     .build();
/*     */   
/*     */   private static final float SPEED_MODIFIER = 0.5F;
/*     */   
/*  48 */   private int timeUntilNextGift = 600;
/*     */   private boolean giftGivenDuringThisRun;
/*     */   private long timeSinceStart;
/*     */   
/*     */   public GiveGiftToHero(int timeout) {
/*  53 */     super(
/*  54 */         ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryStatus.VALUE_PRESENT), timeout);
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
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/*  66 */     if (!isHeroVisible(body)) {
/*  67 */       return false;
/*     */     }
/*     */     
/*  70 */     if (this.timeUntilNextGift > 0) {
/*  71 */       this.timeUntilNextGift--;
/*  72 */       return false;
/*     */     } 
/*     */     
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Villager body, long timestamp) {
/*  80 */     this.giftGivenDuringThisRun = false;
/*  81 */     this.timeSinceStart = timestamp;
/*  82 */     Player player = (Player)getNearestTargetableHero(body).get();
/*  83 */     body.getBrain().setMemory(MemoryModuleType.INTERACTION_TARGET, player);
/*  84 */     BehaviorUtils.lookAtEntity(body, player);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) { return (isHeroVisible(body) && !this.giftGivenDuringThisRun); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Villager villager, long timestamp) {
/*  94 */     Player player = (Player)getNearestTargetableHero(villager).get();
/*  95 */     BehaviorUtils.lookAtEntity(villager, player);
/*     */     
/*  97 */     if (isWithinThrowingDistance(villager, player)) {
/*  98 */       if (timestamp - this.timeSinceStart > 20L) {
/*  99 */         throwGift(level, villager, player);
/* 100 */         this.giftGivenDuringThisRun = true;
/*     */       } 
/*     */     } else {
/* 103 */       BehaviorUtils.setWalkAndLookTargetMemories(villager, player, 0.5F, 5);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 109 */     this.timeUntilNextGift = calculateTimeUntilNextGift(level);
/* 110 */     body.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/* 111 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 112 */     body.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*     */   }
/*     */   
/*     */   private void throwGift(ServerLevel level, Villager villager, LivingEntity target) {
/* 116 */     villager.dropFromGiftLootTable(level, getLootTableToThrow(villager), (l, itemStack) -> 
/* 117 */         BehaviorUtils.throwItem(villager, itemStack, target.position()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResourceKey<LootTable> getLootTableToThrow(Villager villager) {
/* 122 */     if (villager.isBaby()) {
/* 123 */       return BuiltInLootTables.BABY_VILLAGER_GIFT;
/*     */     }
/* 125 */     Optional<ResourceKey<VillagerProfession>> profession = villager.getVillagerData().profession().unwrapKey();
/* 126 */     if (profession.isEmpty()) {
/* 127 */       return BuiltInLootTables.UNEMPLOYED_GIFT;
/*     */     }
/* 129 */     return (ResourceKey)GIFTS.getOrDefault(profession.get(), BuiltInLootTables.UNEMPLOYED_GIFT);
/*     */   }
/*     */ 
/*     */   
/* 133 */   private boolean isHeroVisible(Villager body) { return getNearestTargetableHero(body).isPresent(); }
/*     */ 
/*     */   
/*     */   private Optional<Player> getNearestTargetableHero(Villager body) {
/* 137 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER)
/* 138 */       .filter(this::isHero);
/*     */   }
/*     */ 
/*     */   
/* 142 */   private boolean isHero(Player player) { return player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE); }
/*     */ 
/*     */   
/*     */   private boolean isWithinThrowingDistance(Villager villager, Player player) {
/* 146 */     BlockPos playerPos = player.blockPosition();
/* 147 */     BlockPos villagerPos = villager.blockPosition();
/* 148 */     return villagerPos.closerThan(playerPos, 5.0D);
/*     */   }
/*     */ 
/*     */   
/* 152 */   private static int calculateTimeUntilNextGift(ServerLevel level) { return 600 + level.random.nextInt(6001); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GiveGiftToHero.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */