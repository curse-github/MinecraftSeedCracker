/*    */ package net.minecraft.network.protocol.login;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ServerboundHelloPacket extends Record implements Packet<ServerLoginPacketListener> {
/*    */   private final String name;
/*    */   private final UUID profileId;
/*    */   
/* 11 */   public ServerboundHelloPacket(String name, UUID profileId) { this.name = name; this.profileId = profileId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundHelloPacket; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundHelloPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public UUID profileId() { return this.profileId; }
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundHelloPacket> STREAM_CODEC = Packet.codec(ServerboundHelloPacket::write, ServerboundHelloPacket::new);
/*    */ 
/*    */   
/* 15 */   private ServerboundHelloPacket(FriendlyByteBuf input) { this(input.readUtf(16), input.readUUID()); }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 19 */     output.writeUtf(this.name, 16);
/* 20 */     output.writeUUID(this.profileId);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public PacketType<ServerboundHelloPacket> type() { return LoginPacketTypes.SERVERBOUND_HELLO; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void handle(ServerLoginPacketListener listener) { listener.handleHello(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ServerboundHelloPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */