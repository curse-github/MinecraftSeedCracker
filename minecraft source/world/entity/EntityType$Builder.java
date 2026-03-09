/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public class Builder<T extends Entity>
/*     */   extends Object
/*     */ {
/*     */   private final EntityType.EntityFactory<T> factory;
/*     */   private final MobCategory category;
/*     */   private ImmutableSet<Block> immuneTo;
/*     */   private boolean serialize;
/*     */   private boolean summon;
/*     */   private boolean fireImmune;
/*     */   private boolean canSpawnFarFromPlayer;
/*     */   private int clientTrackingRange;
/*     */   private int updateInterval;
/*     */   private EntityDimensions dimensions;
/*     */   private float spawnDimensionsScale;
/*     */   private EntityAttachments.Builder attachments;
/*     */   private FeatureFlagSet requiredFeatures;
/*     */   private DependantName<EntityType<?>, Optional<ResourceKey<LootTable>>> lootTable;
/*     */   private final DependantName<EntityType<?>, String> descriptionId;
/*     */   private boolean allowedInPeaceful;
/*     */   
/*     */   private Builder(EntityType.EntityFactory<T> factory, MobCategory category) {
/* 777 */     this.immuneTo = ImmutableSet.of();
/* 778 */     this.serialize = true;
/* 779 */     this.summon = true;
/*     */ 
/*     */     
/* 782 */     this.clientTrackingRange = 5;
/* 783 */     this.updateInterval = 3;
/* 784 */     this.dimensions = EntityDimensions.scalable(0.6F, 1.8F);
/* 785 */     this.spawnDimensionsScale = 1.0F;
/* 786 */     this.attachments = EntityAttachments.builder();
/* 787 */     this.requiredFeatures = FeatureFlags.VANILLA_SET;
/* 788 */     this.lootTable = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("entities/"))));
/* 789 */     this.descriptionId = (id -> Util.makeDescriptionId("entity", id.identifier()));
/* 790 */     this.allowedInPeaceful = true;
/*     */ 
/*     */     
/* 793 */     this.factory = factory;
/* 794 */     this.category = category;
/* 795 */     this.canSpawnFarFromPlayer = (category == MobCategory.CREATURE || category == MobCategory.MISC);
/*     */   }
/*     */ 
/*     */   
/* 799 */   public static <T extends Entity> Builder<T> of(EntityType.EntityFactory<T> factory, MobCategory category) { return new Builder(factory, category); }
/*     */ 
/*     */ 
/*     */   
/* 803 */   public static <T extends Entity> Builder<T> createNothing(MobCategory category) { return new Builder((t, l) -> null, category); }
/*     */ 
/*     */   
/*     */   public Builder<T> sized(float width, float height) {
/* 807 */     this.dimensions = EntityDimensions.scalable(width, height);
/* 808 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> spawnDimensionsScale(float scale) {
/* 812 */     this.spawnDimensionsScale = scale;
/* 813 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> eyeHeight(float eyeHeight) {
/* 817 */     this.dimensions = this.dimensions.withEyeHeight(eyeHeight);
/* 818 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> passengerAttachments(float... offsetYs) {
/* 822 */     for (float offsetY : offsetYs) {
/* 823 */       this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, 0.0F, offsetY, 0.0F);
/*     */     }
/* 825 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> passengerAttachments(Vec3... points) {
/* 829 */     for (Vec3 point : points) {
/* 830 */       this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, point);
/*     */     }
/* 832 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 836 */   public Builder<T> vehicleAttachment(Vec3 point) { return attach(EntityAttachment.VEHICLE, point); }
/*     */ 
/*     */ 
/*     */   
/* 840 */   public Builder<T> ridingOffset(float ridingOffset) { return attach(EntityAttachment.VEHICLE, 0.0F, -ridingOffset, 0.0F); }
/*     */ 
/*     */ 
/*     */   
/* 844 */   public Builder<T> nameTagOffset(float nameTagOffset) { return attach(EntityAttachment.NAME_TAG, 0.0F, nameTagOffset, 0.0F); }
/*     */ 
/*     */   
/*     */   public Builder<T> attach(EntityAttachment attachment, float x, float y, float z) {
/* 848 */     this.attachments = this.attachments.attach(attachment, x, y, z);
/* 849 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> attach(EntityAttachment attachment, Vec3 point) {
/* 853 */     this.attachments = this.attachments.attach(attachment, point);
/* 854 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noSummon() {
/* 858 */     this.summon = false;
/* 859 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noSave() {
/* 863 */     this.serialize = false;
/* 864 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> fireImmune() {
/* 868 */     this.fireImmune = true;
/* 869 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> immuneTo(Block... blocks) {
/* 873 */     this.immuneTo = ImmutableSet.copyOf(blocks);
/* 874 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> canSpawnFarFromPlayer() {
/* 878 */     this.canSpawnFarFromPlayer = true;
/* 879 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> clientTrackingRange(int clientChunkRange) {
/* 883 */     this.clientTrackingRange = clientChunkRange;
/* 884 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> updateInterval(int updateInterval) {
/* 888 */     this.updateInterval = updateInterval;
/* 889 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> requiredFeatures(FeatureFlag... flags) {
/* 893 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 894 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noLootTable() {
/* 898 */     this.lootTable = DependantName.fixed(Optional.empty());
/* 899 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> notInPeaceful() {
/* 903 */     this.allowedInPeaceful = false;
/* 904 */     return this;
/*     */   }
/*     */   
/*     */   public EntityType<T> build(ResourceKey<EntityType<?>> name) {
/* 908 */     if (this.serialize) {
/* 909 */       Util.fetchChoiceType(References.ENTITY_TREE, name.identifier().toString());
/*     */     }
/*     */     
/* 912 */     return new EntityType(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions.withAttachments(this.attachments), this.spawnDimensionsScale, this.clientTrackingRange, this.updateInterval, (String)this.descriptionId.get(name), (Optional)this.lootTable.get(name), this.requiredFeatures, this.allowedInPeaceful);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityType$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */