/*   */ package net.minecraft.world.level.entity;
/*   */ 
/*   */ import net.minecraft.world.entity.Entity;
/*   */ 
/*   */ public interface EntityInLevelCallback {
/* 6 */   public static final EntityInLevelCallback NULL = new EntityInLevelCallback() {
/*   */       public void onMove() {}
/*   */       
/*   */       public void onRemove(Entity.RemovalReason reason) {}
/*   */     };
/*   */   
/*   */   void onMove();
/*   */   
/*   */   void onRemove(Entity.RemovalReason paramRemovalReason);
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\EntityInLevelCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */