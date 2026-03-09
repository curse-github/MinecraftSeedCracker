/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.File;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.server.notifications.NotificationService;
/*    */ 
/*    */ 
/*    */ public class ServerOpList
/*    */   extends StoredUserList<NameAndId, ServerOpListEntry>
/*    */ {
/* 12 */   public ServerOpList(File file, NotificationService notificationService) { super(file, notificationService); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   protected StoredUserEntry<NameAndId> createEntry(JsonObject object) { return new ServerOpListEntry(object); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public String[] getUserList() { return (String[])getEntries().stream().map(StoredUserEntry::getUser).filter(Objects::nonNull).map(NameAndId::name).toArray(x$0 -> new String[x$0]); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean add(ServerOpListEntry infos) {
/* 27 */     if (super.add(infos)) {
/* 28 */       if (infos.getUser() != null) {
/* 29 */         this.notificationService.playerOped(infos);
/*    */       }
/* 31 */       return true;
/*    */     } 
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(NameAndId user) {
/* 38 */     ServerOpListEntry entry = (ServerOpListEntry)get(user);
/* 39 */     if (super.remove(user)) {
/* 40 */       if (entry != null) {
/* 41 */         this.notificationService.playerDeoped(entry);
/*    */       }
/* 43 */       return true;
/*    */     } 
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 50 */     for (ServerOpListEntry user : getEntries()) {
/* 51 */       if (user.getUser() == null) {
/*    */         continue;
/*    */       }
/* 54 */       this.notificationService.playerDeoped(user);
/*    */     } 
/* 56 */     super.clear();
/*    */   }
/*    */   
/*    */   public boolean canBypassPlayerLimit(NameAndId user) {
/* 60 */     ServerOpListEntry entry = (ServerOpListEntry)get(user);
/*    */     
/* 62 */     if (entry != null) {
/* 63 */       return entry.getBypassesPlayerLimit();
/*    */     }
/*    */     
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 71 */   protected String getKeyForUser(NameAndId user) { return user.id().toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\ServerOpList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */