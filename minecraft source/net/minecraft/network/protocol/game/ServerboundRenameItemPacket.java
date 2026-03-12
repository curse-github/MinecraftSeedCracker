/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundRenameItemPacket extends Object implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundRenameItemPacket> STREAM_CODEC = Packet.codec(ServerboundRenameItemPacket::write, ServerboundRenameItemPacket::new);
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/* 14 */   public ServerboundRenameItemPacket(String name) { this.name = name; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private ServerboundRenameItemPacket(FriendlyByteBuf input) { this.name = input.readUtf(); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   private void write(FriendlyByteBuf output) { output.writeUtf(this.name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ServerboundRenameItemPacket> type() { return GamePacketTypes.SERVERBOUND_RENAME_ITEM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ServerGamePacketListener listener) { listener.handleRenameItem(this); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public String getName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundRenameItemPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */