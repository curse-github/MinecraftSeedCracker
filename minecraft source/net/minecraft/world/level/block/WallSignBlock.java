/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallSignBlock extends SignBlock {
/*  27 */   public static final MapCodec<WallSignBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(WoodType.CODEC
/*  28 */         .fieldOf("wood_type").forGetter(SignBlock::type), 
/*  29 */         propertiesCodec())
/*  30 */       .apply(i, WallSignBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  34 */   public MapCodec<WallSignBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  37 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  39 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 4.5D, 12.5D, 14.0D, 16.0D));
/*     */   
/*     */   public WallSignBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  42 */     super(type, properties.sound(type.soundType()));
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  48 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return level.getBlockState(pos.relative(((Direction)state.getValue(FACING)).getOpposite())).isSolid(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  58 */     BlockState state = defaultBlockState();
/*  59 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/*  61 */     Level level1 = context.getLevel();
/*  62 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  64 */     Direction[] directions = context.getNearestLookingDirections();
/*  65 */     for (Direction direction : directions) {
/*  66 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  70 */         Direction facing = direction.getOpposite();
/*     */         
/*  72 */         state = (BlockState)state.setValue(FACING, facing);
/*  73 */         if (state.canSurvive(level1, pos)) {
/*  74 */           return (BlockState)state.setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */         }
/*     */       } 
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  83 */     if (directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
/*  84 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  86 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public float getYRotationDegrees(BlockState state) { return ((Direction)state.getValue(FACING)).toYRot(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public Vec3 getSignHitboxCenterPosition(BlockState state) { return ((VoxelShape)SHAPES.get(state.getValue(FACING))).bounds().getCenter(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, WATERLOGGED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WallSignBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */