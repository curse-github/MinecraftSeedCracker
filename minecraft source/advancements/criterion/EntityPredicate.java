/*     */ package net.minecraft.advancements.criterion;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ public final class EntityPredicate extends Record {
/*     */   private final Optional<EntityTypePredicate> entityType;
/*     */   private final Optional<DistancePredicate> distanceToPlayer;
/*     */   private final Optional<MovementPredicate> movement;
/*     */   private final LocationWrapper location;
/*     */   private final Optional<MobEffectsPredicate> effects;
/*     */   private final Optional<NbtPredicate> nbt;
/*     */   private final Optional<EntityFlagsPredicate> flags;
/*     */   private final Optional<EntityEquipmentPredicate> equipment;
/*     */   
/*  29 */   public EntityPredicate(Optional<EntityTypePredicate> entityType, Optional<DistancePredicate> distanceToPlayer, Optional<MovementPredicate> movement, LocationWrapper location, Optional<MobEffectsPredicate> effects, Optional<NbtPredicate> nbt, Optional<EntityFlagsPredicate> flags, Optional<EntityEquipmentPredicate> equipment, Optional<EntitySubPredicate> subPredicate, Optional<Integer> periodicTick, Optional<EntityPredicate> vehicle, Optional<EntityPredicate> passenger, Optional<EntityPredicate> targetedEntity, Optional<String> team, Optional<SlotsPredicate> slots, DataComponentMatchers components) { this.entityType = entityType; this.distanceToPlayer = distanceToPlayer; this.movement = movement; this.location = location; this.effects = effects; this.nbt = nbt; this.flags = flags; this.equipment = equipment; this.subPredicate = subPredicate; this.periodicTick = periodicTick; this.vehicle = vehicle; this.passenger = passenger; this.targetedEntity = targetedEntity; this.team = team; this.slots = slots; this.components = components; } private final Optional<EntitySubPredicate> subPredicate; private final Optional<Integer> periodicTick; private final Optional<EntityPredicate> vehicle; private final Optional<EntityPredicate> passenger; private final Optional<EntityPredicate> targetedEntity; private final Optional<String> team; private final Optional<SlotsPredicate> slots; private final DataComponentMatchers components; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EntityPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EntityPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EntityPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #29	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate;
/*  29 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<EntityTypePredicate> entityType() { return this.entityType; } public Optional<DistancePredicate> distanceToPlayer() { return this.distanceToPlayer; } public Optional<MovementPredicate> movement() { return this.movement; } public LocationWrapper location() { return this.location; } public Optional<MobEffectsPredicate> effects() { return this.effects; } public Optional<NbtPredicate> nbt() { return this.nbt; } public Optional<EntityFlagsPredicate> flags() { return this.flags; } public Optional<EntityEquipmentPredicate> equipment() { return this.equipment; } public Optional<EntitySubPredicate> subPredicate() { return this.subPredicate; } public Optional<Integer> periodicTick() { return this.periodicTick; } public Optional<EntityPredicate> vehicle() { return this.vehicle; } public Optional<EntityPredicate> passenger() { return this.passenger; } public Optional<EntityPredicate> targetedEntity() { return this.targetedEntity; } public Optional<String> team() { return this.team; } public Optional<SlotsPredicate> slots() { return this.slots; } public DataComponentMatchers components() { return this.components; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class LocationWrapper
/*     */     extends Record
/*     */   {
/*     */     private final Optional<LocationPredicate> located;
/*     */ 
/*     */     
/*     */     private final Optional<LocationPredicate> steppingOn;
/*     */ 
/*     */     
/*     */     private final Optional<LocationPredicate> affectsMovement;
/*     */ 
/*     */ 
/*     */     
/*  47 */     public LocationWrapper(Optional<LocationPredicate> located, Optional<LocationPredicate> steppingOn, Optional<LocationPredicate> affectsMovement) { this.located = located; this.steppingOn = steppingOn; this.affectsMovement = affectsMovement; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #47	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/EntityPredicate$LocationWrapper;
/*  47 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<LocationPredicate> located() { return this.located; } public Optional<LocationPredicate> steppingOn() { return this.steppingOn; } public Optional<LocationPredicate> affectsMovement() { return this.affectsMovement; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     public static final MapCodec<LocationWrapper> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LocationPredicate.CODEC
/*  53 */           .optionalFieldOf("location").forGetter(LocationWrapper::located), LocationPredicate.CODEC
/*  54 */           .optionalFieldOf("stepping_on").forGetter(LocationWrapper::steppingOn), LocationPredicate.CODEC
/*  55 */           .optionalFieldOf("movement_affected_by").forGetter(LocationWrapper::affectsMovement))
/*  56 */         .apply(i, LocationWrapper::new));
/*     */   }
/*     */   
/*  59 */   public static final Codec<EntityPredicate> CODEC = Codec.recursive("EntityPredicate", subCodec -> RecordCodecBuilder.create(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final Codec<ContextAwarePredicate> ADVANCEMENT_CODEC = Codec.withAlternative(ContextAwarePredicate.CODEC, CODEC, EntityPredicate::wrap);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static ContextAwarePredicate wrap(Builder singlePredicate) { return wrap(singlePredicate.build()); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static Optional<ContextAwarePredicate> wrap(Optional<EntityPredicate> singlePredicate) { return singlePredicate.map(EntityPredicate::wrap); }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static List<ContextAwarePredicate> wrap(Builder... predicates) { return Stream.of(predicates).map(EntityPredicate::wrap).toList(); }
/*     */ 
/*     */   
/*     */   public static ContextAwarePredicate wrap(EntityPredicate singlePredicate) {
/*  98 */     LootItemCondition asCondition = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, singlePredicate).build();
/*  99 */     return new ContextAwarePredicate(List.of(asCondition));
/*     */   }
/*     */ 
/*     */   
/* 103 */   public boolean matches(ServerPlayer player, Entity entity) { return matches(player.level(), player.position(), entity); }
/*     */ 
/*     */   
/*     */   public boolean matches(ServerLevel level, Vec3 position, Entity entity) {
/* 107 */     if (entity == null) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (this.entityType.isPresent() && !((EntityTypePredicate)this.entityType.get()).matches(entity.getType())) {
/* 111 */       return false;
/*     */     }
/* 113 */     if (position == null) {
/* 114 */       if (this.distanceToPlayer.isPresent()) {
/* 115 */         return false;
/*     */       }
/*     */     }
/* 118 */     else if (this.distanceToPlayer.isPresent() && !((DistancePredicate)this.distanceToPlayer.get()).matches(position.x, position.y, position.z, entity.getX(), entity.getY(), entity.getZ())) {
/* 119 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 123 */     if (this.movement.isPresent()) {
/* 124 */       Vec3 knownMovement = entity.getKnownMovement();
/* 125 */       Vec3 velocity = knownMovement.scale(20.0D);
/* 126 */       if (!((MovementPredicate)this.movement.get()).matches(velocity.x, velocity.y, velocity.z, entity.fallDistance)) {
/* 127 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 131 */     if (this.location.located.isPresent() && !((LocationPredicate)this.location.located.get()).matches(level, entity.getX(), entity.getY(), entity.getZ())) {
/* 132 */       return false;
/*     */     }
/*     */     
/* 135 */     if (this.location.steppingOn.isPresent()) {
/* 136 */       Vec3 onPos = Vec3.atCenterOf(entity.getOnPos());
/* 137 */       if (!entity.onGround() || !((LocationPredicate)this.location.steppingOn.get()).matches(level, onPos.x(), onPos.y(), onPos.z())) {
/* 138 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 142 */     if (this.location.affectsMovement.isPresent()) {
/* 143 */       Vec3 onPos = Vec3.atCenterOf(entity.getBlockPosBelowThatAffectsMyMovement());
/* 144 */       if (!((LocationPredicate)this.location.affectsMovement.get()).matches(level, onPos.x(), onPos.y(), onPos.z())) {
/* 145 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 149 */     if (this.effects.isPresent() && !((MobEffectsPredicate)this.effects.get()).matches(entity)) {
/* 150 */       return false;
/*     */     }
/* 152 */     if (this.flags.isPresent() && !((EntityFlagsPredicate)this.flags.get()).matches(entity)) {
/* 153 */       return false;
/*     */     }
/*     */     
/* 156 */     if (this.equipment.isPresent() && !((EntityEquipmentPredicate)this.equipment.get()).matches(entity)) {
/* 157 */       return false;
/*     */     }
/*     */     
/* 160 */     if (this.subPredicate.isPresent() && !((EntitySubPredicate)this.subPredicate.get()).matches(entity, level, position)) {
/* 161 */       return false;
/*     */     }
/*     */     
/* 164 */     if (this.vehicle.isPresent() && !((EntityPredicate)this.vehicle.get()).matches(level, position, entity.getVehicle())) {
/* 165 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 169 */     if (this.passenger.isPresent() && entity.getPassengers().stream().noneMatch(p -> ((EntityPredicate)this.passenger.get()).matches(level, position, p))) {
/* 170 */       return false;
/*     */     }
/*     */     
/* 173 */     if (this.targetedEntity.isPresent() && !((EntityPredicate)this.targetedEntity.get()).matches(level, position, (entity instanceof Mob) ? ((Mob)entity).getTarget() : null)) {
/* 174 */       return false;
/*     */     }
/*     */     
/* 177 */     if (this.periodicTick.isPresent() && entity.tickCount % ((Integer)this.periodicTick.get()).intValue() != 0) {
/* 178 */       return false;
/*     */     }
/*     */     
/* 181 */     if (this.team.isPresent()) {
/* 182 */       PlayerTeam playerTeam = entity.getTeam();
/* 183 */       if (playerTeam == null || !((String)this.team.get()).equals(playerTeam.getName())) {
/* 184 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 188 */     if (this.slots.isPresent() && !((SlotsPredicate)this.slots.get()).matches(entity)) {
/* 189 */       return false;
/*     */     }
/*     */     
/* 192 */     if (!this.components.test(entity)) {
/* 193 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 197 */     return (this.nbt.isEmpty() || ((NbtPredicate)this.nbt.get()).matches(entity));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LootContext createContext(ServerPlayer player, Entity entity) {
/* 204 */     LootParams lootParams = (new LootParams.Builder(player.level())).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, player.position()).create(LootContextParamSets.ADVANCEMENT_ENTITY);
/* 205 */     return (new LootContext.Builder(lootParams)).create(Optional.empty());
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 209 */     private Optional<EntityTypePredicate> entityType = Optional.empty();
/* 210 */     private Optional<DistancePredicate> distanceToPlayer = Optional.empty();
/* 211 */     private Optional<MovementPredicate> movement = Optional.empty();
/* 212 */     private Optional<LocationPredicate> located = Optional.empty();
/* 213 */     private Optional<LocationPredicate> steppingOnLocation = Optional.empty();
/* 214 */     private Optional<LocationPredicate> movementAffectedBy = Optional.empty();
/* 215 */     private Optional<MobEffectsPredicate> effects = Optional.empty();
/* 216 */     private Optional<NbtPredicate> nbt = Optional.empty();
/* 217 */     private Optional<EntityFlagsPredicate> flags = Optional.empty();
/* 218 */     private Optional<EntityEquipmentPredicate> equipment = Optional.empty();
/* 219 */     private Optional<EntitySubPredicate> subPredicate = Optional.empty();
/* 220 */     private Optional<Integer> periodicTick = Optional.empty();
/* 221 */     private Optional<EntityPredicate> vehicle = Optional.empty();
/* 222 */     private Optional<EntityPredicate> passenger = Optional.empty();
/* 223 */     private Optional<EntityPredicate> targetedEntity = Optional.empty();
/* 224 */     private Optional<String> team = Optional.empty();
/* 225 */     private Optional<SlotsPredicate> slots = Optional.empty();
/* 226 */     private DataComponentMatchers components = DataComponentMatchers.ANY;
/*     */ 
/*     */     
/* 229 */     public static Builder entity() { return new Builder(); }
/*     */ 
/*     */     
/*     */     public Builder of(HolderGetter<EntityType<?>> lookup, EntityType<?> entityType) {
/* 233 */       this.entityType = Optional.of(EntityTypePredicate.of(lookup, entityType));
/* 234 */       return this;
/*     */     }
/*     */     
/*     */     public Builder of(HolderGetter<EntityType<?>> lookup, TagKey<EntityType<?>> entityTypeTag) {
/* 238 */       this.entityType = Optional.of(EntityTypePredicate.of(lookup, entityTypeTag));
/* 239 */       return this;
/*     */     }
/*     */     
/*     */     public Builder entityType(EntityTypePredicate entityType) {
/* 243 */       this.entityType = Optional.of(entityType);
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     public Builder distance(DistancePredicate distanceToPlayer) {
/* 248 */       this.distanceToPlayer = Optional.of(distanceToPlayer);
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     public Builder moving(MovementPredicate movement) {
/* 253 */       this.movement = Optional.of(movement);
/* 254 */       return this;
/*     */     }
/*     */     
/*     */     public Builder located(LocationPredicate.Builder location) {
/* 258 */       this.located = Optional.of(location.build());
/* 259 */       return this;
/*     */     }
/*     */     
/*     */     public Builder steppingOn(LocationPredicate.Builder location) {
/* 263 */       this.steppingOnLocation = Optional.of(location.build());
/* 264 */       return this;
/*     */     }
/*     */     
/*     */     public Builder movementAffectedBy(LocationPredicate.Builder location) {
/* 268 */       this.movementAffectedBy = Optional.of(location.build());
/* 269 */       return this;
/*     */     }
/*     */     
/*     */     public Builder effects(MobEffectsPredicate.Builder effects) {
/* 273 */       this.effects = effects.build();
/* 274 */       return this;
/*     */     }
/*     */     
/*     */     public Builder nbt(NbtPredicate nbt) {
/* 278 */       this.nbt = Optional.of(nbt);
/* 279 */       return this;
/*     */     }
/*     */     
/*     */     public Builder flags(EntityFlagsPredicate.Builder flags) {
/* 283 */       this.flags = Optional.of(flags.build());
/* 284 */       return this;
/*     */     }
/*     */     
/*     */     public Builder equipment(EntityEquipmentPredicate.Builder equipment) {
/* 288 */       this.equipment = Optional.of(equipment.build());
/* 289 */       return this;
/*     */     }
/*     */     
/*     */     public Builder equipment(EntityEquipmentPredicate equipment) {
/* 293 */       this.equipment = Optional.of(equipment);
/* 294 */       return this;
/*     */     }
/*     */     
/*     */     public Builder subPredicate(EntitySubPredicate subPredicate) {
/* 298 */       this.subPredicate = Optional.of(subPredicate);
/* 299 */       return this;
/*     */     }
/*     */     
/*     */     public Builder periodicTick(int period) {
/* 303 */       this.periodicTick = Optional.of(Integer.valueOf(period));
/* 304 */       return this;
/*     */     }
/*     */     
/*     */     public Builder vehicle(Builder vehicle) {
/* 308 */       this.vehicle = Optional.of(vehicle.build());
/* 309 */       return this;
/*     */     }
/*     */     
/*     */     public Builder passenger(Builder passenger) {
/* 313 */       this.passenger = Optional.of(passenger.build());
/* 314 */       return this;
/*     */     }
/*     */     
/*     */     public Builder targetedEntity(Builder targetedEntity) {
/* 318 */       this.targetedEntity = Optional.of(targetedEntity.build());
/* 319 */       return this;
/*     */     }
/*     */     
/*     */     public Builder team(String team) {
/* 323 */       this.team = Optional.of(team);
/* 324 */       return this;
/*     */     }
/*     */     
/*     */     public Builder slots(SlotsPredicate slots) {
/* 328 */       this.slots = Optional.of(slots);
/* 329 */       return this;
/*     */     }
/*     */     
/*     */     public Builder components(DataComponentMatchers components) {
/* 333 */       this.components = components;
/* 334 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 338 */     public EntityPredicate build() { return new EntityPredicate(this.entityType, this.distanceToPlayer, this.movement, new EntityPredicate.LocationWrapper(this.located, this.steppingOnLocation, this.movementAffectedBy), this.effects, this.nbt, this.flags, this.equipment, this.subPredicate, this.periodicTick, this.vehicle, this.passenger, this.targetedEntity, this.team, this.slots, this.components); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EntityPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */