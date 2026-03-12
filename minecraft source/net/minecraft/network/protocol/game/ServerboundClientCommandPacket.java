/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundClientCommandPacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundClientCommandPacket> STREAM_CODEC = Packet.codec(ServerboundClientCommandPacket::write, ServerboundClientCommandPacket::new);
/*    */   
/*    */   private final Action action;
/*    */ 
/*    */   
/* 14 */   public ServerboundClientCommandPacket(Action action) { this.action = action; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ServerboundClientCommandPacket(FriendlyByteBuf input) { this.action = (Action)input.readEnum(Action.class); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeEnum(this.action); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundClientCommandPacket> type() { return GamePacketTypes.SERVERBOUND_CLIENT_COMMAND; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerGamePacketListener listener) { listener.handleClientCommand(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Action getAction() { return this.action; }
/*    */   
/*    */   public enum Action
/*    */   {
/* 40 */     PERFORM_RESPAWN,
/* 41 */     REQUEST_STATS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundClientCommandPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */