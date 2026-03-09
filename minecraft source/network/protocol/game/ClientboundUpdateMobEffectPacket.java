/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ 
/*     */ public class ClientboundUpdateMobEffectPacket
/*     */   extends Object implements Packet<ClientGamePacketListener> {
/*  15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateMobEffectPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateMobEffectPacket::write, ClientboundUpdateMobEffectPacket::new);
/*     */   
/*     */   private static final int FLAG_AMBIENT = 1;
/*     */   
/*     */   private static final int FLAG_VISIBLE = 2;
/*     */   private static final int FLAG_SHOW_ICON = 4;
/*     */   private static final int FLAG_BLEND = 8;
/*     */   private final int entityId;
/*     */   private final Holder<MobEffect> effect;
/*     */   private final int effectAmplifier;
/*     */   private final int effectDurationTicks;
/*     */   private final byte flags;
/*     */   
/*     */   public ClientboundUpdateMobEffectPacket(int entityId, MobEffectInstance effect, boolean blend) {
/*  29 */     this.entityId = entityId;
/*  30 */     this.effect = effect.getEffect();
/*  31 */     this.effectAmplifier = effect.getAmplifier();
/*  32 */     this.effectDurationTicks = effect.getDuration();
/*  33 */     byte flags = 0;
/*     */     
/*  35 */     if (effect.isAmbient()) {
/*  36 */       flags = (byte)(flags | true);
/*     */     }
/*  38 */     if (effect.isVisible()) {
/*  39 */       flags = (byte)(flags | 0x2);
/*     */     }
/*  41 */     if (effect.showIcon()) {
/*  42 */       flags = (byte)(flags | 0x4);
/*     */     }
/*  44 */     if (blend) {
/*  45 */       flags = (byte)(flags | 0x8);
/*     */     }
/*  47 */     this.flags = flags;
/*     */   }
/*     */   
/*     */   private ClientboundUpdateMobEffectPacket(RegistryFriendlyByteBuf input) {
/*  51 */     this.entityId = input.readVarInt();
/*  52 */     this.effect = (Holder)MobEffect.STREAM_CODEC.decode(input);
/*  53 */     this.effectAmplifier = input.readVarInt();
/*  54 */     this.effectDurationTicks = input.readVarInt();
/*  55 */     this.flags = input.readByte();
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  59 */     output.writeVarInt(this.entityId);
/*  60 */     MobEffect.STREAM_CODEC.encode(output, this.effect);
/*  61 */     output.writeVarInt(this.effectAmplifier);
/*  62 */     output.writeVarInt(this.effectDurationTicks);
/*  63 */     output.writeByte(this.flags);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public PacketType<ClientboundUpdateMobEffectPacket> type() { return GamePacketTypes.CLIENTBOUND_UPDATE_MOB_EFFECT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public void handle(ClientGamePacketListener listener) { listener.handleUpdateMobEffect(this); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public int getEntityId() { return this.entityId; }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public Holder<MobEffect> getEffect() { return this.effect; }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public int getEffectAmplifier() { return this.effectAmplifier; }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public int getEffectDurationTicks() { return this.effectDurationTicks; }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public boolean isEffectVisible() { return ((this.flags & 0x2) != 0); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public boolean isEffectAmbient() { return ((this.flags & true) != 0); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public boolean effectShowsIcon() { return ((this.flags & 0x4) != 0); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public boolean shouldBlend() { return ((this.flags & 0x8) != 0); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundUpdateMobEffectPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */