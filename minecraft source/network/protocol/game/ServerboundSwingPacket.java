/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ 
/*    */ public class ServerboundSwingPacket extends Object implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSwingPacket> STREAM_CODEC = Packet.codec(ServerboundSwingPacket::write, ServerboundSwingPacket::new);
/*    */   
/*    */   private final InteractionHand hand;
/*    */ 
/*    */   
/* 15 */   public ServerboundSwingPacket(InteractionHand hand) { this.hand = hand; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   private ServerboundSwingPacket(FriendlyByteBuf input) { this.hand = (InteractionHand)input.readEnum(InteractionHand.class); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private void write(FriendlyByteBuf output) { output.writeEnum(this.hand); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public PacketType<ServerboundSwingPacket> type() { return GamePacketTypes.SERVERBOUND_SWING; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void handle(ServerGamePacketListener listener) { listener.handleAnimate(this); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public InteractionHand getHand() { return this.hand; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSwingPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */