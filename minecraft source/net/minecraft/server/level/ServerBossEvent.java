/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class ServerBossEvent
/*     */   extends BossEvent {
/*  17 */   private final Set<ServerPlayer> players = Sets.newHashSet();
/*  18 */   private final Set<ServerPlayer> unmodifiablePlayers = Collections.unmodifiableSet(this.players);
/*     */   
/*     */   private boolean visible = true;
/*     */   
/*  22 */   public ServerBossEvent(Component name, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) { super(Mth.createInsecureUUID(), name, color, overlay); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProgress(float progress) {
/*  27 */     if (progress != this.progress) {
/*  28 */       super.setProgress(progress);
/*  29 */       broadcast(ClientboundBossEventPacket::createUpdateProgressPacket);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(BossEvent.BossBarColor color) {
/*  35 */     if (color != this.color) {
/*  36 */       super.setColor(color);
/*  37 */       broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOverlay(BossEvent.BossBarOverlay overlay) {
/*  43 */     if (overlay != this.overlay) {
/*  44 */       super.setOverlay(overlay);
/*  45 */       broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setDarkenScreen(boolean darkenScreen) {
/*  51 */     if (darkenScreen != this.darkenScreen) {
/*  52 */       super.setDarkenScreen(darkenScreen);
/*  53 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setPlayBossMusic(boolean playBossMusic) {
/*  60 */     if (playBossMusic != this.playBossMusic) {
/*  61 */       super.setPlayBossMusic(playBossMusic);
/*  62 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setCreateWorldFog(boolean createWorldFog) {
/*  69 */     if (createWorldFog != this.createWorldFog) {
/*  70 */       super.setCreateWorldFog(createWorldFog);
/*  71 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(Component name) {
/*  78 */     if (!Objects.equal(name, this.name)) {
/*  79 */       super.setName(name);
/*  80 */       broadcast(ClientboundBossEventPacket::createUpdateNamePacket);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void broadcast(Function<BossEvent, ClientboundBossEventPacket> factory) {
/*  85 */     if (this.visible) {
/*  86 */       ClientboundBossEventPacket packet = (ClientboundBossEventPacket)factory.apply(this);
/*  87 */       for (ServerPlayer player : this.players) {
/*  88 */         player.connection.send(packet);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addPlayer(ServerPlayer player) {
/*  94 */     if (this.players.add(player) && this.visible) {
/*  95 */       player.connection.send(ClientboundBossEventPacket.createAddPacket(this));
/*     */     }
/*     */   }
/*     */   
/*     */   public void removePlayer(ServerPlayer player) {
/* 100 */     if (this.players.remove(player) && this.visible) {
/* 101 */       player.connection.send(ClientboundBossEventPacket.createRemovePacket(getId()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeAllPlayers() {
/* 106 */     if (!this.players.isEmpty()) {
/* 107 */       for (ServerPlayer player : Lists.newArrayList(this.players)) {
/* 108 */         removePlayer(player);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 114 */   public boolean isVisible() { return this.visible; }
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 118 */     if (visible != this.visible) {
/* 119 */       this.visible = visible;
/*     */       
/* 121 */       for (ServerPlayer player : this.players) {
/* 122 */         player.connection.send(visible ? ClientboundBossEventPacket.createAddPacket(this) : ClientboundBossEventPacket.createRemovePacket(getId()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 128 */   public Collection<ServerPlayer> getPlayers() { return this.unmodifiablePlayers; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ServerBossEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */