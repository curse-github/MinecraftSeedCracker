/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.inventory.GrindstoneMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class GrindstoneBlock extends FaceAttachedHorizontalDirectionalBlock {
/*  31 */   public static final MapCodec<GrindstoneBlock> CODEC = simpleCodec(GrindstoneBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  35 */   public MapCodec<GrindstoneBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  38 */   private static final Component CONTAINER_TITLE = Component.translatable("container.grindstone_title");
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected GrindstoneBlock(BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  44 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(FACE, AttachFace.WALL));
/*     */     
/*  46 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  50 */     VoxelShape leftLegs = Shapes.or(
/*  51 */         Block.box(2.0D, 6.0D, 7.0D, 4.0D, 10.0D, 16.0D), 
/*  52 */         Block.box(2.0D, 5.0D, 3.0D, 4.0D, 11.0D, 9.0D));
/*     */     
/*  54 */     VoxelShape rightLegs = Shapes.rotate(leftLegs, OctahedralGroup.INVERT_X);
/*     */     
/*  56 */     VoxelShape north = Shapes.or(
/*  57 */         Block.boxZ(8.0D, 2.0D, 14.0D, 0.0D, 12.0D), new VoxelShape[] { leftLegs, rightLegs });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     Map<AttachFace, Map<Direction, VoxelShape>> attachFace = Shapes.rotateAttachFace(north);
/*     */     
/*  64 */     return getShapeForEachState(state -> (VoxelShape)((Map)attachFace.get(state.getValue(FACE))).get(state.getValue(FACING)));
/*     */   }
/*     */ 
/*     */   
/*  68 */   private VoxelShape getVoxelShape(BlockState state) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return getVoxelShape(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return getVoxelShape(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  88 */     if (!level.isClientSide()) {
/*  89 */       player.openMenu(state.getMenuProvider(level, pos));
/*  90 */       player.awardStat(Stats.INTERACT_WITH_GRINDSTONE);
/*     */     } 
/*  92 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) { return new SimpleMenuProvider((containerId, inventory, player) -> new GrindstoneMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, FACE }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\GrindstoneBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */