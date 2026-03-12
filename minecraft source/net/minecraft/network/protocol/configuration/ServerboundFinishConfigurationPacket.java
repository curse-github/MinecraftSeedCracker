/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundFinishConfigurationPacket extends Object implements Packet<ServerConfigurationPacketListener> {
/*  9 */   public static final ServerboundFinishConfigurationPacket INSTANCE = new ServerboundFinishConfigurationPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundFinishConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ServerboundFinishConfigurationPacket> type() { return ConfigurationPacketTypes.SERVERBOUND_FINISH_CONFIGURATION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ServerConfigurationPacketListener listener) { listener.handleConfigurationFinished(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean isTerminal() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\configuration\ServerboundFinishConfigurationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */