/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundSetCarriedItemPacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetCarriedItemPacket> STREAM_CODEC = Packet.codec(ServerboundSetCarriedItemPacket::write, ServerboundSetCarriedItemPacket::new);
/*    */   
/*    */   private final int slot;
/*    */ 
/*    */   
/* 14 */   public ServerboundSetCarriedItemPacket(int slot) { this.slot = slot; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ServerboundSetCarriedItemPacket(FriendlyByteBuf input) { this.slot = input.readShort(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeShort(this.slot); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundSetCarriedItemPacket> type() { return GamePacketTypes.SERVERBOUND_SET_CARRIED_ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerGamePacketListener listener) { listener.handleSetCarriedItem(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getSlot() { return this.slot; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetCarriedItemPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */