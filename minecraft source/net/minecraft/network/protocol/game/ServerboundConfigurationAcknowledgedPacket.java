/*    */ package net.minecraft.network.protocol.game;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundConfigurationAcknowledgedPacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final ServerboundConfigurationAcknowledgedPacket INSTANCE = new ServerboundConfigurationAcknowledgedPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundConfigurationAcknowledgedPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ServerboundConfigurationAcknowledgedPacket> type() { return GamePacketTypes.SERVERBOUND_CONFIGURATION_ACKNOWLEDGED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ServerGamePacketListener listener) { listener.handleConfigurationAcknowledged(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean isTerminal() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundConfigurationAcknowledgedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */