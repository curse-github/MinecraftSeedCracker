/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TripWireBlock extends Block {
/*  31 */   public static final MapCodec<TripWireBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK
/*  32 */         .byNameCodec().fieldOf("hook").forGetter(()), 
/*  33 */         propertiesCodec())
/*  34 */       .apply(i, TripWireBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  38 */   public MapCodec<TripWireBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  41 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  42 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*  43 */   public static final BooleanProperty DISARMED = BlockStateProperties.DISARMED;
/*  44 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  45 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  46 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  47 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*     */   
/*  49 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = CrossCollisionBlock.PROPERTY_BY_DIRECTION;
/*     */   
/*  51 */   private static final VoxelShape SHAPE_ATTACHED = Block.column(16.0D, 1.0D, 2.5D);
/*  52 */   private static final VoxelShape SHAPE_NOT_ATTACHED = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*     */   private static final int RECHECK_PERIOD = 10;
/*     */   
/*     */   private final Block hook;
/*     */   
/*     */   public TripWireBlock(Block hook, BlockBehaviour.Properties properties) {
/*  59 */     super(properties);
/*  60 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWERED, Boolean.valueOf(false))).setValue(ATTACHED, Boolean.valueOf(false))).setValue(DISARMED, Boolean.valueOf(false))).setValue(NORTH, Boolean.valueOf(false))).setValue(EAST, Boolean.valueOf(false))).setValue(SOUTH, Boolean.valueOf(false))).setValue(WEST, Boolean.valueOf(false)));
/*  61 */     this.hook = hook;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return ((Boolean)state.getValue(ATTACHED)).booleanValue() ? SHAPE_ATTACHED : SHAPE_NOT_ATTACHED; }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  71 */     Level level1 = context.getLevel();
/*  72 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  74 */     return (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/*  75 */       .setValue(NORTH, Boolean.valueOf(shouldConnectTo(level1.getBlockState(pos.north()), Direction.NORTH))))
/*  76 */       .setValue(EAST, Boolean.valueOf(shouldConnectTo(level1.getBlockState(pos.east()), Direction.EAST))))
/*  77 */       .setValue(SOUTH, Boolean.valueOf(shouldConnectTo(level1.getBlockState(pos.south()), Direction.SOUTH))))
/*  78 */       .setValue(WEST, Boolean.valueOf(shouldConnectTo(level1.getBlockState(pos.west()), Direction.WEST)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  83 */     if (directionToNeighbour.getAxis().isHorizontal()) {
/*  84 */       return (BlockState)state.setValue((Property)PROPERTY_BY_DIRECTION.get(directionToNeighbour), Boolean.valueOf(shouldConnectTo(neighbourState, directionToNeighbour)));
/*     */     }
/*  86 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  91 */     if (oldState.is(state.getBlock())) {
/*     */       return;
/*     */     }
/*  94 */     updateSource(level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/*  99 */     if (!movedByPiston) {
/* 100 */       updateSource(level, pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(true)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 106 */     if (!level.isClientSide() && !player.getMainHandItem().isEmpty() && player.getMainHandItem().is(Items.SHEARS)) {
/* 107 */       level.setBlock(pos, (BlockState)state.setValue(DISARMED, Boolean.valueOf(true)), 260);
/* 108 */       level.gameEvent(player, GameEvent.SHEAR, pos);
/*     */     } 
/* 110 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */   
/*     */   private void updateSource(Level level, BlockPos pos, BlockState state) {
/* 114 */     for (Direction direction : new Direction[] { Direction.SOUTH, Direction.WEST }) {
/* 115 */       for (int i = 1; i < 42; i++) {
/* 116 */         BlockPos testPos = pos.relative(direction, i);
/* 117 */         BlockState block = level.getBlockState(testPos);
/*     */         
/* 119 */         if (block.is(this.hook)) {
/* 120 */           if (block.getValue(TripWireHookBlock.FACING) == direction.getOpposite()) {
/* 121 */             TripWireHookBlock.calculateState(level, testPos, block, false, true, i, state);
/*     */           }
/*     */           break;
/*     */         } 
/* 125 */         if (!block.is(this)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 134 */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) { return state.getShape(level, pos); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 139 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 143 */     if (((Boolean)state.getValue(POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 147 */     checkPressed(level, pos, List.of(entity));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 152 */     if (!((Boolean)level.getBlockState(pos).getValue(POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 156 */     checkPressed(level, pos);
/*     */   }
/*     */   
/*     */   private void checkPressed(Level level, BlockPos pos) {
/* 160 */     BlockState state = level.getBlockState(pos);
/* 161 */     List<? extends Entity> entities = level.getEntities(null, state.getShape(level, pos).bounds().move(pos));
/* 162 */     checkPressed(level, pos, entities);
/*     */   }
/*     */   
/*     */   private void checkPressed(Level level, BlockPos pos, List<? extends Entity> entities) {
/* 166 */     BlockState state = level.getBlockState(pos);
/* 167 */     boolean wasPressed = ((Boolean)state.getValue(POWERED)).booleanValue();
/* 168 */     boolean shouldBePressed = false;
/*     */     
/* 170 */     if (!entities.isEmpty()) {
/* 171 */       for (Entity entity : entities) {
/* 172 */         if (!entity.isIgnoringBlockTriggers()) {
/* 173 */           shouldBePressed = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 179 */     if (shouldBePressed != wasPressed) {
/* 180 */       state = (BlockState)state.setValue(POWERED, Boolean.valueOf(shouldBePressed));
/* 181 */       level.setBlock(pos, state, 3);
/* 182 */       updateSource(level, pos, state);
/*     */     } 
/*     */     
/* 185 */     if (shouldBePressed) {
/* 186 */       level.scheduleTick(new BlockPos(pos), this, 10);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldConnectTo(BlockState blockState, Direction direction) {
/* 191 */     if (blockState.is(this.hook)) {
/* 192 */       return (blockState.getValue(TripWireHookBlock.FACING) == direction.getOpposite());
/*     */     }
/*     */     
/* 195 */     return blockState.is(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 200 */     switch (rotation) {
/*     */       case LEFT_RIGHT:
/* 202 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(SOUTH))).setValue(EAST, (Boolean)state.getValue(WEST))).setValue(SOUTH, (Boolean)state.getValue(NORTH))).setValue(WEST, (Boolean)state.getValue(EAST));
/*     */       case FRONT_BACK:
/* 204 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(EAST))).setValue(EAST, (Boolean)state.getValue(SOUTH))).setValue(SOUTH, (Boolean)state.getValue(WEST))).setValue(WEST, (Boolean)state.getValue(NORTH));
/*     */       case null:
/* 206 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(WEST))).setValue(EAST, (Boolean)state.getValue(NORTH))).setValue(SOUTH, (Boolean)state.getValue(EAST))).setValue(WEST, (Boolean)state.getValue(SOUTH));
/*     */     } 
/* 208 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 214 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 216 */         return (BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(SOUTH))).setValue(SOUTH, (Boolean)state.getValue(NORTH));
/*     */       case FRONT_BACK:
/* 218 */         return (BlockState)((BlockState)state.setValue(EAST, (Boolean)state.getValue(WEST))).setValue(WEST, (Boolean)state.getValue(EAST));
/*     */     } 
/*     */ 
/*     */     
/* 222 */     return super.mirror(state, mirror);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 227 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { POWERED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TripWireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */