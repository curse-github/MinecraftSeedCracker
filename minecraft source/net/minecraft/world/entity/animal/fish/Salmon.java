/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.animal.Bucketable;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public class Salmon
/*     */   extends AbstractSchoolingFish
/*     */ {
/*     */   private static final String TAG_TYPE = "type";
/*  38 */   private static final EntityDataAccessor<Integer> DATA_TYPE = SynchedEntityData.defineId(Salmon.class, EntityDataSerializers.INT);
/*     */   
/*     */   public Salmon(EntityType<? extends Salmon> type, Level level) {
/*  41 */     super(type, level);
/*     */     
/*  43 */     refreshDimensions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public int getMaxSchoolSize() { return 5; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public ItemStack getBucketItemStack() { return new ItemStack(Items.SALMON_BUCKET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   protected SoundEvent getAmbientSound() { return SoundEvents.SALMON_AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected SoundEvent getDeathSound() { return SoundEvents.SALMON_DEATH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.SALMON_HURT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   protected SoundEvent getFlopSound() { return SoundEvents.SALMON_FLOP; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  80 */     super.defineSynchedData(entityData);
/*     */     
/*  82 */     entityData.define(DATA_TYPE, Integer.valueOf(Variant.DEFAULT.id()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  87 */     super.onSyncedDataUpdated(accessor);
/*  88 */     if (DATA_TYPE.equals(accessor)) {
/*  89 */       refreshDimensions();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  95 */     super.addAdditionalSaveData(output);
/*  96 */     output.store("type", Variant.CODEC, getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 101 */     super.readAdditionalSaveData(input);
/* 102 */     setVariant((Variant)input.read("type", Variant.CODEC).orElse(Variant.DEFAULT));
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveToBucketTag(ItemStack bucket) {
/* 107 */     Bucketable.saveDefaultDataToBucketTag(this, bucket);
/* 108 */     bucket.copyFrom(DataComponents.SALMON_SIZE, this);
/*     */   }
/*     */ 
/*     */   
/* 112 */   private void setVariant(Variant variant) { this.entityData.set(DATA_TYPE, Integer.valueOf(variant.id)); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public Variant getVariant() { return (Variant)Variant.BY_ID.apply(((Integer)this.entityData.get(DATA_TYPE)).intValue()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 121 */     if (type == DataComponents.SALMON_SIZE) {
/* 122 */       return (T)castComponentValue(type, getVariant());
/*     */     }
/*     */     
/* 125 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 130 */     applyImplicitComponentIfPresent(components, DataComponents.SALMON_SIZE);
/* 131 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 136 */     if (type == DataComponents.SALMON_SIZE) {
/* 137 */       setVariant((Variant)castComponentValue(DataComponents.SALMON_SIZE, value));
/* 138 */       return true;
/*     */     } 
/*     */     
/* 141 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 146 */     WeightedList.Builder<Variant> builder = WeightedList.builder();
/* 147 */     builder.add(Variant.SMALL, 30);
/* 148 */     builder.add(Variant.MEDIUM, 50);
/* 149 */     builder.add(Variant.LARGE, 15);
/* 150 */     builder.build().getRandom(this.random).ifPresent(this::setVariant);
/*     */     
/* 152 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */ 
/*     */   
/* 156 */   public float getSalmonScale() { return (getVariant()).boundingBoxScale; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   protected EntityDimensions getDefaultDimensions(Pose pose) { return super.getDefaultDimensions(pose).scale(getSalmonScale()); }
/*     */   public enum Variant implements StringRepresentable { public static final Variant DEFAULT;
/*     */     public static final StringRepresentable.EnumCodec<Variant> CODEC;
/*     */     private static final IntFunction<Variant> BY_ID;
/* 165 */     SMALL("small", 0, 0.5F),
/* 166 */     MEDIUM("medium", 1, 1.0F),
/* 167 */     LARGE("large", 2, 1.5F);
/*     */     static  {
/* 169 */       DEFAULT = MEDIUM;
/*     */       
/* 171 */       CODEC = StringRepresentable.fromEnum(Variant::values);
/*     */       
/* 173 */       BY_ID = ByIdMap.continuous(Variant::id, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*     */       
/* 175 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */     }
/*     */     public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC; private final String name;
/*     */     private final int id;
/*     */     private final float boundingBoxScale;
/*     */     
/*     */     Variant(String name, int id, float boundingBoxScale) {
/* 182 */       this.name = name;
/* 183 */       this.id = id;
/* 184 */       this.boundingBoxScale = boundingBoxScale;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 189 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 193 */     private int id() { return this.id; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fish\Salmon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */