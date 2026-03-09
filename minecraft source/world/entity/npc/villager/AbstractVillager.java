/*     */ package net.minecraft.world.entity.npc.villager;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.npc.Npc;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractVillager
/*     */   extends AgeableMob
/*     */   implements Npc, Merchant, InventoryCarrier
/*     */ {
/*  44 */   private static final EntityDataAccessor<Integer> DATA_UNHAPPY_COUNTER = SynchedEntityData.defineId(AbstractVillager.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int VILLAGER_SLOT_OFFSET = 300;
/*     */   
/*     */   private static final int VILLAGER_INVENTORY_SIZE = 8;
/*     */   
/*     */   private Player tradingPlayer;
/*     */   protected MerchantOffers offers;
/*  52 */   private final SimpleContainer inventory = new SimpleContainer(8);
/*     */   
/*     */   public AbstractVillager(EntityType<? extends AbstractVillager> type, Level level) {
/*  55 */     super(type, level);
/*  56 */     setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
/*  57 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/*  62 */     if (groupData == null) {
/*  63 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false);
/*     */     }
/*     */     
/*  66 */     return super.finalizeSpawn(level, difficulty, spawnReason, ageableMobGroupData);
/*     */   }
/*     */ 
/*     */   
/*  70 */   public int getUnhappyCounter() { return ((Integer)this.entityData.get(DATA_UNHAPPY_COUNTER)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public void setUnhappyCounter(int value) { this.entityData.set(DATA_UNHAPPY_COUNTER, Integer.valueOf(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public int getVillagerXp() { return 0; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  84 */     super.defineSynchedData(entityData);
/*  85 */     entityData.define(DATA_UNHAPPY_COUNTER, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public void setTradingPlayer(Player player) { this.tradingPlayer = player; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public Player getTradingPlayer() { return this.tradingPlayer; }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public boolean isTrading() { return (this.tradingPlayer != null); }
/*     */ 
/*     */   
/*     */   public MerchantOffers getOffers() {
/*     */     ServerLevel serverLevel;
/* 104 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 105 */     else { throw new IllegalStateException("Cannot load Villager offers on the client"); }
/*     */     
/* 107 */     if (this.offers == null) {
/* 108 */       this.offers = new MerchantOffers();
/* 109 */       updateTrades(serverLevel);
/*     */     } 
/* 111 */     return this.offers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideOffers(MerchantOffers offers) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideXp(int xp) {}
/*     */ 
/*     */   
/*     */   public void notifyTrade(MerchantOffer offer) {
/* 124 */     offer.increaseUses();
/* 125 */     this.ambientSoundTime = -getAmbientSoundInterval();
/*     */     
/* 127 */     rewardTradeXp(offer);
/*     */     
/* 129 */     if (this.tradingPlayer instanceof ServerPlayer) {
/* 130 */       CriteriaTriggers.TRADE.trigger((ServerPlayer)this.tradingPlayer, this, offer.getResult());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void rewardTradeXp(MerchantOffer paramMerchantOffer);
/*     */ 
/*     */   
/* 138 */   public boolean showProgressBar() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void notifyTradeUpdated(ItemStack itemStack) {
/* 143 */     if (!level().isClientSide() && this.ambientSoundTime > -getAmbientSoundInterval() + 20) {
/* 144 */       this.ambientSoundTime = -getAmbientSoundInterval();
/* 145 */       makeSound(getTradeUpdatedSound(!itemStack.isEmpty()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public SoundEvent getNotifyTradeSound() { return SoundEvents.VILLAGER_YES; }
/*     */ 
/*     */ 
/*     */   
/* 155 */   protected SoundEvent getTradeUpdatedSound(boolean validTrade) { return validTrade ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO; }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public void playCelebrateSound() { makeSound(SoundEvents.VILLAGER_CELEBRATE); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 164 */     super.addAdditionalSaveData(output);
/*     */     
/* 166 */     if (!level().isClientSide()) {
/* 167 */       MerchantOffers offers = getOffers();
/* 168 */       if (!offers.isEmpty()) {
/* 169 */         output.store("Offers", MerchantOffers.CODEC, offers);
/*     */       }
/*     */     } 
/* 172 */     writeInventoryToTag(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 177 */     super.readAdditionalSaveData(input);
/*     */     
/* 179 */     this.offers = (MerchantOffers)input.read("Offers", MerchantOffers.CODEC).orElse(null);
/*     */     
/* 181 */     readInventoryFromTag(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 186 */     stopTrading();
/* 187 */     return super.teleport(transition);
/*     */   }
/*     */ 
/*     */   
/* 191 */   protected void stopTrading() { setTradingPlayer(null); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void die(DamageSource source) {
/* 196 */     super.die(source);
/* 197 */     stopTrading();
/*     */   }
/*     */   
/*     */   protected void addParticlesAroundSelf(ParticleOptions particle) {
/* 201 */     for (int i = 0; i < 5; i++) {
/* 202 */       double xa = this.random.nextGaussian() * 0.02D;
/* 203 */       double ya = this.random.nextGaussian() * 0.02D;
/* 204 */       double za = this.random.nextGaussian() * 0.02D;
/* 205 */       level().addParticle(particle, getRandomX(1.0D), getRandomY() + 1.0D, getRandomZ(1.0D), xa, ya, za);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 211 */   public boolean canBeLeashed() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   public SimpleContainer getInventory() { return this.inventory; }
/*     */ 
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 221 */     int inventorySlot = slot - 300;
/* 222 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize()) {
/* 223 */       return this.inventory.getSlot(inventorySlot);
/*     */     }
/* 225 */     return super.getSlot(slot);
/*     */   }
/*     */   
/*     */   protected abstract void updateTrades(ServerLevel paramServerLevel);
/*     */   
/*     */   protected void addOffersFromItemListings(ServerLevel level, MerchantOffers merchantOffers, ItemListing[] itemListings, int numberOfOffers) {
/* 231 */     ArrayList<VillagerTrades.ItemListing> potentialOffers = Lists.newArrayList(itemListings);
/* 232 */     int offersFound = 0;
/* 233 */     while (offersFound < numberOfOffers && !potentialOffers.isEmpty()) {
/* 234 */       MerchantOffer offer = ((VillagerTrades.ItemListing)potentialOffers.remove(this.random.nextInt(potentialOffers.size()))).getOffer(level, this, this.random);
/* 235 */       if (offer != null) {
/* 236 */         merchantOffers.add(offer);
/* 237 */         offersFound++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getRopeHoldPosition(float partialTickTime) {
/* 244 */     float yRot = Mth.lerp(partialTickTime, this.yBodyRotO, this.yBodyRot) * 0.017453292F;
/* 245 */     Vec3 offset = new Vec3(0.0D, getBoundingBox().getYsize() - 1.0D, 0.2D);
/* 246 */     return getPosition(partialTickTime).add(offset.yRot(-yRot));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 251 */   public boolean isClientSide() { return level().isClientSide(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 256 */   public boolean stillValid(Player player) { return (getTradingPlayer() == player && isAlive() && player.isWithinEntityInteractionRange(this, 4.0D)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\villager\AbstractVillager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */