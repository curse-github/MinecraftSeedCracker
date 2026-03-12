/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class RepeaterBlock extends DiodeBlock {
/*  23 */   public static final MapCodec<RepeaterBlock> CODEC = simpleCodec(RepeaterBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  27 */   public MapCodec<RepeaterBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  30 */   public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
/*  31 */   public static final IntegerProperty DELAY = BlockStateProperties.DELAY;
/*     */   
/*     */   protected RepeaterBlock(BlockBehaviour.Properties properties) {
/*  34 */     super(properties);
/*  35 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(DELAY, Integer.valueOf(1))).setValue(LOCKED, Boolean.valueOf(false))).setValue(POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  40 */     if (!(player.getAbilities()).mayBuild) {
/*  41 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  44 */     level.setBlock(pos, (BlockState)state.cycle(DELAY), 3);
/*  45 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected int getDelay(BlockState state) { return ((Integer)state.getValue(DELAY)).intValue() * 2; }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  55 */     BlockState state = super.getStateForPlacement(context);
/*  56 */     return (BlockState)state.setValue(LOCKED, Boolean.valueOf(isLocked(context.getLevel(), context.getClickedPos(), state)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  61 */     if (directionToNeighbour == Direction.DOWN && !canSurviveOn(level, neighbourPos, neighbourState)) {
/*  62 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  65 */     if (!level.isClientSide() && directionToNeighbour.getAxis() != ((Direction)state.getValue(FACING)).getAxis()) {
/*  66 */       return (BlockState)state.setValue(LOCKED, Boolean.valueOf(isLocked(level, pos, state)));
/*     */     }
/*  68 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public boolean isLocked(LevelReader level, BlockPos pos, BlockState state) { return (getAlternateSignal(level, pos, state) > 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected boolean sideInputDiodesOnly() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  83 */     if (!((Boolean)state.getValue(POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*  86 */     Direction direction = (Direction)state.getValue(FACING);
/*     */     
/*  88 */     double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
/*  89 */     double y = pos.getY() + 0.4D + (random.nextDouble() - 0.5D) * 0.2D;
/*  90 */     double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
/*     */     
/*  92 */     float offset = -5.0F;
/*  93 */     if (random.nextBoolean()) {
/*  94 */       offset = (((Integer)state.getValue(DELAY)).intValue() * 2 - 1);
/*     */     }
/*  96 */     offset /= 16.0F;
/*     */     
/*  98 */     double xo = (offset * direction.getStepX());
/*  99 */     double zo = (offset * direction.getStepZ());
/*     */     
/* 101 */     level.addParticle(DustParticleOptions.REDSTONE, x + xo, y, z + zo, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, DELAY, LOCKED, POWERED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RepeaterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */