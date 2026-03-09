/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.FishingHook;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class FishingRodItem
/*    */   extends Item
/*    */ {
/* 19 */   public FishingRodItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 24 */     ItemStack itemStack = player.getItemInHand(hand);
/* 25 */     if (player.fishing != null) {
/* 26 */       if (!level.isClientSide()) {
/* 27 */         int dmg = player.fishing.retrieve(itemStack);
/* 28 */         itemStack.hurtAndBreak(dmg, player, hand.asEquipmentSlot());
/*    */       } 
/* 30 */       level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/*    */       
/* 32 */       itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_FINISH);
/*    */     } else {
/* 34 */       level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
/*    */       
/* 36 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 37 */         int lureSpeed = (int)(EnchantmentHelper.getFishingTimeReduction(serverLevel, itemStack, player) * 20.0F);
/* 38 */         int luck = EnchantmentHelper.getFishingLuckBonus(serverLevel, itemStack, player);
/* 39 */         Projectile.spawnProjectile(new FishingHook(player, level, luck, lureSpeed), serverLevel, itemStack); }
/*    */       
/* 41 */       player.awardStat(Stats.ITEM_USED.get(this));
/*    */       
/* 43 */       itemStack.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
/*    */     } 
/* 45 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\FishingRodItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */