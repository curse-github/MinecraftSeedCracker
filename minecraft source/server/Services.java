/*    */ package net.minecraft.server;
/*    */ import com.mojang.authlib.GameProfileRepository;
/*    */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*    */ import com.mojang.authlib.yggdrasil.ServicesKeySet;
/*    */ import net.minecraft.server.players.CachedUserNameToIdResolver;
/*    */ import net.minecraft.server.players.ProfileResolver;
/*    */ 
/*    */ public final class Services extends Record {
/*    */   private final MinecraftSessionService sessionService;
/*    */   private final ServicesKeySet servicesKeySet;
/*    */   private final GameProfileRepository profileRepository;
/*    */   private final UserNameToIdResolver nameToIdCache;
/*    */   private final ProfileResolver profileResolver;
/*    */   private static final String USERID_CACHE_FILE = "usercache.json";
/*    */   
/* 16 */   public Services(MinecraftSessionService sessionService, ServicesKeySet servicesKeySet, GameProfileRepository profileRepository, UserNameToIdResolver nameToIdCache, ProfileResolver profileResolver) { this.sessionService = sessionService; this.servicesKeySet = servicesKeySet; this.profileRepository = profileRepository; this.nameToIdCache = nameToIdCache; this.profileResolver = profileResolver; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/Services;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/server/Services; } public MinecraftSessionService sessionService() { return this.sessionService; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/Services;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/Services; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/Services;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/Services;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public ServicesKeySet servicesKeySet() { return this.servicesKeySet; } public GameProfileRepository profileRepository() { return this.profileRepository; } public UserNameToIdResolver nameToIdCache() { return this.nameToIdCache; } public ProfileResolver profileResolver() { return this.profileResolver; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Services create(YggdrasilAuthenticationService serviceAccess, File nameCacheDir) {
/* 26 */     MinecraftSessionService sessionService = serviceAccess.createMinecraftSessionService();
/* 27 */     GameProfileRepository profileRepository = serviceAccess.createProfileRepository();
/* 28 */     CachedUserNameToIdResolver cachedUserNameToIdResolver = new CachedUserNameToIdResolver(profileRepository, new File(nameCacheDir, "usercache.json"));
/* 29 */     ProfileResolver.Cached cached = new ProfileResolver.Cached(sessionService, cachedUserNameToIdResolver);
/* 30 */     return new Services(sessionService, serviceAccess.getServicesKeySet(), profileRepository, cachedUserNameToIdResolver, cached);
/*    */   }
/*    */ 
/*    */   
/* 34 */   public SignatureValidator profileKeySignatureValidator() { return SignatureValidator.from(this.servicesKeySet, ServicesKeyType.PROFILE_KEY); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public boolean canValidateProfileKeys() { return !this.servicesKeySet.keys(ServicesKeyType.PROFILE_KEY).isEmpty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\Services.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */