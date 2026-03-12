/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PitcherCropBlock
/*     */   extends DoublePlantBlock implements BonemealableBlock {
/*  33 */   public static final MapCodec<PitcherCropBlock> CODEC = simpleCodec(PitcherCropBlock::new);
/*     */   
/*     */   public static final int MAX_AGE = 4;
/*     */   
/*  37 */   public MapCodec<PitcherCropBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
/*  42 */   public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;
/*     */   
/*     */   private static final int DOUBLE_PLANT_AGE_INTERSECTION = 3;
/*     */   
/*     */   private static final int BONEMEAL_INCREASE = 1;
/*  47 */   private static final VoxelShape SHAPE_BULB = Block.column(6.0D, -1.0D, 3.0D);
/*  48 */   private static final VoxelShape SHAPE_CROP = Block.column(10.0D, -1.0D, 5.0D);
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   public PitcherCropBlock(BlockBehaviour.Properties properties) {
/*  53 */     super(properties);
/*     */     
/*  55 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  59 */     int[] plantHeights = { 0, 9, 11, 22, 26 };
/*     */     
/*  61 */     return getShapeForEachState(state -> {
/*  62 */           int height = ((((Integer)state.getValue(AGE)).intValue() == 0) ? 4 : 6) + plantHeights[((Integer)state.getValue(AGE)).intValue()];
/*  63 */           int width = (((Integer)state.getValue(AGE)).intValue() == 0) ? 6 : 10;
/*  64 */           switch ((DoubleBlockHalf)state.getValue(HALF)) { default: throw new MatchException(null, null);case LOWER: case UPPER: break; }  return 
/*     */             
/*  66 */             Block.column(width, 0.0D, Math.max(0, -1 + height - 16));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return defaultBlockState(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  83 */     if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
/*  84 */       return (((Integer)state.getValue(AGE)).intValue() == 0) ? SHAPE_BULB : SHAPE_CROP;
/*     */     }
/*  86 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  91 */     if (isDouble(((Integer)state.getValue(AGE)).intValue())) {
/*  92 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */     }
/*  94 */     return state.canSurvive(level, pos) ? state : Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 100 */     if (isLower(state) && !sufficientLight(level, pos)) {
/* 101 */       return false;
/*     */     }
/* 103 */     return super.canSurvive(state, level, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 108 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return state.is(Blocks.FARMLAND); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 113 */     builder.add(new Property[] { AGE });
/* 114 */     super.createBlockStateDefinition(builder);
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 119 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (entity instanceof net.minecraft.world.entity.monster.Ravager && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 120 */         serverLevel.destroyBlock(pos, true, entity);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 126 */   public boolean canBeReplaced(BlockState state, BlockPlaceContext context) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public boolean isRandomlyTicking(BlockState state) { return (state.getValue(HALF) == DoubleBlockHalf.LOWER && !isMaxAge(state)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 142 */     float growthSpeed = CropBlock.getGrowthSpeed(this, level, pos);
/* 143 */     boolean shouldProgressGrowth = (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0);
/*     */     
/* 145 */     if (shouldProgressGrowth) {
/* 146 */       grow(level, state, pos, 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void grow(ServerLevel level, BlockState lowerState, BlockPos lowerPos, int increase) {
/* 151 */     int updatedAge = Math.min(((Integer)lowerState.getValue(AGE)).intValue() + increase, 4);
/* 152 */     if (!canGrow(level, lowerPos, lowerState, updatedAge)) {
/*     */       return;
/*     */     }
/*     */     
/* 156 */     BlockState newLowerState = (BlockState)lowerState.setValue(AGE, Integer.valueOf(updatedAge));
/* 157 */     level.setBlock(lowerPos, newLowerState, 2);
/*     */     
/* 159 */     if (isDouble(updatedAge)) {
/* 160 */       level.setBlock(lowerPos.above(), (BlockState)newLowerState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean canGrowInto(LevelReader level, BlockPos pos) {
/* 165 */     BlockState state = level.getBlockState(pos);
/* 166 */     return (state.isAir() || state.is(Blocks.PITCHER_CROP));
/*     */   }
/*     */ 
/*     */   
/* 170 */   private static boolean sufficientLight(LevelReader level, BlockPos pos) { return CropBlock.hasSufficientLight(level, pos); }
/*     */ 
/*     */ 
/*     */   
/* 174 */   private static boolean isLower(BlockState state) { return (state.is(Blocks.PITCHER_CROP) && state.getValue(HALF) == DoubleBlockHalf.LOWER); }
/*     */ 
/*     */ 
/*     */   
/* 178 */   private static boolean isDouble(int age) { return (age >= 3); }
/*     */ 
/*     */ 
/*     */   
/* 182 */   private boolean canGrow(LevelReader level, BlockPos lowerPos, BlockState lowerState, int newAge) { return (!isMaxAge(lowerState) && sufficientLight(level, lowerPos) && (!isDouble(newAge) || canGrowInto(level, lowerPos.above()))); }
/*     */ 
/*     */ 
/*     */   
/* 186 */   private boolean isMaxAge(BlockState state) { return (((Integer)state.getValue(AGE)).intValue() >= 4); }
/*     */   private static final class PosAndState extends Record { private final BlockPos pos; private final BlockState state;
/*     */     
/* 189 */     private PosAndState(BlockPos pos, BlockState state) { this.pos = pos; this.state = state; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 189 */       //   0	7	0	this	Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;
/* 189 */       //   0	8	1	o	Ljava/lang/Object; } public BlockState state() { return this.state; } }
/*     */   
/*     */   private PosAndState getLowerHalf(LevelReader level, BlockPos pos, BlockState state) {
/* 192 */     if (isLower(state)) {
/* 193 */       return new PosAndState(pos, state);
/*     */     }
/* 195 */     BlockPos lowerPos = pos.below();
/* 196 */     BlockState lowerState = level.getBlockState(lowerPos);
/* 197 */     if (isLower(lowerState)) {
/* 198 */       return new PosAndState(lowerPos, lowerState);
/*     */     }
/* 200 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 205 */     PosAndState lowerHalf = getLowerHalf(level, pos, state);
/* 206 */     if (lowerHalf == null) {
/* 207 */       return false;
/*     */     }
/* 209 */     return canGrow(level, lowerHalf.pos, lowerHalf.state, ((Integer)lowerHalf.state.getValue(AGE)).intValue() + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 214 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 219 */     PosAndState lowerHalf = getLowerHalf(level, pos, state);
/* 220 */     if (lowerHalf == null) {
/*     */       return;
/*     */     }
/* 223 */     grow(level, lowerHalf.state, lowerHalf.pos, 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PitcherCropBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */