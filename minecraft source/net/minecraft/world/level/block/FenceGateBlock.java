/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
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
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FenceGateBlock extends HorizontalDirectionalBlock {
/*  41 */   public static final MapCodec<FenceGateBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WoodType.CODEC
/*  42 */         .fieldOf("wood_type").forGetter(()), 
/*  43 */         propertiesCodec())
/*  44 */       .apply(i, FenceGateBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  48 */   public MapCodec<FenceGateBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  51 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  52 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  53 */   public static final BooleanProperty IN_WALL = BlockStateProperties.IN_WALL;
/*     */   
/*  55 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Block.cube(16.0D, 16.0D, 4.0D));
/*  56 */   private static final Map<Direction.Axis, VoxelShape> SHAPES_WALL = Maps.newEnumMap(Util.mapValues(SHAPES, v -> Shapes.join(v, Block.column(16.0D, 13.0D, 16.0D), BooleanOp.ONLY_FIRST)));
/*     */   
/*  58 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_COLLISION = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 0.0D, 24.0D));
/*  59 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_SUPPORT = Shapes.rotateHorizontalAxis(Block.column(16.0D, 4.0D, 5.0D, 24.0D));
/*     */   
/*  61 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_OCCLUSION = Shapes.rotateHorizontalAxis(Shapes.or(
/*  62 */         Block.box(0.0D, 5.0D, 7.0D, 2.0D, 16.0D, 9.0D), 
/*  63 */         Block.box(14.0D, 5.0D, 7.0D, 16.0D, 16.0D, 9.0D)));
/*     */ 
/*     */   
/*  66 */   private static final Map<Direction.Axis, VoxelShape> SHAPE_OCCLUSION_WALL = Maps.newEnumMap(Util.mapValues(SHAPE_OCCLUSION, v -> v.move(0.0D, -0.1875D, 0.0D).optimize()));
/*     */   
/*     */   private final WoodType type;
/*     */   
/*     */   public FenceGateBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  71 */     super(properties.sound(type.soundType()));
/*  72 */     this.type = type;
/*     */     
/*  74 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(OPEN, Boolean.valueOf(false))).setValue(POWERED, Boolean.valueOf(false))).setValue(IN_WALL, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  79 */     Direction.Axis axis = ((Direction)state.getValue(FACING)).getAxis();
/*  80 */     return (VoxelShape)(((Boolean)state.getValue(IN_WALL)).booleanValue() ? SHAPES_WALL : SHAPES).get(axis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  85 */     Direction.Axis axis = directionToNeighbour.getAxis();
/*  86 */     if (((Direction)state.getValue(FACING)).getClockWise().getAxis() == axis) {
/*  87 */       boolean inWall = (isWall(neighbourState) || isWall(level.getBlockState(pos.relative(directionToNeighbour.getOpposite()))));
/*  88 */       return (BlockState)state.setValue(IN_WALL, Boolean.valueOf(inWall));
/*     */     } 
/*     */     
/*  91 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  96 */     Direction.Axis axis = ((Direction)state.getValue(FACING)).getAxis();
/*     */     
/*  98 */     return ((Boolean)state.getValue(OPEN)).booleanValue() ? Shapes.empty() : (VoxelShape)SHAPE_SUPPORT.get(axis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 103 */     Direction.Axis axis = ((Direction)state.getValue(FACING)).getAxis();
/* 104 */     return ((Boolean)state.getValue(OPEN)).booleanValue() ? Shapes.empty() : (VoxelShape)SHAPE_COLLISION.get(axis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState state) {
/* 109 */     Direction.Axis axis = ((Direction)state.getValue(FACING)).getAxis();
/* 110 */     return (VoxelShape)(((Boolean)state.getValue(IN_WALL)).booleanValue() ? SHAPE_OCCLUSION_WALL : SHAPE_OCCLUSION).get(axis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 115 */     switch (type) {
/*     */       case LAND:
/* 117 */         return ((Boolean)state.getValue(OPEN)).booleanValue();
/*     */       case WATER:
/* 119 */         return false;
/*     */       case AIR:
/* 121 */         return ((Boolean)state.getValue(OPEN)).booleanValue();
/*     */     } 
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 129 */     Level level = context.getLevel();
/* 130 */     BlockPos pos = context.getClickedPos();
/*     */     
/* 132 */     boolean isOpen = level.hasNeighborSignal(pos);
/* 133 */     Direction direction = context.getHorizontalDirection();
/*     */     
/* 135 */     Direction.Axis axis = direction.getAxis();
/*     */     
/* 137 */     boolean inWall = ((axis == Direction.Axis.Z && (isWall(level.getBlockState(pos.west())) || isWall(level.getBlockState(pos.east())))) || (axis == Direction.Axis.X && (isWall(level.getBlockState(pos.north())) || isWall(level.getBlockState(pos.south())))));
/* 138 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue(FACING, direction)).setValue(OPEN, Boolean.valueOf(isOpen))).setValue(POWERED, Boolean.valueOf(isOpen))).setValue(IN_WALL, Boolean.valueOf(inWall));
/*     */   }
/*     */ 
/*     */   
/* 142 */   private boolean isWall(BlockState state) { return state.is(BlockTags.WALLS); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 147 */     if (((Boolean)state.getValue(OPEN)).booleanValue()) {
/* 148 */       state = (BlockState)state.setValue(OPEN, Boolean.valueOf(false));
/* 149 */       level.setBlock(pos, state, 10);
/*     */     } else {
/*     */       
/* 152 */       Direction direction = player.getDirection();
/* 153 */       if (state.getValue(FACING) == direction.getOpposite()) {
/* 154 */         state = (BlockState)state.setValue(FACING, direction);
/*     */       }
/* 156 */       state = (BlockState)state.setValue(OPEN, Boolean.valueOf(true));
/* 157 */       level.setBlock(pos, state, 10);
/*     */     } 
/*     */     
/* 160 */     boolean opens = ((Boolean)state.getValue(OPEN)).booleanValue();
/*     */     
/* 162 */     level.playSound(player, pos, opens ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 163 */     level.gameEvent(player, opens ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
/* 164 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 169 */     if (explosion.canTriggerBlocks() && !((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 170 */       boolean open = ((Boolean)state.getValue(OPEN)).booleanValue();
/* 171 */       level.setBlockAndUpdate(pos, (BlockState)state.setValue(OPEN, Boolean.valueOf(!open)));
/*     */       
/* 173 */       level.playSound(null, pos, open ? this.type.fenceGateClose() : this.type.fenceGateOpen(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 174 */       level.gameEvent(open ? GameEvent.BLOCK_CLOSE : GameEvent.BLOCK_OPEN, pos, GameEvent.Context.of(state));
/*     */     } 
/* 176 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 181 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 185 */     boolean hasPower = level.hasNeighborSignal(pos);
/* 186 */     if (((Boolean)state.getValue(POWERED)).booleanValue() != hasPower) {
/* 187 */       level.setBlock(pos, (BlockState)((BlockState)state.setValue(POWERED, Boolean.valueOf(hasPower))).setValue(OPEN, Boolean.valueOf(hasPower)), 2);
/* 188 */       if (((Boolean)state.getValue(OPEN)).booleanValue() != hasPower) {
/* 189 */         level.playSound(null, pos, hasPower ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 190 */         level.gameEvent(null, hasPower ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 197 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, OPEN, POWERED, IN_WALL }); }
/*     */ 
/*     */ 
/*     */   
/* 201 */   public static boolean connectsToDirection(BlockState state, Direction direction) { return (((Direction)state.getValue(FACING)).getAxis() == direction.getClockWise().getAxis()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FenceGateBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */