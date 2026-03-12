/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SpyglassItem
/*    */   extends Item
/*    */ {
/*    */   public static final int USE_DURATION = 1200;
/*    */   public static final float ZOOM_FOV_MODIFIER = 0.1F;
/*    */   
/* 17 */   public SpyglassItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public int getUseDuration(ItemStack itemStack, LivingEntity user) { return 1200; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) { return ItemUseAnimation.SPYGLASS; }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 32 */     player.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
/* 33 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 34 */     return ItemUtils.startUsingInstantly(level, player, hand);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
/* 39 */     stopUsing(entity);
/* 40 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
/* 45 */     stopUsing(entity);
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/* 50 */   private void stopUsing(LivingEntity entity) { entity.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\SpyglassItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */