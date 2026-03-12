/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CollisionGetter
/*     */   extends BlockGetter
/*     */ {
/*  28 */   default boolean isUnobstructed(Entity source, VoxelShape shape) { return true; }
/*     */ 
/*     */   
/*     */   default boolean isUnobstructed(BlockState state, BlockPos pos, CollisionContext context) {
/*  32 */     VoxelShape shape = state.getCollisionShape(this, pos, context);
/*  33 */     return (shape.isEmpty() || isUnobstructed(null, shape.move(pos)));
/*     */   }
/*     */ 
/*     */   
/*  37 */   default boolean isUnobstructed(Entity ignore) { return isUnobstructed(ignore, Shapes.create(ignore.getBoundingBox())); }
/*     */ 
/*     */ 
/*     */   
/*  41 */   default boolean noCollision(AABB aabb) { return noCollision(null, aabb); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   default boolean noCollision(Entity source) { return noCollision(source, source.getBoundingBox()); }
/*     */ 
/*     */ 
/*     */   
/*  49 */   default boolean noCollision(Entity entity, AABB aabb) { return noCollision(entity, aabb, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   default boolean noCollision(Entity entity, AABB aabb, boolean alwaysCollideWithFluids) { return (noBlockCollision(entity, aabb, alwaysCollideWithFluids) && noEntityCollision(entity, aabb) && noBorderCollision(entity, aabb)); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   default boolean noBlockCollision(Entity entity, AABB aabb) { return noBlockCollision(entity, aabb, false); }
/*     */ 
/*     */   
/*     */   default boolean noBlockCollision(Entity entity, AABB aabb, boolean alwaysCollideWithFluids) {
/*  62 */     Iterable<VoxelShape> blockCollisions = alwaysCollideWithFluids ? getBlockAndLiquidCollisions(entity, aabb) : getBlockCollisions(entity, aabb);
/*  63 */     for (VoxelShape blockCollision : blockCollisions) {
/*  64 */       if (!blockCollision.isEmpty()) {
/*  65 */         return false;
/*     */       }
/*     */     } 
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  72 */   default boolean noEntityCollision(Entity entity, AABB aabb) { return getEntityCollisions(entity, aabb).isEmpty(); }
/*     */ 
/*     */   
/*     */   default boolean noBorderCollision(Entity entity, AABB aabb) {
/*  76 */     if (entity != null) {
/*  77 */       VoxelShape borderShape = borderCollision(entity, aabb);
/*  78 */       return (borderShape == null || !Shapes.joinIsNotEmpty(borderShape, Shapes.create(aabb), BooleanOp.AND));
/*     */     } 
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default Iterable<VoxelShape> getCollisions(Entity source, AABB box) {
/*  86 */     List<VoxelShape> entityCollisions = getEntityCollisions(source, box);
/*  87 */     Iterable<VoxelShape> blockCollisions = getBlockCollisions(source, box);
/*  88 */     return entityCollisions.isEmpty() ? blockCollisions : Iterables.concat(entityCollisions, blockCollisions);
/*     */   }
/*     */   
/*     */   default Iterable<VoxelShape> getPreMoveCollisions(Entity source, AABB box, Vec3 oldPos) {
/*  92 */     List<VoxelShape> entityCollisions = getEntityCollisions(source, box);
/*  93 */     Iterable<VoxelShape> blockCollisions = getBlockCollisionsFromContext(CollisionContext.withPosition(source, oldPos.y), box);
/*  94 */     return entityCollisions.isEmpty() ? blockCollisions : Iterables.concat(entityCollisions, blockCollisions);
/*     */   }
/*     */ 
/*     */   
/*  98 */   default Iterable<VoxelShape> getBlockCollisions(Entity source, AABB box) { return getBlockCollisionsFromContext((source == null) ? CollisionContext.empty() : CollisionContext.of(source), box); }
/*     */ 
/*     */ 
/*     */   
/* 102 */   default Iterable<VoxelShape> getBlockAndLiquidCollisions(Entity source, AABB box) { return getBlockCollisionsFromContext((source == null) ? CollisionContext.emptyWithFluidCollisions() : CollisionContext.of(source, true), box); }
/*     */ 
/*     */   
/*     */   private Iterable<VoxelShape> getBlockCollisionsFromContext(CollisionContext source, AABB box) {
/* 106 */     return () -> new BlockCollisions(this, source, box, false, ());
/*     */   }
/*     */   
/*     */   private VoxelShape borderCollision(Entity source, AABB box) {
/* 110 */     WorldBorder worldBorder = getWorldBorder();
/* 111 */     return worldBorder.isInsideCloseToBorder(source, box) ? worldBorder.getCollisionShape() : null;
/*     */   }
/*     */   
/*     */   default BlockHitResult clipIncludingBorder(ClipContext c) {
/* 115 */     BlockHitResult hitResult = clip(c);
/* 116 */     WorldBorder worldBorder = getWorldBorder();
/* 117 */     if (worldBorder.isWithinBounds(c.getFrom()) && !worldBorder.isWithinBounds(hitResult.getLocation())) {
/* 118 */       Vec3 delta = hitResult.getLocation().subtract(c.getFrom());
/* 119 */       Direction deltaDirection = Direction.getApproximateNearest(delta.x, delta.y, delta.z);
/* 120 */       Vec3 hit = worldBorder.clampVec3ToBound(hitResult.getLocation());
/* 121 */       return new BlockHitResult(hit, deltaDirection, BlockPos.containing(hit), false, true);
/*     */     } 
/* 123 */     return hitResult;
/*     */   }
/*     */   
/*     */   default boolean collidesWithSuffocatingBlock(Entity source, AABB box) {
/* 127 */     BlockCollisions<VoxelShape> blockCollisions = new BlockCollisions<VoxelShape>(this, source, box, true, (p, shape) -> shape);
/* 128 */     while (blockCollisions.hasNext()) {
/* 129 */       if (!((VoxelShape)blockCollisions.next()).isEmpty()) {
/* 130 */         return true;
/*     */       }
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   default Optional<BlockPos> findSupportingBlock(Entity source, AABB box) {
/* 137 */     BlockPos mainSupport = null;
/* 138 */     double mainSupportDistance = Double.MAX_VALUE;
/* 139 */     BlockCollisions<BlockPos> blockCollisions = new BlockCollisions<BlockPos>(this, source, box, false, (pos, shape) -> pos);
/* 140 */     while (blockCollisions.hasNext()) {
/* 141 */       BlockPos pos = (BlockPos)blockCollisions.next();
/* 142 */       double distance = pos.distToCenterSqr(source.position());
/* 143 */       if (distance < mainSupportDistance || (distance == mainSupportDistance && (mainSupport == null || mainSupport.compareTo(pos) < 0))) {
/* 144 */         mainSupport = pos.immutable();
/* 145 */         mainSupportDistance = distance;
/*     */       } 
/*     */     } 
/* 148 */     return Optional.ofNullable(mainSupport);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Optional<Vec3> findFreePosition(Entity source, VoxelShape allowedCenters, Vec3 preferredCenter, double sizeX, double sizeY, double sizeZ) {
/* 158 */     if (allowedCenters.isEmpty()) {
/* 159 */       return Optional.empty();
/*     */     }
/*     */     
/* 162 */     AABB searchArea = allowedCenters.bounds().inflate(sizeX, sizeY, sizeZ);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     VoxelShape expandedCollisions = (VoxelShape)StreamSupport.stream(getBlockCollisions(source, searchArea).spliterator(), false).filter(shape -> (getWorldBorder() == null || getWorldBorder().isWithinBounds(shape.bounds()))).flatMap(shape -> shape.toAabbs().stream()).map(aabb -> aabb.inflate(sizeX / 2.0D, sizeY / 2.0D, sizeZ / 2.0D)).map(Shapes::create).reduce(Shapes.empty(), Shapes::or);
/*     */ 
/*     */     
/* 171 */     VoxelShape freeSpots = Shapes.join(allowedCenters, expandedCollisions, BooleanOp.ONLY_FIRST);
/*     */     
/* 173 */     return freeSpots.closestPointTo(preferredCenter);
/*     */   }
/*     */   
/*     */   WorldBorder getWorldBorder();
/*     */   
/*     */   BlockGetter getChunkForCollisions(int paramInt1, int paramInt2);
/*     */   
/*     */   List<VoxelShape> getEntityCollisions(Entity paramEntity, AABB paramAABB);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\CollisionGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */