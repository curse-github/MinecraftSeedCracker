/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ public class ClientboundStopSoundPacket extends Object implements Packet<ClientGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundStopSoundPacket> STREAM_CODEC = Packet.codec(ClientboundStopSoundPacket::write, ClientboundStopSoundPacket::new);
/*    */   
/*    */   private static final int HAS_SOURCE = 1;
/*    */   
/*    */   private static final int HAS_SOUND = 2;
/*    */   private final Identifier name;
/*    */   private final SoundSource source;
/*    */   
/*    */   public ClientboundStopSoundPacket(Identifier name, SoundSource source) {
/* 21 */     this.name = name;
/* 22 */     this.source = source;
/*    */   }
/*    */   
/*    */   private ClientboundStopSoundPacket(FriendlyByteBuf input) {
/* 26 */     int flags = input.readByte();
/* 27 */     if ((flags & true) > 0) {
/* 28 */       this.source = (SoundSource)input.readEnum(SoundSource.class);
/*    */     } else {
/* 30 */       this.source = null;
/*    */     } 
/* 32 */     if ((flags & 0x2) > 0) {
/* 33 */       this.name = input.readIdentifier();
/*    */     } else {
/* 35 */       this.name = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 40 */     if (this.source != null) {
/* 41 */       if (this.name != null) {
/* 42 */         output.writeByte(3);
/* 43 */         output.writeEnum(this.source);
/* 44 */         output.writeIdentifier(this.name);
/*    */       } else {
/* 46 */         output.writeByte(1);
/* 47 */         output.writeEnum(this.source);
/*    */       }
/*    */     
/* 50 */     } else if (this.name != null) {
/* 51 */       output.writeByte(2);
/* 52 */       output.writeIdentifier(this.name);
/*    */     } else {
/* 54 */       output.writeByte(0);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public PacketType<ClientboundStopSoundPacket> type() { return GamePacketTypes.CLIENTBOUND_STOP_SOUND; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public void handle(ClientGamePacketListener listener) { listener.handleStopSoundEvent(this); }
/*    */ 
/*    */ 
/*    */   
/* 70 */   public Identifier getName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 74 */   public SoundSource getSource() { return this.source; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundStopSoundPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */