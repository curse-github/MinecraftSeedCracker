/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundClearTitlesPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundClearTitlesPacket> STREAM_CODEC = Packet.codec(ClientboundClearTitlesPacket::write, ClientboundClearTitlesPacket::new);
/*    */   
/*    */   private final boolean resetTimes;
/*    */ 
/*    */   
/* 14 */   public ClientboundClearTitlesPacket(boolean resetTimes) { this.resetTimes = resetTimes; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ClientboundClearTitlesPacket(FriendlyByteBuf input) { this.resetTimes = input.readBoolean(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeBoolean(this.resetTimes); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ClientboundClearTitlesPacket> type() { return GamePacketTypes.CLIENTBOUND_CLEAR_TITLES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ClientGamePacketListener listener) { listener.handleTitlesClear(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean shouldResetTimes() { return this.resetTimes; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundClearTitlesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */