/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundStartConfigurationPacket
/*    */   extends Object
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/* 13 */   public static final ClientboundStartConfigurationPacket INSTANCE = new ClientboundStartConfigurationPacket();
/* 14 */   public static final StreamCodec<ByteBuf, ClientboundStartConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ClientboundStartConfigurationPacket> type() { return GamePacketTypes.CLIENTBOUND_START_CONFIGURATION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ClientGamePacketListener listener) { listener.handleConfigurationStart(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public boolean isTerminal() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundStartConfigurationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */