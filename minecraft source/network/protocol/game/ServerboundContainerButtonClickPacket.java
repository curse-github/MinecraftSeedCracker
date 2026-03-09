/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ServerboundContainerButtonClickPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int containerId;
/*    */   private final int buttonId;
/*    */   
/*  9 */   public ServerboundContainerButtonClickPacket(int containerId, int buttonId) { this.containerId = containerId; this.buttonId = buttonId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket; } public int containerId() { return this.containerId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public int buttonId() { return this.buttonId; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundContainerButtonClickPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundContainerButtonClickPacket::containerId, ByteBufCodecs.VAR_INT, ServerboundContainerButtonClickPacket::buttonId, ServerboundContainerButtonClickPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public PacketType<ServerboundContainerButtonClickPacket> type() { return GamePacketTypes.SERVERBOUND_CONTAINER_BUTTON_CLICK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void handle(ServerGamePacketListener listener) { listener.handleContainerButtonClick(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundContainerButtonClickPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */