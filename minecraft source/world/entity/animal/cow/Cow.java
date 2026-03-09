/*    */ package net.minecraft.world.entity.animal.cow;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.EntityDataSerializers;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.SpawnGroupData;
/*    */ import net.minecraft.world.entity.variant.SpawnContext;
/*    */ import net.minecraft.world.entity.variant.VariantUtils;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class Cow
/*    */   extends AbstractCow {
/* 26 */   private static final EntityDataAccessor<Holder<CowVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Cow.class, EntityDataSerializers.COW_VARIANT);
/*    */ 
/*    */   
/* 29 */   public Cow(EntityType<? extends Cow> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 34 */     super.defineSynchedData(entityData);
/* 35 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), CowVariants.TEMPERATE));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {
/* 40 */     super.addAdditionalSaveData(output);
/* 41 */     VariantUtils.writeVariant(output, getVariant());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {
/* 46 */     super.readAdditionalSaveData(input);
/* 47 */     VariantUtils.readVariant(input, Registries.COW_VARIANT).ifPresent(this::setVariant);
/*    */   }
/*    */ 
/*    */   
/*    */   public Cow getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 52 */     Cow baby = (Cow)EntityType.COW.create(level, EntitySpawnReason.BREEDING);
/* 53 */     if (baby != null && partner instanceof Cow) { Cow partnerCow = (Cow)partner;
/* 54 */       baby.setVariant(this.random.nextBoolean() ? getVariant() : partnerCow.getVariant()); }
/*    */     
/* 56 */     return baby;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 61 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.COW_VARIANT).ifPresent(this::setVariant);
/* 62 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*    */   }
/*    */ 
/*    */   
/* 66 */   public void setVariant(Holder<CowVariant> variant) { this.entityData.set(DATA_VARIANT_ID, variant); }
/*    */ 
/*    */ 
/*    */   
/* 70 */   public Holder<CowVariant> getVariant() { return (Holder)this.entityData.get(DATA_VARIANT_ID); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T get(DataComponentType<? extends T> type) {
/* 75 */     if (type == DataComponents.COW_VARIANT) {
/* 76 */       return (T)castComponentValue(type, getVariant());
/*    */     }
/*    */     
/* 79 */     return (T)super.get(type);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 84 */     applyImplicitComponentIfPresent(components, DataComponents.COW_VARIANT);
/* 85 */     super.applyImplicitComponents(components);
/*    */   }
/*    */ 
/*    */   
/*    */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 90 */     if (type == DataComponents.COW_VARIANT) {
/* 91 */       setVariant((Holder)castComponentValue(DataComponents.COW_VARIANT, value));
/* 92 */       return true;
/*    */     } 
/*    */     
/* 95 */     return super.applyImplicitComponent(type, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\cow\Cow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */