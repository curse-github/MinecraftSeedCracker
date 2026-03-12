/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.npc.ClientSideMerchant;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.ItemCost;
/*     */ import net.minecraft.world.item.trading.Merchant;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MerchantMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   protected static final int PAYMENT1_SLOT = 0;
/*     */   protected static final int PAYMENT2_SLOT = 1;
/*     */   protected static final int RESULT_SLOT = 2;
/*     */   private static final int INV_SLOT_START = 3;
/*     */   private static final int INV_SLOT_END = 30;
/*     */   private static final int USE_ROW_SLOT_START = 30;
/*     */   private static final int USE_ROW_SLOT_END = 39;
/*     */   private static final int SELLSLOT1_X = 136;
/*     */   private static final int SELLSLOT2_X = 162;
/*     */   private static final int BUYSLOT_X = 220;
/*     */   private static final int ROW_Y = 37;
/*     */   private final Merchant trader;
/*     */   private final MerchantContainer tradeContainer;
/*     */   private int merchantLevel;
/*     */   private boolean showProgressBar;
/*     */   private boolean canRestock;
/*     */   
/*  39 */   public MerchantMenu(int containerId, Inventory inventory) { this(containerId, inventory, new ClientSideMerchant(inventory.player)); }
/*     */ 
/*     */   
/*     */   public MerchantMenu(int containerId, Inventory inventory, Merchant merchant) {
/*  43 */     super(MenuType.MERCHANT, containerId);
/*  44 */     this.trader = merchant;
/*     */     
/*  46 */     this.tradeContainer = new MerchantContainer(merchant);
/*  47 */     addSlot(new Slot(this.tradeContainer, 0, 136, 37));
/*  48 */     addSlot(new Slot(this.tradeContainer, 1, 162, 37));
/*  49 */     addSlot(new MerchantResultSlot(inventory.player, merchant, this.tradeContainer, 2, 220, 37));
/*     */     
/*  51 */     addStandardInventorySlots(inventory, 108, 84);
/*     */   }
/*     */ 
/*     */   
/*  55 */   public void setShowProgressBar(boolean show) { this.showProgressBar = show; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/*  60 */     this.tradeContainer.updateSellItem();
/*  61 */     super.slotsChanged(container);
/*     */   }
/*     */ 
/*     */   
/*  65 */   public void setSelectionHint(int hint) { this.tradeContainer.setSelectionHint(hint); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public boolean stillValid(Player player) { return this.trader.stillValid(player); }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public int getTraderXp() { return this.trader.getVillagerXp(); }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public int getFutureTraderXp() { return this.tradeContainer.getFutureXp(); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public void setXp(int xp) { this.trader.overrideXp(xp); }
/*     */ 
/*     */ 
/*     */   
/*  86 */   public int getTraderLevel() { return this.merchantLevel; }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public void setMerchantLevel(int level) { this.merchantLevel = level; }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public void setCanRestock(boolean canRestock) { this.canRestock = canRestock; }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public boolean canRestock() { return this.canRestock; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 108 */     ItemStack clicked = ItemStack.EMPTY;
/* 109 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 110 */     if (slot != null && slot.hasItem()) {
/* 111 */       ItemStack stack = slot.getItem();
/* 112 */       clicked = stack.copy();
/*     */       
/* 114 */       if (slotIndex == 2) {
/* 115 */         if (!moveItemStackTo(stack, 3, 39, true)) {
/* 116 */           return ItemStack.EMPTY;
/*     */         }
/* 118 */         slot.onQuickCraft(stack, clicked);
/*     */         
/* 120 */         playTradeSound();
/* 121 */       } else if (slotIndex == 0 || slotIndex == 1) {
/* 122 */         if (!moveItemStackTo(stack, 3, 39, false)) {
/* 123 */           return ItemStack.EMPTY;
/*     */         }
/* 125 */       } else if (slotIndex >= 3 && slotIndex < 30) {
/* 126 */         if (!moveItemStackTo(stack, 30, 39, false)) {
/* 127 */           return ItemStack.EMPTY;
/*     */         }
/* 129 */       } else if (slotIndex >= 30 && slotIndex < 39 && 
/* 130 */         !moveItemStackTo(stack, 3, 30, false)) {
/* 131 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 134 */       if (stack.isEmpty()) {
/* 135 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 137 */         slot.setChanged();
/*     */       } 
/* 139 */       if (stack.getCount() == clicked.getCount()) {
/* 140 */         return ItemStack.EMPTY;
/*     */       }
/* 142 */       slot.onTake(player, stack);
/*     */     } 
/*     */     
/* 145 */     return clicked;
/*     */   }
/*     */   
/*     */   private void playTradeSound() {
/* 149 */     if (!this.trader.isClientSide()) {
/* 150 */       Entity entity = (Entity)this.trader;
/* 151 */       entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 157 */     super.removed(player);
/* 158 */     this.trader.setTradingPlayer(null);
/*     */     
/* 160 */     if (this.trader.isClientSide()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 165 */     if (!player.isAlive() || (player instanceof ServerPlayer && ((ServerPlayer)player).hasDisconnected())) {
/* 166 */       ItemStack itemStack = this.tradeContainer.removeItemNoUpdate(0);
/* 167 */       if (!itemStack.isEmpty()) {
/* 168 */         player.drop(itemStack, false);
/*     */       }
/* 170 */       itemStack = this.tradeContainer.removeItemNoUpdate(1);
/* 171 */       if (!itemStack.isEmpty()) {
/* 172 */         player.drop(itemStack, false);
/*     */       }
/*     */     }
/* 175 */     else if (player instanceof ServerPlayer) {
/* 176 */       player.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(0));
/* 177 */       player.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(1));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tryMoveItems(int newTradeIndex) {
/* 183 */     if (newTradeIndex < 0 || getOffers().size() <= newTradeIndex) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 188 */     ItemStack oldCostA = this.tradeContainer.getItem(0);
/* 189 */     if (!oldCostA.isEmpty()) {
/* 190 */       if (!moveItemStackTo(oldCostA, 3, 39, true)) {
/*     */         return;
/*     */       }
/*     */       
/* 194 */       this.tradeContainer.setItem(0, oldCostA);
/*     */     } 
/*     */     
/* 197 */     ItemStack oldCostB = this.tradeContainer.getItem(1);
/* 198 */     if (!oldCostB.isEmpty()) {
/* 199 */       if (!moveItemStackTo(oldCostB, 3, 39, true)) {
/*     */         return;
/*     */       }
/*     */       
/* 203 */       this.tradeContainer.setItem(1, oldCostB);
/*     */     } 
/*     */ 
/*     */     
/* 207 */     if (this.tradeContainer.getItem(0).isEmpty() && this.tradeContainer.getItem(1).isEmpty()) {
/* 208 */       MerchantOffer merchantOffer = (MerchantOffer)getOffers().get(newTradeIndex);
/* 209 */       moveFromInventoryToPaymentSlot(0, merchantOffer.getItemCostA());
/* 210 */       merchantOffer.getItemCostB().ifPresent(costB -> moveFromInventoryToPaymentSlot(1, costB));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveFromInventoryToPaymentSlot(int paymentSlot, ItemCost cost) {
/* 215 */     for (int i = 3; i < 39; i++) {
/* 216 */       ItemStack inventoryItem = ((Slot)this.slots.get(i)).getItem();
/* 217 */       if (!inventoryItem.isEmpty() && cost.test(inventoryItem)) {
/* 218 */         ItemStack currentPaymentItem = this.tradeContainer.getItem(paymentSlot);
/* 219 */         if (currentPaymentItem.isEmpty() || ItemStack.isSameItemSameComponents(inventoryItem, currentPaymentItem)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 224 */           int maxStackSize = inventoryItem.getMaxStackSize();
/* 225 */           int moveCount = Math.min(maxStackSize - currentPaymentItem.getCount(), inventoryItem.getCount());
/*     */           
/* 227 */           ItemStack newPaymentItem = inventoryItem.copyWithCount(currentPaymentItem.getCount() + moveCount);
/* 228 */           inventoryItem.shrink(moveCount);
/* 229 */           this.tradeContainer.setItem(paymentSlot, newPaymentItem);
/*     */           
/* 231 */           if (newPaymentItem.getCount() >= maxStackSize) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/* 239 */   public void setOffers(MerchantOffers offers) { this.trader.overrideOffers(offers); }
/*     */ 
/*     */ 
/*     */   
/* 243 */   public MerchantOffers getOffers() { return this.trader.getOffers(); }
/*     */ 
/*     */ 
/*     */   
/* 247 */   public boolean showProgressBar() { return this.showProgressBar; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\MerchantMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */