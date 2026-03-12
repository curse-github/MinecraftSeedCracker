/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundContainerClosePacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundContainerClosePacket> STREAM_CODEC = Packet.codec(ServerboundContainerClosePacket::write, ServerboundContainerClosePacket::new);
/*    */   
/*    */   private final int containerId;
/*    */ 
/*    */   
/* 14 */   public ServerboundContainerClosePacket(int containerId) { this.containerId = containerId; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ServerboundContainerClosePacket(FriendlyByteBuf input) { this.containerId = input.readContainerId(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeContainerId(this.containerId); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundContainerClosePacket> type() { return GamePacketTypes.SERVERBOUND_CONTAINER_CLOSE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerGamePacketListener listener) { listener.handleContainerClose(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getContainerId() { return this.containerId; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundContainerClosePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */