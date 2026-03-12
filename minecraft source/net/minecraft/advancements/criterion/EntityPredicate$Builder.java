/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 209 */   private Optional<EntityTypePredicate> entityType = Optional.empty();
/* 210 */   private Optional<DistancePredicate> distanceToPlayer = Optional.empty();
/* 211 */   private Optional<MovementPredicate> movement = Optional.empty();
/* 212 */   private Optional<LocationPredicate> located = Optional.empty();
/* 213 */   private Optional<LocationPredicate> steppingOnLocation = Optional.empty();
/* 214 */   private Optional<LocationPredicate> movementAffectedBy = Optional.empty();
/* 215 */   private Optional<MobEffectsPredicate> effects = Optional.empty();
/* 216 */   private Optional<NbtPredicate> nbt = Optional.empty();
/* 217 */   private Optional<EntityFlagsPredicate> flags = Optional.empty();
/* 218 */   private Optional<EntityEquipmentPredicate> equipment = Optional.empty();
/* 219 */   private Optional<EntitySubPredicate> subPredicate = Optional.empty();
/* 220 */   private Optional<Integer> periodicTick = Optional.empty();
/* 221 */   private Optional<EntityPredicate> vehicle = Optional.empty();
/* 222 */   private Optional<EntityPredicate> passenger = Optional.empty();
/* 223 */   private Optional<EntityPredicate> targetedEntity = Optional.empty();
/* 224 */   private Optional<String> team = Optional.empty();
/* 225 */   private Optional<SlotsPredicate> slots = Optional.empty();
/* 226 */   private DataComponentMatchers components = DataComponentMatchers.ANY;
/*     */ 
/*     */   
/* 229 */   public static Builder entity() { return new Builder(); }
/*     */ 
/*     */   
/*     */   public Builder of(HolderGetter<EntityType<?>> lookup, EntityType<?> entityType) {
/* 233 */     this.entityType = Optional.of(EntityTypePredicate.of(lookup, entityType));
/* 234 */     return this;
/*     */   }
/*     */   
/*     */   public Builder of(HolderGetter<EntityType<?>> lookup, TagKey<EntityType<?>> entityTypeTag) {
/* 238 */     this.entityType = Optional.of(EntityTypePredicate.of(lookup, entityTypeTag));
/* 239 */     return this;
/*     */   }
/*     */   
/*     */   public Builder entityType(EntityTypePredicate entityType) {
/* 243 */     this.entityType = Optional.of(entityType);
/* 244 */     return this;
/*     */   }
/*     */   
/*     */   public Builder distance(DistancePredicate distanceToPlayer) {
/* 248 */     this.distanceToPlayer = Optional.of(distanceToPlayer);
/* 249 */     return this;
/*     */   }
/*     */   
/*     */   public Builder moving(MovementPredicate movement) {
/* 253 */     this.movement = Optional.of(movement);
/* 254 */     return this;
/*     */   }
/*     */   
/*     */   public Builder located(LocationPredicate.Builder location) {
/* 258 */     this.located = Optional.of(location.build());
/* 259 */     return this;
/*     */   }
/*     */   
/*     */   public Builder steppingOn(LocationPredicate.Builder location) {
/* 263 */     this.steppingOnLocation = Optional.of(location.build());
/* 264 */     return this;
/*     */   }
/*     */   
/*     */   public Builder movementAffectedBy(LocationPredicate.Builder location) {
/* 268 */     this.movementAffectedBy = Optional.of(location.build());
/* 269 */     return this;
/*     */   }
/*     */   
/*     */   public Builder effects(MobEffectsPredicate.Builder effects) {
/* 273 */     this.effects = effects.build();
/* 274 */     return this;
/*     */   }
/*     */   
/*     */   public Builder nbt(NbtPredicate nbt) {
/* 278 */     this.nbt = Optional.of(nbt);
/* 279 */     return this;
/*     */   }
/*     */   
/*     */   public Builder flags(EntityFlagsPredicate.Builder flags) {
/* 283 */     this.flags = Optional.of(flags.build());
/* 284 */     return this;
/*     */   }
/*     */   
/*     */   public Builder equipment(EntityEquipmentPredicate.Builder equipment) {
/* 288 */     this.equipment = Optional.of(equipment.build());
/* 289 */     return this;
/*     */   }
/*     */   
/*     */   public Builder equipment(EntityEquipmentPredicate equipment) {
/* 293 */     this.equipment = Optional.of(equipment);
/* 294 */     return this;
/*     */   }
/*     */   
/*     */   public Builder subPredicate(EntitySubPredicate subPredicate) {
/* 298 */     this.subPredicate = Optional.of(subPredicate);
/* 299 */     return this;
/*     */   }
/*     */   
/*     */   public Builder periodicTick(int period) {
/* 303 */     this.periodicTick = Optional.of(Integer.valueOf(period));
/* 304 */     return this;
/*     */   }
/*     */   
/*     */   public Builder vehicle(Builder vehicle) {
/* 308 */     this.vehicle = Optional.of(vehicle.build());
/* 309 */     return this;
/*     */   }
/*     */   
/*     */   public Builder passenger(Builder passenger) {
/* 313 */     this.passenger = Optional.of(passenger.build());
/* 314 */     return this;
/*     */   }
/*     */   
/*     */   public Builder targetedEntity(Builder targetedEntity) {
/* 318 */     this.targetedEntity = Optional.of(targetedEntity.build());
/* 319 */     return this;
/*     */   }
/*     */   
/*     */   public Builder team(String team) {
/* 323 */     this.team = Optional.of(team);
/* 324 */     return this;
/*     */   }
/*     */   
/*     */   public Builder slots(SlotsPredicate slots) {
/* 328 */     this.slots = Optional.of(slots);
/* 329 */     return this;
/*     */   }
/*     */   
/*     */   public Builder components(DataComponentMatchers components) {
/* 333 */     this.components = components;
/* 334 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 338 */   public EntityPredicate build() { return new EntityPredicate(this.entityType, this.distanceToPlayer, this.movement, new EntityPredicate.LocationWrapper(this.located, this.steppingOnLocation, this.movementAffectedBy), this.effects, this.nbt, this.flags, this.equipment, this.subPredicate, this.periodicTick, this.vehicle, this.passenger, this.targetedEntity, this.team, this.slots, this.components); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EntityPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */