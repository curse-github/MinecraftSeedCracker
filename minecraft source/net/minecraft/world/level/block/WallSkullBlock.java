/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WallSkullBlock extends AbstractSkullBlock {
/* 19 */   public static final MapCodec<WallSkullBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SkullBlock.Type.CODEC
/* 20 */         .fieldOf("kind").forGetter(AbstractSkullBlock::getType), 
/* 21 */         propertiesCodec())
/* 22 */       .apply(i, WallSkullBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 26 */   public MapCodec<? extends WallSkullBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 29 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 31 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(8.0D, 8.0D, 16.0D));
/*    */   
/*    */   protected WallSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
/* 34 */     super(type, properties);
/* 35 */     registerDefaultState((BlockState)defaultBlockState().setValue(FACING, Direction.NORTH));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 45 */     BlockState state = super.getStateForPlacement(context);
/*    */     
/* 47 */     Level level1 = context.getLevel();
/* 48 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 50 */     Direction[] directions = context.getNearestLookingDirections();
/* 51 */     for (Direction direction : directions) {
/* 52 */       if (direction.getAxis().isHorizontal()) {
/*    */ 
/*    */ 
/*    */         
/* 56 */         Direction facing = direction.getOpposite();
/*    */         
/* 58 */         state = (BlockState)state.setValue(FACING, facing);
/* 59 */         if (!level1.getBlockState(pos.relative(direction)).canBeReplaced(context)) {
/* 60 */           return state;
/*    */         }
/*    */       } 
/*    */     } 
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 69 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 79 */     super.createBlockStateDefinition(builder);
/* 80 */     builder.add(new Property[] { FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WallSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */