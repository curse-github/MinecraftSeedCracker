/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.players.OldUsersConverter;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UUIDLookup;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public final class EntityReference<StoredEntityType extends UniquelyIdentifyable> extends Object {
/*  21 */   private static final Codec<? extends EntityReference<?>> CODEC = UUIDUtil.CODEC.xmap(EntityReference::new, EntityReference::getUUID);
/*  22 */   private static final StreamCodec<ByteBuf, ? extends EntityReference<?>> STREAM_CODEC = UUIDUtil.STREAM_CODEC.map(EntityReference::new, EntityReference::getUUID);
/*     */   
/*     */   private Either<UUID, StoredEntityType> entity;
/*     */   
/*  26 */   public static <Type extends UniquelyIdentifyable> Codec<EntityReference<Type>> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static <Type extends UniquelyIdentifyable> StreamCodec<ByteBuf, EntityReference<Type>> streamCodec() { return STREAM_CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   private EntityReference(StoredEntityType entity) { this.entity = Either.right(entity); }
/*     */ 
/*     */ 
/*     */   
/*  41 */   private EntityReference(UUID uuid) { this.entity = Either.left(uuid); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public static <T extends UniquelyIdentifyable> EntityReference<T> of(T entity) { return (entity != null) ? new EntityReference(entity) : null; }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static <T extends UniquelyIdentifyable> EntityReference<T> of(UUID uuid) { return new EntityReference(uuid); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public UUID getUUID() { return (UUID)this.entity.map(uuid -> uuid, UniquelyIdentifyable::getUUID); }
/*     */ 
/*     */   
/*     */   public StoredEntityType getEntity(UUIDLookup<? extends UniquelyIdentifyable> lookup, Class<StoredEntityType> clazz) {
/*  57 */     Optional<StoredEntityType> stored = this.entity.right();
/*  58 */     if (stored.isPresent()) {
/*  59 */       StoredEntityType storedEntity = (StoredEntityType)(UniquelyIdentifyable)stored.get();
/*  60 */       if (storedEntity.isRemoved()) {
/*     */         
/*  62 */         this.entity = Either.left(storedEntity.getUUID());
/*     */       } else {
/*  64 */         return storedEntity;
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     Optional<UUID> uuid = this.entity.left();
/*  69 */     if (uuid.isPresent()) {
/*  70 */       StoredEntityType resolved = (StoredEntityType)resolve(lookup.lookup((UUID)uuid.get()), clazz);
/*  71 */       if (resolved != null && !resolved.isRemoved()) {
/*  72 */         this.entity = Either.right(resolved);
/*  73 */         return resolved;
/*     */       } 
/*     */     } 
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public StoredEntityType getEntity(Level level, Class<StoredEntityType> clazz) {
/*  80 */     if (Player.class.isAssignableFrom(clazz)) {
/*  81 */       Objects.requireNonNull(level); return (StoredEntityType)getEntity(level::getPlayerInAnyDimension, clazz);
/*     */     } 
/*  83 */     Objects.requireNonNull(level); return (StoredEntityType)getEntity(level::getEntityInAnyDimension, clazz);
/*     */   }
/*     */   
/*     */   private StoredEntityType resolve(UniquelyIdentifyable entity, Class<StoredEntityType> clazz) {
/*  87 */     if (entity != null && clazz.isAssignableFrom(entity.getClass())) {
/*  88 */       return (StoredEntityType)(UniquelyIdentifyable)clazz.cast(entity);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*  94 */   public boolean matches(StoredEntityType entity) { return getUUID().equals(entity.getUUID()); }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public void store(ValueOutput output, String key) { output.store(key, UUIDUtil.CODEC, getUUID()); }
/*     */ 
/*     */   
/*     */   public static void store(EntityReference<?> reference, ValueOutput output, String key) {
/* 102 */     if (reference != null) {
/* 103 */       reference.store(output, key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 108 */   public static <StoredEntityType extends UniquelyIdentifyable> StoredEntityType get(EntityReference<StoredEntityType> reference, Level level, Class<StoredEntityType> clazz) { return (StoredEntityType)((reference != null) ? reference.getEntity(level, clazz) : null); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static Entity getEntity(EntityReference<Entity> reference, Level level) { return (Entity)get(reference, level, Entity.class); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static LivingEntity getLivingEntity(EntityReference<LivingEntity> reference, Level level) { return (LivingEntity)get(reference, level, LivingEntity.class); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static Player getPlayer(EntityReference<Player> reference, Level level) { return (Player)get(reference, level, Player.class); }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public static <StoredEntityType extends UniquelyIdentifyable> EntityReference<StoredEntityType> read(ValueInput input, String key) { return (EntityReference)input.read(key, codec()).orElse(null); }
/*     */ 
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> EntityReference<StoredEntityType> readWithOldOwnerConversion(ValueInput input, String key, Level level) {
/* 128 */     Optional<UUID> uuid = input.read(key, UUIDUtil.CODEC);
/* 129 */     if (uuid.isPresent()) {
/* 130 */       return of((UUID)uuid.get());
/*     */     }
/* 132 */     return (EntityReference)input.getString(key)
/* 133 */       .map(oldName -> OldUsersConverter.convertMobOwnerIfNecessary(level.getServer(), oldName))
/* 134 */       .map(EntityReference::new)
/* 135 */       .orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 141 */     if (obj == this) {
/* 142 */       return true;
/*     */     }
/* 144 */     if (obj instanceof EntityReference) { EntityReference<?> reference = (EntityReference)obj; if (getUUID().equals(reference.getUUID())); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public int hashCode() { return getUUID().hashCode(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityReference.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */