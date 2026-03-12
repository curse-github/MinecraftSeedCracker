/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedUserNameToIdResolver
/*     */   implements UserNameToIdResolver
/*     */ {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final int GAMEPROFILES_MRU_LIMIT = 1000; private static final int GAMEPROFILES_EXPIRATION_MONTHS = 1; private boolean resolveOfflineUsers; private final Map<String, GameProfileInfo> profilesByName; private final Map<UUID, GameProfileInfo> profilesByUUID; private final GameProfileRepository profileRepository; private final Gson gson; private final File file;
/*     */   private final AtomicLong operationCount;
/*     */   
/*     */   public CachedUserNameToIdResolver(GameProfileRepository profileRepository, File file) {
/*  48 */     this.resolveOfflineUsers = true;
/*  49 */     this.profilesByName = new ConcurrentHashMap();
/*  50 */     this.profilesByUUID = new ConcurrentHashMap();
/*     */     
/*  52 */     this.gson = (new GsonBuilder()).create();
/*     */     
/*  54 */     this.operationCount = new AtomicLong();
/*     */ 
/*     */     
/*  57 */     this.profileRepository = profileRepository;
/*  58 */     this.file = file;
/*     */     
/*  60 */     Lists.reverse(load()).forEach(this::safeAdd);
/*     */   }
/*     */   
/*     */   private void safeAdd(GameProfileInfo profileInfo) {
/*  64 */     NameAndId nameAndId = profileInfo.nameAndId();
/*  65 */     profileInfo.setLastAccess(getNextOperation());
/*  66 */     this.profilesByName.put(nameAndId.name().toLowerCase(Locale.ROOT), profileInfo);
/*  67 */     this.profilesByUUID.put(nameAndId.id(), profileInfo);
/*     */   }
/*     */   
/*     */   private Optional<NameAndId> lookupGameProfile(GameProfileRepository profileRepository, String name) {
/*  71 */     if (!StringUtil.isValidPlayerName(name)) {
/*  72 */       return createUnknownProfile(name);
/*     */     }
/*     */     
/*  75 */     Optional<NameAndId> profile = profileRepository.findProfileByName(name).map(NameAndId::new);
/*  76 */     if (profile.isEmpty()) {
/*  77 */       return createUnknownProfile(name);
/*     */     }
/*  79 */     return profile;
/*     */   }
/*     */   
/*     */   private Optional<NameAndId> createUnknownProfile(String name) {
/*  83 */     if (this.resolveOfflineUsers) {
/*  84 */       return Optional.of(NameAndId.createOffline(name));
/*     */     }
/*  86 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public void resolveOfflineUsers(boolean value) { this.resolveOfflineUsers = value; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public void add(NameAndId nameAndId) { addInternal(nameAndId); }
/*     */ 
/*     */   
/*     */   private GameProfileInfo addInternal(NameAndId profile) {
/* 100 */     Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.ROOT);
/* 101 */     c.setTime(new Date());
/* 102 */     c.add(2, 1);
/* 103 */     Date expirationDate = c.getTime();
/*     */     
/* 105 */     GameProfileInfo profileInfo = new GameProfileInfo(profile, expirationDate);
/* 106 */     safeAdd(profileInfo);
/* 107 */     save();
/* 108 */     return profileInfo;
/*     */   }
/*     */ 
/*     */   
/* 112 */   private long getNextOperation() { return this.operationCount.incrementAndGet(); }
/*     */ 
/*     */   
/*     */   public Optional<NameAndId> get(String name) {
/*     */     Optional<NameAndId> result;
/* 117 */     String userName = name.toLowerCase(Locale.ROOT);
/* 118 */     GameProfileInfo profileInfo = (GameProfileInfo)this.profilesByName.get(userName);
/*     */     
/* 120 */     boolean needsSave = false;
/*     */     
/* 122 */     if (profileInfo != null && (new Date()).getTime() >= profileInfo.expirationDate.getTime()) {
/*     */       
/* 124 */       this.profilesByUUID.remove(profileInfo.nameAndId().id());
/* 125 */       this.profilesByName.remove(profileInfo.nameAndId().name().toLowerCase(Locale.ROOT));
/* 126 */       needsSave = true;
/* 127 */       profileInfo = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (profileInfo != null) {
/* 133 */       profileInfo.setLastAccess(getNextOperation());
/* 134 */       result = Optional.of(profileInfo.nameAndId());
/*     */     } else {
/* 136 */       Optional<NameAndId> profile = lookupGameProfile(this.profileRepository, userName);
/* 137 */       if (profile.isPresent()) {
/* 138 */         result = Optional.of(addInternal((NameAndId)profile.get()).nameAndId());
/*     */         
/* 140 */         needsSave = false;
/*     */       } else {
/* 142 */         result = Optional.empty();
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     if (needsSave) {
/* 147 */       save();
/*     */     }
/* 149 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<NameAndId> get(UUID id) {
/* 154 */     GameProfileInfo profileInfo = (GameProfileInfo)this.profilesByUUID.get(id);
/* 155 */     if (profileInfo == null) {
/* 156 */       return Optional.empty();
/*     */     }
/* 158 */     profileInfo.setLastAccess(getNextOperation());
/* 159 */     return Optional.of(profileInfo.nameAndId());
/*     */   }
/*     */ 
/*     */   
/* 163 */   private static DateFormat createDateFormat() { return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT); }
/*     */ 
/*     */   
/*     */   private List<GameProfileInfo> load() {
/* 167 */     List<GameProfileInfo> result = Lists.newArrayList(); 
/* 168 */     try { Reader reader = Files.newReader(this.file, StandardCharsets.UTF_8); 
/* 169 */       try { JsonArray entryList = (JsonArray)this.gson.fromJson(reader, JsonArray.class);
/* 170 */         if (entryList == null)
/* 171 */         { List<GameProfileInfo> list = result;
/*     */ 
/*     */ 
/*     */           
/* 175 */           if (reader != null) reader.close();  return list; }  DateFormat dateFormat = createDateFormat(); entryList.forEach(element -> { Objects.requireNonNull(result); readGameProfile(element, dateFormat).ifPresent(result::add); }); if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (FileNotFoundException fileNotFoundException)
/*     */     {  }
/* 177 */     catch (IOException|com.google.gson.JsonParseException e)
/* 178 */     { LOGGER.warn("Failed to load profile cache {}", this.file, e); }
/*     */     
/* 180 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() {
/* 185 */     JsonArray entryList = new JsonArray();
/* 186 */     DateFormat dateFormat = createDateFormat();
/* 187 */     getTopMRUProfiles(1000).forEach(entry -> entryList.add(writeGameProfile(entry, dateFormat)));
/*     */     
/* 189 */     String toSave = this.gson.toJson(entryList); 
/* 190 */     try { Writer writer = Files.newWriter(this.file, StandardCharsets.UTF_8); 
/* 191 */       try { writer.write(toSave);
/* 192 */         if (writer != null) writer.close();  } catch (Throwable throwable) { if (writer != null) try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 198 */   private Stream<GameProfileInfo> getTopMRUProfiles(int limit) { return ImmutableList.copyOf(this.profilesByUUID.values()).stream().sorted(Comparator.comparing(GameProfileInfo::lastAccess).reversed()).limit(limit); }
/*     */ 
/*     */   
/*     */   private static JsonElement writeGameProfile(GameProfileInfo src, DateFormat dateFormat) {
/* 202 */     JsonObject object = new JsonObject();
/* 203 */     src.nameAndId().appendTo(object);
/* 204 */     object.addProperty("expiresOn", dateFormat.format(src.expirationDate()));
/* 205 */     return object;
/*     */   }
/*     */   
/*     */   private static Optional<GameProfileInfo> readGameProfile(JsonElement json, DateFormat dateFormat) {
/* 209 */     if (json.isJsonObject()) {
/* 210 */       JsonObject object = json.getAsJsonObject();
/* 211 */       NameAndId nameAndId = NameAndId.fromJson(object);
/* 212 */       if (nameAndId != null) {
/* 213 */         JsonElement expirationElement = object.get("expiresOn");
/* 214 */         if (expirationElement != null) {
/* 215 */           String dateAsString = expirationElement.getAsString();
/*     */           try {
/* 217 */             Date expirationDate = dateFormat.parse(dateAsString);
/* 218 */             return Optional.of(new GameProfileInfo(nameAndId, expirationDate));
/* 219 */           } catch (ParseException e) {
/* 220 */             LOGGER.warn("Failed to parse date {}", dateAsString, e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 225 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class GameProfileInfo
/*     */   {
/*     */     private final NameAndId nameAndId;
/*     */     private final Date expirationDate;
/*     */     
/*     */     private GameProfileInfo(NameAndId nameAndId, Date expirationDate) {
/* 235 */       this.nameAndId = nameAndId;
/* 236 */       this.expirationDate = expirationDate;
/*     */     }
/*     */ 
/*     */     
/* 240 */     public NameAndId nameAndId() { return this.nameAndId; }
/*     */ 
/*     */ 
/*     */     
/* 244 */     public Date expirationDate() { return this.expirationDate; }
/*     */ 
/*     */ 
/*     */     
/* 248 */     public void setLastAccess(long currentOperation) { this.lastAccess = currentOperation; }
/*     */ 
/*     */ 
/*     */     
/* 252 */     public long lastAccess() { return this.lastAccess; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\CachedUserNameToIdResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */