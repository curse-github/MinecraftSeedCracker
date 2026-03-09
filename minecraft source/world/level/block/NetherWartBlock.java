/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherWartBlock extends VegetationBlock {
/* 19 */   public static final MapCodec<NetherWartBlock> CODEC = simpleCodec(NetherWartBlock::new);
/*    */   
/*    */   public static final int MAX_AGE = 3;
/*    */   
/* 23 */   public MapCodec<NetherWartBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*    */   
/* 29 */   private static final VoxelShape[] SHAPES = Block.boxes(3, age -> Block.column(16.0D, 0.0D, (5 + age * 3)));
/*    */   
/*    */   protected NetherWartBlock(BlockBehaviour.Properties properties) {
/* 32 */     super(properties);
/* 33 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[((Integer)state.getValue(AGE)).intValue()]; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return state.is(Blocks.SOUL_SAND); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected boolean isRandomlyTicking(BlockState state) { return (((Integer)state.getValue(AGE)).intValue() < 3); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 53 */     int age = ((Integer)state.getValue(AGE)).intValue();
/* 54 */     if (age < 3 && random.nextInt(10) == 0) {
/* 55 */       state = (BlockState)state.setValue(AGE, Integer.valueOf(age + 1));
/* 56 */       level.setBlock(pos, state, 2);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(Items.NETHER_WART); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\NetherWartBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */