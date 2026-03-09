/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.UUID;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*    */ import net.minecraft.world.waypoints.TrackedWaypointManager;
/*    */ import net.minecraft.world.waypoints.Waypoint;
/*    */ import net.minecraft.world.waypoints.WaypointManager;
/*    */ 
/*    */ public final class ClientboundTrackedWaypointPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Operation operation;
/*    */   private final TrackedWaypoint waypoint;
/*    */   
/* 20 */   public ClientboundTrackedWaypointPacket(Operation operation, TrackedWaypoint waypoint) { this.operation = operation; this.waypoint = waypoint; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket; } public Operation operation() { return this.operation; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;
/* 20 */     //   0	8	1	o	Ljava/lang/Object; } public TrackedWaypoint waypoint() { return this.waypoint; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTrackedWaypointPacket> STREAM_CODEC = StreamCodec.composite(Operation.STREAM_CODEC, ClientboundTrackedWaypointPacket::operation, TrackedWaypoint.STREAM_CODEC, ClientboundTrackedWaypointPacket::waypoint, ClientboundTrackedWaypointPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public static ClientboundTrackedWaypointPacket removeWaypoint(UUID identifier) { return new ClientboundTrackedWaypointPacket(Operation.UNTRACK, TrackedWaypoint.empty(identifier)); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public static ClientboundTrackedWaypointPacket addWaypointPosition(UUID identifier, Waypoint.Icon icon, Vec3i position) { return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setPosition(identifier, icon, position)); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static ClientboundTrackedWaypointPacket updateWaypointPosition(UUID identifier, Waypoint.Icon icon, Vec3i position) { return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setPosition(identifier, icon, position)); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public static ClientboundTrackedWaypointPacket addWaypointChunk(UUID identifier, Waypoint.Icon icon, ChunkPos chunk) { return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setChunk(identifier, icon, chunk)); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public static ClientboundTrackedWaypointPacket updateWaypointChunk(UUID identifier, Waypoint.Icon icon, ChunkPos chunk) { return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setChunk(identifier, icon, chunk)); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static ClientboundTrackedWaypointPacket addWaypointAzimuth(UUID identifier, Waypoint.Icon icon, float angle) { return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setAzimuth(identifier, icon, angle)); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public static ClientboundTrackedWaypointPacket updateWaypointAzimuth(UUID identifier, Waypoint.Icon icon, float angle) { return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setAzimuth(identifier, icon, angle)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public PacketType<ClientboundTrackedWaypointPacket> type() { return GamePacketTypes.CLIENTBOUND_WAYPOINT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public void handle(ClientGamePacketListener listener) { listener.handleWaypoint(this); }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public void apply(TrackedWaypointManager manager) { this.operation.action.accept(manager, this.waypoint); }
/*    */   
/*    */   private enum Operation
/*    */   {
/* 73 */     TRACK(WaypointManager::trackWaypoint),
/* 74 */     UNTRACK(WaypointManager::untrackWaypoint),
/* 75 */     UPDATE(WaypointManager::updateWaypoint); private final BiConsumer<TrackedWaypointManager, TrackedWaypoint> action;
/*    */     public static final IntFunction<Operation> BY_ID;
/*    */     public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC;
/*    */     
/*    */     static  {
/* 80 */       BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 81 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*    */     }
/*    */     
/* 84 */     Operation(BiConsumer<TrackedWaypointManager, TrackedWaypoint> action) { this.action = action; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTrackedWaypointPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */