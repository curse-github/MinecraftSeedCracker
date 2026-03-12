/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderWarningDelayPacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderWarningDelayPacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderWarningDelayPacket::write, ClientboundSetBorderWarningDelayPacket::new);
/*    */   
/*    */   private final int warningDelay;
/*    */ 
/*    */   
/* 15 */   public ClientboundSetBorderWarningDelayPacket(WorldBorder border) { this.warningDelay = border.getWarningTime(); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   private ClientboundSetBorderWarningDelayPacket(FriendlyByteBuf input) { this.warningDelay = input.readVarInt(); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.warningDelay); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public PacketType<ClientboundSetBorderWarningDelayPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DELAY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void handle(ClientGamePacketListener listener) { listener.handleSetBorderWarningDelay(this); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int getWarningDelay() { return this.warningDelay; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetBorderWarningDelayPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */