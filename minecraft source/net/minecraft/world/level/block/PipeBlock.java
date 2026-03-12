/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class PipeBlock extends Block {
/* 20 */   public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
/* 21 */   public static final BooleanProperty EAST = BlockStateProperties.EAST;
/* 22 */   public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
/* 23 */   public static final BooleanProperty WEST = BlockStateProperties.WEST;
/* 24 */   public static final BooleanProperty UP = BlockStateProperties.UP;
/* 25 */   public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
/*    */   
/* 27 */   public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST, Direction.UP, UP, Direction.DOWN, DOWN)));
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final Function<BlockState, VoxelShape> shapes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected PipeBlock(float size, BlockBehaviour.Properties properties) {
/* 39 */     super(properties);
/*    */     
/* 41 */     this.shapes = makeShapes(size);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Function<BlockState, VoxelShape> makeShapes(float size) {
/* 48 */     VoxelShape core = Block.cube(size);
/* 49 */     Map<Direction, VoxelShape> shapes = Shapes.rotateAll(Block.boxZ(size, 0.0D, 8.0D));
/*    */     
/* 51 */     return getShapeForEachState(state -> {
/* 52 */           VoxelShape shape = core;
/* 53 */           for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/* 54 */             if (((Boolean)state.getValue((Property)entry.getValue())).booleanValue()) {
/* 55 */               shape = Shapes.or((VoxelShape)shapes.get(entry.getKey()), shape);
/*    */             }
/*    */           } 
/* 58 */           return shape;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 64 */   protected boolean propagatesSkylightDown(BlockState state) { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*    */   
/*    */   protected abstract MapCodec<? extends PipeBlock> codec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PipeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */