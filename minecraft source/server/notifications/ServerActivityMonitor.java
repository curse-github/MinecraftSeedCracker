/*    */ package net.minecraft.server.notifications;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class ServerActivityMonitor {
/*    */   private final long minimumMillisBetweenNotifications;
/*    */   private final AtomicLong lastNotificationTime;
/*    */   
/*    */   public ServerActivityMonitor(NotificationManager notificationManager, int secondsBetweenNotifications) {
/* 11 */     this.lastNotificationTime = new AtomicLong();
/* 12 */     this.serverActivity = new AtomicBoolean(false);
/*    */ 
/*    */ 
/*    */     
/* 16 */     this.notificationManager = notificationManager;
/* 17 */     this.minimumMillisBetweenNotifications = TimeUnit.SECONDS.toMillis(secondsBetweenNotifications);
/*    */   }
/*    */   private final AtomicBoolean serverActivity; private final NotificationManager notificationManager;
/*    */   
/* 21 */   public void tick() { processWithRateLimit(); }
/*    */ 
/*    */   
/*    */   public void reportLoginActivity() {
/* 25 */     this.serverActivity.set(true);
/* 26 */     processWithRateLimit();
/*    */   }
/*    */   
/*    */   private void processWithRateLimit() {
/* 30 */     long now = Util.getMillis();
/* 31 */     if (this.serverActivity.get() && now - this.lastNotificationTime.get() >= this.minimumMillisBetweenNotifications) {
/* 32 */       this.notificationManager.serverActivityOccured();
/* 33 */       this.lastNotificationTime.set(Util.getMillis());
/*    */     } 
/* 35 */     this.serverActivity.set(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\notifications\ServerActivityMonitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */