/*    */ package net.minecraft.network.protocol.login;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundLoginCompressionPacket extends Object implements Packet<ClientLoginPacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundLoginCompressionPacket> STREAM_CODEC = Packet.codec(ClientboundLoginCompressionPacket::write, ClientboundLoginCompressionPacket::new);
/*    */   
/*    */   private final int compressionThreshold;
/*    */ 
/*    */   
/* 14 */   public ClientboundLoginCompressionPacket(int compressionThreshold) { this.compressionThreshold = compressionThreshold; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ClientboundLoginCompressionPacket(FriendlyByteBuf input) { this.compressionThreshold = input.readVarInt(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.compressionThreshold); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ClientboundLoginCompressionPacket> type() { return LoginPacketTypes.CLIENTBOUND_LOGIN_COMPRESSION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ClientLoginPacketListener listener) { listener.handleCompression(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getCompressionThreshold() { return this.compressionThreshold; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundLoginCompressionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */