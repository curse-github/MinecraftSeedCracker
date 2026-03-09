/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class HangingMossBlock extends Block implements BonemealableBlock {
/*  23 */   public static final MapCodec<HangingMossBlock> CODEC = simpleCodec(HangingMossBlock::new);
/*     */   
/*  25 */   private static final VoxelShape SHAPE_BASE = Block.column(14.0D, 0.0D, 16.0D);
/*  26 */   private static final VoxelShape SHAPE_TIP = Block.column(14.0D, 2.0D, 16.0D);
/*     */ 
/*     */ 
/*     */   
/*  30 */   public MapCodec<HangingMossBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  33 */   public static final BooleanProperty TIP = BlockStateProperties.TIP;
/*     */   
/*     */   public HangingMossBlock(BlockBehaviour.Properties properties) {
/*  36 */     super(properties);
/*  37 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(TIP, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  42 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return ((Boolean)state.getValue(TIP)).booleanValue() ? SHAPE_TIP : SHAPE_BASE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  47 */     if (random.nextInt(500) == 0) {
/*  48 */       BlockState above = level.getBlockState(pos.above());
/*  49 */       if (above.is(BlockTags.PALE_OAK_LOGS) || above.is(Blocks.PALE_OAK_LEAVES)) {
/*  50 */         level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.PALE_HANGING_MOSS_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected boolean propagatesSkylightDown(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return canStayAtPosition(level, pos); }
/*     */ 
/*     */   
/*     */   private boolean canStayAtPosition(BlockGetter level, BlockPos pos) {
/*  66 */     BlockPos neighbourPos = pos.relative(Direction.UP);
/*  67 */     BlockState blockState = level.getBlockState(neighbourPos);
/*  68 */     return (MultifaceBlock.canAttachTo(level, Direction.UP, neighbourPos, blockState) || blockState.is(Blocks.PALE_HANGING_MOSS));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  73 */     if (!canStayAtPosition(level, pos)) {
/*  74 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*  76 */     return (BlockState)state.setValue(TIP, Boolean.valueOf(!level.getBlockState(pos.below()).is(this)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  81 */     if (!canStayAtPosition(level, pos)) {
/*  82 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  88 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { TIP }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return canGrowInto(level.getBlockState(getTip(level, pos).below())); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   private boolean canGrowInto(BlockState state) { return state.isAir(); }
/*     */   
/*     */   public BlockPos getTip(BlockGetter level, BlockPos pos) {
/*     */     BlockState forwardState;
/* 101 */     BlockPos.MutableBlockPos forwardPos = pos.mutable();
/*     */     
/*     */     do {
/* 104 */       forwardPos.move(Direction.DOWN);
/* 105 */       forwardState = level.getBlockState(forwardPos);
/* 106 */     } while (forwardState.is(this));
/*     */     
/* 108 */     return forwardPos.relative(Direction.UP).immutable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 118 */     BlockPos tipPos = getTip(level, pos).below();
/*     */     
/* 120 */     if (!canGrowInto(level.getBlockState(tipPos))) {
/*     */       return;
/*     */     }
/* 123 */     level.setBlockAndUpdate(tipPos, (BlockState)state.setValue(TIP, Boolean.valueOf(true)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\HangingMossBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */