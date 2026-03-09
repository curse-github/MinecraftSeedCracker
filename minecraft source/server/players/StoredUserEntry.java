/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public abstract class StoredUserEntry<T>
/*    */   extends Object
/*    */ {
/*    */   private final T user;
/*    */   
/* 10 */   public StoredUserEntry(T user) { this.user = user; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public T getUser() { return (T)this.user; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   boolean hasExpired() { return false; }
/*    */   
/*    */   protected abstract void serialize(JsonObject paramJsonObject);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\StoredUserEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */