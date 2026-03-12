/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ClientboundSelectAdvancementsTabPacket extends Object implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSelectAdvancementsTabPacket> STREAM_CODEC = Packet.codec(ClientboundSelectAdvancementsTabPacket::write, ClientboundSelectAdvancementsTabPacket::new);
/*    */   
/*    */   private final Identifier tab;
/*    */ 
/*    */   
/* 16 */   public ClientboundSelectAdvancementsTabPacket(Identifier tab) { this.tab = tab; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   private ClientboundSelectAdvancementsTabPacket(FriendlyByteBuf input) { this.tab = (Identifier)input.readNullable(FriendlyByteBuf::readIdentifier); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   private void write(FriendlyByteBuf output) { output.writeNullable(this.tab, FriendlyByteBuf::writeIdentifier); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public PacketType<ClientboundSelectAdvancementsTabPacket> type() { return GamePacketTypes.CLIENTBOUND_SELECT_ADVANCEMENTS_TAB; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public void handle(ClientGamePacketListener listener) { listener.handleSelectAdvancementsTab(this); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public Identifier getTab() { return this.tab; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSelectAdvancementsTabPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */