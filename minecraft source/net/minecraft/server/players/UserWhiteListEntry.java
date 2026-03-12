/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public class UserWhiteListEntry
/*    */   extends StoredUserEntry<NameAndId> {
/*  7 */   public UserWhiteListEntry(NameAndId user) { super(user); }
/*    */ 
/*    */ 
/*    */   
/* 11 */   public UserWhiteListEntry(JsonObject object) { super(NameAndId.fromJson(object)); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject object) {
/* 16 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 19 */     ((NameAndId)getUser()).appendTo(object);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\UserWhiteListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */