/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderWarningDistancePacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderWarningDistancePacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderWarningDistancePacket::write, ClientboundSetBorderWarningDistancePacket::new);
/*    */   
/*    */   private final int warningBlocks;
/*    */ 
/*    */   
/* 15 */   public ClientboundSetBorderWarningDistancePacket(WorldBorder border) { this.warningBlocks = border.getWarningBlocks(); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   private ClientboundSetBorderWarningDistancePacket(FriendlyByteBuf input) { this.warningBlocks = input.readVarInt(); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.warningBlocks); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public PacketType<ClientboundSetBorderWarningDistancePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DISTANCE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void handle(ClientGamePacketListener listener) { listener.handleSetBorderWarningDistance(this); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int getWarningBlocks() { return this.warningBlocks; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetBorderWarningDistancePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */