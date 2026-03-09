/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.File;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.server.notifications.NotificationService;
/*    */ 
/*    */ 
/*    */ public class UserWhiteList
/*    */   extends StoredUserList<NameAndId, UserWhiteListEntry>
/*    */ {
/* 12 */   public UserWhiteList(File file, NotificationService notificationService) { super(file, notificationService); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   protected StoredUserEntry<NameAndId> createEntry(JsonObject object) { return new UserWhiteListEntry(object); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public boolean isWhiteListed(NameAndId user) { return contains(user); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean add(UserWhiteListEntry infos) {
/* 26 */     if (super.add(infos)) {
/* 27 */       if (infos.getUser() != null) {
/* 28 */         this.notificationService.playerAddedToAllowlist((NameAndId)infos.getUser());
/*    */       }
/* 30 */       return true;
/*    */     } 
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(NameAndId user) {
/* 37 */     if (super.remove(user)) {
/* 38 */       this.notificationService.playerRemovedFromAllowlist(user);
/* 39 */       return true;
/*    */     } 
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 46 */     for (UserWhiteListEntry user : getEntries()) {
/* 47 */       if (user.getUser() == null) {
/*    */         continue;
/*    */       }
/* 50 */       this.notificationService.playerRemovedFromAllowlist((NameAndId)user.getUser());
/*    */     } 
/* 52 */     super.clear();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public String[] getUserList() { return (String[])getEntries().stream().map(StoredUserEntry::getUser).filter(Objects::nonNull).map(NameAndId::name).toArray(x$0 -> new String[x$0]); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   protected String getKeyForUser(NameAndId user) { return user.id().toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\UserWhiteList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */