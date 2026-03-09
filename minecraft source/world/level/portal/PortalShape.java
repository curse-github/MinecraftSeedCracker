/*     */ package net.minecraft.world.level.portal;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.NetherPortalBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PortalShape
/*     */ {
/*     */   private static final int MIN_WIDTH = 2;
/*     */   public static final int MAX_WIDTH = 21;
/*     */   private static final int MIN_HEIGHT = 3;
/*     */   public static final int MAX_HEIGHT = 21;
/*  34 */   private static final BlockBehaviour.StatePredicate FRAME = (state, level, pos) -> state.is(Blocks.OBSIDIAN);
/*     */   
/*     */   private static final float SAFE_TRAVEL_MAX_ENTITY_XY = 4.0F;
/*     */   private static final double SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0D;
/*     */   private final Direction.Axis axis;
/*     */   private final Direction rightDir;
/*     */   private final int numPortalBlocks;
/*     */   private final BlockPos bottomLeft;
/*     */   private final int height;
/*     */   private final int width;
/*     */   
/*     */   private PortalShape(Direction.Axis axis, int portalBlockCount, Direction rightDir, BlockPos bottomLeft, int width, int height) {
/*  46 */     this.axis = axis;
/*  47 */     this.numPortalBlocks = portalBlockCount;
/*  48 */     this.rightDir = rightDir;
/*  49 */     this.bottomLeft = bottomLeft;
/*  50 */     this.width = width;
/*  51 */     this.height = height;
/*     */   }
/*     */ 
/*     */   
/*  55 */   public static Optional<PortalShape> findEmptyPortalShape(LevelAccessor level, BlockPos pos, Direction.Axis preferredAxis) { return findPortalShape(level, pos, shape -> (shape.isValid() && shape.numPortalBlocks == 0), preferredAxis); }
/*     */ 
/*     */   
/*     */   public static Optional<PortalShape> findPortalShape(LevelAccessor level, BlockPos pos, Predicate<PortalShape> isValid, Direction.Axis preferredAxis) {
/*  59 */     Optional<PortalShape> firstAxis = Optional.of(findAnyShape(level, pos, preferredAxis)).filter(isValid);
/*  60 */     if (firstAxis.isPresent()) {
/*  61 */       return firstAxis;
/*     */     }
/*     */     
/*  64 */     Direction.Axis otherAxis = (preferredAxis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
/*  65 */     return Optional.of(findAnyShape(level, pos, otherAxis)).filter(isValid);
/*     */   }
/*     */   
/*     */   public static PortalShape findAnyShape(BlockGetter level, BlockPos pos, Direction.Axis axis) {
/*  69 */     Direction rightDir = (axis == Direction.Axis.X) ? Direction.WEST : Direction.SOUTH;
/*     */ 
/*     */ 
/*     */     
/*  73 */     BlockPos bottomLeft = calculateBottomLeft(level, rightDir, pos);
/*  74 */     if (bottomLeft == null) {
/*  75 */       return new PortalShape(axis, 0, rightDir, pos, 0, 0);
/*     */     }
/*     */     
/*  78 */     int width = calculateWidth(level, bottomLeft, rightDir);
/*  79 */     if (width == 0) {
/*  80 */       return new PortalShape(axis, 0, rightDir, bottomLeft, 0, 0);
/*     */     }
/*     */     
/*  83 */     MutableInt portalBlockCountOutput = new MutableInt();
/*  84 */     int height = calculateHeight(level, bottomLeft, rightDir, width, portalBlockCountOutput);
/*  85 */     return new PortalShape(axis, portalBlockCountOutput.intValue(), rightDir, bottomLeft, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   private static BlockPos calculateBottomLeft(BlockGetter level, Direction rightDir, BlockPos pos) {
/*  90 */     int minY = Math.max(level.getMinY(), pos.getY() - 21);
/*  91 */     while (pos.getY() > minY && isEmpty(level.getBlockState(pos.below()))) {
/*  92 */       pos = pos.below();
/*     */     }
/*     */     
/*  95 */     Direction leftDir = rightDir.getOpposite();
/*  96 */     int edge = getDistanceUntilEdgeAboveFrame(level, pos, leftDir) - 1;
/*  97 */     if (edge < 0) {
/*  98 */       return null;
/*     */     }
/* 100 */     return pos.relative(leftDir, edge);
/*     */   }
/*     */   
/*     */   private static int calculateWidth(BlockGetter level, BlockPos bottomLeft, Direction rightDir) {
/* 104 */     int width = getDistanceUntilEdgeAboveFrame(level, bottomLeft, rightDir);
/*     */     
/* 106 */     if (width < 2 || width > 21) {
/* 107 */       return 0;
/*     */     }
/*     */     
/* 110 */     return width;
/*     */   }
/*     */   
/*     */   private static int getDistanceUntilEdgeAboveFrame(BlockGetter level, BlockPos pos, Direction direction) {
/* 114 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 116 */     for (int width = 0; width <= 21; width++) {
/* 117 */       blockPos.set(pos).move(direction, width);
/*     */       
/* 119 */       BlockState blockState = level.getBlockState(blockPos);
/* 120 */       if (!isEmpty(blockState)) {
/* 121 */         if (FRAME.test(blockState, level, blockPos)) {
/* 122 */           return width;
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 128 */       BlockState belowState = level.getBlockState(blockPos.move(Direction.DOWN));
/* 129 */       if (!FRAME.test(belowState, level, blockPos)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 134 */     return 0;
/*     */   }
/*     */   
/*     */   private static int calculateHeight(BlockGetter level, BlockPos bottomLeft, Direction rightDir, int width, MutableInt portalBlockCount) {
/* 138 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 139 */     int height = getDistanceUntilTop(level, bottomLeft, rightDir, pos, width, portalBlockCount);
/*     */     
/* 141 */     if (height < 3 || height > 21 || !hasTopFrame(level, bottomLeft, rightDir, pos, width, height)) {
/* 142 */       return 0;
/*     */     }
/*     */     
/* 145 */     return height;
/*     */   }
/*     */   
/*     */   private static boolean hasTopFrame(BlockGetter level, BlockPos bottomLeft, Direction rightDir, BlockPos.MutableBlockPos pos, int width, int height) {
/* 149 */     for (int i = 0; i < width; i++) {
/* 150 */       BlockPos.MutableBlockPos framePos = pos.set(bottomLeft).move(Direction.UP, height).move(rightDir, i);
/* 151 */       if (!FRAME.test(level.getBlockState(framePos), level, framePos)) {
/* 152 */         return false;
/*     */       }
/*     */     } 
/* 155 */     return true;
/*     */   }
/*     */   
/*     */   private static int getDistanceUntilTop(BlockGetter level, BlockPos bottomLeft, Direction rightDir, BlockPos.MutableBlockPos pos, int width, MutableInt portalBlockCount) {
/* 159 */     for (int height = 0; height < 21; height++) {
/*     */       
/* 161 */       pos.set(bottomLeft).move(Direction.UP, height).move(rightDir, -1);
/* 162 */       if (!FRAME.test(level.getBlockState(pos), level, pos)) {
/* 163 */         return height;
/*     */       }
/*     */ 
/*     */       
/* 167 */       pos.set(bottomLeft).move(Direction.UP, height).move(rightDir, width);
/* 168 */       if (!FRAME.test(level.getBlockState(pos), level, pos)) {
/* 169 */         return height;
/*     */       }
/*     */ 
/*     */       
/* 173 */       for (int i = 0; i < width; i++) {
/* 174 */         pos.set(bottomLeft).move(Direction.UP, height).move(rightDir, i);
/*     */         
/* 176 */         BlockState state = level.getBlockState(pos);
/* 177 */         if (!isEmpty(state)) {
/* 178 */           return height;
/*     */         }
/*     */         
/* 181 */         if (state.is(Blocks.NETHER_PORTAL)) {
/* 182 */           portalBlockCount.increment();
/*     */         }
/*     */       } 
/*     */     } 
/* 186 */     return 21;
/*     */   }
/*     */ 
/*     */   
/* 190 */   private static boolean isEmpty(BlockState state) { return (state.isAir() || state.is(BlockTags.FIRE) || state.is(Blocks.NETHER_PORTAL)); }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public boolean isValid() { return (this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21); }
/*     */ 
/*     */   
/*     */   public void createPortalBlocks(LevelAccessor level) {
/* 198 */     BlockState portalState = (BlockState)Blocks.NETHER_PORTAL.defaultBlockState().setValue(NetherPortalBlock.AXIS, this.axis);
/*     */     
/* 200 */     BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach(pos -> 
/* 201 */         level.setBlock(pos, portalState, 18));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 206 */   public boolean isComplete() { return (isValid() && this.numPortalBlocks == this.width * this.height); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vec3 getRelativePosition(BlockUtil.FoundRectangle largestRectangleAround, Direction.Axis axis, Vec3 position, EntityDimensions dimensions) {
/* 211 */     double relativeUp, relativeRight, width = largestRectangleAround.axis1Size - dimensions.width();
/* 212 */     double height = largestRectangleAround.axis2Size - dimensions.height();
/*     */     
/* 214 */     BlockPos bottomMin = largestRectangleAround.minCorner;
/*     */     
/* 216 */     if (width > 0.0D) {
/* 217 */       relativeUp = bottomMin.get(axis) + dimensions.width() / 2.0D;
/* 218 */       relativeRight = Mth.clamp(Mth.inverseLerp(position.get(axis) - relativeUp, 0.0D, width), 0.0D, 1.0D);
/*     */     } else {
/* 220 */       relativeRight = 0.5D;
/*     */     } 
/*     */     
/* 223 */     if (height > 0.0D) {
/* 224 */       Direction.Axis heightAxis = Direction.Axis.Y;
/* 225 */       relativeUp = Mth.clamp(Mth.inverseLerp(position.get(heightAxis) - bottomMin.get(heightAxis), 0.0D, height), 0.0D, 1.0D);
/*     */     } else {
/* 227 */       relativeUp = 0.0D;
/*     */     } 
/*     */     
/* 230 */     Direction.Axis forwardAxis = (axis == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
/* 231 */     double relativeForward = position.get(forwardAxis) - bottomMin.get(forwardAxis) + 0.5D;
/*     */     
/* 233 */     return new Vec3(relativeRight, relativeUp, relativeForward);
/*     */   }
/*     */   
/*     */   public static Vec3 findCollisionFreePosition(Vec3 bottomCenter, ServerLevel serverLevel, Entity entity, EntityDimensions dimensions) {
/* 237 */     if (dimensions.width() > 4.0F || dimensions.height() > 4.0F) {
/* 238 */       return bottomCenter;
/*     */     }
/*     */     
/* 241 */     double halfHeight = dimensions.height() / 2.0D;
/* 242 */     Vec3 center = bottomCenter.add(0.0D, halfHeight, 0.0D);
/*     */     
/* 244 */     VoxelShape allowedCenters = Shapes.create(AABB.ofSize(center, dimensions.width(), 0.0D, dimensions.width()).expandTowards(0.0D, 1.0D, 0.0D).inflate(1.0E-6D));
/* 245 */     Optional<Vec3> collisionFreePosition = serverLevel.findFreePosition(entity, allowedCenters, center, dimensions.width(), dimensions.height(), dimensions.width());
/* 246 */     Optional<Vec3> collisionFreeBottomCenter = collisionFreePosition.map(vec -> vec.subtract(0.0D, halfHeight, 0.0D));
/*     */     
/* 248 */     return (Vec3)collisionFreeBottomCenter.orElse(bottomCenter);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\portal\PortalShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */