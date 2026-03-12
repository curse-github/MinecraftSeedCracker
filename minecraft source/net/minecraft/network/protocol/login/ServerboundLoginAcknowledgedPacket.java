/*    */ package net.minecraft.network.protocol.login;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundLoginAcknowledgedPacket extends Object implements Packet<ServerLoginPacketListener> {
/*  9 */   public static final ServerboundLoginAcknowledgedPacket INSTANCE = new ServerboundLoginAcknowledgedPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundLoginAcknowledgedPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ServerboundLoginAcknowledgedPacket> type() { return LoginPacketTypes.SERVERBOUND_LOGIN_ACKNOWLEDGED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ServerLoginPacketListener listener) { listener.handleLoginAcknowledgement(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean isTerminal() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ServerboundLoginAcknowledgedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */