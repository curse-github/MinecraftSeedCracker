/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.notifications.EmptyNotificationService;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OldUsersConverter
/*     */ {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  34 */   public static final File OLD_IPBANLIST = new File("banned-ips.txt");
/*  35 */   public static final File OLD_USERBANLIST = new File("banned-players.txt");
/*  36 */   public static final File OLD_OPLIST = new File("ops.txt");
/*  37 */   public static final File OLD_WHITELIST = new File("white-list.txt");
/*     */   
/*     */   static List<String> readOldListFormat(File file, Map<String, String[]> userMap) throws IOException {
/*  40 */     List<String> lines = Files.readLines(file, StandardCharsets.UTF_8);
/*  41 */     for (String line : lines) {
/*  42 */       line = line.trim();
/*  43 */       if (line.startsWith("#") || line.isEmpty()) {
/*     */         continue;
/*     */       }
/*  46 */       String[] parts = line.split("\\|");
/*  47 */       userMap.put(parts[0].toLowerCase(Locale.ROOT), parts);
/*     */     } 
/*  49 */     return lines;
/*     */   }
/*     */   
/*     */   private static void lookupPlayers(MinecraftServer server, Collection<String> names, ProfileLookupCallback callback) {
/*  53 */     String[] filteredNames = (String[])names.stream().filter(s -> !StringUtil.isNullOrEmpty(s)).toArray(x$0 -> new String[x$0]);
/*  54 */     if (server.usesAuthentication()) {
/*  55 */       server.services().profileRepository().findProfilesByNames(filteredNames, callback);
/*     */     } else {
/*  57 */       for (String name : filteredNames) {
/*  58 */         callback.onProfileLookupSucceeded(name, UUIDUtil.createOfflinePlayerUUID(name));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean convertUserBanlist(final MinecraftServer server) {
/*  64 */     final UserBanList bans = new UserBanList(PlayerList.USERBANLIST_FILE, new EmptyNotificationService());
/*  65 */     if (OLD_USERBANLIST.exists() && OLD_USERBANLIST.isFile()) {
/*  66 */       if (bans.getFile().exists()) {
/*     */         try {
/*  68 */           bans.load();
/*  69 */         } catch (IOException e) {
/*  70 */           LOGGER.warn("Could not load existing file {}", bans.getFile().getName(), e);
/*     */         } 
/*     */       }
/*     */       try {
/*  74 */         final Map<String, String[]> userMap = Maps.newHashMap();
/*  75 */         readOldListFormat(OLD_USERBANLIST, userMap);
/*     */         
/*  77 */         ProfileLookupCallback callback = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/*  80 */               NameAndId profile = new NameAndId(profileId, profileName);
/*  81 */               server.services().nameToIdCache().add(profile);
/*  82 */               String[] userDef = (String[])userMap.get(profile.name().toLowerCase(Locale.ROOT));
/*  83 */               if (userDef == null) {
/*  84 */                 OldUsersConverter.LOGGER.warn("Could not convert user banlist entry for {}", profile.name());
/*  85 */                 throw new OldUsersConverter.ConversionError("Profile not in the conversionlist");
/*     */               } 
/*     */               
/*  88 */               Date created = (userDef.length > 1) ? OldUsersConverter.parseDate(userDef[1], null) : null;
/*  89 */               String source = (userDef.length > 2) ? userDef[2] : null;
/*  90 */               Date expires = (userDef.length > 3) ? OldUsersConverter.parseDate(userDef[3], null) : null;
/*  91 */               String reason = (userDef.length > 4) ? userDef[4] : null;
/*  92 */               bans.add(new UserBanListEntry(profile, created, source, expires, reason));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(String profileName, Exception exception) {
/*  97 */               OldUsersConverter.LOGGER.warn("Could not lookup user banlist entry for {}", profileName, exception);
/*  98 */               if (!(exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/*  99 */                 throw new OldUsersConverter.ConversionError("Could not request user " + profileName + " from backend systems", exception);
/*     */               }
/*     */             }
/*     */           };
/* 103 */         lookupPlayers(server, userMap.keySet(), callback);
/* 104 */         bans.save();
/* 105 */         renameOldFile(OLD_USERBANLIST);
/* 106 */       } catch (IOException e) {
/* 107 */         LOGGER.warn("Could not read old user banlist to convert it!", e);
/* 108 */         return false;
/* 109 */       } catch (ConversionError e) {
/* 110 */         LOGGER.error("Conversion failed, please try again later", e);
/* 111 */         return false;
/*     */       } 
/* 113 */       return true;
/*     */     } 
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertIpBanlist(MinecraftServer server) {
/* 119 */     IpBanList ipBans = new IpBanList(PlayerList.IPBANLIST_FILE, new EmptyNotificationService());
/* 120 */     if (OLD_IPBANLIST.exists() && OLD_IPBANLIST.isFile()) {
/* 121 */       if (ipBans.getFile().exists()) {
/*     */         try {
/* 123 */           ipBans.load();
/* 124 */         } catch (IOException e) {
/* 125 */           LOGGER.warn("Could not load existing file {}", ipBans.getFile().getName(), e);
/*     */         } 
/*     */       }
/*     */       try {
/* 129 */         Map<String, String[]> userMap = Maps.newHashMap();
/* 130 */         readOldListFormat(OLD_IPBANLIST, userMap);
/*     */         
/* 132 */         for (String key : userMap.keySet()) {
/* 133 */           String[] userDef = (String[])userMap.get(key);
/* 134 */           Date created = (userDef.length > 1) ? parseDate(userDef[1], null) : null;
/* 135 */           String source = (userDef.length > 2) ? userDef[2] : null;
/* 136 */           Date expires = (userDef.length > 3) ? parseDate(userDef[3], null) : null;
/* 137 */           String reason = (userDef.length > 4) ? userDef[4] : null;
/* 138 */           ipBans.add(new IpBanListEntry(key, created, source, expires, reason));
/*     */         } 
/* 140 */         ipBans.save();
/* 141 */         renameOldFile(OLD_IPBANLIST);
/* 142 */       } catch (IOException e) {
/* 143 */         LOGGER.warn("Could not parse old ip banlist to convert it!", e);
/* 144 */         return false;
/*     */       } 
/* 146 */       return true;
/*     */     } 
/* 148 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertOpsList(final MinecraftServer server) {
/* 152 */     final ServerOpList opsList = new ServerOpList(PlayerList.OPLIST_FILE, new EmptyNotificationService());
/* 153 */     if (OLD_OPLIST.exists() && OLD_OPLIST.isFile()) {
/* 154 */       if (opsList.getFile().exists()) {
/*     */         try {
/* 156 */           opsList.load();
/* 157 */         } catch (IOException e) {
/* 158 */           LOGGER.warn("Could not load existing file {}", opsList.getFile().getName(), e);
/*     */         } 
/*     */       }
/*     */       try {
/* 162 */         List<String> lines = Files.readLines(OLD_OPLIST, StandardCharsets.UTF_8);
/* 163 */         ProfileLookupCallback callback = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 166 */               NameAndId profile = new NameAndId(profileId, profileName);
/* 167 */               server.services().nameToIdCache().add(profile);
/* 168 */               opsList.add(new ServerOpListEntry(profile, server.operatorUserPermissions(), false));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(String profileName, Exception exception) {
/* 173 */               OldUsersConverter.LOGGER.warn("Could not lookup oplist entry for {}", profileName, exception);
/* 174 */               if (!(exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/* 175 */                 throw new OldUsersConverter.ConversionError("Could not request user " + profileName + " from backend systems", exception);
/*     */               }
/*     */             }
/*     */           };
/* 179 */         lookupPlayers(server, lines, callback);
/* 180 */         opsList.save();
/* 181 */         renameOldFile(OLD_OPLIST);
/* 182 */       } catch (IOException e) {
/* 183 */         LOGGER.warn("Could not read old oplist to convert it!", e);
/* 184 */         return false;
/* 185 */       } catch (ConversionError e) {
/* 186 */         LOGGER.error("Conversion failed, please try again later", e);
/* 187 */         return false;
/*     */       } 
/* 189 */       return true;
/*     */     } 
/* 191 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertWhiteList(final MinecraftServer server) {
/* 195 */     final UserWhiteList whitelist = new UserWhiteList(PlayerList.WHITELIST_FILE, new EmptyNotificationService());
/* 196 */     if (OLD_WHITELIST.exists() && OLD_WHITELIST.isFile()) {
/* 197 */       if (whitelist.getFile().exists()) {
/*     */         try {
/* 199 */           whitelist.load();
/* 200 */         } catch (IOException e) {
/* 201 */           LOGGER.warn("Could not load existing file {}", whitelist.getFile().getName(), e);
/*     */         } 
/*     */       }
/*     */       try {
/* 205 */         List<String> lines = Files.readLines(OLD_WHITELIST, StandardCharsets.UTF_8);
/* 206 */         ProfileLookupCallback callback = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 209 */               NameAndId profile = new NameAndId(profileId, profileName);
/* 210 */               server.services().nameToIdCache().add(profile);
/* 211 */               whitelist.add(new UserWhiteListEntry(profile));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(String profileName, Exception exception) {
/* 216 */               OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", profileName, exception);
/* 217 */               if (!(exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/* 218 */                 throw new OldUsersConverter.ConversionError("Could not request user " + profileName + " from backend systems", exception);
/*     */               }
/*     */             }
/*     */           };
/* 222 */         lookupPlayers(server, lines, callback);
/* 223 */         whitelist.save();
/* 224 */         renameOldFile(OLD_WHITELIST);
/* 225 */       } catch (IOException e) {
/* 226 */         LOGGER.warn("Could not read old whitelist to convert it!", e);
/* 227 */         return false;
/* 228 */       } catch (ConversionError e) {
/* 229 */         LOGGER.error("Conversion failed, please try again later", e);
/* 230 */         return false;
/*     */       } 
/* 232 */       return true;
/*     */     } 
/* 234 */     return true;
/*     */   }
/*     */   
/*     */   public static UUID convertMobOwnerIfNecessary(final MinecraftServer server, String owner) {
/* 238 */     if (StringUtil.isNullOrEmpty(owner) || owner.length() > 16) {
/*     */       try {
/* 240 */         return UUID.fromString(owner);
/* 241 */       } catch (IllegalArgumentException ignored) {
/* 242 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 246 */     Optional<UUID> profileId = server.services().nameToIdCache().get(owner).map(NameAndId::id);
/* 247 */     if (profileId.isPresent()) {
/* 248 */       return (UUID)profileId.get();
/*     */     }
/* 250 */     if (server.isSingleplayer() || !server.usesAuthentication()) {
/* 251 */       return UUIDUtil.createOfflinePlayerUUID(owner);
/*     */     }
/* 253 */     final List<NameAndId> profiles = new ArrayList<NameAndId>();
/* 254 */     ProfileLookupCallback callback = new ProfileLookupCallback()
/*     */       {
/*     */         public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 257 */           NameAndId profile = new NameAndId(profileId, profileName);
/* 258 */           server.services().nameToIdCache().add(profile);
/* 259 */           profiles.add(profile);
/*     */         }
/*     */ 
/*     */         
/*     */         public void onProfileLookupFailed(String profileName, Exception exception) {
/* 264 */           OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", profileName, exception);
/*     */         }
/*     */       };
/* 267 */     lookupPlayers(server, Lists.newArrayList(new String[] { owner }, ), callback);
/* 268 */     if (!profiles.isEmpty()) {
/* 269 */       return ((NameAndId)profiles.getFirst()).id();
/*     */     }
/*     */     
/* 272 */     return null;
/*     */   }
/*     */   
/*     */   private static class ConversionError
/*     */     extends RuntimeException {
/* 277 */     private ConversionError(String message, Throwable cause) { super(message, cause); }
/*     */ 
/*     */ 
/*     */     
/* 281 */     private ConversionError(String message) { super(message); }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean convertPlayers(final DedicatedServer server) {
/* 286 */     final File worldPlayerDirectory = getWorldPlayersDirectory(server);
/* 287 */     final File worldNewPlayerDirectory = new File(worldPlayerDirectory.getParentFile(), "playerdata");
/* 288 */     final File unknownPlayerDirectory = new File(worldPlayerDirectory.getParentFile(), "unknownplayers");
/* 289 */     if (!worldPlayerDirectory.exists() || !worldPlayerDirectory.isDirectory()) {
/* 290 */       return true;
/*     */     }
/* 292 */     File[] playerFiles = worldPlayerDirectory.listFiles();
/* 293 */     List<String> playerNames = Lists.newArrayList();
/* 294 */     for (File file : playerFiles) {
/* 295 */       String fileName = file.getName();
/* 296 */       if (fileName.toLowerCase(Locale.ROOT).endsWith(".dat")) {
/*     */ 
/*     */         
/* 299 */         String playerName = fileName.substring(0, fileName.length() - ".dat".length());
/* 300 */         if (!playerName.isEmpty()) {
/* 301 */           playerNames.add(playerName);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     try {
/* 306 */       final String[] names = (String[])playerNames.toArray(new String[playerNames.size()]);
/* 307 */       ProfileLookupCallback callback = new ProfileLookupCallback()
/*     */         {
/*     */           public void onProfileLookupSucceeded(String profileName, UUID profileId) {
/* 310 */             NameAndId profile = new NameAndId(profileId, profileName);
/* 311 */             server.services().nameToIdCache().add(profile);
/* 312 */             movePlayerFile(worldNewPlayerDirectory, getFileNameForProfile(profileName), profileId.toString());
/*     */           }
/*     */ 
/*     */           
/*     */           public void onProfileLookupFailed(String profileName, Exception exception) {
/* 317 */             OldUsersConverter.LOGGER.warn("Could not lookup user uuid for {}", profileName, exception);
/* 318 */             if (exception instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException) {
/* 319 */               String fileNameForProfile = getFileNameForProfile(profileName);
/* 320 */               movePlayerFile(unknownPlayerDirectory, fileNameForProfile, fileNameForProfile);
/*     */             } else {
/* 322 */               throw new OldUsersConverter.ConversionError("Could not request user " + profileName + " from backend systems", exception);
/*     */             } 
/*     */           }
/*     */           
/*     */           private void movePlayerFile(File directory, String oldName, String newName) {
/* 327 */             File oldFileName = new File(worldPlayerDirectory, oldName + ".dat");
/* 328 */             File newFileName = new File(directory, newName + ".dat");
/* 329 */             OldUsersConverter.ensureDirectoryExists(directory);
/* 330 */             if (!oldFileName.renameTo(newFileName)) {
/* 331 */               throw new OldUsersConverter.ConversionError("Could not convert file for " + oldName);
/*     */             }
/*     */           }
/*     */           
/*     */           private String getFileNameForProfile(String profileName) {
/* 336 */             String fileName = null;
/* 337 */             for (String name : names) {
/* 338 */               if (name != null && name.equalsIgnoreCase(profileName)) {
/* 339 */                 fileName = name;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 343 */             if (fileName == null) {
/* 344 */               throw new OldUsersConverter.ConversionError("Could not find the filename for " + profileName + " anymore");
/*     */             }
/* 346 */             return fileName;
/*     */           }
/*     */         };
/* 349 */       lookupPlayers(server, Lists.newArrayList(names), callback);
/* 350 */     } catch (ConversionError e) {
/* 351 */       LOGGER.error("Conversion failed, please try again later", e);
/* 352 */       return false;
/*     */     } 
/*     */     
/* 355 */     return true;
/*     */   }
/*     */   
/*     */   private static void ensureDirectoryExists(File directory) {
/* 359 */     if (directory.exists()) {
/* 360 */       if (directory.isDirectory()) {
/*     */         return;
/*     */       }
/* 363 */       throw new ConversionError("Can't create directory " + directory.getName() + " in world save directory.");
/*     */     } 
/*     */     
/* 366 */     if (!directory.mkdirs()) {
/* 367 */       throw new ConversionError("Can't create directory " + directory.getName() + " in world save directory.");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean serverReadyAfterUserconversion(MinecraftServer server) {
/* 373 */     ready = areOldUserlistsRemoved();
/* 374 */     return (ready && areOldPlayersConverted(server));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean areOldUserlistsRemoved() {
/* 379 */     foundUserBanlist = false;
/* 380 */     if (OLD_USERBANLIST.exists() && OLD_USERBANLIST.isFile()) {
/* 381 */       foundUserBanlist = true;
/*     */     }
/* 383 */     boolean foundIpBanlist = false;
/* 384 */     if (OLD_IPBANLIST.exists() && OLD_IPBANLIST.isFile()) {
/* 385 */       foundIpBanlist = true;
/*     */     }
/* 387 */     boolean foundOpList = false;
/* 388 */     if (OLD_OPLIST.exists() && OLD_OPLIST.isFile()) {
/* 389 */       foundOpList = true;
/*     */     }
/* 391 */     boolean foundWhitelist = false;
/* 392 */     if (OLD_WHITELIST.exists() && OLD_WHITELIST.isFile()) {
/* 393 */       foundWhitelist = true;
/*     */     }
/*     */     
/* 396 */     if (foundUserBanlist || foundIpBanlist || foundOpList || foundWhitelist) {
/* 397 */       LOGGER.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
/* 398 */       LOGGER.warn("** please remove the following files and restart the server:");
/* 399 */       if (foundUserBanlist) {
/* 400 */         LOGGER.warn("* {}", OLD_USERBANLIST.getName());
/*     */       }
/* 402 */       if (foundIpBanlist) {
/* 403 */         LOGGER.warn("* {}", OLD_IPBANLIST.getName());
/*     */       }
/* 405 */       if (foundOpList) {
/* 406 */         LOGGER.warn("* {}", OLD_OPLIST.getName());
/*     */       }
/* 408 */       if (foundWhitelist) {
/* 409 */         LOGGER.warn("* {}", OLD_WHITELIST.getName());
/*     */       }
/* 411 */       return false;
/*     */     } 
/* 413 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean areOldPlayersConverted(MinecraftServer server) {
/* 417 */     File worldPlayerDirectory = getWorldPlayersDirectory(server);
/* 418 */     if (worldPlayerDirectory.exists() && worldPlayerDirectory.isDirectory() && (
/* 419 */       worldPlayerDirectory.list().length > 0 || !worldPlayerDirectory.delete())) {
/* 420 */       LOGGER.warn("**** DETECTED OLD PLAYER DIRECTORY IN THE WORLD SAVE");
/* 421 */       LOGGER.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
/* 422 */       LOGGER.warn("** please restart the server and if the problem persists, remove the directory '{}'", worldPlayerDirectory.getPath());
/* 423 */       return false;
/*     */     } 
/*     */     
/* 426 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 430 */   private static File getWorldPlayersDirectory(MinecraftServer server) { return server.getWorldPath(LevelResource.PLAYER_OLD_DATA_DIR).toFile(); }
/*     */ 
/*     */   
/*     */   private static void renameOldFile(File file) {
/* 434 */     File newFile = new File(file.getName() + ".converted");
/* 435 */     file.renameTo(newFile);
/*     */   }
/*     */   
/*     */   private static Date parseDate(String dateString, Date defaultValue) {
/*     */     Date parsedDate;
/*     */     try {
/* 441 */       parsedDate = BanListEntry.DATE_FORMAT.parse(dateString);
/* 442 */     } catch (ParseException ignored) {
/* 443 */       parsedDate = defaultValue;
/*     */     } 
/* 445 */     return parsedDate;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\OldUsersConverter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */