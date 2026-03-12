/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class RedstoneTorchBlock extends BaseTorchBlock {
/*  26 */   public static final MapCodec<RedstoneTorchBlock> CODEC = simpleCodec(RedstoneTorchBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  30 */   public MapCodec<? extends RedstoneTorchBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  33 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*     */ 
/*     */   
/*  36 */   private static final Map<BlockGetter, List<Toggle>> RECENT_TOGGLES = new WeakHashMap();
/*     */   
/*     */   public static final int RECENT_TOGGLE_TIMER = 60;
/*     */   public static final int MAX_RECENT_TOGGLES = 8;
/*     */   public static final int RESTART_DELAY = 160;
/*     */   private static final int TOGGLE_DELAY = 2;
/*     */   
/*     */   protected RedstoneTorchBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LIT, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { notifyNeighbors(level, pos, state); }
/*     */ 
/*     */   
/*     */   private void notifyNeighbors(Level level, BlockPos pos, BlockState state) {
/*  54 */     Orientation orientation = randomOrientation(level, state);
/*  55 */     for (Direction direction : Direction.values()) {
/*  56 */       level.updateNeighborsAt(pos.relative(direction), this, ExperimentalRedstoneUtils.withFront(orientation, direction));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/*  62 */     if (!movedByPiston) {
/*  63 */       notifyNeighbors(level, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  69 */     if (((Boolean)state.getValue(LIT)).booleanValue() && Direction.UP != direction) {
/*  70 */       return 15;
/*     */     }
/*     */     
/*  73 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*  77 */   protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) { return level.hasSignal(pos.below(), Direction.DOWN); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  82 */     boolean neighborSignal = hasNeighborSignal(level, pos, state);
/*     */     
/*  84 */     List<Toggle> toggles = (List)RECENT_TOGGLES.get(level);
/*  85 */     while (toggles != null && !toggles.isEmpty() && level.getGameTime() - ((Toggle)toggles.get(0)).when > 60L) {
/*  86 */       toggles.remove(0);
/*     */     }
/*     */     
/*  89 */     if (((Boolean)state.getValue(LIT)).booleanValue()) {
/*  90 */       if (neighborSignal) {
/*  91 */         level.setBlock(pos, (BlockState)state.setValue(LIT, Boolean.valueOf(false)), 3);
/*     */         
/*  93 */         if (isToggledTooFrequently(level, pos, true)) {
/*  94 */           level.levelEvent(1502, pos, 0);
/*  95 */           level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
/*     */         }
/*     */       
/*     */       } 
/*  99 */     } else if (!neighborSignal && !isToggledTooFrequently(level, pos, false)) {
/* 100 */       level.setBlock(pos, (BlockState)state.setValue(LIT, Boolean.valueOf(true)), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 107 */     if (((Boolean)state.getValue(LIT)).booleanValue() == hasNeighborSignal(level, pos, state) && !level.getBlockTicks().willTickThisTick(pos, this)) {
/* 108 */       level.scheduleTick(pos, this, 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 114 */     if (direction == Direction.DOWN) {
/* 115 */       return state.getSignal(level, pos, direction);
/*     */     }
/* 117 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 122 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 127 */     if (!((Boolean)state.getValue(LIT)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 131 */     double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
/* 132 */     double y = pos.getY() + 0.7D + (random.nextDouble() - 0.5D) * 0.2D;
/* 133 */     double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
/*     */     
/* 135 */     level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 140 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LIT }); }
/*     */   
/*     */   public static class Toggle
/*     */   {
/*     */     private final BlockPos pos;
/*     */     private final long when;
/*     */     
/*     */     public Toggle(BlockPos pos, long when) {
/* 148 */       this.pos = pos;
/* 149 */       this.when = when;
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isToggledTooFrequently(Level level, BlockPos pos, boolean add) {
/* 154 */     List<Toggle> toggles = (List)RECENT_TOGGLES.computeIfAbsent(level, k -> Lists.newArrayList());
/*     */     
/* 156 */     if (add) {
/* 157 */       toggles.add(new Toggle(pos.immutable(), level.getGameTime()));
/*     */     }
/*     */     
/* 160 */     int count = 0;
/* 161 */     for (Toggle toggle : toggles) {
/*     */       
/* 163 */       count++;
/* 164 */       if (toggle.pos.equals(pos) && count >= 8) {
/* 165 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 173 */   protected Orientation randomOrientation(Level level, BlockState state) { return ExperimentalRedstoneUtils.initialOrientation(level, null, Direction.UP); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RedstoneTorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */