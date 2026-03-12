/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ public abstract class HitResult {
/*    */   protected final Vec3 location;
/*    */   
/*    */   public enum Type {
/*  7 */     MISS, BLOCK, ENTITY;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   protected HitResult(Vec3 location) { this.location = location; }
/*    */ 
/*    */   
/*    */   public double distanceTo(Entity entity) {
/* 17 */     double xd = this.location.x - entity.getX();
/* 18 */     double yd = this.location.y - entity.getY();
/* 19 */     double zd = this.location.z - entity.getZ();
/* 20 */     return xd * xd + yd * yd + zd * zd;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract Type getType();
/*    */   
/* 26 */   public Vec3 getLocation() { return this.location; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\HitResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */