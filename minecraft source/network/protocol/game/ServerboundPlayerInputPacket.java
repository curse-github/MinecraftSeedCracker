/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.entity.player.Input;
/*    */ 
/*    */ public final class ServerboundPlayerInputPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final Input input;
/*    */   
/*  9 */   public ServerboundPlayerInputPacket(Input input) { this.input = input; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundPlayerInputPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlayerInputPacket; } public Input input() { return this.input; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundPlayerInputPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlayerInputPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundPlayerInputPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlayerInputPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerInputPacket> STREAM_CODEC = StreamCodec.composite(Input.STREAM_CODEC, ServerboundPlayerInputPacket::input, ServerboundPlayerInputPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ServerboundPlayerInputPacket> type() { return GamePacketTypes.SERVERBOUND_PLAYER_INPUT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ServerGamePacketListener listener) { listener.handlePlayerInput(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlayerInputPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */