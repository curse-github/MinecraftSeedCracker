/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ClientboundDamageEventPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int entityId;
/*    */   private final Holder<DamageType> sourceType;
/*    */   
/* 17 */   public ClientboundDamageEventPacket(int entityId, Holder<DamageType> sourceType, int sourceCauseId, int sourceDirectId, Optional<Vec3> sourcePosition) { this.entityId = entityId; this.sourceType = sourceType; this.sourceCauseId = sourceCauseId; this.sourceDirectId = sourceDirectId; this.sourcePosition = sourcePosition; } private final int sourceCauseId; private final int sourceDirectId; private final Optional<Vec3> sourcePosition; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDamageEventPacket;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public int entityId() { return this.entityId; } public Holder<DamageType> sourceType() { return this.sourceType; } public int sourceCauseId() { return this.sourceCauseId; } public int sourceDirectId() { return this.sourceDirectId; } public Optional<Vec3> sourcePosition() { return this.sourcePosition; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDamageEventPacket> STREAM_CODEC = Packet.codec(ClientboundDamageEventPacket::write, ClientboundDamageEventPacket::new);
/*    */   
/*    */   public ClientboundDamageEventPacket(Entity entity, DamageSource source) {
/* 27 */     this(entity
/* 28 */         .getId(), source
/* 29 */         .typeHolder(), 
/* 30 */         (source.getEntity() != null) ? source.getEntity().getId() : -1, 
/* 31 */         (source.getDirectEntity() != null) ? source.getDirectEntity().getId() : -1, 
/* 32 */         Optional.ofNullable(source.sourcePositionRaw()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   private static void writeOptionalEntityId(FriendlyByteBuf output, int id) { output.writeVarInt(id + 1); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   private static int readOptionalEntityId(FriendlyByteBuf input) { return input.readVarInt() - 1; }
/*    */ 
/*    */   
/*    */   private ClientboundDamageEventPacket(RegistryFriendlyByteBuf input) {
/* 46 */     this(input
/* 47 */         .readVarInt(), (Holder)DamageType.STREAM_CODEC
/* 48 */         .decode(input), 
/* 49 */         readOptionalEntityId(input), 
/* 50 */         readOptionalEntityId(input), input
/* 51 */         .readOptional(i -> new Vec3(i.readDouble(), i.readDouble(), i.readDouble())));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 56 */     output.writeVarInt(this.entityId);
/* 57 */     DamageType.STREAM_CODEC.encode(output, this.sourceType);
/* 58 */     writeOptionalEntityId(output, this.sourceCauseId);
/* 59 */     writeOptionalEntityId(output, this.sourceDirectId);
/* 60 */     output.writeOptional(this.sourcePosition, (o, pos) -> {
/* 61 */           o.writeDouble(pos.x());
/* 62 */           o.writeDouble(pos.y());
/* 63 */           o.writeDouble(pos.z());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public PacketType<ClientboundDamageEventPacket> type() { return GamePacketTypes.CLIENTBOUND_DAMAGE_EVENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public void handle(ClientGamePacketListener listener) { listener.handleDamageEvent(this); }
/*    */ 
/*    */   
/*    */   public DamageSource getSource(Level level) {
/* 78 */     if (this.sourcePosition.isPresent()) {
/* 79 */       return new DamageSource(this.sourceType, (Vec3)this.sourcePosition.get());
/*    */     }
/* 81 */     Entity cause = level.getEntity(this.sourceCauseId);
/* 82 */     Entity direct = level.getEntity(this.sourceDirectId);
/* 83 */     return new DamageSource(this.sourceType, direct, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundDamageEventPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */