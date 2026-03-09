/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.gossip.GossipContainer;
/*     */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerDataHolder;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZombieVillager
/*     */   extends Zombie
/*     */   implements VillagerDataHolder
/*     */ {
/*  62 */   private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.BOOLEAN);
/*  63 */   private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(ZombieVillager.class, EntityDataSerializers.VILLAGER_DATA);
/*     */   
/*     */   private static final int VILLAGER_CONVERSION_WAIT_MIN = 3600;
/*     */   
/*     */   private static final int VILLAGER_CONVERSION_WAIT_MAX = 6000;
/*     */   private static final int MAX_SPECIAL_BLOCKS_COUNT = 14;
/*     */   private static final int SPECIAL_BLOCK_RADIUS = 4;
/*     */   private static final int NOT_CONVERTING = -1;
/*     */   private static final int DEFAULT_XP = 0;
/*  72 */   private static final Set<EntitySpawnReason> REASONS_NOT_TO_SET_TYPE = EnumSet.of(EntitySpawnReason.LOAD, new EntitySpawnReason[] { EntitySpawnReason.DIMENSION_TRAVEL, EntitySpawnReason.CONVERSION, EntitySpawnReason.SPAWN_ITEM_USE, EntitySpawnReason.SPAWNER, EntitySpawnReason.TRIAL_SPAWNER });
/*     */   
/*     */   private int villagerConversionTime;
/*     */   
/*     */   private UUID conversionStarter;
/*     */   
/*     */   private GossipContainer gossips;
/*     */   
/*     */   private MerchantOffers tradeOffers;
/*     */   
/*  82 */   private int villagerXp = 0;
/*     */ 
/*     */   
/*  85 */   public ZombieVillager(EntityType<? extends ZombieVillager> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  90 */     super.defineSynchedData(entityData);
/*     */     
/*  92 */     entityData.define(DATA_CONVERTING_ID, Boolean.valueOf(false));
/*  93 */     entityData.define(DATA_VILLAGER_DATA, initializeVillagerData());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  98 */     super.addAdditionalSaveData(output);
/*     */     
/* 100 */     output.store("VillagerData", VillagerData.CODEC, getVillagerData());
/*     */     
/* 102 */     output.storeNullable("Offers", MerchantOffers.CODEC, this.tradeOffers);
/*     */     
/* 104 */     output.storeNullable("Gossips", GossipContainer.CODEC, this.gossips);
/*     */     
/* 106 */     output.putInt("ConversionTime", isConverting() ? this.villagerConversionTime : -1);
/*     */     
/* 108 */     output.storeNullable("ConversionPlayer", UUIDUtil.CODEC, this.conversionStarter);
/*     */     
/* 110 */     output.putInt("Xp", this.villagerXp);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 115 */     super.readAdditionalSaveData(input);
/*     */     
/* 117 */     this.entityData.set(DATA_VILLAGER_DATA, (VillagerData)input.read("VillagerData", VillagerData.CODEC).orElseGet(this::initializeVillagerData));
/*     */     
/* 119 */     this.tradeOffers = (MerchantOffers)input.read("Offers", MerchantOffers.CODEC).orElse(null);
/*     */     
/* 121 */     this.gossips = (GossipContainer)input.read("Gossips", GossipContainer.CODEC).orElse(null);
/*     */     
/* 123 */     int conversionTime = input.getIntOr("ConversionTime", -1);
/* 124 */     if (conversionTime != -1) {
/* 125 */       UUID conversionStarter = (UUID)input.read("ConversionPlayer", UUIDUtil.CODEC).orElse(null);
/* 126 */       startConverting(conversionStarter, conversionTime);
/*     */     } else {
/* 128 */       getEntityData().set(DATA_CONVERTING_ID, Boolean.valueOf(false));
/* 129 */       this.villagerConversionTime = -1;
/*     */     } 
/*     */     
/* 132 */     this.villagerXp = input.getIntOr("Xp", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 137 */     if (!REASONS_NOT_TO_SET_TYPE.contains(spawnReason)) {
/* 138 */       setVillagerData(getVillagerData().withType(level.registryAccess(), VillagerType.byBiome(level.getBiome(blockPosition()))));
/*     */     }
/* 140 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   private VillagerData initializeVillagerData() {
/* 144 */     Optional<Holder.Reference<VillagerProfession>> profession = BuiltInRegistries.VILLAGER_PROFESSION.getRandom(this.random);
/* 145 */     VillagerData villagerData = Villager.createDefaultVillagerData();
/* 146 */     if (profession.isPresent()) {
/* 147 */       villagerData = villagerData.withProfession((Holder)profession.get());
/*     */     }
/* 149 */     return villagerData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 154 */     if (!level().isClientSide() && isAlive() && isConverting()) {
/* 155 */       int amount = getConversionProgress();
/*     */       
/* 157 */       this.villagerConversionTime -= amount;
/*     */       
/* 159 */       if (this.villagerConversionTime <= 0) {
/* 160 */         finishConversion((ServerLevel)level());
/*     */       }
/*     */     } 
/*     */     
/* 164 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 169 */     ItemStack itemStack = player.getItemInHand(hand);
/* 170 */     if (itemStack.is(Items.GOLDEN_APPLE)) {
/* 171 */       if (hasEffect(MobEffects.WEAKNESS)) {
/* 172 */         itemStack.consume(1, player);
/*     */         
/* 174 */         if (!level().isClientSide()) {
/* 175 */           startConverting(player.getUUID(), this.random.nextInt(2401) + 3600);
/*     */         }
/*     */ 
/*     */         
/* 179 */         return InteractionResult.SUCCESS_SERVER;
/*     */       } 
/* 181 */       return InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 184 */     return super.mobInteract(player, hand);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   protected boolean convertsInWater() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public boolean removeWhenFarAway(double distSqr) { return (!isConverting() && this.villagerXp == 0); }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public boolean isConverting() { return ((Boolean)getEntityData().get(DATA_CONVERTING_ID)).booleanValue(); }
/*     */ 
/*     */   
/*     */   private void startConverting(UUID player, int time) {
/* 202 */     this.conversionStarter = player;
/* 203 */     this.villagerConversionTime = time;
/* 204 */     getEntityData().set(DATA_CONVERTING_ID, Boolean.valueOf(true));
/*     */     
/* 206 */     removeEffect(MobEffects.WEAKNESS);
/* 207 */     addEffect(new MobEffectInstance(MobEffects.STRENGTH, time, Math.min(level().getDifficulty().getId() - 1, 0)));
/*     */     
/* 209 */     level().broadcastEntityEvent(this, (byte)16);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 214 */     if (id == 16) {
/* 215 */       if (!isSilent()) {
/* 216 */         level().playLocalSound(getX(), getEyeY(), getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
/*     */       }
/*     */       return;
/*     */     } 
/* 220 */     super.handleEntityEvent(id);
/*     */   }
/*     */   
/*     */   private void finishConversion(ServerLevel level) {
/* 224 */     convertTo(EntityType.VILLAGER, ConversionParams.single(this, false, false), villager -> {
/* 225 */           for (EquipmentSlot undroppedSlot : dropPreservedEquipment(level, ())) {
/* 226 */             SlotAccess offsetSlot = villager.getSlot(undroppedSlot.getIndex() + 300);
/* 227 */             if (offsetSlot != null) {
/* 228 */               offsetSlot.set(getItemBySlot(undroppedSlot));
/*     */             }
/*     */           } 
/*     */           
/* 232 */           villager.setVillagerData(getVillagerData());
/* 233 */           if (this.gossips != null) {
/* 234 */             villager.setGossips(this.gossips);
/*     */           }
/* 236 */           if (this.tradeOffers != null) {
/* 237 */             villager.setOffers(this.tradeOffers.copy());
/*     */           }
/* 239 */           villager.setVillagerXp(this.villagerXp);
/* 240 */           villager.finalizeSpawn(level, level.getCurrentDifficultyAt(villager.blockPosition()), EntitySpawnReason.CONVERSION, null);
/* 241 */           villager.refreshBrain(level);
/*     */           
/* 243 */           if (this.conversionStarter != null) {
/* 244 */             Player player = level.getPlayerByUUID(this.conversionStarter);
/* 245 */             if (player instanceof ServerPlayer) {
/* 246 */               CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((ServerPlayer)player, this, villager);
/* 247 */               level.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, player, villager);
/*     */             } 
/*     */           } 
/*     */           
/* 251 */           villager.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0));
/* 252 */           if (!isSilent()) {
/* 253 */             level.levelEvent(null, 1027, blockPosition(), 0);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 260 */   public void setVillagerConversionTime(int conversionTime) { this.villagerConversionTime = conversionTime; }
/*     */ 
/*     */   
/*     */   private int getConversionProgress() {
/* 264 */     int amount = 1;
/*     */     
/* 266 */     if (this.random.nextFloat() < 0.01F) {
/* 267 */       int specialBlocksCount = 0;
/*     */       
/* 269 */       BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*     */       
/* 271 */       for (int xx = (int)getX() - 4; xx < (int)getX() + 4 && specialBlocksCount < 14; xx++) {
/* 272 */         for (int yy = (int)getY() - 4; yy < (int)getY() + 4 && specialBlocksCount < 14; yy++) {
/* 273 */           for (int zz = (int)getZ() - 4; zz < (int)getZ() + 4 && specialBlocksCount < 14; zz++) {
/* 274 */             BlockState state = level().getBlockState(blockPos.set(xx, yy, zz));
/* 275 */             if (state.is(Blocks.IRON_BARS) || state.getBlock() instanceof net.minecraft.world.level.block.BedBlock) {
/* 276 */               if (this.random.nextFloat() < 0.3F) {
/* 277 */                 amount++;
/*     */               }
/* 279 */               specialBlocksCount++;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 285 */     return amount;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVoicePitch() {
/* 290 */     if (isBaby()) {
/* 291 */       return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F;
/*     */     }
/* 293 */     return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 298 */   public SoundEvent getAmbientSound() { return SoundEvents.ZOMBIE_VILLAGER_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 303 */   public SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ZOMBIE_VILLAGER_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 308 */   public SoundEvent getDeathSound() { return SoundEvents.ZOMBIE_VILLAGER_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 313 */   public SoundEvent getStepSound() { return SoundEvents.ZOMBIE_VILLAGER_STEP; }
/*     */ 
/*     */ 
/*     */   
/* 317 */   public void setTradeOffers(MerchantOffers tradeOffers) { this.tradeOffers = tradeOffers; }
/*     */ 
/*     */ 
/*     */   
/* 321 */   public void setGossips(GossipContainer gossips) { this.gossips = gossips; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVillagerData(VillagerData villagerData) {
/* 326 */     VillagerData currentData = getVillagerData();
/* 327 */     if (!currentData.profession().equals(villagerData.profession())) {
/* 328 */       this.tradeOffers = null;
/*     */     }
/*     */     
/* 331 */     this.entityData.set(DATA_VILLAGER_DATA, villagerData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 336 */   public VillagerData getVillagerData() { return (VillagerData)this.entityData.get(DATA_VILLAGER_DATA); }
/*     */ 
/*     */ 
/*     */   
/* 340 */   public int getVillagerXp() { return this.villagerXp; }
/*     */ 
/*     */ 
/*     */   
/* 344 */   public void setVillagerXp(int villagerXp) { this.villagerXp = villagerXp; }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 349 */     if (type == DataComponents.VILLAGER_VARIANT) {
/* 350 */       return (T)castComponentValue(type, getVillagerData().type());
/*     */     }
/*     */     
/* 353 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 358 */     applyImplicitComponentIfPresent(components, DataComponents.VILLAGER_VARIANT);
/* 359 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 364 */     if (type == DataComponents.VILLAGER_VARIANT) {
/* 365 */       Holder<VillagerType> variant = (Holder)castComponentValue(DataComponents.VILLAGER_VARIANT, value);
/* 366 */       setVillagerData(getVillagerData().withType(variant));
/* 367 */       return true;
/*     */     } 
/*     */     
/* 370 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\zombie\ZombieVillager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */