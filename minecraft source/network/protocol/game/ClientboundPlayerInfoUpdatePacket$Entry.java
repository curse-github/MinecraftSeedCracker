/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.RemoteChatSession;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.level.GameType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Entry
/*     */   extends Record
/*     */ {
/*     */   private final UUID profileId;
/*     */   private final GameProfile profile;
/*     */   private final boolean listed;
/*     */   private final int latency;
/*     */   private final GameType gameMode;
/*     */   private final Component displayName;
/*     */   private final boolean showHat;
/*     */   private final int listOrder;
/*     */   private final RemoteChatSession.Data chatSession;
/*     */   
/* 159 */   public RemoteChatSession.Data chatSession() { return this.chatSession; } public int listOrder() { return this.listOrder; } public boolean showHat() { return this.showHat; } public Component displayName() { return this.displayName; } public GameType gameMode() { return this.gameMode; } public int latency() { return this.latency; } public boolean listed() { return this.listed; } public GameProfile profile() { return this.profile; } public UUID profileId() { return this.profileId; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;
/* 159 */     //   0	8	1	o	Ljava/lang/Object; } public Entry(UUID profileId, GameProfile profile, boolean listed, int latency, GameType gameMode, Component displayName, boolean showHat, int listOrder, RemoteChatSession.Data chatSession) { this.profileId = profileId; this.profile = profile; this.listed = listed; this.latency = latency; this.gameMode = gameMode; this.displayName = displayName; this.showHat = showHat; this.listOrder = listOrder; this.chatSession = chatSession; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #159	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoUpdatePacket$Entry; }
/*     */   private Entry(ServerPlayer player) {
/* 161 */     this(player
/* 162 */         .getUUID(), player
/* 163 */         .getGameProfile(), true, player.connection
/*     */         
/* 165 */         .latency(), player
/* 166 */         .gameMode(), player
/* 167 */         .getTabListDisplayName(), player
/* 168 */         .isModelPartShown(PlayerModelPart.HAT), player
/* 169 */         .getTabListOrder(), 
/* 170 */         (RemoteChatSession.Data)Optionull.map(player.getChatSession(), RemoteChatSession::asData));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */