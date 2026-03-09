/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetExperiencePacket extends Object implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetExperiencePacket> STREAM_CODEC = Packet.codec(ClientboundSetExperiencePacket::write, ClientboundSetExperiencePacket::new);
/*    */   
/*    */   private final float experienceProgress;
/*    */   private final int totalExperience;
/*    */   private final int experienceLevel;
/*    */   
/*    */   public ClientboundSetExperiencePacket(float experienceProgress, int totalExperience, int experienceLevel) {
/* 16 */     this.experienceProgress = experienceProgress;
/* 17 */     this.totalExperience = totalExperience;
/* 18 */     this.experienceLevel = experienceLevel;
/*    */   }
/*    */   
/*    */   private ClientboundSetExperiencePacket(FriendlyByteBuf input) {
/* 22 */     this.experienceProgress = input.readFloat();
/* 23 */     this.experienceLevel = input.readVarInt();
/* 24 */     this.totalExperience = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeFloat(this.experienceProgress);
/* 29 */     output.writeVarInt(this.experienceLevel);
/* 30 */     output.writeVarInt(this.totalExperience);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ClientboundSetExperiencePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_EXPERIENCE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ClientGamePacketListener listener) { listener.handleSetExperience(this); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public float getExperienceProgress() { return this.experienceProgress; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public int getTotalExperience() { return this.totalExperience; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public int getExperienceLevel() { return this.experienceLevel; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetExperiencePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */