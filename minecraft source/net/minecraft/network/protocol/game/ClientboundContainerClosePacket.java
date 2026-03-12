/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundContainerClosePacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundContainerClosePacket> STREAM_CODEC = Packet.codec(ClientboundContainerClosePacket::write, ClientboundContainerClosePacket::new);
/*    */   
/*    */   private final int containerId;
/*    */ 
/*    */   
/* 14 */   public ClientboundContainerClosePacket(int containerId) { this.containerId = containerId; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ClientboundContainerClosePacket(FriendlyByteBuf input) { this.containerId = input.readContainerId(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeContainerId(this.containerId); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ClientboundContainerClosePacket> type() { return GamePacketTypes.CLIENTBOUND_CONTAINER_CLOSE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ClientGamePacketListener listener) { listener.handleContainerClose(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getContainerId() { return this.containerId; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundContainerClosePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */