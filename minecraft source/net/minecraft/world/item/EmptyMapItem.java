/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class EmptyMapItem
/*    */   extends Item {
/* 13 */   public EmptyMapItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/*    */     ServerLevel serverLevel;
/* 18 */     ItemStack itemStack = player.getItemInHand(hand);
/*    */     
/* 20 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/* 21 */     else { return InteractionResult.SUCCESS; }
/*    */ 
/*    */     
/* 24 */     itemStack.consume(1, player);
/*    */     
/* 26 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 27 */     serverLevel.playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0F, 1.0F);
/*    */     
/* 29 */     ItemStack map = MapItem.create(serverLevel, player.getBlockX(), player.getBlockZ(), (byte)0, true, false);
/* 30 */     if (itemStack.isEmpty()) {
/* 31 */       return InteractionResult.SUCCESS.heldItemTransformedTo(map);
/*    */     }
/* 33 */     if (!player.getInventory().add(map.copy())) {
/* 34 */       player.drop(map, false);
/*    */     }
/*    */     
/* 37 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\EmptyMapItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */