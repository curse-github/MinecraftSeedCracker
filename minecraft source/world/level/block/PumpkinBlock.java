/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class PumpkinBlock extends Block {
/* 23 */   public static final MapCodec<PumpkinBlock> CODEC = simpleCodec(PumpkinBlock::new);
/*    */ 
/*    */ 
/*    */   
/* 27 */   public MapCodec<PumpkinBlock> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   protected PumpkinBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */   
/*    */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*    */     ServerLevel serverLevel;
/* 36 */     if (!itemStack.is(Items.SHEARS)) {
/* 37 */       return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*    */     }
/* 39 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 40 */     else { return InteractionResult.SUCCESS; }
/*    */     
/* 42 */     Direction clickedDirection = hitResult.getDirection();
/* 43 */     Direction direction = (clickedDirection.getAxis() == Direction.Axis.Y) ? player.getDirection().getOpposite() : clickedDirection;
/*    */     
/* 45 */     dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.CARVE_PUMPKIN, state, level.getBlockEntity(pos), itemStack, player, (ignored, pumpkinSeeds) -> {
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 50 */           ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D + direction.getStepX() * 0.65D, pos.getY() + 0.1D, pos.getZ() + 0.5D + direction.getStepZ() * 0.65D, pumpkinSeeds);
/*    */ 
/*    */ 
/*    */           
/* 54 */           entity.setDeltaMovement(0.05D * direction
/* 55 */               .getStepX() + level.random.nextDouble() * 0.02D, 0.05D, 0.05D * direction
/*    */               
/* 57 */               .getStepZ() + level.random.nextDouble() * 0.02D);
/*    */ 
/*    */           
/* 60 */           level.addFreshEntity(entity);
/*    */         });
/*    */     
/* 63 */     level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 64 */     level.setBlock(pos, (BlockState)Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction), 11);
/* 65 */     itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
/* 66 */     level.gameEvent(player, GameEvent.SHEAR, pos);
/* 67 */     player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
/*    */     
/* 69 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\PumpkinBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */