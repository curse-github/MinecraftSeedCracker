/*    */ package net.minecraft.network.protocol.login;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundLoginFinishedPacket extends Record implements Packet<ClientLoginPacketListener> {
/* 10 */   public ClientboundLoginFinishedPacket(GameProfile gameProfile) { this.gameProfile = gameProfile; } private final GameProfile gameProfile; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket; } public GameProfile gameProfile() { return this.gameProfile; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginFinishedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final StreamCodec<ByteBuf, ClientboundLoginFinishedPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.GAME_PROFILE, ClientboundLoginFinishedPacket::gameProfile, ClientboundLoginFinishedPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public PacketType<ClientboundLoginFinishedPacket> type() { return LoginPacketTypes.CLIENTBOUND_LOGIN_FINISHED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void handle(ClientLoginPacketListener listener) { listener.handleLoginFinished(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public boolean isTerminal() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundLoginFinishedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */