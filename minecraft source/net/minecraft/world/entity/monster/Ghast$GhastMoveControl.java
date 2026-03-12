/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GhastMoveControl
/*     */   extends MoveControl
/*     */ {
/*     */   private final Mob ghast;
/*     */   private int floatDuration;
/*     */   private final boolean careful;
/*     */   private final BooleanSupplier shouldBeStopped;
/*     */   
/*     */   public GhastMoveControl(Mob ghast, boolean careful, BooleanSupplier shouldBeStopped) {
/* 210 */     super(ghast);
/* 211 */     this.ghast = ghast;
/* 212 */     this.careful = careful;
/* 213 */     this.shouldBeStopped = shouldBeStopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 218 */     if (this.shouldBeStopped.getAsBoolean()) {
/* 219 */       this.operation = MoveControl.Operation.WAIT;
/* 220 */       this.ghast.stopInPlace();
/*     */     } 
/*     */     
/* 223 */     if (this.operation != MoveControl.Operation.MOVE_TO) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 228 */     if (this.floatDuration-- <= 0) {
/* 229 */       this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 234 */       Vec3 travel = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
/*     */ 
/*     */       
/* 237 */       if (canReach(travel)) {
/* 238 */         this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(travel.normalize().scale(this.ghast.getAttributeValue(Attributes.FLYING_SPEED) * 5.0D / 3.0D)));
/*     */       } else {
/* 240 */         this.operation = MoveControl.Operation.WAIT;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canReach(Vec3 travel) {
/* 246 */     AABB aabb = this.ghast.getBoundingBox();
/* 247 */     AABB aabbAtDestination = aabb.move(travel);
/* 248 */     if (this.careful) {
/* 249 */       for (BlockPos pos : BlockPos.betweenClosed(aabbAtDestination.inflate(1.0D))) {
/* 250 */         if (!blockTraversalPossible(this.ghast.level(), null, null, pos, false, false)) {
/* 251 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 255 */     boolean isInWater = this.ghast.isInWater();
/* 256 */     boolean isInLava = this.ghast.isInLava();
/* 257 */     Vec3 start = this.ghast.position();
/* 258 */     Vec3 end = start.add(travel);
/*     */     
/* 260 */     return BlockGetter.forEachBlockIntersectedBetween(start, end, aabbAtDestination, (blockPos, i) -> {
/* 261 */           if (aabb.intersects(blockPos)) {
/* 262 */             return true;
/*     */           }
/* 264 */           return blockTraversalPossible(this.ghast.level(), start, end, blockPos, isInWater, isInLava);
/*     */         });
/*     */   }
/*     */   
/*     */   private boolean blockTraversalPossible(BlockGetter level, Vec3 start, Vec3 end, BlockPos pos, boolean canPathThroughWater, boolean canPathThroughLava) {
/* 269 */     BlockState state = level.getBlockState(pos);
/* 270 */     if (state.isAir()) {
/* 271 */       return true;
/*     */     }
/* 273 */     boolean preciseBlockCollisions = (start != null && end != null);
/* 274 */     boolean pathNoCollisions = preciseBlockCollisions ? (!this.ghast.collidedWithShapeMovingFrom(start, end, state.getCollisionShape(level, pos).move(new Vec3(pos)).toAabbs())) : state.getCollisionShape(level, pos).isEmpty();
/* 275 */     if (!this.careful) {
/* 276 */       return pathNoCollisions;
/*     */     }
/* 278 */     if (state.is(BlockTags.HAPPY_GHAST_AVOIDS)) {
/* 279 */       return false;
/*     */     }
/* 281 */     FluidState fluidState = level.getFluidState(pos);
/* 282 */     if (!fluidState.isEmpty() && (!preciseBlockCollisions || this.ghast.collidedWithFluid(fluidState, pos, start, end))) {
/* 283 */       if (fluidState.is(FluidTags.WATER)) {
/* 284 */         return canPathThroughWater;
/*     */       }
/* 286 */       if (fluidState.is(FluidTags.LAVA)) {
/* 287 */         return canPathThroughLava;
/*     */       }
/*     */     } 
/* 290 */     return pathNoCollisions;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Ghast$GhastMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */