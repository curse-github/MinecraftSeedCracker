/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.google.common.cache.LoadingCache;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import com.mojang.authlib.yggdrasil.ProfileResult;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.time.Duration;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.util.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ProfileResolver
/*    */ {
/*    */   Optional<GameProfile> fetchByName(String paramString);
/*    */   
/*    */   Optional<GameProfile> fetchById(UUID paramUUID);
/*    */   
/* 27 */   default Optional<GameProfile> fetchByNameOrId(Either<String, UUID> nameOrId) { return (Optional)nameOrId.map(this::fetchByName, this::fetchById); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Cached
/*    */     implements ProfileResolver
/*    */   {
/* 38 */     private final LoadingCache<UUID, Optional<GameProfile>> profileCacheById = CacheBuilder.newBuilder()
/* 39 */       .expireAfterAccess(Duration.ofMinutes(10L))
/* 40 */       .maximumSize(256L)
/* 41 */       .build(new CacheLoader<UUID, Optional<GameProfile>>(this)
/*    */         {
/*    */           public Optional<GameProfile> load(UUID profileId) {
/* 44 */             ProfileResult result = sessionService.fetchProfile(profileId, true);
/* 45 */             return Optional.ofNullable(result).map(ProfileResult::profile);
/*    */           }
/*    */         });
/*    */     
/* 49 */     private final LoadingCache<String, Optional<GameProfile>> profileCacheByName = CacheBuilder.newBuilder()
/* 50 */       .expireAfterAccess(Duration.ofMinutes(10L))
/* 51 */       .maximumSize(256L)
/* 52 */       .build(new CacheLoader<String, Optional<GameProfile>>()
/*    */         {
/*    */           public Optional<GameProfile> load(String name) {
/* 55 */             return nameToIdCache
/* 56 */               .get(name)
/* 57 */               .flatMap(nameAndId -> (Optional)ProfileResolver.Cached.this.profileCacheById.getUnchecked(nameAndId.id()));
/*    */           }
/*    */         });
/*    */     
/*    */     public Cached(final MinecraftSessionService sessionService, final UserNameToIdResolver nameToIdCache) {}
/*    */     
/*    */     public Optional<GameProfile> fetchByName(String name) {
/* 64 */       if (StringUtil.isValidPlayerName(name)) {
/* 65 */         return (Optional)this.profileCacheByName.getUnchecked(name);
/*    */       }
/* 67 */       return Optional.empty();
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 72 */     public Optional<GameProfile> fetchById(UUID id) { return (Optional)this.profileCacheById.getUnchecked(id); }
/*    */   }
/*    */   
/*    */   class null extends CacheLoader<UUID, Optional<GameProfile>> {
/*    */     null(ProfileResolver.Cached this$0) {}
/*    */     
/*    */     public Optional<GameProfile> load(UUID profileId) {
/*    */       ProfileResult result = sessionService.fetchProfile(profileId, true);
/*    */       return Optional.ofNullable(result).map(ProfileResult::profile);
/*    */     }
/*    */   }
/*    */   
/*    */   class null extends CacheLoader<String, Optional<GameProfile>> {
/*    */     public Optional<GameProfile> load(String name) { return nameToIdCache.get(name).flatMap(nameAndId -> (Optional)ProfileResolver.Cached.this.profileCacheById.getUnchecked(nameAndId.id())); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\ProfileResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */