/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ChorusFlowerBlock extends Block {
/*  25 */   public static final MapCodec<ChorusFlowerBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/*  26 */         .byNameCodec().fieldOf("plant").forGetter(()), 
/*  27 */         propertiesCodec())
/*  28 */       .apply(i, ChorusFlowerBlock::new));
/*     */   
/*     */   public static final int DEAD_AGE = 5;
/*     */   
/*  32 */   public MapCodec<ChorusFlowerBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
/*     */   
/*  38 */   private static final VoxelShape SHAPE_BLOCK_SUPPORT = Block.column(14.0D, 0.0D, 15.0D);
/*     */   
/*     */   private final Block plant;
/*     */   
/*     */   protected ChorusFlowerBlock(Block plant, BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  44 */     this.plant = plant;
/*  45 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  50 */     if (!state.canSurvive(level, pos)) {
/*  51 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected boolean isRandomlyTicking(BlockState state) { return (((Integer)state.getValue(AGE)).intValue() < 5); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) { return SHAPE_BLOCK_SUPPORT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  68 */     BlockPos above = pos.above();
/*  69 */     if (!level.isEmptyBlock(above) || above.getY() > level.getMaxY()) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     int currentAge = ((Integer)state.getValue(AGE)).intValue();
/*  74 */     if (currentAge >= 5) {
/*     */       return;
/*     */     }
/*     */     
/*  78 */     boolean growUpwards = false;
/*  79 */     boolean pillarOnEndStone = false;
/*     */     
/*  81 */     BlockState belowState = level.getBlockState(pos.below());
/*  82 */     if (belowState.is(Blocks.END_STONE)) {
/*  83 */       growUpwards = true;
/*  84 */     } else if (belowState.is(this.plant)) {
/*  85 */       int height = 1;
/*  86 */       for (int i = 0; i < 4; i++) {
/*  87 */         BlockState testState = level.getBlockState(pos.below(height + 1));
/*  88 */         if (testState.is(this.plant)) {
/*  89 */           height++;
/*     */         } else {
/*  91 */           if (testState.is(Blocks.END_STONE)) {
/*  92 */             pillarOnEndStone = true;
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/*  97 */       if (height < 2 || height <= random.nextInt(pillarOnEndStone ? 5 : 4)) {
/*  98 */         growUpwards = true;
/*     */       }
/* 100 */     } else if (belowState.isAir()) {
/* 101 */       growUpwards = true;
/*     */     } 
/*     */     
/* 104 */     if (growUpwards && allNeighborsEmpty(level, above, null) && level.isEmptyBlock(pos.above(2))) {
/* 105 */       level.setBlock(pos, ChorusPlantBlock.getStateWithConnections(level, pos, this.plant.defaultBlockState()), 2);
/* 106 */       placeGrownFlower(level, above, currentAge);
/* 107 */     } else if (currentAge < 4) {
/* 108 */       int numBranchAttempts = random.nextInt(4);
/* 109 */       if (pillarOnEndStone) {
/* 110 */         numBranchAttempts++;
/*     */       }
/*     */       
/* 113 */       boolean createdBranch = false;
/* 114 */       for (int i = 0; i < numBranchAttempts; i++) {
/* 115 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 116 */         BlockPos target = pos.relative(direction);
/* 117 */         if (level.isEmptyBlock(target) && level.isEmptyBlock(target.below()) && allNeighborsEmpty(level, target, direction.getOpposite())) {
/* 118 */           placeGrownFlower(level, target, currentAge + 1);
/* 119 */           createdBranch = true;
/*     */         } 
/*     */       } 
/*     */       
/* 123 */       if (createdBranch) {
/* 124 */         level.setBlock(pos, ChorusPlantBlock.getStateWithConnections(level, pos, this.plant.defaultBlockState()), 2);
/*     */       } else {
/* 126 */         placeDeadFlower(level, pos);
/*     */       } 
/*     */     } else {
/* 129 */       placeDeadFlower(level, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeGrownFlower(Level level, BlockPos pos, int age) {
/* 134 */     level.setBlock(pos, (BlockState)defaultBlockState().setValue(AGE, Integer.valueOf(age)), 2);
/* 135 */     level.levelEvent(1033, pos, 0);
/*     */   }
/*     */   
/*     */   private void placeDeadFlower(Level level, BlockPos pos) {
/* 139 */     level.setBlock(pos, (BlockState)defaultBlockState().setValue(AGE, Integer.valueOf(5)), 2);
/* 140 */     level.levelEvent(1034, pos, 0);
/*     */   }
/*     */   
/*     */   private static boolean allNeighborsEmpty(LevelReader level, BlockPos pos, Direction ignore) {
/* 144 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 145 */       if (direction != ignore && !level.isEmptyBlock(pos.relative(direction))) {
/* 146 */         return false;
/*     */       }
/*     */     } 
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 154 */     if (directionToNeighbour != Direction.UP && !state.canSurvive(level, pos)) {
/* 155 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*     */     
/* 158 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 163 */     BlockState belowState = level.getBlockState(pos.below());
/* 164 */     if (belowState.is(this.plant) || belowState.is(Blocks.END_STONE)) {
/* 165 */       return true;
/*     */     }
/* 167 */     if (!belowState.isAir()) {
/* 168 */       return false;
/*     */     }
/*     */     
/* 171 */     boolean oneNeighbor = false;
/* 172 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 173 */       BlockState neighbor = level.getBlockState(pos.relative(direction));
/* 174 */       if (neighbor.is(this.plant)) {
/* 175 */         if (oneNeighbor) {
/* 176 */           return false;
/*     */         }
/* 178 */         oneNeighbor = true; continue;
/* 179 */       }  if (!neighbor.isAir()) {
/* 180 */         return false;
/*     */       }
/*     */     } 
/* 183 */     return oneNeighbor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 188 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*     */ 
/*     */   
/*     */   public static void generatePlant(LevelAccessor level, BlockPos target, RandomSource random, int maxHorizontalSpread) {
/* 192 */     level.setBlock(target, ChorusPlantBlock.getStateWithConnections(level, target, Blocks.CHORUS_PLANT.defaultBlockState()), 2);
/* 193 */     growTreeRecursive(level, target, random, target, maxHorizontalSpread, 0);
/*     */   }
/*     */   
/*     */   private static void growTreeRecursive(LevelAccessor level, BlockPos current, RandomSource random, BlockPos startPos, int maxHorizontalSpread, int depth) {
/* 197 */     Block chorus = Blocks.CHORUS_PLANT;
/*     */     
/* 199 */     int height = random.nextInt(4) + 1;
/* 200 */     if (depth == 0) {
/* 201 */       height++;
/*     */     }
/*     */     
/* 204 */     for (int i = 0; i < height; i++) {
/* 205 */       BlockPos target = current.above(i + 1);
/* 206 */       if (!allNeighborsEmpty(level, target, null)) {
/*     */         return;
/*     */       }
/*     */       
/* 210 */       level.setBlock(target, ChorusPlantBlock.getStateWithConnections(level, target, chorus.defaultBlockState()), 2);
/* 211 */       level.setBlock(target.below(), ChorusPlantBlock.getStateWithConnections(level, target.below(), chorus.defaultBlockState()), 2);
/*     */     } 
/*     */     
/* 214 */     boolean placedStem = false;
/* 215 */     if (depth < 4) {
/* 216 */       int stems = random.nextInt(4);
/* 217 */       if (depth == 0) {
/* 218 */         stems++;
/*     */       }
/* 220 */       for (int i = 0; i < stems; i++) {
/* 221 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 222 */         BlockPos target = current.above(height).relative(direction);
/* 223 */         if (Math.abs(target.getX() - startPos.getX()) < maxHorizontalSpread && Math.abs(target.getZ() - startPos.getZ()) < maxHorizontalSpread)
/*     */         {
/*     */           
/* 226 */           if (level.isEmptyBlock(target) && level.isEmptyBlock(target.below()) && allNeighborsEmpty(level, target, direction.getOpposite())) {
/* 227 */             placedStem = true;
/* 228 */             level.setBlock(target, ChorusPlantBlock.getStateWithConnections(level, target, chorus.defaultBlockState()), 2);
/* 229 */             level.setBlock(target.relative(direction.getOpposite()), ChorusPlantBlock.getStateWithConnections(level, target.relative(direction.getOpposite()), chorus.defaultBlockState()), 2);
/* 230 */             growTreeRecursive(level, target, random, startPos, maxHorizontalSpread, depth + 1);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 235 */     if (!placedStem) {
/* 236 */       level.setBlock(current.above(height), (BlockState)Blocks.CHORUS_FLOWER.defaultBlockState().setValue(AGE, Integer.valueOf(5)), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 242 */     BlockPos pos = blockHit.getBlockPos();
/* 243 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (projectile.mayInteract(serverLevel, pos) && projectile.mayBreak(serverLevel))
/* 244 */         level.destroyBlock(pos, true, projectile);  }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ChorusFlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */