/*    */ package net.minecraft.network.protocol.login;
/*    */ import java.security.PublicKey;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.Crypt;
/*    */ import net.minecraft.util.CryptException;
/*    */ 
/*    */ public class ClientboundHelloPacket extends Object implements Packet<ClientLoginPacketListener> {
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundHelloPacket> STREAM_CODEC = Packet.codec(ClientboundHelloPacket::write, ClientboundHelloPacket::new);
/*    */   
/*    */   private final String serverId;
/*    */   private final byte[] publicKey;
/*    */   private final byte[] challenge;
/*    */   private final boolean shouldAuthenticate;
/*    */   
/*    */   public ClientboundHelloPacket(String serverId, byte[] publicKey, byte[] challenge, boolean shouldAuthenticate) {
/* 21 */     this.serverId = serverId;
/* 22 */     this.publicKey = publicKey;
/* 23 */     this.challenge = challenge;
/* 24 */     this.shouldAuthenticate = shouldAuthenticate;
/*    */   }
/*    */   
/*    */   private ClientboundHelloPacket(FriendlyByteBuf input) {
/* 28 */     this.serverId = input.readUtf(20);
/* 29 */     this.publicKey = input.readByteArray();
/* 30 */     this.challenge = input.readByteArray();
/* 31 */     this.shouldAuthenticate = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 35 */     output.writeUtf(this.serverId);
/* 36 */     output.writeByteArray(this.publicKey);
/* 37 */     output.writeByteArray(this.challenge);
/* 38 */     output.writeBoolean(this.shouldAuthenticate);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public PacketType<ClientboundHelloPacket> type() { return LoginPacketTypes.CLIENTBOUND_HELLO; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public void handle(ClientLoginPacketListener listener) { listener.handleHello(this); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public String getServerId() { return this.serverId; }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public PublicKey getPublicKey() throws CryptException { return Crypt.byteToPublicKey(this.publicKey); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public byte[] getChallenge() { return this.challenge; }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public boolean shouldAuthenticate() { return this.shouldAuthenticate; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundHelloPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */