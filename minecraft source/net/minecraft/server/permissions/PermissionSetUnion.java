/*    */ package net.minecraft.server.permissions;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceSet;
/*    */ 
/*    */ public class PermissionSetUnion implements PermissionSet {
/*    */   PermissionSetUnion(PermissionSet first, PermissionSet second) {
/*  8 */     this.permissions = new ReferenceArraySet();
/*    */ 
/*    */     
/* 11 */     this.permissions.add(first);
/* 12 */     this.permissions.add(second);
/* 13 */     ensureNoUnionsWithinUnions();
/*    */   } private final ReferenceSet<PermissionSet> permissions;
/*    */   private PermissionSetUnion(ReferenceSet<PermissionSet> oldPermissions, PermissionSet other) {
/*    */     this.permissions = new ReferenceArraySet();
/* 17 */     this.permissions.addAll(oldPermissions);
/* 18 */     this.permissions.add(other);
/* 19 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */   private PermissionSetUnion(ReferenceSet<PermissionSet> oldPermissions, ReferenceSet<PermissionSet> other) {
/*    */     this.permissions = new ReferenceArraySet();
/* 23 */     this.permissions.addAll(oldPermissions);
/* 24 */     this.permissions.addAll(other);
/* 25 */     ensureNoUnionsWithinUnions();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasPermission(Permission permission) {
/* 30 */     for (ObjectIterator objectIterator = this.permissions.iterator(); objectIterator.hasNext(); ) { PermissionSet set = (PermissionSet)objectIterator.next();
/* 31 */       if (set.hasPermission(permission)) {
/* 32 */         return true;
/*    */       } }
/*    */     
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public PermissionSet union(PermissionSet other) {
/* 40 */     if (other instanceof PermissionSetUnion) { PermissionSetUnion otherUnion = (PermissionSetUnion)other;
/* 41 */       return new PermissionSetUnion(this.permissions, otherUnion.permissions); }
/*    */     
/* 43 */     return new PermissionSetUnion(this.permissions, other);
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 48 */   public ReferenceSet<PermissionSet> getPermissions() { return new ReferenceArraySet(this.permissions); }
/*    */ 
/*    */   
/*    */   private void ensureNoUnionsWithinUnions() {
/* 52 */     for (ObjectIterator objectIterator = this.permissions.iterator(); objectIterator.hasNext(); ) { PermissionSet set = (PermissionSet)objectIterator.next();
/* 53 */       if (set instanceof PermissionSetUnion)
/* 54 */         throw new IllegalArgumentException("Cannot have PermissionSetUnion within another PermissionSetUnion");  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\permissions\PermissionSetUnion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */