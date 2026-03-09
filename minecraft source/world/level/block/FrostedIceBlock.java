/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class FrostedIceBlock extends IceBlock {
/*  22 */   public static final MapCodec<FrostedIceBlock> CODEC = simpleCodec(FrostedIceBlock::new);
/*     */   
/*     */   public static final int MAX_AGE = 3;
/*     */   
/*  26 */   public MapCodec<FrostedIceBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  30 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*     */   
/*     */   private static final int NEIGHBORS_TO_AGE = 4;
/*     */   private static final int NEIGHBORS_TO_MELT = 2;
/*     */   
/*     */   public FrostedIceBlock(BlockBehaviour.Properties properties) {
/*  36 */     super(properties);
/*  37 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) { level.scheduleTick(pos, this, Mth.nextInt(level.getRandom(), 60, 120)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  47 */     if (random.nextInt(3) == 0 || fewerNeigboursThan(level, pos, 4)) {
/*  48 */       int brightness = (level.dimension() == Level.END) ? level.getBrightness(LightLayer.BLOCK, pos) : level.getMaxLocalRawBrightness(pos);
/*  49 */       if (brightness > 11 - ((Integer)state.getValue(AGE)).intValue() - state.getLightBlock() && slightlyMelt(state, level, pos)) {
/*  50 */         BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/*  51 */         for (Direction direction : Direction.values()) {
/*  52 */           neighborPos.setWithOffset(pos, direction);
/*  53 */           BlockState neighbour = level.getBlockState(neighborPos);
/*  54 */           if (neighbour.is(this) && !slightlyMelt(neighbour, level, neighborPos)) {
/*  55 */             level.scheduleTick(neighborPos, this, Mth.nextInt(random, 20, 40));
/*     */           }
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/*  61 */     level.scheduleTick(pos, this, Mth.nextInt(random, 20, 40));
/*     */   }
/*     */   
/*     */   private boolean slightlyMelt(BlockState state, Level level, BlockPos pos) {
/*  65 */     int age = ((Integer)state.getValue(AGE)).intValue();
/*  66 */     if (age < 3) {
/*  67 */       level.setBlock(pos, (BlockState)state.setValue(AGE, Integer.valueOf(age + 1)), 2);
/*  68 */       return false;
/*     */     } 
/*  70 */     melt(state, level, pos);
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  77 */     if (block.defaultBlockState().is(this) && 
/*  78 */       fewerNeigboursThan(level, pos, 2)) {
/*  79 */       melt(state, level, pos);
/*     */     }
/*     */ 
/*     */     
/*  83 */     super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
/*     */   }
/*     */   
/*     */   private boolean fewerNeigboursThan(BlockGetter level, BlockPos pos, int limit) {
/*  87 */     int result = 0;
/*  88 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/*  89 */     for (Direction direction : Direction.values()) {
/*  90 */       neighborPos.setWithOffset(pos, direction);
/*     */       
/*  92 */       result++;
/*  93 */       if (level.getBlockState(neighborPos).is(this) && result >= limit) {
/*  94 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return ItemStack.EMPTY; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FrostedIceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */