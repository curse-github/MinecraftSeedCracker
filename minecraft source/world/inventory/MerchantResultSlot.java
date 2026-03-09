/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.trading.Merchant;
/*    */ import net.minecraft.world.item.trading.MerchantOffer;
/*    */ 
/*    */ public class MerchantResultSlot extends Slot {
/*    */   private final MerchantContainer slots;
/*    */   private final Player player;
/*    */   private int removeCount;
/*    */   private final Merchant merchant;
/*    */   
/*    */   public MerchantResultSlot(Player player, Merchant merchant, MerchantContainer slots, int id, int x, int y) {
/* 16 */     super(slots, id, x, y);
/* 17 */     this.player = player;
/* 18 */     this.merchant = merchant;
/* 19 */     this.slots = slots;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean mayPlace(ItemStack itemStack) { return false; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack remove(int amount) {
/* 29 */     if (hasItem()) {
/* 30 */       this.removeCount += Math.min(amount, getItem().getCount());
/*    */     }
/* 32 */     return super.remove(amount);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onQuickCraft(ItemStack picked, int count) {
/* 37 */     this.removeCount += count;
/* 38 */     checkTakeAchievements(picked);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkTakeAchievements(ItemStack carried) {
/* 43 */     carried.onCraftedBy(this.player, this.removeCount);
/* 44 */     this.removeCount = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTake(Player player, ItemStack carried) {
/* 49 */     checkTakeAchievements(carried);
/*    */     
/* 51 */     MerchantOffer offer = this.slots.getActiveOffer();
/*    */     
/* 53 */     if (offer != null) {
/* 54 */       ItemStack buyA = this.slots.getItem(0);
/* 55 */       ItemStack buyB = this.slots.getItem(1);
/*    */ 
/*    */       
/* 58 */       if (offer.take(buyA, buyB) || offer.take(buyB, buyA)) {
/* 59 */         this.merchant.notifyTrade(offer);
/* 60 */         player.awardStat(Stats.TRADED_WITH_VILLAGER);
/*    */         
/* 62 */         this.slots.setItem(0, buyA);
/* 63 */         this.slots.setItem(1, buyB);
/*    */       } 
/* 65 */       this.merchant.overrideXp(this.merchant.getVillagerXp() + offer.getXp());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\MerchantResultSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */