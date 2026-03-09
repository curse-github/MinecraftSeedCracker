/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class EntityCollisionContext
/*    */   implements CollisionContext
/*    */ {
/*    */   private final boolean descending;
/*    */   private final double entityBottom;
/*    */   private final boolean placement;
/*    */   private final ItemStack heldItem;
/*    */   private final boolean alwaysCollideWithFluid;
/*    */   private final Entity entity;
/*    */   
/*    */   protected EntityCollisionContext(boolean descending, boolean placement, double entityBottom, ItemStack heldItem, boolean alwaysCollideWithFluid, Entity entity) {
/* 24 */     this.descending = descending;
/* 25 */     this.placement = placement;
/* 26 */     this.entityBottom = entityBottom;
/* 27 */     this.heldItem = heldItem;
/* 28 */     this.alwaysCollideWithFluid = alwaysCollideWithFluid;
/* 29 */     this.entity = entity;
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   protected EntityCollisionContext(Entity entity, boolean alwaysCollideWithFluid, boolean placement) {
/* 35 */     this(entity
/* 36 */         .isDescending(), placement, entity
/*    */         
/* 38 */         .getY(), 
/* 39 */         (entity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY, alwaysCollideWithFluid, entity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public boolean isHoldingItem(Item item) { return this.heldItem.is(item); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public boolean alwaysCollideWithFluid() { return this.alwaysCollideWithFluid; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canStandOnFluid(FluidState fluidStateAbove, FluidState fluid) {
/* 57 */     Entity entity1 = this.entity; if (entity1 instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity1;
/* 58 */       return (livingEntity.canStandOnFluid(fluid) && !fluidStateAbove.getType().isSame(fluid.getType())); }
/*    */     
/* 60 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public VoxelShape getCollisionShape(BlockState state, CollisionGetter collisionGetter, BlockPos pos) { return state.getCollisionShape(collisionGetter, pos, this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public boolean isDescending() { return this.descending; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 75 */   public boolean isAbove(VoxelShape shape, BlockPos pos, boolean defaultValue) { return (this.entityBottom > pos.getY() + shape.max(Direction.Axis.Y) - 9.999999747378752E-6D); }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public Entity getEntity() { return this.entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   public boolean isPlacement() { return this.placement; }
/*    */   
/*    */   protected static class Empty
/*    */     extends EntityCollisionContext {
/* 88 */     protected static final CollisionContext WITHOUT_FLUID_COLLISIONS = new Empty(false);
/* 89 */     protected static final CollisionContext WITH_FLUID_COLLISIONS = new Empty(true);
/*    */ 
/*    */     
/* 92 */     public Empty(boolean alwaysCollideWithFluid) { super(false, false, -1.7976931348623157E308D, ItemStack.EMPTY, alwaysCollideWithFluid, null); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 97 */     public boolean isAbove(VoxelShape shape, BlockPos pos, boolean defaultValue) { return defaultValue; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\EntityCollisionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */