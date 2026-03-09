/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.mojang.authlib.ProfileLookupCallback;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.MinecraftServer;
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
/*    */   implements ProfileLookupCallback
/*    */ {
/*    */   public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 80 */     NameAndId profile = new NameAndId(profileId, profileName);
/* 81 */     server.services().nameToIdCache().add(profile);
/* 82 */     String[] userDef = (String[])userMap.get(profile.name().toLowerCase(Locale.ROOT));
/* 83 */     if (userDef == null) {
/* 84 */       OldUsersConverter.LOGGER.warn("Could not convert user banlist entry for {}", profile.name());
/* 85 */       throw new OldUsersConverter.ConversionError("Profile not in the conversionlist");
/*    */     } 
/*    */     
/* 88 */     Date created = (userDef.length > 1) ? OldUsersConverter.parseDate(userDef[1], null) : null;
/* 89 */     String source = (userDef.length > 2) ? userDef[2] : null;
/* 90 */     Date expires = (userDef.length > 3) ? OldUsersConverter.parseDate(userDef[3], null) : null;
/* 91 */     String reason = (userDef.length > 4) ? userDef[4] : null;
/* 92 */     bans.add(new UserBanListEntry(profile, created, source, expires, reason));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onProfileLookupFailed(String profileName, Exception exception) {
/* 97 */     OldUsersConverter.LOGGER.warn("Could not lookup user banlist entry for {}", profileName, exception);
/* 98 */     if (!(exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException))
/* 99 */       throw new OldUsersConverter.ConversionError("Could not request user " + profileName + " from backend systems", exception); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\OldUsersConverter$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */