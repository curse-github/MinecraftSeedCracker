/*    */ package net.minecraft.network.protocol.common;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundClearDialogPacket extends Object implements Packet<ClientCommonPacketListener> {
/*  9 */   public static final ClientboundClearDialogPacket INSTANCE = new ClientboundClearDialogPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundClearDialogPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ClientboundClearDialogPacket> type() { return CommonPacketTypes.CLIENTBOUND_CLEAR_DIALOG; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ClientCommonPacketListener listener) { listener.handleClearDialog(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundClearDialogPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */