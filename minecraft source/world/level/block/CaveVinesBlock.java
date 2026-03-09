/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CaveVinesBlock extends GrowingPlantHeadBlock implements CaveVines {
/* 19 */   public static final MapCodec<CaveVinesBlock> CODEC = simpleCodec(CaveVinesBlock::new);
/*    */   
/*    */   private static final float CHANCE_OF_BERRIES_ON_GROWTH = 0.11F;
/*    */   
/* 23 */   public MapCodec<CaveVinesBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CaveVinesBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(properties, Direction.DOWN, SHAPE, false, 0.1D);
/* 30 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, Integer.valueOf(0))).setValue(BERRIES, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   protected int getBlocksToGrowWhenBonemealed(RandomSource random) { return 1; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected boolean canGrowInto(BlockState state) { return state.isAir(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   protected Block getBodyBlock() { return Blocks.CAVE_VINES_PLANT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   protected BlockState updateBodyAfterConvertedFromHead(BlockState headState, BlockState bodyState) { return (BlockState)bodyState.setValue(BERRIES, (Boolean)headState.getValue(BERRIES)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   protected BlockState getGrowIntoState(BlockState growFromState, RandomSource random) { return (BlockState)super.getGrowIntoState(growFromState, random).setValue(BERRIES, Boolean.valueOf((random.nextFloat() < 0.11F))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(Items.GLOW_BERRIES); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) { return CaveVines.use(player, state, level, pos); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 70 */     super.createBlockStateDefinition(builder);
/* 71 */     builder.add(new Property[] { BERRIES });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return !((Boolean)state.getValue(BERRIES)).booleanValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { level.setBlock(pos, (BlockState)state.setValue(BERRIES, Boolean.valueOf(true)), 2); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CaveVinesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */