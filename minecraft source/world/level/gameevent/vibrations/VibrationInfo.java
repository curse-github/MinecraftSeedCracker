/*    */ package net.minecraft.world.level.gameevent.vibrations;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class VibrationInfo extends Record {
/*    */   private final Holder<GameEvent> gameEvent;
/*    */   private final float distance;
/*    */   private final Vec3 pos;
/*    */   
/* 17 */   public VibrationInfo(Holder<GameEvent> gameEvent, float distance, Vec3 pos, UUID uuid, UUID projectileOwnerUuid, Entity entity) { this.gameEvent = gameEvent; this.distance = distance; this.pos = pos; this.uuid = uuid; this.projectileOwnerUuid = projectileOwnerUuid; this.entity = entity; } private final UUID uuid; private final UUID projectileOwnerUuid; private final Entity entity; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<GameEvent> gameEvent() { return this.gameEvent; } public float distance() { return this.distance; } public Vec3 pos() { return this.pos; } public UUID uuid() { return this.uuid; } public UUID projectileOwnerUuid() { return this.projectileOwnerUuid; } public Entity entity() { return this.entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final Codec<VibrationInfo> CODEC = RecordCodecBuilder.create(i -> i.group(GameEvent.CODEC
/* 26 */         .fieldOf("game_event").forGetter(VibrationInfo::gameEvent), 
/* 27 */         Codec.floatRange(0.0F, Float.MAX_VALUE).fieldOf("distance").forGetter(VibrationInfo::distance), Vec3.CODEC
/* 28 */         .fieldOf("pos").forGetter(VibrationInfo::pos), UUIDUtil.CODEC
/* 29 */         .lenientOptionalFieldOf("source").forGetter(()), UUIDUtil.CODEC
/* 30 */         .lenientOptionalFieldOf("projectile_owner").forGetter(()))
/* 31 */       .apply(i, ()));
/*    */ 
/*    */   
/* 34 */   public VibrationInfo(Holder<GameEvent> gameEvent, float distance, Vec3 pos, UUID uuid, UUID projectileOwnerUuid) { this(gameEvent, distance, pos, uuid, projectileOwnerUuid, null); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public VibrationInfo(Holder<GameEvent> gameEvent, float distance, Vec3 pos, Entity entity) { this(gameEvent, distance, pos, (entity == null) ? null : entity.getUUID(), getProjectileOwner(entity), entity); }
/*    */ 
/*    */   
/*    */   private static UUID getProjectileOwner(Entity entity) {
/* 42 */     if (entity instanceof Projectile) { Projectile projectile = (Projectile)entity; if (projectile.getOwner() != null)
/* 43 */         return projectile.getOwner().getUUID();  }
/*    */     
/* 45 */     return null;
/*    */   }
/*    */ 
/*    */   
/* 49 */   public Optional<Entity> getEntity(ServerLevel level) { return Optional.ofNullable(this.entity).or(() -> { Objects.requireNonNull(level); return Optional.ofNullable(this.uuid).map(level::getEntity);
/*    */         }); }
/*    */ 
/*    */   
/* 53 */   public Optional<Entity> getProjectileOwner(ServerLevel level) { return getEntity(level)
/* 54 */       .filter(e -> e instanceof Projectile)
/* 55 */       .map(e -> (Projectile)e)
/* 56 */       .map(Projectile::getOwner)
/* 57 */       .or(() -> { Objects.requireNonNull(level); return Optional.ofNullable(this.projectileOwnerUuid).map(level::getEntity);
/*    */         }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */