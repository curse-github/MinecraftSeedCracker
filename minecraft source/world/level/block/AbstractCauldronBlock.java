/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.cauldron.CauldronInteraction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class AbstractCauldronBlock extends Block {
/*     */   protected static final int FLOOR_LEVEL = 4;
/*  28 */   private static final VoxelShape SHAPE_INSIDE = Block.column(12.0D, 4.0D, 16.0D);
/*  29 */   protected static final VoxelShape SHAPE = (VoxelShape)Util.make(() -> {
/*  30 */         legWidth = 4;
/*  31 */         int legHeight = 3;
/*  32 */         int legThickness = 2;
/*     */         
/*  34 */         return Shapes.join(
/*  35 */             Shapes.block(), 
/*  36 */             Shapes.or(
/*  37 */               Block.column(16.0D, 8.0D, 0.0D, 3.0D), new VoxelShape[] {
/*  38 */                 Block.column(8.0D, 16.0D, 0.0D, 3.0D), 
/*  39 */                 Block.column(12.0D, 0.0D, 3.0D), SHAPE_INSIDE
/*     */               }), BooleanOp.ONLY_FIRST);
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final CauldronInteraction.InteractionMap interactions;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractCauldronBlock(BlockBehaviour.Properties properties, CauldronInteraction.InteractionMap interactions) {
/*  52 */     super(properties);
/*  53 */     this.interactions = interactions;
/*     */   }
/*     */ 
/*     */   
/*  57 */   protected double getContentHeight(BlockState state) { return 0.0D; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  62 */     CauldronInteraction behavior = (CauldronInteraction)this.interactions.map().get(itemStack.getItem());
/*  63 */     return behavior.interact(state, level, pos, player, hand, itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) { return SHAPE_INSIDE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState cauldronState, ServerLevel level, BlockPos pos, RandomSource random) {
/*  90 */     BlockPos stalactitePos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(level, pos);
/*  91 */     if (stalactitePos == null) {
/*     */       return;
/*     */     }
/*  94 */     Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(level, stalactitePos);
/*  95 */     if (fluid != Fluids.EMPTY && canReceiveStalactiteDrip(fluid)) {
/*  96 */       receiveStalactiteDrip(cauldronState, level, pos, fluid);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 101 */   protected boolean canReceiveStalactiteDrip(Fluid fluid) { return false; }
/*     */   
/*     */   protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {}
/*     */   
/*     */   protected abstract MapCodec<? extends AbstractCauldronBlock> codec();
/*     */   
/*     */   public abstract boolean isFull(BlockState paramBlockState);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\AbstractCauldronBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */