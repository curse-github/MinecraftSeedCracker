/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TrapDoorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
/*  41 */   public static final MapCodec<TrapDoorBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockSetType.CODEC
/*  42 */         .fieldOf("block_set_type").forGetter(()), 
/*  43 */         propertiesCodec())
/*  44 */       .apply(i, TrapDoorBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  48 */   public MapCodec<? extends TrapDoorBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  51 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  52 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*  53 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  54 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  56 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   protected TrapDoorBlock(BlockSetType type, BlockBehaviour.Properties properties) {
/*  61 */     super(properties.sound(type.soundType()));
/*  62 */     this.type = type;
/*  63 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(OPEN, Boolean.valueOf(false))).setValue(HALF, Half.BOTTOM)).setValue(POWERED, Boolean.valueOf(false))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  68 */     return (VoxelShape)SHAPES.get(((Boolean)state.getValue(OPEN)).booleanValue() ? 
/*  69 */         state.getValue(FACING) : (
/*  70 */         (state.getValue(HALF) == Half.TOP) ? Direction.DOWN : Direction.UP));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  76 */     switch (type) {
/*     */       case LAND:
/*  78 */         return ((Boolean)state.getValue(OPEN)).booleanValue();
/*     */       case WATER:
/*  80 */         return ((Boolean)state.getValue(WATERLOGGED)).booleanValue();
/*     */       case AIR:
/*  82 */         return ((Boolean)state.getValue(OPEN)).booleanValue();
/*     */     } 
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  90 */     if (!this.type.canOpenByHand()) {
/*  91 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  94 */     toggle(state, level, pos, player);
/*  95 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 100 */     if (explosion.canTriggerBlocks() && this.type.canOpenByWindCharge() && !((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 101 */       toggle(state, level, pos, null);
/*     */     }
/* 103 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   private void toggle(BlockState state, Level level, BlockPos pos, Player player) {
/* 107 */     BlockState updated = (BlockState)state.cycle(OPEN);
/* 108 */     level.setBlock(pos, updated, 2);
/*     */     
/* 110 */     if (((Boolean)updated.getValue(WATERLOGGED)).booleanValue()) {
/* 111 */       level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 114 */     playSound(player, level, pos, ((Boolean)updated.getValue(OPEN)).booleanValue());
/*     */   }
/*     */   
/*     */   protected void playSound(Player player, Level level, BlockPos pos, boolean opening) {
/* 118 */     level.playSound(player, pos, opening ? this.type.trapdoorOpen() : this.type.trapdoorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 119 */     level.gameEvent(player, opening ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 124 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     boolean signal = level.hasNeighborSignal(pos);
/* 129 */     if (signal != ((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 130 */       if (((Boolean)state.getValue(OPEN)).booleanValue() != signal) {
/* 131 */         state = (BlockState)state.setValue(OPEN, Boolean.valueOf(signal));
/* 132 */         playSound(null, level, pos, signal);
/*     */       } 
/* 134 */       level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(signal)), 2);
/*     */       
/* 136 */       if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 137 */         level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 144 */     BlockState state = defaultBlockState();
/* 145 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/* 147 */     Direction clickedFace = context.getClickedFace();
/* 148 */     if (context.replacingClickedOnBlock() || !clickedFace.getAxis().isHorizontal()) {
/* 149 */       state = (BlockState)((BlockState)state.setValue(FACING, context.getHorizontalDirection().getOpposite())).setValue(HALF, (clickedFace == Direction.UP) ? Half.BOTTOM : Half.TOP);
/*     */     } else {
/* 151 */       state = (BlockState)((BlockState)state.setValue(FACING, clickedFace)).setValue(HALF, ((context.getClickLocation()).y - context.getClickedPos().getY() > 0.5D) ? Half.TOP : Half.BOTTOM);
/*     */     } 
/* 153 */     if (context.getLevel().hasNeighborSignal(context.getClickedPos())) {
/* 154 */       state = (BlockState)((BlockState)state.setValue(OPEN, Boolean.valueOf(true))).setValue(POWERED, Boolean.valueOf(true));
/*     */     }
/* 156 */     return (BlockState)state.setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 161 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, OPEN, HALF, POWERED, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 166 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 167 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 169 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 174 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 175 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 178 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/* 182 */   protected BlockSetType getType() { return this.type; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TrapDoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */