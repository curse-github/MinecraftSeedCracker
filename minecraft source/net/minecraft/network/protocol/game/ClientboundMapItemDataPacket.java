/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.saveddata.maps.MapDecoration;
/*    */ import net.minecraft.world.level.saveddata.maps.MapId;
/*    */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*    */ 
/*    */ public final class ClientboundMapItemDataPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final MapId mapId;
/*    */   private final byte scale;
/*    */   
/* 17 */   public ClientboundMapItemDataPacket(MapId mapId, byte scale, boolean locked, Optional<List<MapDecoration>> decorations, Optional<MapItemSavedData.MapPatch> colorPatch) { this.mapId = mapId; this.scale = scale; this.locked = locked; this.decorations = decorations; this.colorPatch = colorPatch; } private final boolean locked; private final Optional<List<MapDecoration>> decorations; private final Optional<MapItemSavedData.MapPatch> colorPatch; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public MapId mapId() { return this.mapId; } public byte scale() { return this.scale; } public boolean locked() { return this.locked; } public Optional<List<MapDecoration>> decorations() { return this.decorations; } public Optional<MapItemSavedData.MapPatch> colorPatch() { return this.colorPatch; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundMapItemDataPacket> STREAM_CODEC = StreamCodec.composite(MapId.STREAM_CODEC, ClientboundMapItemDataPacket::mapId, ByteBufCodecs.BYTE, ClientboundMapItemDataPacket::scale, ByteBufCodecs.BOOL, ClientboundMapItemDataPacket::locked, MapDecoration.STREAM_CODEC
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 29 */       .apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional), ClientboundMapItemDataPacket::decorations, MapItemSavedData.MapPatch.STREAM_CODEC, ClientboundMapItemDataPacket::colorPatch, ClientboundMapItemDataPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public ClientboundMapItemDataPacket(MapId mapId, byte scale, boolean locked, Collection<MapDecoration> decorations, MapItemSavedData.MapPatch colorPatch) { this(mapId, scale, locked, (decorations != null) ? Optional.of(List.copyOf(decorations)) : Optional.empty(), Optional.ofNullable(colorPatch)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public PacketType<ClientboundMapItemDataPacket> type() { return GamePacketTypes.CLIENTBOUND_MAP_ITEM_DATA; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void handle(ClientGamePacketListener listener) { listener.handleMapItemData(this); }
/*    */ 
/*    */   
/*    */   public void applyToMap(MapItemSavedData map) {
/* 49 */     Objects.requireNonNull(map); this.decorations.ifPresent(map::addClientSideDecorations);
/* 50 */     this.colorPatch.ifPresent(patch -> patch.applyToMap(map));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMapItemDataPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */