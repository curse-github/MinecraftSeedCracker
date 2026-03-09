/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
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
/*     */ public class ChargeCursor
/*     */ {
/* 134 */   private static final ObjectArrayList<Vec3i> NON_CORNER_NEIGHBOURS = (ObjectArrayList)Util.make(new ObjectArrayList(18), list -> {
/*     */ 
/*     */ 
/*     */         
/* 138 */         Objects.requireNonNull(list); BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).filter(()).map(BlockPos::immutable).forEach(list::add);
/*     */       });
/*     */   
/*     */   public static final int MAX_CURSOR_DECAY_DELAY = 1;
/*     */   
/*     */   private BlockPos pos;
/*     */   
/*     */   private int charge;
/*     */   
/*     */   private int updateDelay;
/*     */   
/*     */   private int decayDelay;
/*     */   private Set<Direction> facings;
/* 151 */   private static final Codec<Set<Direction>> DIRECTION_SET = Direction.CODEC.listOf().xmap(l -> Sets.newEnumSet(l, Direction.class), Lists::newArrayList);
/*     */   
/* 153 */   public static final Codec<ChargeCursor> CODEC = RecordCodecBuilder.create(i -> i.group(BlockPos.CODEC
/* 154 */         .fieldOf("pos").forGetter(ChargeCursor::getPos), 
/* 155 */         Codec.intRange(0, 1000).fieldOf("charge").orElse(Integer.valueOf(0)).forGetter(ChargeCursor::getCharge), 
/* 156 */         Codec.intRange(0, 1).fieldOf("decay_delay").orElse(Integer.valueOf(1)).forGetter(ChargeCursor::getDecayDelay), 
/* 157 */         Codec.intRange(0, 2147483647).fieldOf("update_delay").orElse(Integer.valueOf(0)).forGetter(()), DIRECTION_SET
/* 158 */         .lenientOptionalFieldOf("facings").forGetter(()))
/* 159 */       .apply(i, ChargeCursor::new));
/*     */   
/*     */   private ChargeCursor(BlockPos pos, int charge, int decayDelay, int updateDelay, Optional<Set<Direction>> facings) {
/* 162 */     this.pos = pos;
/* 163 */     this.charge = charge;
/* 164 */     this.decayDelay = decayDelay;
/* 165 */     this.updateDelay = updateDelay;
/* 166 */     this.facings = (Set)facings.orElse(null);
/*     */   }
/*     */ 
/*     */   
/* 170 */   public ChargeCursor(BlockPos pos, int charge) { this(pos, charge, 1, 0, Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/* 174 */   public BlockPos getPos() { return this.pos; }
/*     */ 
/*     */ 
/*     */   
/* 178 */   private boolean isPosUnreasonable(BlockPos originPos) { return (this.pos.distChessboard(originPos) > 1024); }
/*     */ 
/*     */ 
/*     */   
/* 182 */   public int getCharge() { return this.charge; }
/*     */ 
/*     */ 
/*     */   
/* 186 */   public int getDecayDelay() { return this.decayDelay; }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public Set<Direction> getFacingData() { return this.facings; }
/*     */ 
/*     */   
/*     */   private boolean shouldUpdate(LevelAccessor level, BlockPos pos, boolean isWorldGen) {
/* 194 */     if (this.charge <= 0) {
/* 195 */       return false;
/*     */     }
/* 197 */     if (isWorldGen) {
/* 198 */       return true;
/*     */     }
/* 200 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 201 */       return serverLevel.shouldTickBlocksAt(pos); }
/*     */     
/* 203 */     return false;
/*     */   }
/*     */   
/*     */   public void update(LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVeins) {
/* 207 */     if (!shouldUpdate(level, originPos, spreader.isWorldGeneration)) {
/*     */       return;
/*     */     }
/*     */     
/* 211 */     if (this.updateDelay > 0) {
/* 212 */       this.updateDelay--;
/*     */       
/*     */       return;
/*     */     } 
/* 216 */     BlockState currentState = level.getBlockState(this.pos);
/* 217 */     SculkBehaviour sculkBehaviour = getBlockBehaviour(currentState);
/*     */ 
/*     */     
/* 220 */     if (spreadVeins && sculkBehaviour.attemptSpreadVein(level, this.pos, currentState, this.facings, spreader.isWorldGeneration())) {
/* 221 */       if (sculkBehaviour.canChangeBlockStateOnSpread()) {
/* 222 */         currentState = level.getBlockState(this.pos);
/* 223 */         sculkBehaviour = getBlockBehaviour(currentState);
/*     */       } 
/* 225 */       level.playSound(null, this.pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */ 
/*     */     
/* 229 */     this.charge = sculkBehaviour.attemptUseCharge(this, level, originPos, random, spreader, spreadVeins);
/*     */     
/* 231 */     if (this.charge <= 0) {
/* 232 */       sculkBehaviour.onDischarged(level, currentState, this.pos, random);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 237 */     BlockPos transferPos = getValidMovementPos(level, this.pos, random);
/* 238 */     if (transferPos != null) {
/* 239 */       sculkBehaviour.onDischarged(level, currentState, this.pos, random);
/* 240 */       this.pos = transferPos.immutable();
/* 241 */       if (spreader.isWorldGeneration() && !this.pos.closerThan(new Vec3i(originPos.getX(), this.pos.getY(), originPos.getZ()), 15.0D)) {
/* 242 */         this.charge = 0;
/*     */         return;
/*     */       } 
/* 245 */       currentState = level.getBlockState(transferPos);
/*     */     } 
/*     */     
/* 248 */     if (currentState.getBlock() instanceof SculkBehaviour) {
/* 249 */       this.facings = MultifaceBlock.availableFaces(currentState);
/*     */     }
/* 251 */     this.decayDelay = sculkBehaviour.updateDecayDelay(this.decayDelay);
/* 252 */     this.updateDelay = sculkBehaviour.getSculkSpreadDelay();
/*     */   }
/*     */   
/*     */   private void mergeWith(ChargeCursor other) {
/* 256 */     this.charge += other.charge;
/* 257 */     other.charge = 0;
/* 258 */     this.updateDelay = Math.min(this.updateDelay, other.updateDelay);
/*     */   }
/*     */   
/*     */   private static SculkBehaviour getBlockBehaviour(BlockState state) {
/* 262 */     Block block = state.getBlock(); SculkBehaviour behaviour = (SculkBehaviour)block; return (block instanceof SculkBehaviour) ? behaviour : SculkBehaviour.DEFAULT;
/*     */   }
/*     */ 
/*     */   
/* 266 */   private static List<Vec3i> getRandomizedNonCornerNeighbourOffsets(RandomSource random) { return Util.shuffledCopy(NON_CORNER_NEIGHBOURS, random); }
/*     */ 
/*     */   
/*     */   private static BlockPos getValidMovementPos(LevelAccessor level, BlockPos pos, RandomSource random) {
/* 270 */     BlockPos.MutableBlockPos sculkPosition = pos.mutable();
/* 271 */     BlockPos.MutableBlockPos neighbour = pos.mutable();
/*     */     
/* 273 */     for (Vec3i offset : getRandomizedNonCornerNeighbourOffsets(random)) {
/* 274 */       neighbour.setWithOffset(pos, offset);
/* 275 */       BlockState transferee = level.getBlockState(neighbour);
/*     */       
/* 277 */       if (transferee.getBlock() instanceof SculkBehaviour && isMovementUnobstructed(level, pos, neighbour)) {
/* 278 */         sculkPosition.set(neighbour);
/*     */         
/* 280 */         if (SculkVeinBlock.hasSubstrateAccess(level, transferee, neighbour)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 285 */     return sculkPosition.equals(pos) ? null : sculkPosition;
/*     */   }
/*     */   
/*     */   private static boolean isMovementUnobstructed(LevelAccessor level, BlockPos from, BlockPos to) {
/* 289 */     if (from.distManhattan(to) == 1) {
/* 290 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 294 */     BlockPos delta = to.subtract(from);
/* 295 */     Direction directionX = Direction.fromAxisAndDirection(Direction.Axis.X, (delta.getX() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 296 */     Direction directionY = Direction.fromAxisAndDirection(Direction.Axis.Y, (delta.getY() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 297 */     Direction directionZ = Direction.fromAxisAndDirection(Direction.Axis.Z, (delta.getZ() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/*     */     
/* 299 */     if (delta.getX() == 0)
/* 300 */       return (isUnobstructed(level, from, directionY) || isUnobstructed(level, from, directionZ)); 
/* 301 */     if (delta.getY() == 0) {
/* 302 */       return (isUnobstructed(level, from, directionX) || isUnobstructed(level, from, directionZ));
/*     */     }
/* 304 */     return (isUnobstructed(level, from, directionX) || isUnobstructed(level, from, directionY));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isUnobstructed(LevelAccessor level, BlockPos from, Direction direction) {
/* 309 */     BlockPos testPos = from.relative(direction);
/* 310 */     return !level.getBlockState(testPos).isFaceSturdy(level, testPos, direction.getOpposite());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SculkSpreader$ChargeCursor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */