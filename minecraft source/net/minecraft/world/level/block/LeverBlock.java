/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LeverBlock
/*     */   extends FaceAttachedHorizontalDirectionalBlock {
/*  38 */   public static final MapCodec<LeverBlock> CODEC = simpleCodec(LeverBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  42 */   public MapCodec<LeverBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  45 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected LeverBlock(BlockBehaviour.Properties properties) {
/*  50 */     super(properties);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(POWERED, Boolean.valueOf(false))).setValue(FACE, AttachFace.WALL));
/*     */     
/*  53 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  57 */     Map<AttachFace, Map<Direction, VoxelShape>> attachFace = Shapes.rotateAttachFace(Block.boxZ(6.0D, 8.0D, 10.0D, 16.0D));
/*     */     
/*  59 */     return getShapeForEachState(state -> (VoxelShape)((Map)attachFace.get(state.getValue(FACE))).get(state.getValue(FACING)), new Property[] { POWERED });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  64 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState stateBefore, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  70 */     if (level.isClientSide()) {
/*  71 */       BlockState stateAfter = (BlockState)stateBefore.cycle(POWERED);
/*  72 */       if (((Boolean)stateAfter.getValue(POWERED)).booleanValue()) {
/*  73 */         makeParticle(stateAfter, level, pos, 1.0F);
/*     */       }
/*     */     } else {
/*  76 */       pull(stateBefore, level, pos, null);
/*     */     } 
/*  78 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/*  83 */     if (explosion.canTriggerBlocks()) {
/*  84 */       pull(state, level, pos, null);
/*     */     }
/*  86 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   public void pull(BlockState state, Level level, BlockPos pos, Player player) {
/*  90 */     state = (BlockState)state.cycle(POWERED);
/*  91 */     level.setBlock(pos, state, 3);
/*  92 */     updateNeighbours(state, level, pos);
/*  93 */     playSound(player, level, pos, state);
/*  94 */     level.gameEvent(player, ((Boolean)state.getValue(POWERED)).booleanValue() ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
/*     */   }
/*     */   
/*     */   protected static void playSound(Player player, LevelAccessor level, BlockPos pos, BlockState stateAfter) {
/*  98 */     float pitch = ((Boolean)stateAfter.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F;
/*  99 */     level.playSound(player, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, pitch);
/*     */   }
/*     */   
/*     */   private static void makeParticle(BlockState state, LevelAccessor level, BlockPos pos, float scale) {
/* 103 */     Direction opposite = ((Direction)state.getValue(FACING)).getOpposite();
/* 104 */     Direction oppositeConnect = getConnectedDirection(state).getOpposite();
/* 105 */     double x = pos.getX() + 0.5D + 0.1D * opposite.getStepX() + 0.2D * oppositeConnect.getStepX();
/* 106 */     double y = pos.getY() + 0.5D + 0.1D * opposite.getStepY() + 0.2D * oppositeConnect.getStepY();
/* 107 */     double z = pos.getZ() + 0.5D + 0.1D * opposite.getStepZ() + 0.2D * oppositeConnect.getStepZ();
/*     */     
/* 109 */     level.addParticle(new DustParticleOptions(16711680, scale), x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 114 */     if (((Boolean)state.getValue(POWERED)).booleanValue() && random.nextFloat() < 0.25F) {
/* 115 */       makeParticle(state, level, pos, 0.5F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 121 */     if (!movedByPiston && ((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 122 */       updateNeighbours(state, level, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 128 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 133 */     if (((Boolean)state.getValue(POWERED)).booleanValue() && getConnectedDirection(state) == direction) {
/* 134 */       return 15;
/*     */     }
/* 136 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
/* 146 */     Direction front = getConnectedDirection(state).getOpposite();
/* 147 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, front, front.getAxis().isHorizontal() ? Direction.UP : (Direction)state.getValue(FACING));
/* 148 */     level.updateNeighborsAt(pos, this, orientation);
/* 149 */     level.updateNeighborsAt(pos.relative(front), this, orientation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 154 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACE, FACING, POWERED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LeverBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */