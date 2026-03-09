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
/*    */ public class CaveVinesPlantBlock extends GrowingPlantBodyBlock implements CaveVines {
/* 19 */   public static final MapCodec<CaveVinesPlantBlock> CODEC = simpleCodec(CaveVinesPlantBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 23 */   public MapCodec<CaveVinesPlantBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/*    */   public CaveVinesPlantBlock(BlockBehaviour.Properties properties) {
/* 27 */     super(properties, Direction.DOWN, SHAPE, false);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(BERRIES, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected GrowingPlantHeadBlock getHeadBlock() { return (GrowingPlantHeadBlock)Blocks.CAVE_VINES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected BlockState updateHeadAfterConvertedFromBody(BlockState bodyState, BlockState headState) { return (BlockState)headState.setValue(BERRIES, (Boolean)bodyState.getValue(BERRIES)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return new ItemStack(Items.GLOW_BERRIES); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) { return CaveVines.use(player, state, level, pos); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { BERRIES }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return !((Boolean)state.getValue(BERRIES)).booleanValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { level.setBlock(pos, (BlockState)state.setValue(BERRIES, Boolean.valueOf(true)), 2); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CaveVinesPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */