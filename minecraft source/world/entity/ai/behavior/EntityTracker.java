/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityTracker
/*    */   implements PositionTracker
/*    */ {
/*    */   private final Entity entity;
/*    */   private final boolean trackEyeHeight;
/*    */   private final boolean targetEyeHeight;
/*    */   
/* 18 */   public EntityTracker(Entity entity, boolean trackEyeHeight) { this(entity, trackEyeHeight, false); }
/*    */ 
/*    */   
/*    */   public EntityTracker(Entity entity, boolean trackEyeHeight, boolean targetEyeHeight) {
/* 22 */     this.entity = entity;
/* 23 */     this.trackEyeHeight = trackEyeHeight;
/* 24 */     this.targetEyeHeight = targetEyeHeight;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public Vec3 currentPosition() { return this.trackEyeHeight ? this.entity.position().add(0.0D, this.entity.getEyeHeight(), 0.0D) : this.entity.position(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public BlockPos currentBlockPosition() { return this.targetEyeHeight ? BlockPos.containing(this.entity.getEyePosition()) : this.entity.blockPosition(); }
/*    */ 
/*    */   
/*    */   public boolean isVisibleBy(LivingEntity body) {
/*    */     LivingEntity livingEntity;
/* 39 */     Entity entity1 = this.entity; if (entity1 instanceof LivingEntity) { livingEntity = (LivingEntity)entity1; }
/* 40 */     else { return true; }
/*    */ 
/*    */     
/* 43 */     if (!livingEntity.isAlive()) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     Optional<NearestVisibleLivingEntities> visibleEntities = body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/* 48 */     return (visibleEntities.isPresent() && ((NearestVisibleLivingEntities)visibleEntities.get()).contains(livingEntity));
/*    */   }
/*    */ 
/*    */   
/* 52 */   public Entity getEntity() { return this.entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public String toString() { return "EntityTracker for " + String.valueOf(this.entity); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\EntityTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */