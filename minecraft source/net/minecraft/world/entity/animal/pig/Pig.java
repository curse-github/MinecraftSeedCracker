/*     */ package net.minecraft.world.entity.animal.pig;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ItemBasedSteering;
/*     */ import net.minecraft.world.entity.ItemSteerable;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Pig
/*     */   extends Animal
/*     */   implements ItemSteerable
/*     */ {
/*  62 */   private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.INT);
/*  63 */   private static final EntityDataAccessor<Holder<PigVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Pig.class, EntityDataSerializers.PIG_VARIANT);
/*     */   
/*     */   private final ItemBasedSteering steering;
/*     */   
/*     */   public Pig(EntityType<? extends Pig> type, Level level) {
/*  68 */     super(type, level);
/*  69 */     this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  74 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/*  75 */     this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
/*  76 */     this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
/*  77 */     this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, i -> i.is(Items.CARROT_ON_A_STICK), false));
/*  78 */     this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, i -> i.is(ItemTags.PIG_FOOD), false));
/*  79 */     this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
/*  80 */     this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  81 */     this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
/*  82 */     this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  86 */     return Animal.createAnimalAttributes()
/*  87 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  88 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/*  93 */     if (isSaddled()) { Entity entity = getFirstPassenger(); if (entity instanceof Player) { Player player = (Player)entity; if (player.isHolding(Items.CARROT_ON_A_STICK))
/*  94 */           return player;  }
/*     */        }
/*  96 */      return super.getControllingPassenger();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 101 */     if (DATA_BOOST_TIME.equals(accessor) && level().isClientSide()) {
/* 102 */       this.steering.onSynced();
/*     */     }
/* 104 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 109 */     super.defineSynchedData(entityData);
/* 110 */     entityData.define(DATA_BOOST_TIME, Integer.valueOf(0));
/* 111 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), PigVariants.DEFAULT));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 116 */     super.addAdditionalSaveData(output);
/* 117 */     VariantUtils.writeVariant(output, getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 122 */     super.readAdditionalSaveData(input);
/* 123 */     VariantUtils.readVariant(input, Registries.PIG_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 128 */   protected SoundEvent getAmbientSound() { return SoundEvents.PIG_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.PIG_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   protected SoundEvent getDeathSound() { return SoundEvents.PIG_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 148 */     boolean hasFood = isFood(player.getItemInHand(hand));
/*     */     
/* 150 */     if (!hasFood && isSaddled() && !isVehicle() && !player.isSecondaryUseActive()) {
/* 151 */       if (!level().isClientSide()) {
/* 152 */         player.startRiding(this);
/*     */       }
/* 154 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 157 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 158 */     if (!interactionResult.consumesAction()) {
/* 159 */       ItemStack itemStack = player.getItemInHand(hand);
/* 160 */       if (isEquippableInSlot(itemStack, EquipmentSlot.SADDLE)) {
/* 161 */         return itemStack.interactLivingEntity(player, this, hand);
/*     */       }
/* 163 */       return InteractionResult.PASS;
/*     */     } 
/* 165 */     return interactionResult;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 170 */     if (slot == EquipmentSlot.SADDLE) {
/* 171 */       return (isAlive() && !isBaby());
/*     */     }
/* 173 */     return super.canUseSlot(slot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 178 */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) { return (slot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(slot)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/* 183 */     if (slot == EquipmentSlot.SADDLE) {
/* 184 */       return SoundEvents.PIG_SADDLE;
/*     */     }
/* 186 */     return super.getEquipSound(slot, stack, equippable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 191 */     if (level.getDifficulty() != Difficulty.PEACEFUL) {
/* 192 */       ZombifiedPiglin zombifiedPiglin = (ZombifiedPiglin)convertTo(EntityType.ZOMBIFIED_PIGLIN, ConversionParams.single(this, false, true), zp -> {
/* 193 */             zp.populateDefaultEquipmentSlots(getRandom(), level.getCurrentDifficultyAt(blockPosition()));
/* 194 */             zp.setPersistenceRequired();
/*     */           });
/*     */       
/* 197 */       if (zombifiedPiglin == null) {
/* 198 */         super.thunderHit(level, lightningBolt);
/*     */       }
/*     */     } else {
/* 201 */       super.thunderHit(level, lightningBolt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 207 */     super.tickRidden(controller, riddenInput);
/* 208 */     setRot(controller.getYRot(), controller.getXRot() * 0.5F);
/* 209 */     this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
/* 210 */     this.steering.tickBoost();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 215 */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) { return new Vec3(0.0D, 0.0D, 1.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   protected float getRiddenSpeed(Player controller) { return (float)(getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225D * this.steering.boostFactor()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 225 */   public boolean boost() { return this.steering.boost(getRandom()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Pig getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 230 */     Pig baby = (Pig)EntityType.PIG.create(level, EntitySpawnReason.BREEDING);
/* 231 */     if (baby != null && partner instanceof Pig) { Pig partnerPig = (Pig)partner;
/* 232 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : partnerPig.getVariant()); }
/*     */     
/* 234 */     return baby;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 239 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.PIG_FOOD); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, (0.6F * getEyeHeight()), (getBbWidth() * 0.4F)); }
/*     */ 
/*     */ 
/*     */   
/* 248 */   private void setVariant(Holder<PigVariant> variant) { this.entityData.set(DATA_VARIANT_ID, variant); }
/*     */ 
/*     */ 
/*     */   
/* 252 */   public Holder<PigVariant> getVariant() { return (Holder)this.entityData.get(DATA_VARIANT_ID); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 257 */     if (type == DataComponents.PIG_VARIANT) {
/* 258 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 261 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 266 */     applyImplicitComponentIfPresent(components, DataComponents.PIG_VARIANT);
/* 267 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 272 */     if (type == DataComponents.PIG_VARIANT) {
/* 273 */       setVariant((Holder)castComponentValue(DataComponents.PIG_VARIANT, value));
/* 274 */       return true;
/*     */     } 
/*     */     
/* 277 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 282 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.PIG_VARIANT).ifPresent(this::setVariant);
/* 283 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\pig\Pig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */