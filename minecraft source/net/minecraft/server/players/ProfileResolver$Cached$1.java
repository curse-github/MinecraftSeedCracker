/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends CacheLoader<UUID, Optional<GameProfile>>
/*    */ {
/*    */   null(ProfileResolver.Cached this$0) {}
/*    */   
/*    */   public Optional<GameProfile> load(UUID profileId) {
/* 44 */     ProfileResult result = sessionService.fetchProfile(profileId, true);
/* 45 */     return Optional.ofNullable(result).map(ProfileResult::profile);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\ProfileResolver$Cached$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */