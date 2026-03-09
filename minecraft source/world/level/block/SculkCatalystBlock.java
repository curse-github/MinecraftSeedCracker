/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SculkCatalystBlock extends BaseEntityBlock {
/* 22 */   public static final MapCodec<SculkCatalystBlock> CODEC = simpleCodec(SculkCatalystBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 26 */   public MapCodec<SculkCatalystBlock> codec() { return CODEC; }
/*    */ 
/*    */   
/* 29 */   public static final BooleanProperty PULSE = BlockStateProperties.BLOOM;
/* 30 */   private final IntProvider xpRange = ConstantInt.of(5);
/*    */   
/*    */   public SculkCatalystBlock(BlockBehaviour.Properties properties) {
/* 33 */     super(properties);
/*    */     
/* 35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(PULSE, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 40 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { PULSE }); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 45 */     if (((Boolean)state.getValue(PULSE)).booleanValue()) {
/* 46 */       level.setBlock(pos, (BlockState)state.setValue(PULSE, Boolean.valueOf(false)), 3);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new SculkCatalystBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 57 */     if (level.isClientSide()) {
/* 58 */       return null;
/*    */     }
/* 60 */     return createTickerHelper(type, BlockEntityType.SCULK_CATALYST, SculkCatalystBlockEntity::serverTick);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 65 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 66 */     if (dropExperience)
/* 67 */       tryDropExperience(level, pos, tool, this.xpRange); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SculkCatalystBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */