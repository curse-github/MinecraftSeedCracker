/*     */ package net.minecraft.world.item.equipment;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final EquipmentSlot slot;
/*     */   private Holder<SoundEvent> equipSound;
/*     */   private Optional<ResourceKey<EquipmentAsset>> assetId;
/*     */   private Optional<Identifier> cameraOverlay;
/*     */   private Optional<HolderSet<EntityType<?>>> allowedEntities;
/*     */   private boolean dispensable;
/*     */   private boolean swappable;
/*     */   private boolean damageOnHurt;
/*     */   private boolean equipOnInteract;
/*     */   private boolean canBeSheared;
/*     */   private Holder<SoundEvent> shearingSound;
/*     */   
/*     */   private Builder(EquipmentSlot slot) {
/* 165 */     this.equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
/* 166 */     this.assetId = Optional.empty();
/* 167 */     this.cameraOverlay = Optional.empty();
/* 168 */     this.allowedEntities = Optional.empty();
/* 169 */     this.dispensable = true;
/* 170 */     this.swappable = true;
/* 171 */     this.damageOnHurt = true;
/*     */ 
/*     */     
/* 174 */     this.shearingSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP);
/*     */ 
/*     */     
/* 177 */     this.slot = slot;
/*     */   }
/*     */   
/*     */   public Builder setEquipSound(Holder<SoundEvent> equipSound) {
/* 181 */     this.equipSound = equipSound;
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setAsset(ResourceKey<EquipmentAsset> assetId) {
/* 186 */     this.assetId = Optional.of(assetId);
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCameraOverlay(Identifier cameraOverlay) {
/* 191 */     this.cameraOverlay = Optional.of(cameraOverlay);
/* 192 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 196 */   public Builder setAllowedEntities(EntityType... allowedEntities) { return setAllowedEntities(HolderSet.direct(EntityType::builtInRegistryHolder, allowedEntities)); }
/*     */ 
/*     */   
/*     */   public Builder setAllowedEntities(HolderSet<EntityType<?>> allowedEntities) {
/* 200 */     this.allowedEntities = Optional.of(allowedEntities);
/* 201 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDispensable(boolean dispensable) {
/* 205 */     this.dispensable = dispensable;
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSwappable(boolean swappable) {
/* 210 */     this.swappable = swappable;
/* 211 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDamageOnHurt(boolean damageOnHurt) {
/* 215 */     this.damageOnHurt = damageOnHurt;
/* 216 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setEquipOnInteract(boolean equipOnInteract) {
/* 220 */     this.equipOnInteract = equipOnInteract;
/* 221 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCanBeSheared(boolean canBeSheared) {
/* 225 */     this.canBeSheared = canBeSheared;
/* 226 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setShearingSound(Holder<SoundEvent> shearingSound) {
/* 230 */     this.shearingSound = shearingSound;
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 235 */   public Equippable build() { return new Equippable(this.slot, this.equipSound, this.assetId, this.cameraOverlay, this.allowedEntities, this.dispensable, this.swappable, this.damageOnHurt, this.equipOnInteract, this.canBeSheared, this.shearingSound); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\Equippable$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */