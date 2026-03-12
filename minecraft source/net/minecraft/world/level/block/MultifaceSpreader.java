/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class MultifaceSpreader
/*     */ {
/*  15 */   public static final SpreadType[] DEFAULT_SPREAD_ORDER = { SpreadType.SAME_POSITION, SpreadType.SAME_PLANE, SpreadType.WRAP_AROUND };
/*     */ 
/*     */ 
/*     */   
/*     */   private final SpreadConfig config;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   public MultifaceSpreader(MultifaceBlock multifaceBlock) { this(new DefaultSpreaderConfig(multifaceBlock)); }
/*     */ 
/*     */ 
/*     */   
/*  28 */   public MultifaceSpreader(SpreadConfig config) { this.config = config; }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public boolean canSpreadInAnyDirection(BlockState state, BlockGetter level, BlockPos pos, Direction startingFace) { return Direction.stream().anyMatch(spreadDirection -> { Objects.requireNonNull(this.config); return getSpreadFromFaceTowardDirection(state, level, pos, startingFace, spreadDirection, this.config::canSpreadInto).isPresent();
/*     */         }); }
/*     */   
/*     */   public Optional<SpreadPos> spreadFromRandomFaceTowardRandomDirection(BlockState state, LevelAccessor level, BlockPos pos, RandomSource random) {
/*  36 */     return (Optional)Direction.allShuffled(random).stream()
/*  37 */       .filter(faceDirection -> this.config.canSpreadFrom(state, faceDirection))
/*  38 */       .map(faceDirection -> spreadFromFaceTowardRandomDirection(state, level, pos, faceDirection, random, false))
/*  39 */       .filter(Optional::isPresent)
/*  40 */       .findFirst()
/*  41 */       .orElse(Optional.empty());
/*     */   }
/*     */ 
/*     */   
/*  45 */   public long spreadAll(BlockState state, LevelAccessor level, BlockPos pos, boolean postProcess) { return ((Long)Direction.stream()
/*  46 */       .filter(faceDirection -> this.config.canSpreadFrom(state, faceDirection))
/*  47 */       .map(faceDirection -> Long.valueOf(spreadFromFaceTowardAllDirections(state, level, pos, faceDirection, postProcess)))
/*  48 */       .reduce(Long.valueOf(0L), Long::sum)).longValue(); }
/*     */ 
/*     */   
/*     */   public Optional<SpreadPos> spreadFromFaceTowardRandomDirection(BlockState state, LevelAccessor level, BlockPos pos, Direction startingFace, RandomSource random, boolean postProcess) {
/*  52 */     return (Optional)Direction.allShuffled(random).stream()
/*  53 */       .map(spreadDirection -> spreadFromFaceTowardDirection(state, level, pos, startingFace, spreadDirection, postProcess))
/*  54 */       .filter(Optional::isPresent)
/*  55 */       .findFirst()
/*  56 */       .orElse(Optional.empty());
/*     */   }
/*     */ 
/*     */   
/*  60 */   private long spreadFromFaceTowardAllDirections(BlockState state, LevelAccessor level, BlockPos pos, Direction startingFace, boolean postProcess) { return Direction.stream()
/*  61 */       .map(spreadDirection -> spreadFromFaceTowardDirection(state, level, pos, startingFace, spreadDirection, postProcess))
/*  62 */       .filter(Optional::isPresent).count(); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public Optional<SpreadPos> spreadFromFaceTowardDirection(BlockState state, LevelAccessor level, BlockPos pos, Direction fromFace, Direction spreadDirection, boolean postProcess) {
/*  67 */     Objects.requireNonNull(this.config); return getSpreadFromFaceTowardDirection(state, level, pos, fromFace, spreadDirection, this.config::canSpreadInto)
/*  68 */       .flatMap(spreadPos -> spreadToFace(level, spreadPos, postProcess));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<SpreadPos> getSpreadFromFaceTowardDirection(BlockState state, BlockGetter level, BlockPos pos, Direction startingFace, Direction spreadDirection, SpreadPredicate canSpreadInto) {
/*  73 */     if (spreadDirection.getAxis() == startingFace.getAxis()) {
/*  74 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*  78 */     if (!this.config.isOtherBlockValidAsSource(state) && (!this.config.hasFace(state, startingFace) || this.config.hasFace(state, spreadDirection))) {
/*  79 */       return Optional.empty();
/*     */     }
/*  81 */     for (SpreadType type : this.config.getSpreadTypes()) {
/*  82 */       SpreadPos spreadPos = type.getSpreadPos(pos, spreadDirection, startingFace);
/*  83 */       if (canSpreadInto.test(level, pos, spreadPos)) {
/*  84 */         return Optional.of(spreadPos);
/*     */       }
/*     */     } 
/*  87 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<SpreadPos> spreadToFace(LevelAccessor level, SpreadPos spreadPos, boolean postProcess) {
/*  91 */     BlockState oldState = level.getBlockState(spreadPos.pos());
/*  92 */     if (this.config.placeBlock(level, spreadPos, oldState, postProcess)) {
/*  93 */       return Optional.of(spreadPos);
/*     */     }
/*  95 */     return Optional.empty();
/*     */   }
/*     */   public static final class SpreadPos extends Record { private final BlockPos pos; private final Direction face;
/*  98 */     public SpreadPos(BlockPos pos, Direction face) { this.pos = pos; this.face = face; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  98 */       //   0	7	0	this	Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadPos;
/*  98 */       //   0	8	1	o	Ljava/lang/Object; } public Direction face() { return this.face; } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface SpreadConfig
/*     */   {
/*     */     BlockState getStateForPlacement(BlockState param1BlockState, BlockGetter param1BlockGetter, BlockPos param1BlockPos, Direction param1Direction);
/*     */ 
/*     */     
/*     */     boolean canSpreadInto(BlockGetter param1BlockGetter, BlockPos param1BlockPos, MultifaceSpreader.SpreadPos param1SpreadPos);
/*     */ 
/*     */     
/* 111 */     default MultifaceSpreader.SpreadType[] getSpreadTypes() { return MultifaceSpreader.DEFAULT_SPREAD_ORDER; }
/*     */ 
/*     */ 
/*     */     
/* 115 */     default boolean hasFace(BlockState state, Direction face) { return MultifaceBlock.hasFace(state, face); }
/*     */ 
/*     */ 
/*     */     
/* 119 */     default boolean isOtherBlockValidAsSource(BlockState state) { return false; }
/*     */ 
/*     */ 
/*     */     
/* 123 */     default boolean canSpreadFrom(BlockState state, Direction face) { return (isOtherBlockValidAsSource(state) || hasFace(state, face)); }
/*     */ 
/*     */     
/*     */     default boolean placeBlock(LevelAccessor level, MultifaceSpreader.SpreadPos spreadPos, BlockState oldState, boolean postProcess) {
/* 127 */       BlockState spreadState = getStateForPlacement(oldState, level, spreadPos.pos(), spreadPos.face());
/* 128 */       if (spreadState != null) {
/*     */         
/* 130 */         if (postProcess) {
/* 131 */           level.getChunk(spreadPos.pos()).markPosForPostprocessing(spreadPos.pos());
/*     */         }
/* 133 */         return level.setBlock(spreadPos.pos(), spreadState, 2);
/*     */       } 
/* 135 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DefaultSpreaderConfig
/*     */     implements SpreadConfig {
/*     */     protected MultifaceBlock block;
/*     */     
/* 143 */     public DefaultSpreaderConfig(MultifaceBlock block) { this.block = block; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     public BlockState getStateForPlacement(BlockState oldState, BlockGetter level, BlockPos placementPos, Direction placementDirection) { return this.block.getStateForPlacement(oldState, level, placementPos, placementDirection); }
/*     */ 
/*     */ 
/*     */     
/* 152 */     protected boolean stateCanBeReplaced(BlockGetter level, BlockPos sourcePos, BlockPos placementPos, Direction placementDirection, BlockState existingState) { return (existingState.isAir() || existingState.is(this.block) || (existingState.is(Blocks.WATER) && existingState.getFluidState().isSource())); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canSpreadInto(BlockGetter level, BlockPos sourcePos, MultifaceSpreader.SpreadPos spreadPos) {
/* 157 */       BlockState existingState = level.getBlockState(spreadPos.pos());
/* 158 */       return (stateCanBeReplaced(level, sourcePos, spreadPos.pos(), spreadPos.face(), existingState) && this.block.isValidStateForPlacement(level, existingState, spreadPos.pos(), spreadPos.face()));
/*     */     } }
/*     */   public final abstract enum SpreadType { SAME_POSITION, SAME_PLANE, WRAP_AROUND;
/*     */     public abstract MultifaceSpreader.SpreadPos getSpreadPos(BlockPos param1BlockPos, Direction param1Direction1, Direction param1Direction2);
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new net/minecraft/world/level/block/MultifaceSpreader$SpreadType$1
/*     */       //   3: dup
/*     */       //   4: ldc 'SAME_POSITION'
/*     */       //   6: iconst_0
/*     */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   10: putstatic net/minecraft/world/level/block/MultifaceSpreader$SpreadType.SAME_POSITION : Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */       //   13: new net/minecraft/world/level/block/MultifaceSpreader$SpreadType$2
/*     */       //   16: dup
/*     */       //   17: ldc 'SAME_PLANE'
/*     */       //   19: iconst_1
/*     */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   23: putstatic net/minecraft/world/level/block/MultifaceSpreader$SpreadType.SAME_PLANE : Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */       //   26: new net/minecraft/world/level/block/MultifaceSpreader$SpreadType$3
/*     */       //   29: dup
/*     */       //   30: ldc 'WRAP_AROUND'
/*     */       //   32: iconst_2
/*     */       //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   36: putstatic net/minecraft/world/level/block/MultifaceSpreader$SpreadType.WRAP_AROUND : Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */       //   39: invokestatic $values : ()[Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */       //   42: putstatic net/minecraft/world/level/block/MultifaceSpreader$SpreadType.$VALUES : [Lnet/minecraft/world/level/block/MultifaceSpreader$SpreadType;
/*     */       //   45: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #163	-> 0
/*     */       //   #169	-> 13
/*     */       //   #175	-> 26
/*     */       //   #162	-> 39
/*     */     } }
/*     */   
/* 166 */   static enum null { public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) { return new MultifaceSpreader.SpreadPos(pos, spreadDirection); } }
/*     */ 
/*     */ 
/*     */   
/*     */   static enum null
/*     */   {
/* 172 */     public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) { return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection), fromFace); }
/*     */   }
/*     */ 
/*     */   
/*     */   static enum null
/*     */   {
/* 178 */     public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) { return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection).relative(fromFace), spreadDirection.getOpposite()); }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SpreadPredicate {
/*     */     boolean test(BlockGetter param1BlockGetter, BlockPos param1BlockPos, MultifaceSpreader.SpreadPos param1SpreadPos);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MultifaceSpreader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */