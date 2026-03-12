/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.damagesource.CombatTracker;
/*    */ 
/*    */ public class ClientboundPlayerCombatEndPacket extends Object implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerCombatEndPacket> STREAM_CODEC = Packet.codec(ClientboundPlayerCombatEndPacket::write, ClientboundPlayerCombatEndPacket::new);
/*    */   
/*    */   private final int duration;
/*    */ 
/*    */   
/* 15 */   public ClientboundPlayerCombatEndPacket(CombatTracker tracker) { this(tracker.getCombatDuration()); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public ClientboundPlayerCombatEndPacket(int duration) { this.duration = duration; }
/*    */ 
/*    */ 
/*    */   
/* 23 */   private ClientboundPlayerCombatEndPacket(FriendlyByteBuf input) { this.duration = input.readVarInt(); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.duration); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public PacketType<ClientboundPlayerCombatEndPacket> type() { return GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_END; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void handle(ClientGamePacketListener listener) { listener.handlePlayerCombatEnd(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerCombatEndPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */