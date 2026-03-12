/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.io.File;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements ProfileLookupCallback
/*     */ {
/*     */   public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 310 */     NameAndId profile = new NameAndId(profileId, profileName);
/* 311 */     server.services().nameToIdCache().add(profile);
/* 312 */     movePlayerFile(worldNewPlayerDirectory, getFileNameForProfile(profileName), profileId.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onProfileLookupFailed(String profileName, Exception exception) {
/* 317 */     OldUsersConverter.LOGGER.warn("Could not lookup user uuid for {}", profileName, exception);
/* 318 */     if (exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException) {
/* 319 */       String fileNameForProfile = getFileNameForProfile(profileName);
/* 320 */       movePlayerFile(unknownPlayerDirectory, fileNameForProfile, fileNameForProfile);
/*     */     } else {
/* 322 */       throw new OldUsersConverter.ConversionError("Could not request user " + profileName + " from backend systems", exception);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void movePlayerFile(File directory, String oldName, String newName) {
/* 327 */     File oldFileName = new File(worldPlayerDirectory, oldName + ".dat");
/* 328 */     File newFileName = new File(directory, newName + ".dat");
/* 329 */     OldUsersConverter.ensureDirectoryExists(directory);
/* 330 */     if (!oldFileName.renameTo(newFileName)) {
/* 331 */       throw new OldUsersConverter.ConversionError("Could not convert file for " + oldName);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getFileNameForProfile(String profileName) {
/* 336 */     String fileName = null;
/* 337 */     for (String name : names) {
/* 338 */       if (name != null && name.equalsIgnoreCase(profileName)) {
/* 339 */         fileName = name;
/*     */         break;
/*     */       } 
/*     */     } 
/* 343 */     if (fileName == null) {
/* 344 */       throw new OldUsersConverter.ConversionError("Could not find the filename for " + profileName + " anymore");
/*     */     }
/* 346 */     return fileName;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\OldUsersConverter$5.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */