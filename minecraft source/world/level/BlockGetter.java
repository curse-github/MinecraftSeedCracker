/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
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
/*     */ public interface BlockGetter
/*     */   extends LevelHeightAccessor
/*     */ {
/*     */   default <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
/*  39 */     BlockEntity blockEntity = getBlockEntity(pos);
/*  40 */     if (blockEntity == null || blockEntity.getType() != type) {
/*  41 */       return Optional.empty();
/*     */     }
/*  43 */     return Optional.of(blockEntity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   default int getLightEmission(BlockPos pos) { return getBlockState(pos).getLightEmission(); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   default Stream<BlockState> getBlockStates(AABB box) { return BlockPos.betweenClosedStream(box).map(this::getBlockState); }
/*     */ 
/*     */   
/*     */   default BlockHitResult isBlockInLine(ClipBlockStateContext c) {
/*  59 */     return (BlockHitResult)traverseBlocks(c.getFrom(), c.getTo(), c, (context, pos) -> {
/*  60 */           BlockState blockState = getBlockState(pos);
/*  61 */           Vec3 delta = context.getFrom().subtract(context.getTo());
/*     */           
/*  63 */           return context.isTargetBlock().test(blockState) ? new BlockHitResult(context.getTo(), Direction.getApproximateNearest(delta.x, delta.y, delta.z), BlockPos.containing(context.getTo()), false) : null;
/*     */         }context -> {
/*  65 */           Vec3 delta = context.getFrom().subtract(context.getTo());
/*  66 */           return BlockHitResult.miss(context.getTo(), Direction.getApproximateNearest(delta.x, delta.y, delta.z), BlockPos.containing(context.getTo()));
/*     */         });
/*     */   }
/*     */   
/*     */   default BlockHitResult clip(ClipContext c) {
/*  71 */     return (BlockHitResult)traverseBlocks(c.getFrom(), c.getTo(), c, (context, pos) -> {
/*  72 */           BlockState blockState = getBlockState(pos);
/*  73 */           FluidState fluidState = getFluidState(pos);
/*     */           
/*  75 */           Vec3 from = context.getFrom();
/*  76 */           Vec3 to = context.getTo();
/*     */           
/*  78 */           VoxelShape blockShape = context.getBlockShape(blockState, this, pos);
/*  79 */           BlockHitResult blockResult = clipWithInteractionOverride(from, to, pos, blockShape, blockState);
/*     */           
/*  81 */           VoxelShape fluidShape = context.getFluidShape(fluidState, this, pos);
/*  82 */           BlockHitResult liquidResult = fluidShape.clip(from, to, pos);
/*     */           
/*  84 */           double blockDistanceSquared = (blockResult == null) ? Double.MAX_VALUE : context.getFrom().distanceToSqr(blockResult.getLocation());
/*  85 */           double liquidDistanceSquared = (liquidResult == null) ? Double.MAX_VALUE : context.getFrom().distanceToSqr(liquidResult.getLocation());
/*     */           
/*  87 */           return (blockDistanceSquared <= liquidDistanceSquared) ? blockResult : liquidResult;
/*     */         }context -> {
/*  89 */           Vec3 delta = context.getFrom().subtract(context.getTo());
/*  90 */           return BlockHitResult.miss(context.getTo(), Direction.getApproximateNearest(delta.x, delta.y, delta.z), BlockPos.containing(context.getTo()));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   default BlockHitResult clipWithInteractionOverride(Vec3 from, Vec3 to, BlockPos pos, VoxelShape blockShape, BlockState blockState) {
/*  96 */     BlockHitResult result = blockShape.clip(from, to, pos);
/*  97 */     if (result != null) {
/*     */       
/*  99 */       BlockHitResult hitOverride = blockState.getInteractionShape(this, pos).clip(from, to, pos);
/* 100 */       if (hitOverride != null && hitOverride.getLocation().subtract(from).lengthSqr() < result.getLocation().subtract(from).lengthSqr()) {
/* 101 */         return result.withDirection(hitOverride.getDirection());
/*     */       }
/*     */     } 
/* 104 */     return result;
/*     */   }
/*     */   
/*     */   default double getBlockFloorHeight(VoxelShape blockShape, Supplier<VoxelShape> belowBlockShape) {
/* 108 */     if (!blockShape.isEmpty()) {
/* 109 */       return blockShape.max(Direction.Axis.Y);
/*     */     }
/*     */ 
/*     */     
/* 113 */     double belowFloor = ((VoxelShape)belowBlockShape.get()).max(Direction.Axis.Y);
/* 114 */     if (belowFloor >= 1.0D) {
/* 115 */       return belowFloor - 1.0D;
/*     */     }
/*     */     
/* 118 */     return Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   default double getBlockFloorHeight(BlockPos pos) {
/* 122 */     return getBlockFloorHeight(getBlockState(pos).getCollisionShape(this, pos), () -> {
/* 123 */           BlockPos below = pos.below();
/* 124 */           return getBlockState(below).getCollisionShape(this, below);
/*     */         });
/*     */   }
/*     */   
/*     */   static <T, C> T traverseBlocks(Vec3 from, Vec3 to, C context, BiFunction<C, BlockPos, T> consumer, Function<C, T> missFactory) {
/* 129 */     if (from.equals(to)) {
/* 130 */       return (T)missFactory.apply(context);
/*     */     }
/*     */ 
/*     */     
/* 134 */     double toX = Mth.lerp(-1.0E-7D, to.x, from.x);
/* 135 */     double toY = Mth.lerp(-1.0E-7D, to.y, from.y);
/* 136 */     double toZ = Mth.lerp(-1.0E-7D, to.z, from.z);
/*     */     
/* 138 */     double fromX = Mth.lerp(-1.0E-7D, from.x, to.x);
/* 139 */     double fromY = Mth.lerp(-1.0E-7D, from.y, to.y);
/* 140 */     double fromZ = Mth.lerp(-1.0E-7D, from.z, to.z);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     int currentBlockX = Mth.floor(fromX);
/* 146 */     int currentBlockY = Mth.floor(fromY);
/* 147 */     int currentBlockZ = Mth.floor(fromZ);
/*     */     
/* 149 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(currentBlockX, currentBlockY, currentBlockZ);
/* 150 */     T first = (T)consumer.apply(context, pos);
/* 151 */     if (first != null) {
/* 152 */       return first;
/*     */     }
/*     */     
/* 155 */     double dx = toX - fromX;
/* 156 */     double dy = toY - fromY;
/* 157 */     double dz = toZ - fromZ;
/*     */     
/* 159 */     int signX = Mth.sign(dx);
/* 160 */     int signY = Mth.sign(dy);
/* 161 */     int signZ = Mth.sign(dz);
/*     */     
/* 163 */     double tDeltaX = (signX == 0) ? Double.MAX_VALUE : (signX / dx);
/* 164 */     double tDeltaY = (signY == 0) ? Double.MAX_VALUE : (signY / dy);
/* 165 */     double tDeltaZ = (signZ == 0) ? Double.MAX_VALUE : (signZ / dz);
/*     */     
/* 167 */     double tX = tDeltaX * ((signX > 0) ? (1.0D - Mth.frac(fromX)) : Mth.frac(fromX));
/* 168 */     double tY = tDeltaY * ((signY > 0) ? (1.0D - Mth.frac(fromY)) : Mth.frac(fromY));
/* 169 */     double tZ = tDeltaZ * ((signZ > 0) ? (1.0D - Mth.frac(fromZ)) : Mth.frac(fromZ));
/*     */     
/* 171 */     while (tX <= 1.0D || tY <= 1.0D || tZ <= 1.0D) {
/* 172 */       if (tX < tY) {
/* 173 */         if (tX < tZ) {
/* 174 */           currentBlockX += signX;
/* 175 */           tX += tDeltaX;
/*     */         } else {
/* 177 */           currentBlockZ += signZ;
/* 178 */           tZ += tDeltaZ;
/*     */         }
/*     */       
/* 181 */       } else if (tY < tZ) {
/* 182 */         currentBlockY += signY;
/* 183 */         tY += tDeltaY;
/*     */       } else {
/* 185 */         currentBlockZ += signZ;
/* 186 */         tZ += tDeltaZ;
/*     */       } 
/*     */ 
/*     */       
/* 190 */       T result = (T)consumer.apply(context, pos.set(currentBlockX, currentBlockY, currentBlockZ));
/* 191 */       if (result != null) {
/* 192 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 196 */     return (T)missFactory.apply(context);
/*     */   }
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
/*     */   static boolean forEachBlockIntersectedBetween(Vec3 from, Vec3 to, AABB aabbAtTarget, BlockStepVisitor visitor) {
/* 212 */     Vec3 travel = to.subtract(from);
/*     */     
/* 214 */     if (travel.lengthSqr() < Mth.square(1.0E-5F)) {
/*     */       
/* 216 */       for (BlockPos blockPos : BlockPos.betweenClosed(aabbAtTarget)) {
/* 217 */         if (!visitor.visit(blockPos, 0)) {
/* 218 */           return false;
/*     */         }
/*     */       } 
/* 221 */       return true;
/*     */     } 
/*     */     
/* 224 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/*     */     
/* 226 */     for (BlockPos blockPos : BlockPos.betweenCornersInDirection(aabbAtTarget.move(travel.scale(-1.0D)), travel)) {
/* 227 */       if (!visitor.visit(blockPos, 0)) {
/* 228 */         return false;
/*     */       }
/* 230 */       longOpenHashSet.add(blockPos.asLong());
/*     */     } 
/*     */ 
/*     */     
/* 234 */     int iterations = addCollisionsAlongTravel(longOpenHashSet, travel, aabbAtTarget, visitor);
/* 235 */     if (iterations < 0)
/*     */     {
/* 237 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 241 */     for (BlockPos blockPos : BlockPos.betweenCornersInDirection(aabbAtTarget, travel)) {
/* 242 */       if (longOpenHashSet.add(blockPos.asLong()) && !visitor.visit(blockPos, iterations + 1)) {
/* 243 */         return false;
/*     */       }
/*     */     } 
/* 246 */     return true;
/*     */   }
/*     */   
/*     */   private static int addCollisionsAlongTravel(LongSet visitedBlocks, Vec3 deltaMove, AABB aabbAtTarget, BlockStepVisitor visitor) {
/* 250 */     double boxSizeX = aabbAtTarget.getXsize();
/* 251 */     double boxSizeY = aabbAtTarget.getYsize();
/* 252 */     double boxSizeZ = aabbAtTarget.getZsize();
/*     */ 
/*     */ 
/*     */     
/* 256 */     Vec3i cornerDir = getFurthestCorner(deltaMove);
/* 257 */     Vec3 toCenter = aabbAtTarget.getCenter();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     Vec3 toCorner = new Vec3(toCenter.x() + boxSizeX * 0.5D * cornerDir.getX(), toCenter.y() + boxSizeY * 0.5D * cornerDir.getY(), toCenter.z() + boxSizeZ * 0.5D * cornerDir.getZ());
/*     */ 
/*     */     
/* 265 */     Vec3 fromCorner = toCorner.subtract(deltaMove);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     int cornerVisitedBlockX = Mth.floor(fromCorner.x);
/* 271 */     int cornerVisitedBlockY = Mth.floor(fromCorner.y);
/* 272 */     int cornerVisitedBlockZ = Mth.floor(fromCorner.z);
/*     */     
/* 274 */     int signX = Mth.sign(deltaMove.x);
/* 275 */     int signY = Mth.sign(deltaMove.y);
/* 276 */     int signZ = Mth.sign(deltaMove.z);
/*     */     
/* 278 */     double tDeltaX = (signX == 0) ? Double.MAX_VALUE : (signX / deltaMove.x);
/* 279 */     double tDeltaY = (signY == 0) ? Double.MAX_VALUE : (signY / deltaMove.y);
/* 280 */     double tDeltaZ = (signZ == 0) ? Double.MAX_VALUE : (signZ / deltaMove.z);
/*     */     
/* 282 */     double tX = tDeltaX * ((signX > 0) ? (1.0D - Mth.frac(fromCorner.x)) : Mth.frac(fromCorner.x));
/* 283 */     double tY = tDeltaY * ((signY > 0) ? (1.0D - Mth.frac(fromCorner.y)) : Mth.frac(fromCorner.y));
/* 284 */     double tZ = tDeltaZ * ((signZ > 0) ? (1.0D - Mth.frac(fromCorner.z)) : Mth.frac(fromCorner.z));
/* 285 */     int iterations = 0;
/*     */     
/* 287 */     while (tX <= 1.0D || tY <= 1.0D || tZ <= 1.0D) {
/* 288 */       if (tX < tY) {
/* 289 */         if (tX < tZ) {
/* 290 */           cornerVisitedBlockX += signX;
/* 291 */           tX += tDeltaX;
/*     */         } else {
/* 293 */           cornerVisitedBlockZ += signZ;
/* 294 */           tZ += tDeltaZ;
/*     */         }
/*     */       
/* 297 */       } else if (tY < tZ) {
/* 298 */         cornerVisitedBlockY += signY;
/* 299 */         tY += tDeltaY;
/*     */       } else {
/* 301 */         cornerVisitedBlockZ += signZ;
/* 302 */         tZ += tDeltaZ;
/*     */       } 
/*     */ 
/*     */       
/* 306 */       Optional<Vec3> hitPointOpt = AABB.clip(cornerVisitedBlockX, cornerVisitedBlockY, cornerVisitedBlockZ, (cornerVisitedBlockX + 1), (cornerVisitedBlockY + 1), (cornerVisitedBlockZ + 1), fromCorner, toCorner);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 311 */       if (hitPointOpt.isEmpty()) {
/*     */         continue;
/*     */       }
/*     */       
/* 315 */       iterations++;
/*     */ 
/*     */       
/* 318 */       Vec3 hitPoint = (Vec3)hitPointOpt.get();
/* 319 */       double cornerHitX = Mth.clamp(hitPoint.x, cornerVisitedBlockX + 9.999999747378752E-6D, cornerVisitedBlockX + 1.0D - 9.999999747378752E-6D);
/* 320 */       double cornerHitY = Mth.clamp(hitPoint.y, cornerVisitedBlockY + 9.999999747378752E-6D, cornerVisitedBlockY + 1.0D - 9.999999747378752E-6D);
/* 321 */       double cornerHitZ = Mth.clamp(hitPoint.z, cornerVisitedBlockZ + 9.999999747378752E-6D, cornerVisitedBlockZ + 1.0D - 9.999999747378752E-6D);
/*     */       
/* 323 */       int oppositeCornerX = Mth.floor(cornerHitX - boxSizeX * cornerDir.getX());
/* 324 */       int oppositeCornerY = Mth.floor(cornerHitY - boxSizeY * cornerDir.getY());
/* 325 */       int oppositeCornerZ = Mth.floor(cornerHitZ - boxSizeZ * cornerDir.getZ());
/*     */       
/* 327 */       int currentIteration = iterations;
/* 328 */       for (BlockPos pos : BlockPos.betweenCornersInDirection(cornerVisitedBlockX, cornerVisitedBlockY, cornerVisitedBlockZ, oppositeCornerX, oppositeCornerY, oppositeCornerZ, deltaMove)) {
/*     */ 
/*     */ 
/*     */         
/* 332 */         if (visitedBlocks.add(pos.asLong()) && !visitor.visit(pos, currentIteration)) {
/* 333 */           return -1;
/*     */         }
/*     */       } 
/*     */     } 
/* 337 */     return iterations;
/*     */   }
/*     */   
/*     */   private static Vec3i getFurthestCorner(Vec3 direction) {
/* 341 */     double xDot = Math.abs(Vec3.X_AXIS.dot(direction));
/* 342 */     double yDot = Math.abs(Vec3.Y_AXIS.dot(direction));
/* 343 */     double zDot = Math.abs(Vec3.Z_AXIS.dot(direction));
/*     */     
/* 345 */     int xSign = (direction.x >= 0.0D) ? 1 : -1;
/* 346 */     int ySign = (direction.y >= 0.0D) ? 1 : -1;
/* 347 */     int zSign = (direction.z >= 0.0D) ? 1 : -1;
/*     */     
/* 349 */     if (xDot <= yDot && xDot <= zDot)
/* 350 */       return new Vec3i(-xSign, -zSign, ySign); 
/* 351 */     if (yDot <= zDot) {
/* 352 */       return new Vec3i(zSign, -ySign, -xSign);
/*     */     }
/* 354 */     return new Vec3i(-ySign, xSign, -zSign);
/*     */   }
/*     */   
/*     */   BlockEntity getBlockEntity(BlockPos paramBlockPos);
/*     */   
/*     */   BlockState getBlockState(BlockPos paramBlockPos);
/*     */   
/*     */   FluidState getFluidState(BlockPos paramBlockPos);
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BlockStepVisitor {
/*     */     boolean visit(BlockPos param1BlockPos, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\BlockGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */