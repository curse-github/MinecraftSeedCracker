/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ 
/*    */ public class ServerOpListEntry extends StoredUserEntry<NameAndId> {
/*    */   private final LevelBasedPermissionSet permissions;
/*    */   private final boolean bypassesPlayerLimit;
/*    */   
/*    */   public ServerOpListEntry(NameAndId user, LevelBasedPermissionSet permissions, boolean bypassesPlayerLimit) {
/* 12 */     super(user);
/* 13 */     this.permissions = permissions;
/* 14 */     this.bypassesPlayerLimit = bypassesPlayerLimit;
/*    */   }
/*    */   
/*    */   public ServerOpListEntry(JsonObject object) {
/* 18 */     super(NameAndId.fromJson(object));
/* 19 */     PermissionLevel level = object.has("level") ? PermissionLevel.byId(object.get("level").getAsInt()) : PermissionLevel.ALL;
/* 20 */     this.permissions = LevelBasedPermissionSet.forLevel(level);
/* 21 */     this.bypassesPlayerLimit = (object.has("bypassesPlayerLimit") && object.get("bypassesPlayerLimit").getAsBoolean());
/*    */   }
/*    */ 
/*    */   
/* 25 */   public LevelBasedPermissionSet permissions() { return this.permissions; }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public boolean getBypassesPlayerLimit() { return this.bypassesPlayerLimit; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void serialize(JsonObject object) {
/* 34 */     if (getUser() == null) {
/*    */       return;
/*    */     }
/* 37 */     ((NameAndId)getUser()).appendTo(object);
/* 38 */     object.addProperty("level", Integer.valueOf(this.permissions.level().id()));
/* 39 */     object.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.bypassesPlayerLimit));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\ServerOpListEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */