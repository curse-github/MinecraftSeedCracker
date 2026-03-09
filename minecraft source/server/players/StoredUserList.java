/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class StoredUserList<K, V extends StoredUserEntry<K>>
/*     */   extends Object {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  30 */   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create(); private final File file; private final Map<String, V> map; protected final NotificationService notificationService;
/*     */   
/*     */   public StoredUserList(File file, NotificationService notificationService) {
/*  33 */     this.map = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */     
/*  37 */     this.file = file;
/*  38 */     this.notificationService = notificationService;
/*     */   }
/*     */ 
/*     */   
/*  42 */   public File getFile() { return this.file; }
/*     */ 
/*     */   
/*     */   public boolean add(V infos) {
/*  46 */     String keyForUser = getKeyForUser(infos.getUser());
/*  47 */     V previous = (V)(StoredUserEntry)this.map.get(keyForUser);
/*  48 */     if (infos.equals(previous)) {
/*  49 */       return false;
/*     */     }
/*  51 */     this.map.put(keyForUser, infos);
/*     */     try {
/*  53 */       save();
/*  54 */     } catch (IOException e) {
/*  55 */       LOGGER.warn("Could not save the list after adding a user.", e);
/*     */     } 
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   public V get(K user) {
/*  61 */     removeExpired();
/*  62 */     return (V)(StoredUserEntry)this.map.get(getKeyForUser(user));
/*     */   }
/*     */   
/*     */   public boolean remove(K user) {
/*  66 */     V removed = (V)(StoredUserEntry)this.map.remove(getKeyForUser(user));
/*  67 */     if (removed == null) {
/*  68 */       return false;
/*     */     }
/*     */     try {
/*  71 */       save();
/*  72 */     } catch (IOException e) {
/*  73 */       LOGGER.warn("Could not save the list after removing a user.", e);
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  79 */   public boolean remove(StoredUserEntry<K> infos) { return remove(Objects.requireNonNull(infos.getUser())); }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  83 */     this.map.clear();
/*     */     try {
/*  85 */       save();
/*  86 */     } catch (IOException e) {
/*  87 */       LOGGER.warn("Could not save the list after removing a user.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  92 */   public String[] getUserList() { return (String[])this.map.keySet().toArray(new String[0]); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public boolean isEmpty() { return this.map.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected String getKeyForUser(K user) { return user.toString(); }
/*     */ 
/*     */ 
/*     */   
/* 104 */   protected boolean contains(K user) { return this.map.containsKey(getKeyForUser(user)); }
/*     */ 
/*     */   
/*     */   private void removeExpired() {
/* 108 */     List<K> toRemove = Lists.newArrayList();
/* 109 */     for (null = this.map.values().iterator(); null.hasNext(); ) { V entry = (V)(StoredUserEntry)null.next();
/* 110 */       if (entry.hasExpired()) {
/* 111 */         toRemove.add(entry.getUser());
/*     */       } }
/*     */     
/* 114 */     for (K user : toRemove) {
/* 115 */       this.map.remove(getKeyForUser(user));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public Collection<V> getEntries() { return this.map.values(); }
/*     */ 
/*     */   
/*     */   public void save() {
/* 126 */     JsonArray result = new JsonArray();
/* 127 */     Objects.requireNonNull(result); this.map.values().stream().map(entry -> { Objects.requireNonNull(entry); return (JsonObject)Util.make(new JsonObject(), entry::serialize); }).forEach(result::add);
/* 128 */     BufferedWriter writer = Files.newWriter(this.file, StandardCharsets.UTF_8); 
/* 129 */     try { GSON.toJson(result, GSON.newJsonWriter(writer));
/* 130 */       if (writer != null) writer.close();  }
/*     */     catch (Throwable throwable) { if (writer != null)
/*     */         try { writer.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 136 */      } public void load() { if (!this.file.exists()) {
/*     */       return;
/*     */     }
/* 139 */     BufferedReader reader = Files.newReader(this.file, StandardCharsets.UTF_8); try {
/* 140 */       this.map.clear();
/* 141 */       JsonArray contents = (JsonArray)GSON.fromJson(reader, JsonArray.class);
/* 142 */       if (contents == null)
/*     */       
/*     */       { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 152 */         if (reader != null) reader.close();  return; }  for (JsonElement element : contents) { JsonObject object = GsonHelper.convertToJsonObject(element, "entry"); StoredUserEntry<K> entry = createEntry(object); if (entry.getUser() != null) this.map.put(getKeyForUser(entry.getUser()), entry);  }  if (reader != null) reader.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (reader != null)
/*     */         try {
/*     */           reader.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     }  }
/*     */ 
/*     */   
/*     */   protected abstract StoredUserEntry<K> createEntry(JsonObject paramJsonObject);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\StoredUserList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */