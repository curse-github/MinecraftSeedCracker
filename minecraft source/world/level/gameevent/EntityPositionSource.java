/*    */ package net.minecraft.world.level.gameevent;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityPositionSource implements PositionSource {
/* 21 */   public static final MapCodec<EntityPositionSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(UUIDUtil.CODEC
/* 22 */         .fieldOf("source_entity").forGetter(EntityPositionSource::getUuid), Codec.FLOAT
/* 23 */         .fieldOf("y_offset").orElse(Float.valueOf(0.0F)).forGetter(()))
/* 24 */       .apply(i, ()));
/*    */   
/* 26 */   public static final StreamCodec<ByteBuf, EntityPositionSource> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, EntityPositionSource::getId, ByteBufCodecs.FLOAT, o -> 
/*    */       
/* 28 */       Float.valueOf(o.yOffset), (id, offset) -> 
/* 29 */       new EntityPositionSource(Either.right(Either.right(id)), offset.floatValue()));
/*    */   
/*    */   private Either<Entity, Either<UUID, Integer>> entityOrUuidOrId;
/*    */   
/*    */   private final float yOffset;
/*    */ 
/*    */   
/* 36 */   public EntityPositionSource(Entity entity, float yOffset) { this(Either.left(entity), yOffset); }
/*    */ 
/*    */   
/*    */   private EntityPositionSource(Either<Entity, Either<UUID, Integer>> entityOrUuidOrId, float yOffset) {
/* 40 */     this.entityOrUuidOrId = entityOrUuidOrId;
/* 41 */     this.yOffset = yOffset;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Vec3> getPosition(Level level) {
/* 46 */     if (this.entityOrUuidOrId.left().isEmpty()) {
/* 47 */       resolveEntity(level);
/*    */     }
/* 49 */     return this.entityOrUuidOrId.left().map(entity -> entity.position().add(0.0D, this.yOffset, 0.0D));
/*    */   }
/*    */ 
/*    */   
/* 53 */   private void resolveEntity(Level level) { ((Optional)this.entityOrUuidOrId.map(Optional::of, uuidOrId -> {
/*    */ 
/*    */ 
/*    */           
/* 57 */           Objects.requireNonNull(level); return Optional.ofNullable((Entity)uuidOrId.map((), level::getEntity));
/*    */         
/* 59 */         })).ifPresent(entity -> this.entityOrUuidOrId = Either.left(entity)); }
/*    */ 
/*    */   
/*    */   public UUID getUuid() {
/* 63 */     return (UUID)this.entityOrUuidOrId.map(Entity::getUUID, uuidOrId -> 
/*    */         
/* 65 */         (UUID)uuidOrId.map(
/* 66 */           Function.identity(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 75 */   private int getId() { return ((Integer)this.entityOrUuidOrId.map(Entity::getId, uuidOrId -> 
/*    */         
/* 77 */         (Integer)uuidOrId.map((), 
/*    */ 
/*    */ 
/*    */           
/* 81 */           Function.identity()))).intValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   public PositionSourceType<EntityPositionSource> getType() { return PositionSourceType.ENTITY; }
/*    */   
/*    */   public static class Type
/*    */     extends Object
/*    */     implements PositionSourceType<EntityPositionSource>
/*    */   {
/* 94 */     public MapCodec<EntityPositionSource> codec() { return EntityPositionSource.CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 99 */     public StreamCodec<ByteBuf, EntityPositionSource> streamCodec() { return EntityPositionSource.STREAM_CODEC; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\EntityPositionSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */