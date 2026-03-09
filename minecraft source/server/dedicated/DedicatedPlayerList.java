/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DedicatedPlayerList extends PlayerList {
/*  14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public DedicatedPlayerList(DedicatedServer server, LayeredRegistryAccess<RegistryLayer> registries, PlayerDataStorage playerDataStorage) {
/*  17 */     super(server, registries, playerDataStorage, server.notificationManager());
/*     */     
/*  19 */     setViewDistance(server.viewDistance());
/*  20 */     setSimulationDistance(server.simulationDistance());
/*     */     
/*  22 */     loadUserBanList();
/*  23 */     saveUserBanList();
/*  24 */     loadIpBanList();
/*  25 */     saveIpBanList();
/*  26 */     loadOps();
/*  27 */     loadWhiteList();
/*  28 */     saveOps();
/*  29 */     if (!getWhiteList().getFile().exists()) {
/*  30 */       saveWhiteList();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public void reloadWhiteList() { loadWhiteList(); }
/*     */ 
/*     */   
/*     */   private void saveIpBanList() {
/*     */     try {
/*  41 */       getIpBans().save();
/*  42 */     } catch (IOException e) {
/*  43 */       LOGGER.warn("Failed to save ip banlist: ", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveUserBanList() {
/*     */     try {
/*  49 */       getBans().save();
/*  50 */     } catch (IOException e) {
/*  51 */       LOGGER.warn("Failed to save user banlist: ", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadIpBanList() {
/*     */     try {
/*  57 */       getIpBans().load();
/*  58 */     } catch (IOException e) {
/*  59 */       LOGGER.warn("Failed to load ip banlist: ", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadUserBanList() {
/*     */     try {
/*  65 */       getBans().load();
/*  66 */     } catch (IOException e) {
/*  67 */       LOGGER.warn("Failed to load user banlist: ", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadOps() {
/*     */     try {
/*  73 */       getOps().load();
/*  74 */     } catch (Exception e) {
/*  75 */       LOGGER.warn("Failed to load operators list: ", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveOps() {
/*     */     try {
/*  81 */       getOps().save();
/*  82 */     } catch (Exception e) {
/*  83 */       LOGGER.warn("Failed to save operators list: ", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadWhiteList() {
/*     */     try {
/*  89 */       getWhiteList().load();
/*  90 */     } catch (Exception e) {
/*  91 */       LOGGER.warn("Failed to load white-list: ", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveWhiteList() {
/*     */     try {
/*  97 */       getWhiteList().save();
/*  98 */     } catch (Exception e) {
/*  99 */       LOGGER.warn("Failed to save white-list: ", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public boolean isWhiteListed(NameAndId nameAndId) { return (!isUsingWhitelist() || isOp(nameAndId) || getWhiteList().isWhiteListed(nameAndId)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public DedicatedServer getServer() { return (DedicatedServer)super.getServer(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean canBypassPlayerLimit(NameAndId nameAndId) { return getOps().canBypassPlayerLimit(nameAndId); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dedicated\DedicatedPlayerList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */