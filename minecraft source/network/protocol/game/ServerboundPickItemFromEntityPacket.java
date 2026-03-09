/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ServerboundPickItemFromEntityPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int id;
/*    */   private final boolean includeData;
/*    */   
/*  9 */   public ServerboundPickItemFromEntityPacket(int id, boolean includeData) { this.id = id; this.includeData = includeData; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromEntityPacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public boolean includeData() { return this.includeData; }
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundPickItemFromEntityPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundPickItemFromEntityPacket::id, ByteBufCodecs.BOOL, ServerboundPickItemFromEntityPacket::includeData, ServerboundPickItemFromEntityPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public PacketType<ServerboundPickItemFromEntityPacket> type() { return GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_ENTITY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void handle(ServerGamePacketListener listener) { listener.handlePickItemFromEntity(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPickItemFromEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */