/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.trading.Merchant;
/*    */ import net.minecraft.world.item.trading.MerchantOffer;
/*    */ import net.minecraft.world.item.trading.MerchantOffers;
/*    */ 
/*    */ public class ClientSideMerchant
/*    */   implements Merchant {
/*    */   public ClientSideMerchant(Player source) {
/* 14 */     this.offers = new MerchantOffers();
/*    */ 
/*    */ 
/*    */     
/* 18 */     this.source = source;
/*    */   }
/*    */   private final Player source; private MerchantOffers offers;
/*    */   private int xp;
/*    */   
/* 23 */   public Player getTradingPlayer() { return this.source; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTradingPlayer(Player player) {}
/*    */ 
/*    */ 
/*    */   
/* 32 */   public MerchantOffers getOffers() { return this.offers; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public void overrideOffers(MerchantOffers offers) { this.offers = offers; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void notifyTrade(MerchantOffer offer) { offer.increaseUses(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void notifyTradeUpdated(ItemStack itemStack) {}
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean isClientSide() { return this.source.level().isClientSide(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public boolean stillValid(Player player) { return (this.source == player); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public int getVillagerXp() { return this.xp; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public void overrideXp(int xp) { this.xp = xp; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public boolean showProgressBar() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public SoundEvent getNotifyTradeSound() { return SoundEvents.VILLAGER_YES; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\ClientSideMerchant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */