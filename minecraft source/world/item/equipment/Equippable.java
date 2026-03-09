/*     */ package net.minecraft.world.item.equipment;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class Equippable extends Record {
/*     */   private final EquipmentSlot slot;
/*     */   private final Holder<SoundEvent> equipSound;
/*     */   private final Optional<ResourceKey<EquipmentAsset>> assetId;
/*     */   private final Optional<Identifier> cameraOverlay;
/*     */   private final Optional<HolderSet<EntityType<?>>> allowedEntities;
/*     */   
/*  33 */   public Equippable(EquipmentSlot slot, Holder<SoundEvent> equipSound, Optional<ResourceKey<EquipmentAsset>> assetId, Optional<Identifier> cameraOverlay, Optional<HolderSet<EntityType<?>>> allowedEntities, boolean dispensable, boolean swappable, boolean damageOnHurt, boolean equipOnInteract, boolean canBeSheared, Holder<SoundEvent> shearingSound) { this.slot = slot; this.equipSound = equipSound; this.assetId = assetId; this.cameraOverlay = cameraOverlay; this.allowedEntities = allowedEntities; this.dispensable = dispensable; this.swappable = swappable; this.damageOnHurt = damageOnHurt; this.equipOnInteract = equipOnInteract; this.canBeSheared = canBeSheared; this.shearingSound = shearingSound; } private final boolean dispensable; private final boolean swappable; private final boolean damageOnHurt; private final boolean equipOnInteract; private final boolean canBeSheared; private final Holder<SoundEvent> shearingSound; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/Equippable;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/Equippable; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/Equippable;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/Equippable; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/Equippable;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/Equippable;
/*  33 */     //   0	8	1	o	Ljava/lang/Object; } public EquipmentSlot slot() { return this.slot; } public Holder<SoundEvent> equipSound() { return this.equipSound; } public Optional<ResourceKey<EquipmentAsset>> assetId() { return this.assetId; } public Optional<Identifier> cameraOverlay() { return this.cameraOverlay; } public Optional<HolderSet<EntityType<?>>> allowedEntities() { return this.allowedEntities; } public boolean dispensable() { return this.dispensable; } public boolean swappable() { return this.swappable; } public boolean damageOnHurt() { return this.damageOnHurt; } public boolean equipOnInteract() { return this.equipOnInteract; } public boolean canBeSheared() { return this.canBeSheared; } public Holder<SoundEvent> shearingSound() { return this.shearingSound; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final Codec<Equippable> CODEC = RecordCodecBuilder.create(i -> i.group(EquipmentSlot.CODEC
/*  47 */         .fieldOf("slot").forGetter(Equippable::slot), SoundEvent.CODEC
/*  48 */         .optionalFieldOf("equip_sound", SoundEvents.ARMOR_EQUIP_GENERIC).forGetter(Equippable::equipSound), 
/*  49 */         ResourceKey.codec(EquipmentAssets.ROOT_ID).optionalFieldOf("asset_id").forGetter(Equippable::assetId), Identifier.CODEC
/*  50 */         .optionalFieldOf("camera_overlay").forGetter(Equippable::cameraOverlay), 
/*  51 */         RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("allowed_entities").forGetter(Equippable::allowedEntities), Codec.BOOL
/*  52 */         .optionalFieldOf("dispensable", Boolean.valueOf(true)).forGetter(Equippable::dispensable), Codec.BOOL
/*  53 */         .optionalFieldOf("swappable", Boolean.valueOf(true)).forGetter(Equippable::swappable), Codec.BOOL
/*  54 */         .optionalFieldOf("damage_on_hurt", Boolean.valueOf(true)).forGetter(Equippable::damageOnHurt), Codec.BOOL
/*  55 */         .optionalFieldOf("equip_on_interact", Boolean.valueOf(false)).forGetter(Equippable::equipOnInteract), Codec.BOOL
/*  56 */         .optionalFieldOf("can_be_sheared", Boolean.valueOf(false)).forGetter(Equippable::canBeSheared), SoundEvent.CODEC
/*  57 */         .optionalFieldOf("shearing_sound", BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP)).forGetter(Equippable::shearingSound))
/*  58 */       .apply(i, Equippable::new));
/*     */   
/*  60 */   public static final StreamCodec<RegistryFriendlyByteBuf, Equippable> STREAM_CODEC = StreamCodec.composite(EquipmentSlot.STREAM_CODEC, Equippable::slot, SoundEvent.STREAM_CODEC, Equippable::equipSound, 
/*     */ 
/*     */       
/*  63 */       ResourceKey.streamCodec(EquipmentAssets.ROOT_ID).apply(ByteBufCodecs::optional), Equippable::assetId, Identifier.STREAM_CODEC
/*  64 */       .apply(ByteBufCodecs::optional), Equippable::cameraOverlay, 
/*  65 */       ByteBufCodecs.holderSet(Registries.ENTITY_TYPE).apply(ByteBufCodecs::optional), Equippable::allowedEntities, ByteBufCodecs.BOOL, Equippable::dispensable, ByteBufCodecs.BOOL, Equippable::swappable, ByteBufCodecs.BOOL, Equippable::damageOnHurt, ByteBufCodecs.BOOL, Equippable::equipOnInteract, ByteBufCodecs.BOOL, Equippable::canBeSheared, SoundEvent.STREAM_CODEC, Equippable::shearingSound, Equippable::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public static Equippable llamaSwag(DyeColor color) { return builder(EquipmentSlot.BODY)
/*  77 */       .setEquipSound(SoundEvents.LLAMA_SWAG)
/*  78 */       .setAsset((ResourceKey)EquipmentAssets.CARPETS.get(color))
/*  79 */       .setAllowedEntities(new EntityType[] { EntityType.LLAMA, EntityType.TRADER_LLAMA
/*  80 */         }).setCanBeSheared(true)
/*  81 */       .setShearingSound(SoundEvents.LLAMA_CARPET_UNEQUIP)
/*  82 */       .build(); }
/*     */ 
/*     */   
/*     */   public static Equippable saddle() {
/*  86 */     entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
/*  87 */     return builder(EquipmentSlot.SADDLE)
/*  88 */       .setEquipSound(SoundEvents.HORSE_SADDLE)
/*  89 */       .setAsset(EquipmentAssets.SADDLE)
/*  90 */       .setAllowedEntities(entityGetter.getOrThrow(EntityTypeTags.CAN_EQUIP_SADDLE))
/*  91 */       .setEquipOnInteract(true)
/*  92 */       .setCanBeSheared(true)
/*  93 */       .setShearingSound(SoundEvents.SADDLE_UNEQUIP)
/*  94 */       .build();
/*     */   }
/*     */   
/*     */   public static Equippable harness(DyeColor color) {
/*  98 */     HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
/*  99 */     return builder(EquipmentSlot.BODY)
/* 100 */       .setEquipSound(SoundEvents.HARNESS_EQUIP)
/* 101 */       .setAsset((ResourceKey)EquipmentAssets.HARNESSES.get(color))
/* 102 */       .setAllowedEntities(entityGetter.getOrThrow(EntityTypeTags.CAN_EQUIP_HARNESS))
/* 103 */       .setEquipOnInteract(true)
/* 104 */       .setCanBeSheared(true)
/* 105 */       .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.HARNESS_UNEQUIP))
/* 106 */       .build();
/*     */   }
/*     */ 
/*     */   
/* 110 */   public static Builder builder(EquipmentSlot slot) { return new Builder(slot); }
/*     */ 
/*     */   
/*     */   public InteractionResult swapWithEquipmentSlot(ItemStack inHand, Player player) {
/* 114 */     if (!player.canUseSlot(this.slot) || !canBeEquippedBy(player.getType())) {
/* 115 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 118 */     ItemStack inEquipmentSlot = player.getItemBySlot(this.slot);
/*     */     
/* 120 */     if ((EnchantmentHelper.has(inEquipmentSlot, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) && !player.isCreative()) || ItemStack.isSameItemSameComponents(inHand, inEquipmentSlot)) {
/* 121 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/* 124 */     if (!player.level().isClientSide()) {
/* 125 */       player.awardStat(Stats.ITEM_USED.get(inHand.getItem()));
/*     */     }
/*     */     
/* 128 */     if (inHand.getCount() <= 1) {
/*     */       
/* 130 */       ItemStack swappedToHand = inEquipmentSlot.isEmpty() ? inHand : inEquipmentSlot.copyAndClear();
/* 131 */       ItemStack swappedToEquipment = player.isCreative() ? inHand.copy() : inHand.copyAndClear();
/* 132 */       player.setItemSlot(this.slot, swappedToEquipment);
/* 133 */       return InteractionResult.SUCCESS.heldItemTransformedTo(swappedToHand);
/*     */     } 
/*     */     
/* 136 */     ItemStack swappedToInventory = inEquipmentSlot.copyAndClear();
/* 137 */     ItemStack swappedToEquipment = inHand.consumeAndReturn(1, player);
/* 138 */     player.setItemSlot(this.slot, swappedToEquipment);
/* 139 */     if (!player.getInventory().add(swappedToInventory)) {
/* 140 */       player.drop(swappedToInventory, false);
/*     */     }
/* 142 */     return InteractionResult.SUCCESS.heldItemTransformedTo(inHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult equipOnTarget(Player player, LivingEntity target, ItemStack itemStack) {
/* 147 */     if (!target.isEquippableInSlot(itemStack, this.slot) || target.hasItemInSlot(this.slot) || !target.isAlive()) {
/* 148 */       return InteractionResult.PASS;
/*     */     }
/* 150 */     if (!player.level().isClientSide()) {
/* 151 */       target.setItemSlot(this.slot, itemStack.split(1));
/* 152 */       if (target instanceof Mob) { Mob mob = (Mob)target;
/* 153 */         mob.setGuaranteedDrop(this.slot); }
/*     */     
/*     */     } 
/* 156 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/* 160 */   public boolean canBeEquippedBy(EntityType<?> type) { return (this.allowedEntities.isEmpty() || ((HolderSet)this.allowedEntities.get()).contains(type.builtInRegistryHolder())); }
/*     */   public static class Builder { private final EquipmentSlot slot; private Holder<SoundEvent> equipSound; private Optional<ResourceKey<EquipmentAsset>> assetId; private Optional<Identifier> cameraOverlay; private Optional<HolderSet<EntityType<?>>> allowedEntities; private boolean dispensable; private boolean swappable; private boolean damageOnHurt; private boolean equipOnInteract; private boolean canBeSheared;
/*     */     private Holder<SoundEvent> shearingSound;
/*     */     
/*     */     private Builder(EquipmentSlot slot) {
/* 165 */       this.equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
/* 166 */       this.assetId = Optional.empty();
/* 167 */       this.cameraOverlay = Optional.empty();
/* 168 */       this.allowedEntities = Optional.empty();
/* 169 */       this.dispensable = true;
/* 170 */       this.swappable = true;
/* 171 */       this.damageOnHurt = true;
/*     */ 
/*     */       
/* 174 */       this.shearingSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP);
/*     */ 
/*     */       
/* 177 */       this.slot = slot;
/*     */     }
/*     */     
/*     */     public Builder setEquipSound(Holder<SoundEvent> equipSound) {
/* 181 */       this.equipSound = equipSound;
/* 182 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAsset(ResourceKey<EquipmentAsset> assetId) {
/* 186 */       this.assetId = Optional.of(assetId);
/* 187 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCameraOverlay(Identifier cameraOverlay) {
/* 191 */       this.cameraOverlay = Optional.of(cameraOverlay);
/* 192 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 196 */     public Builder setAllowedEntities(EntityType... allowedEntities) { return setAllowedEntities(HolderSet.direct(EntityType::builtInRegistryHolder, allowedEntities)); }
/*     */ 
/*     */     
/*     */     public Builder setAllowedEntities(HolderSet<EntityType<?>> allowedEntities) {
/* 200 */       this.allowedEntities = Optional.of(allowedEntities);
/* 201 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDispensable(boolean dispensable) {
/* 205 */       this.dispensable = dispensable;
/* 206 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSwappable(boolean swappable) {
/* 210 */       this.swappable = swappable;
/* 211 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDamageOnHurt(boolean damageOnHurt) {
/* 215 */       this.damageOnHurt = damageOnHurt;
/* 216 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setEquipOnInteract(boolean equipOnInteract) {
/* 220 */       this.equipOnInteract = equipOnInteract;
/* 221 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCanBeSheared(boolean canBeSheared) {
/* 225 */       this.canBeSheared = canBeSheared;
/* 226 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShearingSound(Holder<SoundEvent> shearingSound) {
/* 230 */       this.shearingSound = shearingSound;
/* 231 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 235 */     public Equippable build() { return new Equippable(this.slot, this.equipSound, this.assetId, this.cameraOverlay, this.allowedEntities, this.dispensable, this.swappable, this.damageOnHurt, this.equipOnInteract, this.canBeSheared, this.shearingSound); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\Equippable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */