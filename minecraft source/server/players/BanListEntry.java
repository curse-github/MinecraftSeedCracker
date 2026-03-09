/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public abstract class BanListEntry<T>
/*     */   extends StoredUserEntry<T>
/*     */ {
/*  14 */   public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT);
/*     */   
/*     */   public static final String EXPIRES_NEVER = "forever";
/*     */   protected final Date created;
/*     */   protected final String source;
/*     */   protected final Date expires;
/*     */   protected final String reason;
/*     */   
/*     */   public BanListEntry(T user, Date created, String source, Date expires, String reason) {
/*  23 */     super(user);
/*  24 */     this.created = (created == null) ? new Date() : created;
/*  25 */     this.source = (source == null) ? "(Unknown)" : source;
/*  26 */     this.expires = expires;
/*  27 */     this.reason = reason;
/*     */   }
/*     */   
/*     */   protected BanListEntry(T user, JsonObject object) {
/*  31 */     super(user);
/*     */     
/*     */     try {
/*  34 */       created = object.has("created") ? DATE_FORMAT.parse(object.get("created").getAsString()) : new Date();
/*  35 */     } catch (ParseException ignored) {
/*  36 */       created = new Date();
/*     */     } 
/*  38 */     this.created = created;
/*  39 */     this.source = object.has("source") ? object.get("source").getAsString() : "(Unknown)";
/*     */     
/*     */     try {
/*  42 */       expires = object.has("expires") ? DATE_FORMAT.parse(object.get("expires").getAsString()) : null;
/*  43 */     } catch (ParseException ignored) {
/*  44 */       expires = null;
/*     */     } 
/*  46 */     this.expires = expires;
/*  47 */     this.reason = object.has("reason") ? object.get("reason").getAsString() : null;
/*     */   }
/*     */ 
/*     */   
/*  51 */   public Date getCreated() { return this.created; }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public String getSource() { return this.source; }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public Date getExpires() { return this.expires; }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public String getReason() { return this.reason; }
/*     */ 
/*     */   
/*     */   public Component getReasonMessage() {
/*  67 */     String reason = getReason();
/*  68 */     return (reason == null) ? 
/*  69 */       Component.translatable("multiplayer.disconnect.banned.reason.default") : 
/*  70 */       Component.literal(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Component getDisplayName();
/*     */   
/*     */   boolean hasExpired() {
/*  77 */     if (this.expires == null) {
/*  78 */       return false;
/*     */     }
/*  80 */     return this.expires.before(new Date());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void serialize(JsonObject object) {
/*  85 */     object.addProperty("created", DATE_FORMAT.format(this.created));
/*  86 */     object.addProperty("source", this.source);
/*  87 */     object.addProperty("expires", (this.expires == null) ? "forever" : DATE_FORMAT.format(this.expires));
/*  88 */     object.addProperty("reason", this.reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  93 */     if (this == o) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (o == null || getClass() != o.getClass()) {
/*  97 */       return false;
/*     */     }
/*  99 */     BanListEntry<?> that = (BanListEntry)o;
/*     */ 
/*     */     
/* 102 */     return (Objects.equals(this.source, that.source) && 
/* 103 */       Objects.equals(this.expires, that.expires) && 
/* 104 */       Objects.equals(this.reason, that.reason) && 
/* 105 */       Objects.equals(getUser(), that.getUser()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\BanListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */