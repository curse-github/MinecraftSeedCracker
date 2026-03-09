/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.RemoteChatSession;
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
/*     */ class EntryBuilder
/*     */ {
/*     */   private final UUID profileId;
/*     */   private GameProfile profile;
/*     */   private boolean listed;
/*     */   private int latency;
/*     */   private GameType gameMode;
/*     */   private Component displayName;
/*     */   private boolean showHat;
/*     */   private int listOrder;
/*     */   private RemoteChatSession.Data chatSession;
/*     */   
/*     */   private EntryBuilder(UUID profileId) {
/* 180 */     this.gameMode = GameType.DEFAULT_MODE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     this.profileId = profileId;
/*     */   }
/*     */ 
/*     */   
/* 191 */   private ClientboundPlayerInfoUpdatePacket.Entry build() { return new ClientboundPlayerInfoUpdatePacket.Entry(this.profileId, this.profile, this.listed, this.latency, this.gameMode, this.displayName, this.showHat, this.listOrder, this.chatSession); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$EntryBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */