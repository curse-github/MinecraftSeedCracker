/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class TestInstanceBlock extends BaseEntityBlock implements GameMasterBlock {
/* 15 */   public static final MapCodec<TestInstanceBlock> CODEC = simpleCodec(TestInstanceBlock::new);
/*    */ 
/*    */   
/* 18 */   public TestInstanceBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new TestInstanceBlockEntity(worldPosition, blockState); }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*    */     TestInstanceBlockEntity testInstance;
/* 28 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 29 */     if (blockEntity instanceof TestInstanceBlockEntity) { testInstance = (TestInstanceBlockEntity)blockEntity; }
/* 30 */     else { return InteractionResult.PASS; }
/*    */     
/* 32 */     if (!player.canUseGameMasterBlocks())
/*    */     {
/* 34 */       return InteractionResult.PASS;
/*    */     }
/* 36 */     if (player.level().isClientSide()) {
/* 37 */       player.openTestInstanceBlock(testInstance);
/*    */     }
/* 39 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected MapCodec<TestInstanceBlock> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TestInstanceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */