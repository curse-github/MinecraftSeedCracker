/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.grower.TreeGrower;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SaplingBlock extends VegetationBlock implements BonemealableBlock {
/* 20 */   public static final MapCodec<SaplingBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TreeGrower.CODEC
/* 21 */         .fieldOf("tree").forGetter(()), 
/* 22 */         propertiesCodec())
/* 23 */       .apply(i, SaplingBlock::new));
/*    */ 
/*    */ 
/*    */   
/* 27 */   public MapCodec<? extends SaplingBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 30 */   public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
/*    */   
/* 32 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 12.0D);
/*    */   
/*    */   protected final TreeGrower treeGrower;
/*    */   
/*    */   protected SaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
/* 37 */     super(properties);
/* 38 */     this.treeGrower = treeGrower;
/* 39 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(STAGE, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 49 */     if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && 
/* 50 */       random.nextInt(7) == 0) {
/* 51 */       advanceTree(level, pos, state, random);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
/* 57 */     if (((Integer)state.getValue(STAGE)).intValue() == 0) {
/* 58 */       level.setBlock(pos, (BlockState)state.cycle(STAGE), 260);
/*    */     } else {
/* 60 */       this.treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return (level.random.nextFloat() < 0.45D); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { advanceTree(level, pos, state, random); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { STAGE }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SaplingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */