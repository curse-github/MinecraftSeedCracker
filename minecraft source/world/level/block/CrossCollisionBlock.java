/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class CrossCollisionBlock extends Block implements SimpleWaterloggedBlock {
/*  22 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  23 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  24 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  25 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*  26 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  27 */   public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = (Map)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(e -> ((Direction)e.getKey()).getAxis().isHorizontal()).collect(Util.toMap());
/*     */   
/*     */   private final Function<BlockState, VoxelShape> collisionShapes;
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected CrossCollisionBlock(float postWidth, float postHeight, float wallWidth, float wallHeight, float collisionHeight, BlockBehaviour.Properties properties) {
/*  33 */     super(properties);
/*     */     
/*  35 */     this.collisionShapes = makeShapes(postWidth, collisionHeight, wallWidth, 0.0F, collisionHeight);
/*  36 */     this.shapes = makeShapes(postWidth, postHeight, wallWidth, 0.0F, wallHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Function<BlockState, VoxelShape> makeShapes(float postWidth, float postHeight, float wallWidth, float wallBottom, float wallTop) {
/*  43 */     VoxelShape post = Block.column(postWidth, 0.0D, postHeight);
/*  44 */     Map<Direction, VoxelShape> arms = Shapes.rotateHorizontal(Block.boxZ(wallWidth, wallBottom, wallTop, 0.0D, 8.0D));
/*     */     
/*  46 */     return getShapeForEachState(state -> {
/*  47 */           VoxelShape shape = post;
/*     */           
/*  49 */           for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*  50 */             if (((Boolean)state.getValue((Property)entry.getValue())).booleanValue()) {
/*  51 */               shape = Shapes.or(shape, (VoxelShape)arms.get(entry.getKey()));
/*     */             }
/*     */           } 
/*  54 */           return shape;
/*     */         }new Property[] { WATERLOGGED });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  60 */   protected boolean propagatesSkylightDown(BlockState state) { return !((Boolean)state.getValue(WATERLOGGED)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.collisionShapes.apply(state); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  75 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  76 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  78 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  88 */     switch (rotation) {
/*     */       case LEFT_RIGHT:
/*  90 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(SOUTH))).setValue(EAST, (Boolean)state.getValue(WEST))).setValue(SOUTH, (Boolean)state.getValue(NORTH))).setValue(WEST, (Boolean)state.getValue(EAST));
/*     */       case FRONT_BACK:
/*  92 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(EAST))).setValue(EAST, (Boolean)state.getValue(SOUTH))).setValue(SOUTH, (Boolean)state.getValue(WEST))).setValue(WEST, (Boolean)state.getValue(NORTH));
/*     */       case null:
/*  94 */         return (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(WEST))).setValue(EAST, (Boolean)state.getValue(NORTH))).setValue(SOUTH, (Boolean)state.getValue(EAST))).setValue(WEST, (Boolean)state.getValue(SOUTH));
/*     */     } 
/*  96 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 102 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 104 */         return (BlockState)((BlockState)state.setValue(NORTH, (Boolean)state.getValue(SOUTH))).setValue(SOUTH, (Boolean)state.getValue(NORTH));
/*     */       case FRONT_BACK:
/* 106 */         return (BlockState)((BlockState)state.setValue(EAST, (Boolean)state.getValue(WEST))).setValue(WEST, (Boolean)state.getValue(EAST));
/*     */     } 
/*     */ 
/*     */     
/* 110 */     return super.mirror(state, mirror);
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends CrossCollisionBlock> codec();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CrossCollisionBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */