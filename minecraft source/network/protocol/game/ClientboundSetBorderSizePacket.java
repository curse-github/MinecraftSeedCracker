/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderSizePacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderSizePacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderSizePacket::write, ClientboundSetBorderSizePacket::new);
/*    */   
/*    */   private final double size;
/*    */ 
/*    */   
/* 15 */   public ClientboundSetBorderSizePacket(WorldBorder border) { this.size = border.getLerpTarget(); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   private ClientboundSetBorderSizePacket(FriendlyByteBuf input) { this.size = input.readDouble(); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private void write(FriendlyByteBuf output) { output.writeDouble(this.size); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public PacketType<ClientboundSetBorderSizePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_BORDER_SIZE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void handle(ClientGamePacketListener listener) { listener.handleSetBorderSize(this); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public double getSize() { return this.size; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetBorderSizePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */