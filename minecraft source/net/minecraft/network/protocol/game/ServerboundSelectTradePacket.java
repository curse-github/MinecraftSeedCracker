/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundSelectTradePacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSelectTradePacket> STREAM_CODEC = Packet.codec(ServerboundSelectTradePacket::write, ServerboundSelectTradePacket::new);
/*    */   
/*    */   private final int item;
/*    */ 
/*    */   
/* 14 */   public ServerboundSelectTradePacket(int item) { this.item = item; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ServerboundSelectTradePacket(FriendlyByteBuf input) { this.item = input.readVarInt(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.item); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundSelectTradePacket> type() { return GamePacketTypes.SERVERBOUND_SELECT_TRADE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerGamePacketListener listener) { listener.handleSelectTrade(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getItem() { return this.item; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSelectTradePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */