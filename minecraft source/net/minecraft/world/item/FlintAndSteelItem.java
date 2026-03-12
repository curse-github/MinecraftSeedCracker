/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.CandleBlock;
/*    */ import net.minecraft.world.level.block.CandleCakeBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ 
/*    */ public class FlintAndSteelItem
/*    */   extends Item
/*    */ {
/* 24 */   public FlintAndSteelItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 29 */     Player player = context.getPlayer();
/* 30 */     Level level = context.getLevel();
/* 31 */     BlockPos pos = context.getClickedPos();
/*    */     
/* 33 */     BlockState state = level.getBlockState(pos);
/* 34 */     if (CampfireBlock.canLight(state) || CandleBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
/* 35 */       level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
/* 36 */       level.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
/* 37 */       level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
/* 38 */       if (player != null) {
/* 39 */         context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
/*    */       }
/* 41 */       return InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 44 */     BlockPos relativePos = pos.relative(context.getClickedFace());
/* 45 */     if (BaseFireBlock.canBePlacedAt(level, relativePos, context.getHorizontalDirection())) {
/* 46 */       level.playSound(player, relativePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
/*    */       
/* 48 */       BlockState fireState = BaseFireBlock.getState(level, relativePos);
/* 49 */       level.setBlock(relativePos, fireState, 11);
/* 50 */       level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
/*    */       
/* 52 */       ItemStack itemStack = context.getItemInHand();
/* 53 */       if (player instanceof ServerPlayer) {
/* 54 */         CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, relativePos, itemStack);
/* 55 */         itemStack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
/*    */       } 
/*    */       
/* 58 */       return InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 61 */     return InteractionResult.FAIL;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\FlintAndSteelItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */