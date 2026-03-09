/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ClientboundContainerSetContentPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int containerId;
/*    */   private final int stateId;
/*    */   private final List<ItemStack> items;
/*    */   private final ItemStack carriedItem;
/*    */   
/* 12 */   public ClientboundContainerSetContentPacket(int containerId, int stateId, List<ItemStack> items, ItemStack carriedItem) { this.containerId = containerId; this.stateId = stateId; this.items = items; this.carriedItem = carriedItem; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket; } public int containerId() { return this.containerId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public int stateId() { return this.stateId; } public List<ItemStack> items() { return this.items; } public ItemStack carriedItem() { return this.carriedItem; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundContainerSetContentPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ClientboundContainerSetContentPacket::containerId, ByteBufCodecs.VAR_INT, ClientboundContainerSetContentPacket::stateId, ItemStack.OPTIONAL_LIST_STREAM_CODEC, ClientboundContainerSetContentPacket::items, ItemStack.OPTIONAL_STREAM_CODEC, ClientboundContainerSetContentPacket::carriedItem, ClientboundContainerSetContentPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public PacketType<ClientboundContainerSetContentPacket> type() { return GamePacketTypes.CLIENTBOUND_CONTAINER_SET_CONTENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public void handle(ClientGamePacketListener listener) { listener.handleContainerContent(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundContainerSetContentPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */