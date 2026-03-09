/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class EntityHitResult
/*    */   extends HitResult {
/*    */   private final Entity entity;
/*    */   
/*  9 */   public EntityHitResult(Entity entity) { this(entity, entity.position()); }
/*    */ 
/*    */   
/*    */   public EntityHitResult(Entity entity, Vec3 location) {
/* 13 */     super(location);
/*    */     
/* 15 */     this.entity = entity;
/*    */   }
/*    */ 
/*    */   
/* 19 */   public Entity getEntity() { return this.entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public HitResult.Type getType() { return HitResult.Type.ENTITY; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\EntityHitResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */