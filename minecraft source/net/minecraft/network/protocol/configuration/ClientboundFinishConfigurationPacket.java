/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundFinishConfigurationPacket extends Object implements Packet<ClientConfigurationPacketListener> {
/*  9 */   public static final ClientboundFinishConfigurationPacket INSTANCE = new ClientboundFinishConfigurationPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundFinishConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ClientboundFinishConfigurationPacket> type() { return ConfigurationPacketTypes.CLIENTBOUND_FINISH_CONFIGURATION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ClientConfigurationPacketListener listener) { listener.handleConfigurationFinished(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean isTerminal() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\configuration\ClientboundFinishConfigurationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */