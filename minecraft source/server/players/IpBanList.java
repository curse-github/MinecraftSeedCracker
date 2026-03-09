/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.File;
/*    */ import java.net.SocketAddress;
/*    */ import net.minecraft.server.notifications.NotificationService;
/*    */ 
/*    */ 
/*    */ public class IpBanList
/*    */   extends StoredUserList<String, IpBanListEntry>
/*    */ {
/* 12 */   public IpBanList(File file, NotificationService notificationService) { super(file, notificationService); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   protected StoredUserEntry<String> createEntry(JsonObject object) { return new IpBanListEntry(object); }
/*    */ 
/*    */   
/*    */   public boolean isBanned(SocketAddress address) {
/* 21 */     String ip = getIpFromAddress(address);
/* 22 */     return contains(ip);
/*    */   }
/*    */ 
/*    */   
/* 26 */   public boolean isBanned(String ip) { return contains(ip); }
/*    */ 
/*    */   
/*    */   public IpBanListEntry get(SocketAddress address) {
/* 30 */     String ip = getIpFromAddress(address);
/* 31 */     return (IpBanListEntry)get(ip);
/*    */   }
/*    */   
/*    */   private String getIpFromAddress(SocketAddress address) {
/* 35 */     String ip = address.toString();
/* 36 */     if (ip.contains("/")) {
/* 37 */       ip = ip.substring(ip.indexOf('/') + 1);
/*    */     }
/* 39 */     if (ip.contains(":")) {
/* 40 */       ip = ip.substring(0, ip.indexOf(':'));
/*    */     }
/* 42 */     return ip;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(IpBanListEntry infos) {
/* 47 */     if (super.add(infos)) {
/* 48 */       if (infos.getUser() != null) {
/* 49 */         this.notificationService.ipBanned(infos);
/*    */       }
/* 51 */       return true;
/*    */     } 
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(String ip) {
/* 58 */     if (super.remove(ip)) {
/* 59 */       this.notificationService.ipUnbanned(ip);
/* 60 */       return true;
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 67 */     for (IpBanListEntry user : getEntries()) {
/* 68 */       if (user.getUser() == null) {
/*    */         continue;
/*    */       }
/* 71 */       this.notificationService.ipUnbanned((String)user.getUser());
/*    */     } 
/* 73 */     super.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\IpBanList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */