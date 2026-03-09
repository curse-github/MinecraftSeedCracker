/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetChunkCacheRadiusPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetChunkCacheRadiusPacket> STREAM_CODEC = Packet.codec(ClientboundSetChunkCacheRadiusPacket::write, ClientboundSetChunkCacheRadiusPacket::new);
/*    */   
/*    */   private final int radius;
/*    */ 
/*    */   
/* 14 */   public ClientboundSetChunkCacheRadiusPacket(int radius) { this.radius = radius; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ClientboundSetChunkCacheRadiusPacket(FriendlyByteBuf input) { this.radius = input.readVarInt(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.radius); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ClientboundSetChunkCacheRadiusPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_RADIUS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ClientGamePacketListener listener) { listener.handleSetChunkCacheRadius(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getRadius() { return this.radius; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetChunkCacheRadiusPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */