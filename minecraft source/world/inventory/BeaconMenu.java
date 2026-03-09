/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeaconMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   private static final int PAYMENT_SLOT = 0;
/*     */   private static final int SLOT_COUNT = 1;
/*     */   private static final int DATA_COUNT = 3;
/*     */   private static final int INV_SLOT_START = 1;
/*     */   private static final int INV_SLOT_END = 28;
/*     */   private static final int USE_ROW_SLOT_START = 28;
/*     */   private static final int USE_ROW_SLOT_END = 37;
/*     */   private static final int NO_EFFECT = 0;
/*     */   
/*  30 */   private final Container beacon = new SimpleContainer(this, 1)
/*     */     {
/*     */       public boolean canPlaceItem(int slot, ItemStack itemStack) {
/*  33 */         return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  38 */       public int getMaxStackSize() { return 1; }
/*     */     };
/*     */ 
/*     */   
/*     */   private final PaymentSlot paymentSlot;
/*     */   
/*     */   private final ContainerLevelAccess access;
/*     */   private final ContainerData beaconData;
/*     */   
/*  47 */   public BeaconMenu(int containerId, Container inventory) { this(containerId, inventory, new SimpleContainerData(3), ContainerLevelAccess.NULL); }
/*     */ 
/*     */   
/*     */   public BeaconMenu(int containerId, Container inventory, ContainerData beaconData, ContainerLevelAccess access) {
/*  51 */     super(MenuType.BEACON, containerId);
/*  52 */     checkContainerDataCount(beaconData, 3);
/*  53 */     this.beaconData = beaconData;
/*  54 */     this.access = access;
/*     */     
/*  56 */     this.paymentSlot = new PaymentSlot(this.beacon, 0, 136, 110);
/*  57 */     addSlot(this.paymentSlot);
/*     */     
/*  59 */     addDataSlots(beaconData);
/*     */     
/*  61 */     addStandardInventorySlots(inventory, 36, 137);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/*  66 */     super.removed(player);
/*  67 */     if (player.level().isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     ItemStack itemStack = this.paymentSlot.remove(this.paymentSlot.getMaxStackSize());
/*  72 */     if (!itemStack.isEmpty()) {
/*  73 */       player.drop(itemStack, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public boolean stillValid(Player player) { return stillValid(this.access, player, Blocks.BEACON); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setData(int id, int value) {
/*  84 */     super.setData(id, value);
/*  85 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  90 */     ItemStack clicked = ItemStack.EMPTY;
/*  91 */     Slot slot = (Slot)this.slots.get(slotIndex);
/*  92 */     if (slot != null && slot.hasItem()) {
/*  93 */       ItemStack stack = slot.getItem();
/*  94 */       clicked = stack.copy();
/*     */       
/*  96 */       if (slotIndex == 0) {
/*  97 */         if (!moveItemStackTo(stack, 1, 37, true)) {
/*  98 */           return ItemStack.EMPTY;
/*     */         }
/* 100 */         slot.onQuickCraft(stack, clicked);
/* 101 */       } else if (!this.paymentSlot.hasItem() && this.paymentSlot.mayPlace(stack) && stack.getCount() == 1) {
/* 102 */         if (!moveItemStackTo(stack, 0, 1, false)) {
/* 103 */           return ItemStack.EMPTY;
/*     */         }
/* 105 */       } else if (slotIndex >= 1 && slotIndex < 28) {
/* 106 */         if (!moveItemStackTo(stack, 28, 37, false)) {
/* 107 */           return ItemStack.EMPTY;
/*     */         }
/* 109 */       } else if (slotIndex >= 28 && slotIndex < 37) {
/* 110 */         if (!moveItemStackTo(stack, 1, 28, false)) {
/* 111 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/* 114 */       else if (!moveItemStackTo(stack, 1, 37, false)) {
/* 115 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 118 */       if (stack.isEmpty()) {
/* 119 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 121 */         slot.setChanged();
/*     */       } 
/* 123 */       if (stack.getCount() == clicked.getCount()) {
/* 124 */         return ItemStack.EMPTY;
/*     */       }
/* 126 */       slot.onTake(player, stack);
/*     */     } 
/*     */     
/* 129 */     return clicked;
/*     */   }
/*     */ 
/*     */   
/* 133 */   public int getLevels() { return this.beaconData.get(0); }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public static int encodeEffect(Holder<MobEffect> mobEffect) { return (mobEffect == null) ? 0 : (BuiltInRegistries.MOB_EFFECT.asHolderIdMap().getId(mobEffect) + 1); }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public static Holder<MobEffect> decodeEffect(int id) { return (id == 0) ? null : (Holder)BuiltInRegistries.MOB_EFFECT.asHolderIdMap().byId(id - 1); }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public Holder<MobEffect> getPrimaryEffect() { return decodeEffect(this.beaconData.get(1)); }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public Holder<MobEffect> getSecondaryEffect() { return decodeEffect(this.beaconData.get(2)); }
/*     */ 
/*     */   
/*     */   public void updateEffects(Optional<Holder<MobEffect>> primary, Optional<Holder<MobEffect>> secondary) {
/* 153 */     if (this.paymentSlot.hasItem()) {
/* 154 */       this.beaconData.set(1, encodeEffect((Holder)primary.orElse(null)));
/* 155 */       this.beaconData.set(2, encodeEffect((Holder)secondary.orElse(null)));
/* 156 */       this.paymentSlot.remove(1);
/* 157 */       this.access.execute(Level::blockEntityChanged);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 162 */   public boolean hasPayment() { return !this.beacon.getItem(0).isEmpty(); }
/*     */   
/*     */   private static class PaymentSlot
/*     */     extends Slot
/*     */   {
/* 167 */     public PaymentSlot(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     public boolean mayPlace(ItemStack itemStack) { return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     public int getMaxStackSize() { return 1; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\BeaconMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */