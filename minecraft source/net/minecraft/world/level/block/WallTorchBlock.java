/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallTorchBlock extends TorchBlock {
/*  26 */   public static final MapCodec<WallTorchBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PARTICLE_OPTIONS_FIELD
/*  27 */         .forGetter(()), 
/*  28 */         propertiesCodec())
/*  29 */       .apply(i, WallTorchBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  33 */   public MapCodec<WallTorchBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  36 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  38 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(5.0D, 3.0D, 13.0D, 11.0D, 16.0D));
/*     */   
/*     */   protected WallTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
/*  41 */     super(flameParticle, properties);
/*  42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return getShape(state); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static VoxelShape getShape(BlockState state) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return canSurvive(level, pos, (Direction)state.getValue(FACING)); }
/*     */ 
/*     */   
/*     */   public static boolean canSurvive(LevelReader level, BlockPos pos, Direction facing) {
/*  60 */     BlockPos relativePos = pos.relative(facing.getOpposite());
/*  61 */     BlockState relativeState = level.getBlockState(relativePos);
/*  62 */     return relativeState.isFaceSturdy(level, relativePos, facing);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  67 */     BlockState state = defaultBlockState();
/*     */     
/*  69 */     Level level1 = context.getLevel();
/*  70 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  72 */     Direction[] directions = context.getNearestLookingDirections();
/*  73 */     for (Direction direction : directions) {
/*  74 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  78 */         Direction facing = direction.getOpposite();
/*     */         
/*  80 */         state = (BlockState)state.setValue(FACING, facing);
/*  81 */         if (state.canSurvive(level1, pos)) {
/*  82 */           return state;
/*     */         }
/*     */       } 
/*     */     } 
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  91 */     if (directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
/*  92 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  94 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  99 */     Direction direction = (Direction)state.getValue(FACING);
/* 100 */     double x = pos.getX() + 0.5D;
/* 101 */     double y = pos.getY() + 0.7D;
/* 102 */     double z = pos.getZ() + 0.5D;
/* 103 */     double h = 0.22D;
/* 104 */     double r = 0.27D;
/*     */     
/* 106 */     Direction opposite = direction.getOpposite();
/* 107 */     level.addParticle(ParticleTypes.SMOKE, x + 0.27D * opposite.getStepX(), y + 0.22D, z + 0.27D * opposite.getStepZ(), 0.0D, 0.0D, 0.0D);
/* 108 */     level.addParticle(this.flameParticle, x + 0.27D * opposite.getStepX(), y + 0.22D, z + 0.27D * opposite.getStepZ(), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WallTorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */