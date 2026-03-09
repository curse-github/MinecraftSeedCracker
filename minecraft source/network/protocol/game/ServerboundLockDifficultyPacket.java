/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundLockDifficultyPacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundLockDifficultyPacket> STREAM_CODEC = Packet.codec(ServerboundLockDifficultyPacket::write, ServerboundLockDifficultyPacket::new);
/*    */   
/*    */   private final boolean locked;
/*    */ 
/*    */   
/* 14 */   public ServerboundLockDifficultyPacket(boolean locked) { this.locked = locked; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ServerboundLockDifficultyPacket(FriendlyByteBuf input) { this.locked = input.readBoolean(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeBoolean(this.locked); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundLockDifficultyPacket> type() { return GamePacketTypes.SERVERBOUND_LOCK_DIFFICULTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerGamePacketListener listener) { listener.handleLockDifficulty(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean isLocked() { return this.locked; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundLockDifficultyPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */