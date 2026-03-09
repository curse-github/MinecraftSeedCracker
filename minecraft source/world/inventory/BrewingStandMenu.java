/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ 
/*     */ public class BrewingStandMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*  23 */   private static final Identifier EMPTY_SLOT_FUEL = Identifier.withDefaultNamespace("container/slot/brewing_fuel");
/*  24 */   private static final Identifier EMPTY_SLOT_POTION = Identifier.withDefaultNamespace("container/slot/potion");
/*     */   
/*     */   private static final int BOTTLE_SLOT_START = 0;
/*     */   
/*     */   private static final int BOTTLE_SLOT_END = 2;
/*     */   
/*     */   private static final int INGREDIENT_SLOT = 3;
/*     */   
/*     */   private static final int FUEL_SLOT = 4;
/*     */   
/*     */   private static final int SLOT_COUNT = 5;
/*     */   private static final int DATA_COUNT = 2;
/*     */   private static final int INV_SLOT_START = 5;
/*     */   private static final int INV_SLOT_END = 32;
/*     */   private static final int USE_ROW_SLOT_START = 32;
/*     */   private static final int USE_ROW_SLOT_END = 41;
/*     */   private final Container brewingStand;
/*     */   private final ContainerData brewingStandData;
/*     */   private final Slot ingredientSlot;
/*     */   
/*  44 */   public BrewingStandMenu(int containerId, Inventory inventory) { this(containerId, inventory, new SimpleContainer(5), new SimpleContainerData(2)); }
/*     */ 
/*     */   
/*     */   public BrewingStandMenu(int containerId, Inventory inventory, Container brewingStand, ContainerData brewingStandData) {
/*  48 */     super(MenuType.BREWING_STAND, containerId);
/*  49 */     checkContainerSize(brewingStand, 5);
/*  50 */     checkContainerDataCount(brewingStandData, 2);
/*  51 */     this.brewingStand = brewingStand;
/*  52 */     this.brewingStandData = brewingStandData;
/*     */     
/*  54 */     PotionBrewing potionBrewing = inventory.player.level().potionBrewing();
/*     */     
/*  56 */     addSlot(new PotionSlot(brewingStand, 0, 56, 51));
/*  57 */     addSlot(new PotionSlot(brewingStand, 1, 79, 58));
/*  58 */     addSlot(new PotionSlot(brewingStand, 2, 102, 51));
/*  59 */     this.ingredientSlot = addSlot(new IngredientsSlot(potionBrewing, brewingStand, 3, 79, 17));
/*  60 */     addSlot(new FuelSlot(brewingStand, 4, 17, 17));
/*     */     
/*  62 */     addDataSlots(brewingStandData);
/*     */     
/*  64 */     addStandardInventorySlots(inventory, 8, 84);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public boolean stillValid(Player player) { return this.brewingStand.stillValid(player); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  74 */     ItemStack clicked = ItemStack.EMPTY;
/*  75 */     Slot slot = (Slot)this.slots.get(slotIndex);
/*  76 */     if (slot != null && slot.hasItem()) {
/*  77 */       ItemStack stack = slot.getItem();
/*  78 */       clicked = stack.copy();
/*     */       
/*  80 */       if ((slotIndex >= 0 && slotIndex <= 2) || slotIndex == 3 || slotIndex == 4) {
/*  81 */         if (!moveItemStackTo(stack, 5, 41, true)) {
/*  82 */           return ItemStack.EMPTY;
/*     */         }
/*  84 */         slot.onQuickCraft(stack, clicked);
/*  85 */       } else if (FuelSlot.mayPlaceItem(clicked)) {
/*  86 */         if (moveItemStackTo(stack, 4, 5, false) || (this.ingredientSlot.mayPlace(stack) && !moveItemStackTo(stack, 3, 4, false))) {
/*  87 */           return ItemStack.EMPTY;
/*     */         }
/*  89 */       } else if (this.ingredientSlot.mayPlace(stack)) {
/*  90 */         if (!moveItemStackTo(stack, 3, 4, false)) {
/*  91 */           return ItemStack.EMPTY;
/*     */         }
/*  93 */       } else if (PotionSlot.mayPlaceItem(clicked)) {
/*  94 */         if (!moveItemStackTo(stack, 0, 3, false)) {
/*  95 */           return ItemStack.EMPTY;
/*     */         }
/*  97 */       } else if (slotIndex >= 5 && slotIndex < 32) {
/*  98 */         if (!moveItemStackTo(stack, 32, 41, false)) {
/*  99 */           return ItemStack.EMPTY;
/*     */         }
/* 101 */       } else if (slotIndex >= 32 && slotIndex < 41) {
/* 102 */         if (!moveItemStackTo(stack, 5, 32, false)) {
/* 103 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/* 106 */       else if (!moveItemStackTo(stack, 5, 41, false)) {
/* 107 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 110 */       if (stack.isEmpty()) {
/* 111 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 113 */         slot.setChanged();
/*     */       } 
/* 115 */       if (stack.getCount() == clicked.getCount()) {
/* 116 */         return ItemStack.EMPTY;
/*     */       }
/* 118 */       slot.onTake(player, clicked);
/*     */     } 
/*     */     
/* 121 */     return clicked;
/*     */   }
/*     */ 
/*     */   
/* 125 */   public int getFuel() { return this.brewingStandData.get(1); }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public int getBrewingTicks() { return this.brewingStandData.get(0); }
/*     */   
/*     */   private static class PotionSlot
/*     */     extends Slot
/*     */   {
/* 134 */     public PotionSlot(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     public boolean mayPlace(ItemStack itemStack) { return mayPlaceItem(itemStack); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     public int getMaxStackSize() { return 1; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onTake(Player player, ItemStack carried) {
/* 149 */       Optional<Holder<Potion>> potion = ((PotionContents)carried.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).potion();
/* 150 */       if (potion.isPresent() && player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 151 */         CriteriaTriggers.BREWED_POTION.trigger(serverPlayer, (Holder)potion.get()); }
/*     */       
/* 153 */       super.onTake(player, carried);
/*     */     }
/*     */ 
/*     */     
/* 157 */     public static boolean mayPlaceItem(ItemStack itemStack) { return (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.GLASS_BOTTLE)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     public Identifier getNoItemIcon() { return BrewingStandMenu.EMPTY_SLOT_POTION; }
/*     */   }
/*     */   
/*     */   private static class IngredientsSlot
/*     */     extends Slot {
/*     */     private final PotionBrewing potionBrewing;
/*     */     
/*     */     public IngredientsSlot(PotionBrewing potionBrewing, Container container, int slot, int x, int y) {
/* 170 */       super(container, slot, x, y);
/* 171 */       this.potionBrewing = potionBrewing;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 176 */     public boolean mayPlace(ItemStack itemStack) { return this.potionBrewing.isIngredient(itemStack); }
/*     */   }
/*     */   
/*     */   private static class FuelSlot
/*     */     extends Slot
/*     */   {
/* 182 */     public FuelSlot(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     public boolean mayPlace(ItemStack itemStack) { return mayPlaceItem(itemStack); }
/*     */ 
/*     */ 
/*     */     
/* 191 */     public static boolean mayPlaceItem(ItemStack itemStack) { return itemStack.is(ItemTags.BREWING_FUEL); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     public Identifier getNoItemIcon() { return BrewingStandMenu.EMPTY_SLOT_FUEL; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\BrewingStandMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */