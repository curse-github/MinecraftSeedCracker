/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.LlamaSpit;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Llama
/*     */   extends AbstractChestedHorse
/*     */   implements RangedAttackMob {
/*     */   private static final int MAX_STRENGTH = 5;
/*  73 */   private static final EntityDataAccessor<Integer> DATA_STRENGTH_ID = SynchedEntityData.defineId(Llama.class, EntityDataSerializers.INT);
/*  74 */   private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Llama.class, EntityDataSerializers.INT);
/*     */   
/*  76 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.LLAMA.getDimensions()
/*  77 */     .withAttachments(EntityAttachments.builder()
/*  78 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.LLAMA.getHeight() - 0.8125F, -0.3F))
/*     */     
/*  80 */     .scale(0.5F);
/*     */   private boolean didSpit;
/*     */   private Llama caravanHead;
/*     */   private Llama caravanTail;
/*     */   
/*     */   public enum Variant
/*     */     implements StringRepresentable
/*     */   {
/*  88 */     CREAMY(0, "creamy"),
/*  89 */     WHITE(1, "white"),
/*  90 */     BROWN(2, "brown"),
/*  91 */     GRAY(3, "gray"); public static final Variant DEFAULT; private static final IntFunction<Variant> BY_ID; public static final Codec<Variant> CODEC;
/*     */     
/*     */     static  {
/*  94 */       DEFAULT = CREAMY;
/*     */       
/*  96 */       BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*  97 */       CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */       
/*  99 */       Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */       
/* 101 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */     }
/*     */     @Deprecated
/*     */     public static final Codec<Variant> LEGACY_CODEC; public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */     
/*     */     Variant(int id, String name) {
/* 107 */       this.id = id;
/* 108 */       this.name = name;
/*     */     }
/*     */     private final int id; private final String name;
/*     */     
/* 112 */     public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */     
/* 116 */     public static Variant byId(int id) { return (Variant)BY_ID.apply(id); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */ 
/*     */   
/*     */   public Llama(EntityType<? extends Llama> type, Level level) {
/* 126 */     super(type, level);
/* 127 */     getNavigation().setRequiredPathLength(40.0F);
/*     */   }
/*     */ 
/*     */   
/* 131 */   public boolean isTraderLlama() { return false; }
/*     */ 
/*     */ 
/*     */   
/* 135 */   private void setStrength(int strength) { this.entityData.set(DATA_STRENGTH_ID, Integer.valueOf(Math.max(1, Math.min(5, strength)))); }
/*     */ 
/*     */   
/*     */   private void setRandomStrength(RandomSource random) {
/* 139 */     int maxStrength = (random.nextFloat() < 0.04F) ? 5 : 3;
/*     */     
/* 141 */     setStrength(1 + random.nextInt(maxStrength));
/*     */   }
/*     */ 
/*     */   
/* 145 */   public int getStrength() { return ((Integer)this.entityData.get(DATA_STRENGTH_ID)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 150 */     super.addAdditionalSaveData(output);
/*     */     
/* 152 */     output.store("Variant", Variant.LEGACY_CODEC, getVariant());
/* 153 */     output.putInt("Strength", getStrength());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 158 */     setStrength(input.getIntOr("Strength", 0));
/*     */     
/* 160 */     super.readAdditionalSaveData(input);
/* 161 */     setVariant((Variant)input.read("Variant", Variant.LEGACY_CODEC).orElse(Variant.DEFAULT));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 166 */     this.goalSelector.addGoal(0, new FloatGoal(this));
/* 167 */     this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2D));
/* 168 */     this.goalSelector.addGoal(2, new LlamaFollowCaravanGoal(this, 2.0999999046325684D));
/* 169 */     this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.25D, 40, 20.0F));
/* 170 */     this.goalSelector.addGoal(3, new PanicGoal(this, 1.2D));
/* 171 */     this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
/* 172 */     this.goalSelector.addGoal(5, new TemptGoal(this, 1.25D, i -> i.is(ItemTags.LLAMA_TEMPT_ITEMS), false));
/* 173 */     this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.0D));
/* 174 */     this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.7D));
/* 175 */     this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
/* 176 */     this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
/*     */     
/* 178 */     this.targetSelector.addGoal(1, new LlamaHurtByTargetGoal(this));
/* 179 */     this.targetSelector.addGoal(2, new LlamaAttackWolfGoal(this));
/*     */   }
/*     */ 
/*     */   
/* 183 */   public static AttributeSupplier.Builder createAttributes() { return createBaseChestedHorseAttributes(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 188 */     super.defineSynchedData(entityData);
/*     */     
/* 190 */     entityData.define(DATA_STRENGTH_ID, Integer.valueOf(0));
/* 191 */     entityData.define(DATA_VARIANT_ID, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/* 195 */   public Variant getVariant() { return Variant.byId(((Integer)this.entityData.get(DATA_VARIANT_ID)).intValue()); }
/*     */ 
/*     */ 
/*     */   
/* 199 */   private void setVariant(Variant variant) { this.entityData.set(DATA_VARIANT_ID, Integer.valueOf(variant.id)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 204 */     if (type == DataComponents.LLAMA_VARIANT) {
/* 205 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 208 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 213 */     applyImplicitComponentIfPresent(components, DataComponents.LLAMA_VARIANT);
/* 214 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 219 */     if (type == DataComponents.LLAMA_VARIANT) {
/* 220 */       setVariant((Variant)castComponentValue(DataComponents.LLAMA_VARIANT, value));
/* 221 */       return true;
/*     */     } 
/*     */     
/* 224 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 231 */   public boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.LLAMA_FOOD); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleEating(Player player, ItemStack itemStack) {
/* 236 */     int ageUp = 0;
/* 237 */     int temper = 0;
/* 238 */     float heal = 0.0F;
/* 239 */     boolean itemUsed = false;
/*     */     
/* 241 */     if (itemStack.is(Items.WHEAT)) {
/* 242 */       ageUp = 10;
/* 243 */       temper = 3;
/* 244 */       heal = 2.0F;
/* 245 */     } else if (itemStack.is(Blocks.HAY_BLOCK.asItem())) {
/* 246 */       ageUp = 90;
/* 247 */       temper = 6;
/* 248 */       heal = 10.0F;
/* 249 */       if (isTamed() && getAge() == 0 && canFallInLove()) {
/* 250 */         itemUsed = true;
/* 251 */         setInLove(player);
/*     */       } 
/*     */     } 
/* 254 */     if (getHealth() < getMaxHealth() && heal > 0.0F) {
/* 255 */       heal(heal);
/* 256 */       itemUsed = true;
/*     */     } 
/* 258 */     if (isBaby() && ageUp > 0) {
/* 259 */       level().addParticle(ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/* 260 */       if (!level().isClientSide()) {
/* 261 */         ageUp(ageUp);
/* 262 */         itemUsed = true;
/*     */       } 
/*     */     } 
/* 265 */     if (temper > 0 && (itemUsed || !isTamed()) && getTemper() < getMaxTemper() && 
/* 266 */       !level().isClientSide()) {
/* 267 */       modifyTemper(temper);
/* 268 */       itemUsed = true;
/*     */     } 
/*     */     
/* 271 */     if (itemUsed && 
/* 272 */       !isSilent()) {
/* 273 */       SoundEvent eatingSound = getEatingSound();
/* 274 */       if (eatingSound != null) {
/* 275 */         level().playSound(null, getX(), getY(), getZ(), getEatingSound(), getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 280 */     return itemUsed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 285 */   public boolean isImmobile() { return (isDeadOrDying() || isEating()); }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     Variant variant;
/*     */     LlamaGroupData llamaGroupData;
/* 290 */     RandomSource random = level.getRandom();
/* 291 */     setRandomStrength(random);
/*     */ 
/*     */     
/* 294 */     if (groupData instanceof LlamaGroupData) {
/* 295 */       variant = ((LlamaGroupData)groupData).variant;
/*     */     } else {
/* 297 */       variant = (Variant)Util.getRandom(Variant.values(), random);
/* 298 */       llamaGroupData = new LlamaGroupData(variant);
/*     */     } 
/* 300 */     setVariant(variant);
/*     */     
/* 302 */     return super.finalizeSpawn(level, difficulty, spawnReason, llamaGroupData);
/*     */   }
/*     */   
/*     */   private static class LlamaGroupData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Llama.Variant variant;
/*     */     
/*     */     private LlamaGroupData(Llama.Variant variant) {
/* 309 */       super(true);
/* 310 */       this.variant = variant;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 316 */   protected boolean canPerformRearing() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 321 */   protected SoundEvent getAngrySound() { return SoundEvents.LLAMA_ANGRY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   protected SoundEvent getAmbientSound() { return SoundEvents.LLAMA_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 331 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.LLAMA_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 336 */   protected SoundEvent getDeathSound() { return SoundEvents.LLAMA_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 341 */   protected SoundEvent getEatingSound() { return SoundEvents.LLAMA_EAT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 346 */   protected void playStepSound(BlockPos pos, BlockState blockState) { playSound(SoundEvents.LLAMA_STEP, 0.15F, 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 351 */   protected void playChestEquipsSound() { playSound(SoundEvents.LLAMA_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 356 */   public int getInventoryColumns() { return hasChest() ? getStrength() : 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 361 */   public boolean canUseSlot(EquipmentSlot slot) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 366 */   public int getMaxTemper() { return 30; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 371 */   public boolean canMate(Animal partner) { return (partner != this && partner instanceof Llama && canParent() && ((Llama)partner).canParent()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Llama getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 376 */     Llama baby = makeNewLlama();
/*     */     
/* 378 */     if (baby != null) {
/* 379 */       setOffspringAttributes(partner, baby);
/*     */       
/* 381 */       Llama otherLlama = (Llama)partner;
/*     */       
/* 383 */       int babyStrength = this.random.nextInt(Math.max(getStrength(), otherLlama.getStrength())) + 1;
/* 384 */       if (this.random.nextFloat() < 0.03F) {
/* 385 */         babyStrength++;
/*     */       }
/* 387 */       baby.setStrength(babyStrength);
/*     */       
/* 389 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : otherLlama.getVariant());
/*     */     } 
/* 391 */     return baby;
/*     */   }
/*     */ 
/*     */   
/* 395 */   protected Llama makeNewLlama() { return (Llama)EntityType.LLAMA.create(level(), EntitySpawnReason.BREEDING); }
/*     */ 
/*     */   
/*     */   private void spit(LivingEntity target) {
/* 399 */     LlamaSpit spit = new LlamaSpit(level(), this);
/* 400 */     double xd = target.getX() - getX();
/* 401 */     double yd = target.getY(0.3333333333333333D) - spit.getY();
/* 402 */     double zd = target.getZ() - getZ();
/* 403 */     double yo = Math.sqrt(xd * xd + zd * zd) * 0.20000000298023224D;
/* 404 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 405 */       Projectile.spawnProjectileUsingShoot(spit, serverLevel, ItemStack.EMPTY, xd, yd + yo, zd, 1.5F, 10.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 411 */     if (!isSilent()) {
/* 412 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.LLAMA_SPIT, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */     }
/* 414 */     this.didSpit = true;
/*     */   }
/*     */ 
/*     */   
/* 418 */   private void setDidSpit(boolean b) { this.didSpit = b; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 423 */     int dmg = calculateFallDamage(fallDistance, damageModifier);
/* 424 */     if (dmg <= 0) {
/* 425 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 429 */     if (fallDistance >= 6.0D) {
/* 430 */       hurt(damageSource, dmg);
/*     */       
/* 432 */       propagateFallToPassengers(fallDistance, damageModifier, damageSource);
/*     */     } 
/*     */     
/* 435 */     playBlockFallSound();
/* 436 */     return true;
/*     */   }
/*     */   
/*     */   public void leaveCaravan() {
/* 440 */     if (this.caravanHead != null) {
/* 441 */       this.caravanHead.caravanTail = null;
/*     */     }
/* 443 */     this.caravanHead = null;
/*     */   }
/*     */   
/*     */   public void joinCaravan(Llama tail) {
/* 447 */     this.caravanHead = tail;
/* 448 */     this.caravanHead.caravanTail = this;
/*     */   }
/*     */ 
/*     */   
/* 452 */   public boolean hasCaravanTail() { return (this.caravanTail != null); }
/*     */ 
/*     */ 
/*     */   
/* 456 */   public boolean inCaravan() { return (this.caravanHead != null); }
/*     */ 
/*     */ 
/*     */   
/* 460 */   public Llama getCaravanHead() { return this.caravanHead; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 465 */   protected double followLeashSpeed() { return 2.0D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 470 */   public boolean supportQuadLeash() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void followMommy(ServerLevel level) {
/* 475 */     if (!inCaravan() && isBaby()) {
/* 476 */       super.followMommy(level);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 482 */   public boolean canEatGrass() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 487 */   public void performRangedAttack(LivingEntity target, float power) { spit(target); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 492 */   public Vec3 getLeashOffset() { return new Vec3(0.0D, 0.75D * getEyeHeight(), getBbWidth() * 0.5D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 497 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 502 */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) { return getDefaultPassengerAttachmentPoint(this, passenger, dimensions.attachments()); }
/*     */   
/*     */   private static class LlamaHurtByTargetGoal
/*     */     extends HurtByTargetGoal
/*     */   {
/* 507 */     public LlamaHurtByTargetGoal(Llama llama) { super(llama, new Class[0]); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 512 */       Mob mob = this.mob; if (mob instanceof Llama) { Llama llama = (Llama)mob;
/* 513 */         if (llama.didSpit) {
/* 514 */           llama.setDidSpit(false);
/* 515 */           return false;
/*     */         }  }
/*     */       
/* 518 */       return super.canContinueToUse();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LlamaAttackWolfGoal
/*     */     extends NearestAttackableTargetGoal<Wolf> {
/* 524 */     public LlamaAttackWolfGoal(Llama llama) { super(llama, Wolf.class, 16, false, true, (target, level) -> !((Wolf)target).isTame()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 529 */     protected double getFollowDistance() { return super.getFollowDistance() * 0.25D; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\Llama.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */