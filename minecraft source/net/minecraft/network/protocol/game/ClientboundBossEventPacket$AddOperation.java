/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AddOperation
/*     */   implements ClientboundBossEventPacket.Operation
/*     */ {
/*     */   private final Component name;
/*     */   private final float progress;
/*     */   private final BossEvent.BossBarColor color;
/*     */   private final BossEvent.BossBarOverlay overlay;
/*     */   private final boolean darkenScreen;
/*     */   private final boolean playMusic;
/*     */   private final boolean createWorldFog;
/*     */   
/*     */   private AddOperation(BossEvent event) {
/* 148 */     this.name = event.getName();
/* 149 */     this.progress = event.getProgress();
/* 150 */     this.color = event.getColor();
/* 151 */     this.overlay = event.getOverlay();
/* 152 */     this.darkenScreen = event.shouldDarkenScreen();
/* 153 */     this.playMusic = event.shouldPlayBossMusic();
/* 154 */     this.createWorldFog = event.shouldCreateWorldFog();
/*     */   }
/*     */   
/*     */   private AddOperation(RegistryFriendlyByteBuf input) {
/* 158 */     this.name = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 159 */     this.progress = input.readFloat();
/* 160 */     this.color = (BossEvent.BossBarColor)input.readEnum(BossEvent.BossBarColor.class);
/* 161 */     this.overlay = (BossEvent.BossBarOverlay)input.readEnum(BossEvent.BossBarOverlay.class);
/* 162 */     int flags = input.readUnsignedByte();
/* 163 */     this.darkenScreen = ((flags & true) > 0);
/* 164 */     this.playMusic = ((flags & 0x2) > 0);
/* 165 */     this.createWorldFog = ((flags & 0x4) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 170 */   public ClientboundBossEventPacket.OperationType getType() { return ClientboundBossEventPacket.OperationType.ADD; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public void dispatch(UUID id, ClientboundBossEventPacket.Handler handler) { handler.add(id, this.name, this.progress, this.color, this.overlay, this.darkenScreen, this.playMusic, this.createWorldFog); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(RegistryFriendlyByteBuf output) {
/* 180 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.name);
/* 181 */     output.writeFloat(this.progress);
/* 182 */     output.writeEnum(this.color);
/* 183 */     output.writeEnum(this.overlay);
/* 184 */     output.writeByte(ClientboundBossEventPacket.encodeProperties(this.darkenScreen, this.playMusic, this.createWorldFog));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$AddOperation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */