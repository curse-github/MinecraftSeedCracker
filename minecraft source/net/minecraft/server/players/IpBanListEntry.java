/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ public class IpBanListEntry
/*    */   extends BanListEntry<String>
/*    */ {
/* 11 */   public IpBanListEntry(String address) { this(address, null, null, null, null); }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public IpBanListEntry(String address, Date created, String source, Date expires, String reason) { super(address, created, source, expires, reason); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public Component getDisplayName() { return Component.literal(String.valueOf(getUser())); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public IpBanListEntry(JsonObject object) { super(createIpInfo(object), object); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   private static String createIpInfo(JsonObject object) { return object.has("ip") ? object.get("ip").getAsString() : null; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject object) {
/* 33 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 36 */     object.addProperty("ip", (String)getUser());
/* 37 */     super.serialize(object);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\IpBanListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */