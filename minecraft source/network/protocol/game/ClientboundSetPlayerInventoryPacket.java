/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ClientboundSetPlayerInventoryPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int slot;
/*    */   private final ItemStack contents;
/*    */   
/* 10 */   public ClientboundSetPlayerInventoryPacket(int slot, ItemStack contents) { this.slot = slot; this.contents = contents; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket; } public int slot() { return this.slot; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetPlayerInventoryPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public ItemStack contents() { return this.contents; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetPlayerInventoryPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundSetPlayerInventoryPacket::slot, ItemStack.OPTIONAL_STREAM_CODEC, ClientboundSetPlayerInventoryPacket::contents, ClientboundSetPlayerInventoryPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ClientboundSetPlayerInventoryPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_PLAYER_INVENTORY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ClientGamePacketListener listener) { listener.handleSetPlayerInventory(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetPlayerInventoryPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */