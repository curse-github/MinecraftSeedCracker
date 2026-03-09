/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class DetectorRailBlock
/*     */   extends BaseRailBlock {
/*  32 */   public static final MapCodec<DetectorRailBlock> CODEC = simpleCodec(DetectorRailBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  36 */   public MapCodec<DetectorRailBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  39 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
/*  40 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   private static final int PRESSED_CHECK_PERIOD = 20;
/*     */   
/*     */   public DetectorRailBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(true, properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWERED, Boolean.valueOf(false))).setValue(SHAPE, RailShape.NORTH_SOUTH)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  55 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     if (((Boolean)state.getValue(POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  63 */     checkPressed(level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  68 */     if (!((Boolean)state.getValue(POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     checkPressed(level, pos, state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  77 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  82 */     if (!((Boolean)state.getValue(POWERED)).booleanValue()) {
/*  83 */       return 0;
/*     */     }
/*  85 */     return (direction == Direction.UP) ? 15 : 0;
/*     */   }
/*     */   
/*     */   private void checkPressed(Level level, BlockPos pos, BlockState state) {
/*  89 */     if (!canSurvive(state, level, pos)) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     boolean wasPressed = ((Boolean)state.getValue(POWERED)).booleanValue();
/*  94 */     boolean shouldBePressed = false;
/*     */     
/*  96 */     List<AbstractMinecart> entities = getInteractingMinecartOfType(level, pos, AbstractMinecart.class, e -> true);
/*  97 */     if (!entities.isEmpty()) {
/*  98 */       shouldBePressed = true;
/*     */     }
/*     */     
/* 101 */     if (shouldBePressed && !wasPressed) {
/* 102 */       BlockState newState = (BlockState)state.setValue(POWERED, Boolean.valueOf(true));
/* 103 */       level.setBlock(pos, newState, 3);
/* 104 */       updatePowerToConnected(level, pos, newState, true);
/* 105 */       level.updateNeighborsAt(pos, this);
/* 106 */       level.updateNeighborsAt(pos.below(), this);
/* 107 */       level.setBlocksDirty(pos, state, newState);
/*     */     } 
/*     */     
/* 110 */     if (!shouldBePressed && wasPressed) {
/* 111 */       BlockState newState = (BlockState)state.setValue(POWERED, Boolean.valueOf(false));
/* 112 */       level.setBlock(pos, newState, 3);
/* 113 */       updatePowerToConnected(level, pos, newState, false);
/* 114 */       level.updateNeighborsAt(pos, this);
/* 115 */       level.updateNeighborsAt(pos.below(), this);
/* 116 */       level.setBlocksDirty(pos, state, newState);
/*     */     } 
/*     */     
/* 119 */     if (shouldBePressed) {
/* 120 */       level.scheduleTick(pos, this, 20);
/*     */     }
/*     */     
/* 123 */     level.updateNeighbourForOutputSignal(pos, this);
/*     */   }
/*     */   
/*     */   protected void updatePowerToConnected(Level level, BlockPos pos, BlockState state, boolean powered) {
/* 127 */     RailState rail = new RailState(level, pos, state);
/* 128 */     List<BlockPos> connections = rail.getConnections();
/*     */     
/* 130 */     for (BlockPos connectionPos : connections) {
/* 131 */       BlockState connectionState = level.getBlockState(connectionPos);
/* 132 */       level.neighborChanged(connectionState, connectionPos, connectionState.getBlock(), null, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 138 */     if (oldState.is(state.getBlock())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     BlockState updatedState = updateState(state, level, pos, movedByPiston);
/*     */ 
/*     */     
/* 150 */     checkPressed(level, pos, updatedState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public Property<RailShape> getShapeProperty() { return SHAPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 165 */     if (((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 166 */       List<MinecartCommandBlock> commandBlocks = getInteractingMinecartOfType(level, pos, MinecartCommandBlock.class, e -> true);
/* 167 */       if (!commandBlocks.isEmpty()) {
/* 168 */         return ((MinecartCommandBlock)commandBlocks.get(0)).getCommandBlock().getSuccessCount();
/*     */       }
/*     */       
/* 171 */       List<AbstractMinecart> entities = getInteractingMinecartOfType(level, pos, AbstractMinecart.class, EntitySelector.CONTAINER_ENTITY_SELECTOR);
/* 172 */       if (!entities.isEmpty()) {
/* 173 */         return AbstractContainerMenu.getRedstoneSignalFromContainer((Container)entities.get(0));
/*     */       }
/*     */     } 
/*     */     
/* 177 */     return 0;
/*     */   }
/*     */ 
/*     */   
/* 181 */   private <T extends AbstractMinecart> List<T> getInteractingMinecartOfType(Level level, BlockPos pos, Class<T> type, Predicate<Entity> containerEntitySelector) { return level.getEntitiesOfClass(type, getSearchBB(pos), containerEntitySelector); }
/*     */ 
/*     */   
/*     */   private AABB getSearchBB(BlockPos pos) {
/* 185 */     double b = 0.2D;
/*     */     
/* 187 */     return new AABB(pos.getX() + 0.2D, pos.getY(), pos.getZ() + 0.2D, (pos.getX() + 1) - 0.2D, (pos.getY() + 1) - 0.2D, (pos.getZ() + 1) - 0.2D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 192 */     RailShape currentShape = (RailShape)state.getValue(SHAPE);
/* 193 */     RailShape newShape = rotate(currentShape, rotation);
/* 194 */     return (BlockState)state.setValue(SHAPE, newShape);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 199 */     RailShape currentShape = (RailShape)state.getValue(SHAPE);
/* 200 */     RailShape newShape = mirror(currentShape, mirror);
/* 201 */     return (BlockState)state.setValue(SHAPE, newShape);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 206 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { SHAPE, POWERED, WATERLOGGED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DetectorRailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */