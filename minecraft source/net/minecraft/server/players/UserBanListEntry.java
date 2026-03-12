/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class UserBanListEntry
/*    */   extends BanListEntry<NameAndId>
/*    */ {
/* 10 */   private static final Component MESSAGE_UNKNOWN_USER = Component.translatable("commands.banlist.entry.unknown");
/*    */ 
/*    */   
/* 13 */   public UserBanListEntry(NameAndId user) { this(user, null, null, null, null); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public UserBanListEntry(NameAndId user, Date created, String source, Date expires, String reason) { super(user, created, source, expires, reason); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public UserBanListEntry(JsonObject object) { super(NameAndId.fromJson(object), object); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject object) {
/* 26 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 29 */     ((NameAndId)getUser()).appendTo(object);
/* 30 */     super.serialize(object);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDisplayName() {
/* 35 */     NameAndId user = (NameAndId)getUser();
/* 36 */     return (user != null) ? Component.literal(user.name()) : MESSAGE_UNKNOWN_USER;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\UserBanListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */