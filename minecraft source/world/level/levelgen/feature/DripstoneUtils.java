/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PointedDripstoneBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.DripstoneThickness;
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
/*     */ public class DripstoneUtils
/*     */ {
/*     */   protected static double getDripstoneHeight(double xzDistanceFromCenter, double dripstoneRadius, double scale, double bluntness) {
/*  31 */     if (xzDistanceFromCenter < bluntness) {
/*  32 */       xzDistanceFromCenter = bluntness;
/*     */     }
/*     */ 
/*     */     
/*  36 */     double cutoff = 0.384D;
/*  37 */     double r = xzDistanceFromCenter / dripstoneRadius * 0.384D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  42 */     double part1 = 0.75D * Math.pow(r, 1.3333333333333333D);
/*  43 */     double part2 = Math.pow(r, 0.6666666666666666D);
/*  44 */     double part3 = 0.3333333333333333D * Math.log(r);
/*  45 */     double heightRelativeToMaxRadius = scale * (part1 - part2 - part3);
/*     */     
/*  47 */     heightRelativeToMaxRadius = Math.max(heightRelativeToMaxRadius, 0.0D);
/*  48 */     return heightRelativeToMaxRadius / 0.384D * dripstoneRadius;
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
/*     */   protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel level, BlockPos center, int xzRadius) {
/*  61 */     if (isEmptyOrWaterOrLava(level, center)) {
/*  62 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  66 */     float arcLength = 6.0F;
/*  67 */     float angleIncrement = 6.0F / xzRadius; float angle;
/*  68 */     for (angle = 0.0F; angle < 6.2831855F; angle += angleIncrement) {
/*  69 */       int dx = (int)(Mth.cos(angle) * xzRadius);
/*  70 */       int dz = (int)(Mth.sin(angle) * xzRadius);
/*  71 */       if (isEmptyOrWaterOrLava(level, center.offset(dx, 0, dz))) {
/*  72 */         return false;
/*     */       }
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  79 */   protected static boolean isEmptyOrWater(LevelAccessor level, BlockPos pos) { return level.isStateAtPosition(pos, DripstoneUtils::isEmptyOrWater); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected static boolean isEmptyOrWaterOrLava(LevelAccessor level, BlockPos pos) { return level.isStateAtPosition(pos, DripstoneUtils::isEmptyOrWaterOrLava); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void buildBaseToTipColumn(Direction direction, int totalLength, boolean mergedTip, Consumer<BlockState> consumer) {
/*  92 */     if (totalLength >= 3) {
/*  93 */       consumer.accept(createPointedDripstone(direction, DripstoneThickness.BASE));
/*  94 */       for (int i = 0; i < totalLength - 3; i++) {
/*  95 */         consumer.accept(createPointedDripstone(direction, DripstoneThickness.MIDDLE));
/*     */       }
/*     */     } 
/*  98 */     if (totalLength >= 2) {
/*  99 */       consumer.accept(createPointedDripstone(direction, DripstoneThickness.FRUSTUM));
/*     */     }
/* 101 */     if (totalLength >= 1) {
/* 102 */       consumer.accept(createPointedDripstone(direction, mergedTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void growPointedDripstone(LevelAccessor level, BlockPos startPos, Direction tipDirection, int height, boolean mergedTip) {
/* 107 */     if (!isDripstoneBase(level.getBlockState(startPos.relative(tipDirection.getOpposite())))) {
/*     */       return;
/*     */     }
/*     */     
/* 111 */     BlockPos.MutableBlockPos pos = startPos.mutable();
/* 112 */     buildBaseToTipColumn(tipDirection, height, mergedTip, state -> {
/* 113 */           if (state.is(Blocks.POINTED_DRIPSTONE)) {
/* 114 */             state = (BlockState)state.setValue(PointedDripstoneBlock.WATERLOGGED, Boolean.valueOf(level.isWaterAt(pos)));
/*     */           }
/* 116 */           level.setBlock(pos, state, 2);
/* 117 */           pos.move(tipDirection);
/*     */         });
/*     */   }
/*     */   
/*     */   protected static boolean placeDripstoneBlockIfPossible(LevelAccessor level, BlockPos pos) {
/* 122 */     BlockState state = level.getBlockState(pos);
/* 123 */     if (state.is(BlockTags.DRIPSTONE_REPLACEABLE)) {
/* 124 */       level.setBlock(pos, Blocks.DRIPSTONE_BLOCK.defaultBlockState(), 2);
/* 125 */       return true;
/*     */     } 
/* 127 */     return false;
/*     */   }
/*     */   
/*     */   private static BlockState createPointedDripstone(Direction direction, DripstoneThickness thickness) {
/* 131 */     return (BlockState)((BlockState)Blocks.POINTED_DRIPSTONE.defaultBlockState()
/* 132 */       .setValue(PointedDripstoneBlock.TIP_DIRECTION, direction))
/* 133 */       .setValue(PointedDripstoneBlock.THICKNESS, thickness);
/*     */   }
/*     */ 
/*     */   
/* 137 */   public static boolean isDripstoneBaseOrLava(BlockState state) { return (isDripstoneBase(state) || state.is(Blocks.LAVA)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public static boolean isDripstoneBase(BlockState state) { return (state.is(Blocks.DRIPSTONE_BLOCK) || state.is(BlockTags.DRIPSTONE_REPLACEABLE)); }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public static boolean isEmptyOrWater(BlockState state) { return (state.isAir() || state.is(Blocks.WATER)); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static boolean isNeitherEmptyNorWater(BlockState state) { return (!state.isAir() && !state.is(Blocks.WATER)); }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public static boolean isEmptyOrWaterOrLava(BlockState state) { return (state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\DripstoneUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */