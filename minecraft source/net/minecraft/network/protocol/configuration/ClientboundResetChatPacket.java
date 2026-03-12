/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundResetChatPacket extends Object implements Packet<ClientConfigurationPacketListener> {
/*  9 */   public static final ClientboundResetChatPacket INSTANCE = new ClientboundResetChatPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundResetChatPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ClientboundResetChatPacket> type() { return ConfigurationPacketTypes.CLIENTBOUND_RESET_CHAT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ClientConfigurationPacketListener listener) { listener.handleResetChat(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\configuration\ClientboundResetChatPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */