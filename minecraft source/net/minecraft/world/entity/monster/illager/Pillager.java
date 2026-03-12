/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Pillager
/*     */   extends AbstractIllager
/*     */   implements CrossbowAttackMob, InventoryCarrier
/*     */ {
/*  62 */   private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Pillager.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int INVENTORY_SIZE = 5;
/*     */   
/*     */   private static final int SLOT_OFFSET = 300;
/*  67 */   private final SimpleContainer inventory = new SimpleContainer(5);
/*     */ 
/*     */   
/*  70 */   public Pillager(EntityType<? extends Pillager> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  75 */     super.registerGoals();
/*     */     
/*  77 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  78 */     this.goalSelector.addGoal(1, new AvoidEntityGoal(this, net.minecraft.world.entity.monster.creaking.Creaking.class, 8.0F, 1.0D, 1.2D));
/*  79 */     this.goalSelector.addGoal(2, new Raider.HoldGroundAttackGoal(this, 10.0F));
/*  80 */     this.goalSelector.addGoal(3, new RangedCrossbowAttackGoal(this, 1.0D, 8.0F));
/*  81 */     this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
/*  82 */     this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 15.0F, 1.0F));
/*  83 */     this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, net.minecraft.world.entity.Mob.class, 15.0F));
/*     */     
/*  85 */     this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  86 */     this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.player.Player.class, true));
/*  87 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.npc.villager.AbstractVillager.class, false));
/*  88 */     this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, net.minecraft.world.entity.animal.golem.IronGolem.class, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  92 */     return Monster.createMonsterAttributes()
/*  93 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  94 */       .add(Attributes.MAX_HEALTH, 24.0D)
/*  95 */       .add(Attributes.ATTACK_DAMAGE, 5.0D)
/*  96 */       .add(Attributes.FOLLOW_RANGE, 32.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 101 */     super.defineSynchedData(entityData);
/*     */     
/* 103 */     entityData.define(IS_CHARGING_CROSSBOW, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public boolean canUseNonMeleeWeapon(ItemStack item) { return (item.getItem() == Items.CROSSBOW); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public boolean isChargingCrossbow() { return ((Boolean)this.entityData.get(IS_CHARGING_CROSSBOW)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public void setChargingCrossbow(boolean isCharging) { this.entityData.set(IS_CHARGING_CROSSBOW, Boolean.valueOf(isCharging)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public void onCrossbowAttackPerformed() { this.noActionTime = 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public TagKey<Item> getPreferredWeaponType() { return ItemTags.PILLAGER_PREFERRED_WEAPONS; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 132 */     super.addAdditionalSaveData(output);
/* 133 */     writeInventoryToTag(output);
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractIllager.IllagerArmPose getArmPose() {
/* 138 */     if (isChargingCrossbow())
/* 139 */       return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE; 
/* 140 */     if (isHolding(Items.CROSSBOW))
/* 141 */       return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD; 
/* 142 */     if (isAggressive()) {
/* 143 */       return AbstractIllager.IllagerArmPose.ATTACKING;
/*     */     }
/*     */     
/* 146 */     return AbstractIllager.IllagerArmPose.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 151 */     super.readAdditionalSaveData(input);
/*     */     
/* 153 */     readInventoryFromTag(input);
/*     */     
/* 155 */     setCanPickUpLoot(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public float getWalkTargetValue(BlockPos pos, LevelReader level) { return 0.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   public int getMaxSpawnClusterSize() { return 1; }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 171 */     RandomSource random = level.getRandom();
/* 172 */     populateDefaultEquipmentSlots(random, difficulty);
/* 173 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/*     */     
/* 175 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 180 */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) { setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void enchantSpawnedWeapon(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
/* 185 */     super.enchantSpawnedWeapon(level, random, difficulty);
/*     */     
/* 187 */     if (random.nextInt(300) == 0) {
/* 188 */       ItemStack weapon = getMainHandItem();
/* 189 */       if (weapon.is(Items.CROSSBOW)) {
/* 190 */         EnchantmentHelper.enchantItemFromProvider(weapon, level.registryAccess(), VanillaEnchantmentProviders.PILLAGER_SPAWN_CROSSBOW, difficulty, random);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 197 */   protected SoundEvent getAmbientSound() { return SoundEvents.PILLAGER_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   protected SoundEvent getDeathSound() { return SoundEvents.PILLAGER_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.PILLAGER_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public void performRangedAttack(LivingEntity target, float power) { performCrossbowAttack(this, 1.6F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public SimpleContainer getInventory() { return this.inventory; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 222 */     ItemStack itemStack = entity.getItem();
/* 223 */     if (itemStack.getItem() instanceof net.minecraft.world.item.BannerItem) {
/* 224 */       super.pickUpItem(level, entity);
/*     */     }
/* 226 */     else if (wantsItem(itemStack)) {
/* 227 */       onItemPickup(entity);
/* 228 */       ItemStack remainder = this.inventory.addItem(itemStack);
/* 229 */       if (remainder.isEmpty()) {
/* 230 */         entity.discard();
/*     */       } else {
/* 232 */         itemStack.setCount(remainder.getCount());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 239 */   private boolean wantsItem(ItemStack itemStack) { return (hasActiveRaid() && itemStack.is(Items.WHITE_BANNER)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 244 */     int inventorySlot = slot - 300;
/* 245 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize()) {
/* 246 */       return this.inventory.getSlot(inventorySlot);
/*     */     }
/* 248 */     return super.getSlot(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {
/* 253 */     Raid raid = getCurrentRaid();
/* 254 */     boolean shouldEnchant = (this.random.nextFloat() <= raid.getEnchantOdds());
/*     */     
/* 256 */     if (shouldEnchant) {
/* 257 */       ResourceKey<EnchantmentProvider> provider; ItemStack crossbow = new ItemStack(Items.CROSSBOW);
/*     */       
/* 259 */       if (wave > raid.getNumGroups(Difficulty.NORMAL)) {
/* 260 */         provider = VanillaEnchantmentProviders.RAID_PILLAGER_POST_WAVE_5;
/* 261 */       } else if (wave > raid.getNumGroups(Difficulty.EASY)) {
/* 262 */         provider = VanillaEnchantmentProviders.RAID_PILLAGER_POST_WAVE_3;
/*     */       } else {
/* 264 */         provider = null;
/*     */       } 
/*     */       
/* 267 */       if (provider != null) {
/* 268 */         EnchantmentHelper.enchantItemFromProvider(crossbow, level.registryAccess(), provider, level.getCurrentDifficultyAt(blockPosition()), getRandom());
/* 269 */         setItemSlot(EquipmentSlot.MAINHAND, crossbow);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 276 */   public SoundEvent getCelebrateSound() { return SoundEvents.PILLAGER_CELEBRATE; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Pillager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */