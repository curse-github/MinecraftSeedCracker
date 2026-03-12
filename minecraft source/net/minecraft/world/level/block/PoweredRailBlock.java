/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ 
/*     */ public class PoweredRailBlock extends BaseRailBlock {
/*  15 */   public static final MapCodec<PoweredRailBlock> CODEC = simpleCodec(PoweredRailBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  19 */   public MapCodec<PoweredRailBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  22 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
/*  23 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   protected PoweredRailBlock(BlockBehaviour.Properties properties) {
/*  26 */     super(true, properties);
/*  27 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(SHAPE, RailShape.NORTH_SOUTH)).setValue(POWERED, Boolean.valueOf(false))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected boolean findPoweredRailSignal(Level level, BlockPos pos, BlockState state, boolean forward, int searchDepth) {
/*  31 */     if (searchDepth >= 8) {
/*  32 */       return false;
/*     */     }
/*     */     
/*  35 */     int x = pos.getX();
/*  36 */     int y = pos.getY();
/*  37 */     int z = pos.getZ();
/*     */     
/*  39 */     boolean checkBelow = true;
/*  40 */     RailShape shape = (RailShape)state.getValue(SHAPE);
/*  41 */     switch (shape) {
/*     */       case NORTH_SOUTH:
/*  43 */         if (forward) {
/*  44 */           z++; break;
/*     */         } 
/*  46 */         z--;
/*     */         break;
/*     */       
/*     */       case EAST_WEST:
/*  50 */         if (forward) {
/*  51 */           x--; break;
/*     */         } 
/*  53 */         x++;
/*     */         break;
/*     */       
/*     */       case ASCENDING_EAST:
/*  57 */         if (forward) {
/*  58 */           x--;
/*     */         } else {
/*  60 */           x++;
/*  61 */           y++;
/*  62 */           checkBelow = false;
/*     */         } 
/*  64 */         shape = RailShape.EAST_WEST;
/*     */         break;
/*     */       case ASCENDING_WEST:
/*  67 */         if (forward) {
/*  68 */           x--;
/*  69 */           y++;
/*  70 */           checkBelow = false;
/*     */         } else {
/*  72 */           x++;
/*     */         } 
/*  74 */         shape = RailShape.EAST_WEST;
/*     */         break;
/*     */       case ASCENDING_NORTH:
/*  77 */         if (forward) {
/*  78 */           z++;
/*     */         } else {
/*  80 */           z--;
/*  81 */           y++;
/*  82 */           checkBelow = false;
/*     */         } 
/*  84 */         shape = RailShape.NORTH_SOUTH;
/*     */         break;
/*     */       case ASCENDING_SOUTH:
/*  87 */         if (forward) {
/*  88 */           z++;
/*  89 */           y++;
/*  90 */           checkBelow = false;
/*     */         } else {
/*  92 */           z--;
/*     */         } 
/*  94 */         shape = RailShape.NORTH_SOUTH;
/*     */         break;
/*     */     } 
/*     */     
/*  98 */     if (isSameRailWithPower(level, new BlockPos(x, y, z), forward, searchDepth, shape)) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (checkBelow && isSameRailWithPower(level, new BlockPos(x, y - 1, z), forward, searchDepth, shape)) {
/* 102 */       return true;
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isSameRailWithPower(Level level, BlockPos pos, boolean forward, int searchDepth, RailShape dir) {
/* 108 */     BlockState state = level.getBlockState(pos);
/*     */     
/* 110 */     if (!state.is(this)) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     RailShape myShape = (RailShape)state.getValue(SHAPE);
/* 115 */     if (dir == RailShape.EAST_WEST && (myShape == RailShape.NORTH_SOUTH || myShape == RailShape.ASCENDING_NORTH || myShape == RailShape.ASCENDING_SOUTH)) {
/* 116 */       return false;
/*     */     }
/* 118 */     if (dir == RailShape.NORTH_SOUTH && (myShape == RailShape.EAST_WEST || myShape == RailShape.ASCENDING_EAST || myShape == RailShape.ASCENDING_WEST)) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     if (((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 123 */       if (level.hasNeighborSignal(pos)) {
/* 124 */         return true;
/*     */       }
/* 126 */       return findPoweredRailSignal(level, pos, state, forward, searchDepth + 1);
/*     */     } 
/*     */     
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {
/* 134 */     boolean isPowered = ((Boolean)state.getValue(POWERED)).booleanValue();
/* 135 */     boolean shouldPower = (level.hasNeighborSignal(pos) || findPoweredRailSignal(level, pos, state, true, 0) || findPoweredRailSignal(level, pos, state, false, 0));
/*     */     
/* 137 */     if (shouldPower != isPowered) {
/* 138 */       level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(shouldPower)), 3);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       level.updateNeighborsAt(pos.below(), this);
/* 144 */       if (((RailShape)state.getValue(SHAPE)).isSlope()) {
/* 145 */         level.updateNeighborsAt(pos.above(), this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public Property<RailShape> getShapeProperty() { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 157 */     RailShape currentShape = (RailShape)state.getValue(SHAPE);
/* 158 */     RailShape newShape = rotate(currentShape, rotation);
/* 159 */     return (BlockState)state.setValue(SHAPE, newShape);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 164 */     RailShape currentShape = (RailShape)state.getValue(SHAPE);
/* 165 */     RailShape newShape = mirror(currentShape, mirror);
/* 166 */     return (BlockState)state.setValue(SHAPE, newShape);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 171 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { SHAPE, POWERED, WATERLOGGED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PoweredRailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */