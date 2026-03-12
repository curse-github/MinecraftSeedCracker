/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LevelBasedPermissionSet
/*    */   extends PermissionSet
/*    */ {
/*    */   @Deprecated
/* 14 */   public static final LevelBasedPermissionSet ALL = create(PermissionLevel.ALL);
/* 15 */   public static final LevelBasedPermissionSet MODERATOR = create(PermissionLevel.MODERATORS);
/* 16 */   public static final LevelBasedPermissionSet GAMEMASTER = create(PermissionLevel.GAMEMASTERS);
/* 17 */   public static final LevelBasedPermissionSet ADMIN = create(PermissionLevel.ADMINS);
/* 18 */   public static final LevelBasedPermissionSet OWNER = create(PermissionLevel.OWNERS);
/*    */ 
/*    */   
/*    */   PermissionLevel level();
/*    */   
/*    */   default boolean hasPermission(Permission permission) {
/* 24 */     if (permission instanceof Permission.HasCommandLevel) { Permission.HasCommandLevel levelCheck = (Permission.HasCommandLevel)permission;
/* 25 */       return level().isEqualOrHigherThan(levelCheck.level()); }
/*    */ 
/*    */     
/* 28 */     if (permission.equals(Permissions.COMMANDS_ENTITY_SELECTORS)) {
/* 29 */       return level().isEqualOrHigherThan(PermissionLevel.GAMEMASTERS);
/*    */     }
/* 31 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   default PermissionSet union(PermissionSet other) {
/* 36 */     if (other instanceof LevelBasedPermissionSet) { LevelBasedPermissionSet otherSet = (LevelBasedPermissionSet)other;
/* 37 */       if (level().isEqualOrHigherThan(otherSet.level())) {
/* 38 */         return otherSet;
/*    */       }
/* 40 */       return this; }
/*    */ 
/*    */     
/* 43 */     return super.union(other);
/*    */   }
/*    */   
/*    */   static LevelBasedPermissionSet forLevel(PermissionLevel level) {
/* 47 */     switch (level) { default: throw new MatchException(null, null);case ALL: case MODERATORS: case GAMEMASTERS: case ADMINS: case OWNERS: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 52 */       OWNER;
/*    */   }
/*    */ 
/*    */   
/*    */   private static LevelBasedPermissionSet create(final PermissionLevel level) {
/* 57 */     return new LevelBasedPermissionSet()
/*    */       {
/*    */         public PermissionLevel level() {
/* 60 */           return level;
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 65 */         public String toString() { return "permission level: " + level.name(); }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\LevelBasedPermissionSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */