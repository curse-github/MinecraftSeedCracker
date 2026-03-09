/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundSetHeldSlotPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int slot;
/*    */   
/*  9 */   public ClientboundSetHeldSlotPacket(int slot) { this.slot = slot; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetHeldSlotPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetHeldSlotPacket; } public int slot() { return this.slot; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetHeldSlotPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetHeldSlotPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetHeldSlotPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetHeldSlotPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundSetHeldSlotPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundSetHeldSlotPacket::slot, ClientboundSetHeldSlotPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ClientboundSetHeldSlotPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_HELD_SLOT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ClientGamePacketListener listener) { listener.handleSetHeldSlot(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetHeldSlotPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */