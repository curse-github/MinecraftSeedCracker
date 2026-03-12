/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TorchflowerCropBlock extends CropBlock {
/* 19 */   public static final MapCodec<TorchflowerCropBlock> CODEC = simpleCodec(TorchflowerCropBlock::new);
/*    */   
/*    */   public static final int MAX_AGE = 1;
/*    */   
/* 23 */   public MapCodec<TorchflowerCropBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
/*    */   
/* 29 */   private static final VoxelShape[] SHAPES = Block.boxes(1, age -> Block.column(6.0D, 0.0D, (6 + age * 4)));
/*    */   
/*    */   private static final int BONEMEAL_INCREASE = 1;
/*    */ 
/*    */   
/* 34 */   public TorchflowerCropBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[getAge(state)]; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   protected IntegerProperty getAgeProperty() { return AGE; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public int getMaxAge() { return 2; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   protected ItemLike getBaseSeedId() { return Items.TORCHFLOWER_SEEDS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForAge(int age) {
/* 66 */     if (age == 2) {
/* 67 */       return Blocks.TORCHFLOWER.defaultBlockState();
/*    */     }
/* 69 */     return super.getStateForAge(age);
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 74 */     if (random.nextInt(3) != 0) {
/* 75 */       super.randomTick(state, level, pos, random);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 81 */   protected int getBonemealAgeIncrease(Level level) { return 1; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TorchflowerCropBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */