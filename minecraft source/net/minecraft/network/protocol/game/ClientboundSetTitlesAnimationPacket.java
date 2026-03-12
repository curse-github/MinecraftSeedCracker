/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetTitlesAnimationPacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetTitlesAnimationPacket> STREAM_CODEC = Packet.codec(ClientboundSetTitlesAnimationPacket::write, ClientboundSetTitlesAnimationPacket::new);
/*    */   
/*    */   private final int fadeIn;
/*    */   private final int stay;
/*    */   private final int fadeOut;
/*    */   
/*    */   public ClientboundSetTitlesAnimationPacket(int fadeIn, int stay, int fadeOut) {
/* 16 */     this.fadeIn = fadeIn;
/* 17 */     this.stay = stay;
/* 18 */     this.fadeOut = fadeOut;
/*    */   }
/*    */   
/*    */   private ClientboundSetTitlesAnimationPacket(FriendlyByteBuf input) {
/* 22 */     this.fadeIn = input.readInt();
/* 23 */     this.stay = input.readInt();
/* 24 */     this.fadeOut = input.readInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeInt(this.fadeIn);
/* 29 */     output.writeInt(this.stay);
/* 30 */     output.writeInt(this.fadeOut);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ClientboundSetTitlesAnimationPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_TITLES_ANIMATION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ClientGamePacketListener listener) { listener.setTitlesAnimation(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public int getFadeIn() { return this.fadeIn; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getStay() { return this.stay; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getFadeOut() { return this.fadeOut; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetTitlesAnimationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */