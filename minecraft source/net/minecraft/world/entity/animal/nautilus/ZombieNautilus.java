/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
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
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.EitherHolder;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public class ZombieNautilus
/*     */   extends AbstractNautilus
/*     */ {
/*  40 */   private static final EntityDataAccessor<Holder<ZombieNautilusVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(ZombieNautilus.class, EntityDataSerializers.ZOMBIE_NAUTILUS_VARIANT);
/*     */ 
/*     */   
/*  43 */   public ZombieNautilus(EntityType<? extends ZombieNautilus> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  48 */     return AbstractNautilus.createAttributes()
/*  49 */       .add(Attributes.MOVEMENT_SPEED, 1.100000023841858D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public ZombieNautilus getBreedOffspring(ServerLevel level, AgeableMob partner) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected EquipmentSlot sunProtectionSlot() { return EquipmentSlot.BODY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   protected Brain.Provider<ZombieNautilus> brainProvider() { return ZombieNautilusAi.brainProvider(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   protected Brain<?> makeBrain(Dynamic<?> input) { return ZombieNautilusAi.makeBrain(brainProvider().makeBrain(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public Brain<ZombieNautilus> getBrain() { return super.getBrain(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  80 */     ProfilerFiller profiler = Profiler.get();
/*  81 */     profiler.push("zombieNautilusBrain");
/*  82 */     getBrain().tick(level, this);
/*  83 */     profiler.pop();
/*     */     
/*  85 */     profiler.push("zombieNautilusActivityUpdate");
/*  86 */     ZombieNautilusAi.updateActivity(this);
/*  87 */     profiler.pop();
/*  88 */     super.customServerAiStep(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected SoundEvent getAmbientSound() { return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_AMBIENT : SoundEvents.ZOMBIE_NAUTILUS_AMBIENT_ON_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected SoundEvent getHurtSound(DamageSource source) { return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_HURT : SoundEvents.ZOMBIE_NAUTILUS_HURT_ON_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected SoundEvent getDeathSound() { return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DEATH : SoundEvents.ZOMBIE_NAUTILUS_DEATH_ON_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   protected SoundEvent getDashSound() { return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DASH : SoundEvents.ZOMBIE_NAUTILUS_DASH_ON_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected SoundEvent getDashReadySound() { return isUnderWater() ? SoundEvents.ZOMBIE_NAUTILUS_DASH_READY : SoundEvents.ZOMBIE_NAUTILUS_DASH_READY_ON_LAND; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   protected void playEatingSound() { makeSound(SoundEvents.ZOMBIE_NAUTILUS_EAT); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   protected SoundEvent getSwimSound() { return SoundEvents.ZOMBIE_NAUTILUS_SWIM; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 128 */     super.defineSynchedData(entityData);
/* 129 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), ZombieNautilusVariants.TEMPERATE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 134 */     super.readAdditionalSaveData(input);
/* 135 */     VariantUtils.readVariant(input, Registries.ZOMBIE_NAUTILUS_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 140 */     super.addAdditionalSaveData(output);
/* 141 */     VariantUtils.writeVariant(output, getVariant());
/*     */   }
/*     */ 
/*     */   
/* 145 */   public void setVariant(Holder<ZombieNautilusVariant> variant) { this.entityData.set(DATA_VARIANT_ID, variant); }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public Holder<ZombieNautilusVariant> getVariant() { return (Holder)this.entityData.get(DATA_VARIANT_ID); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 154 */     if (type == DataComponents.ZOMBIE_NAUTILUS_VARIANT) {
/* 155 */       return (T)castComponentValue(type, new EitherHolder(getVariant()));
/*     */     }
/*     */     
/* 158 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 163 */     applyImplicitComponentIfPresent(components, DataComponents.ZOMBIE_NAUTILUS_VARIANT);
/* 164 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 169 */     if (type == DataComponents.ZOMBIE_NAUTILUS_VARIANT) {
/* 170 */       Optional<Holder<ZombieNautilusVariant>> variant = ((EitherHolder)castComponentValue(DataComponents.ZOMBIE_NAUTILUS_VARIANT, value)).unwrap(registryAccess());
/* 171 */       if (variant.isPresent()) {
/* 172 */         setVariant((Holder)variant.get());
/* 173 */         return true;
/*     */       } 
/* 175 */       return false;
/*     */     } 
/*     */     
/* 178 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 183 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.ZOMBIE_NAUTILUS_VARIANT).ifPresent(this::setVariant);
/* 184 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public boolean canBeLeashed() { return (!isAggravated() && !isMobControlled()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public boolean isBaby() { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */