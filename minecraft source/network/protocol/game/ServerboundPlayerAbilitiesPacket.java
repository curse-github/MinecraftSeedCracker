/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.player.Abilities;
/*    */ 
/*    */ public class ServerboundPlayerAbilitiesPacket extends Object implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerAbilitiesPacket> STREAM_CODEC = Packet.codec(ServerboundPlayerAbilitiesPacket::write, ServerboundPlayerAbilitiesPacket::new);
/*    */   
/*    */   private static final int FLAG_FLYING = 2;
/*    */   
/*    */   private final boolean isFlying;
/*    */ 
/*    */   
/* 17 */   public ServerboundPlayerAbilitiesPacket(Abilities abilities) { this.isFlying = abilities.flying; }
/*    */ 
/*    */   
/*    */   private ServerboundPlayerAbilitiesPacket(FriendlyByteBuf input) {
/* 21 */     byte bitfield = input.readByte();
/* 22 */     this.isFlying = ((bitfield & 0x2) != 0);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     byte bitfield = 0;
/* 27 */     if (this.isFlying) {
/* 28 */       bitfield = (byte)(bitfield | 0x2);
/*    */     }
/* 30 */     output.writeByte(bitfield);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ServerboundPlayerAbilitiesPacket> type() { return GamePacketTypes.SERVERBOUND_PLAYER_ABILITIES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ServerGamePacketListener listener) { listener.handlePlayerAbilities(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public boolean isFlying() { return this.isFlying; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPlayerAbilitiesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */