/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BeetrootBlock extends CropBlock {
/* 19 */   public static final MapCodec<BeetrootBlock> CODEC = simpleCodec(BeetrootBlock::new);
/*    */   
/*    */   public static final int MAX_AGE = 3;
/*    */   
/* 23 */   public MapCodec<BeetrootBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*    */   
/* 29 */   private static final VoxelShape[] SHAPES = Block.boxes(3, age -> Block.column(16.0D, 0.0D, (2 + age * 2)));
/*    */ 
/*    */   
/* 32 */   public BeetrootBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   protected IntegerProperty getAgeProperty() { return AGE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int getMaxAge() { return 3; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   protected ItemLike getBaseSeedId() { return Items.BEETROOT_SEEDS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 53 */     if (random.nextInt(3) != 0) {
/* 54 */       super.randomTick(state, level, pos, random);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 60 */   protected int getBonemealAgeIncrease(Level level) { return super.getBonemealAgeIncrease(level) / 3; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[getAge(state)]; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BeetrootBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */