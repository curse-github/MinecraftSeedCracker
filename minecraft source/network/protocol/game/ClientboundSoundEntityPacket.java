/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ClientboundSoundEntityPacket extends Object implements Packet<ClientGamePacketListener> {
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSoundEntityPacket> STREAM_CODEC = Packet.codec(ClientboundSoundEntityPacket::write, ClientboundSoundEntityPacket::new);
/*    */   
/*    */   private final Holder<SoundEvent> sound;
/*    */   private final SoundSource source;
/*    */   private final int id;
/*    */   private final float volume;
/*    */   private final float pitch;
/*    */   private final long seed;
/*    */   
/*    */   public ClientboundSoundEntityPacket(Holder<SoundEvent> sound, SoundSource source, Entity sourceEntity, float volume, float pitch, long seed) {
/* 23 */     this.sound = sound;
/* 24 */     this.source = source;
/* 25 */     this.id = sourceEntity.getId();
/* 26 */     this.volume = volume;
/* 27 */     this.pitch = pitch;
/* 28 */     this.seed = seed;
/*    */   }
/*    */   
/*    */   private ClientboundSoundEntityPacket(RegistryFriendlyByteBuf input) {
/* 32 */     this.sound = (Holder)SoundEvent.STREAM_CODEC.decode(input);
/* 33 */     this.source = (SoundSource)input.readEnum(SoundSource.class);
/* 34 */     this.id = input.readVarInt();
/* 35 */     this.volume = input.readFloat();
/* 36 */     this.pitch = input.readFloat();
/* 37 */     this.seed = input.readLong();
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 41 */     SoundEvent.STREAM_CODEC.encode(output, this.sound);
/* 42 */     output.writeEnum(this.source);
/* 43 */     output.writeVarInt(this.id);
/* 44 */     output.writeFloat(this.volume);
/* 45 */     output.writeFloat(this.pitch);
/* 46 */     output.writeLong(this.seed);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public PacketType<ClientboundSoundEntityPacket> type() { return GamePacketTypes.CLIENTBOUND_SOUND_ENTITY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public void handle(ClientGamePacketListener listener) { listener.handleSoundEntityEvent(this); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public Holder<SoundEvent> getSound() { return this.sound; }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public SoundSource getSource() { return this.source; }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public float getVolume() { return this.volume; }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public float getPitch() { return this.pitch; }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public long getSeed() { return this.seed; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSoundEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */