/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InventoryMenu
/*     */   extends AbstractCraftingMenu
/*     */ {
/*     */   public static final int CONTAINER_ID = 0;
/*     */   public static final int RESULT_SLOT = 0;
/*     */   private static final int CRAFTING_GRID_WIDTH = 2;
/*     */   private static final int CRAFTING_GRID_HEIGHT = 2;
/*     */   public static final int CRAFT_SLOT_START = 1;
/*     */   public static final int CRAFT_SLOT_COUNT = 4;
/*     */   public static final int CRAFT_SLOT_END = 5;
/*     */   public static final int ARMOR_SLOT_START = 5;
/*     */   public static final int ARMOR_SLOT_COUNT = 4;
/*     */   public static final int ARMOR_SLOT_END = 9;
/*     */   public static final int INV_SLOT_START = 9;
/*     */   public static final int INV_SLOT_END = 36;
/*     */   public static final int USE_ROW_SLOT_START = 36;
/*     */   public static final int USE_ROW_SLOT_END = 45;
/*     */   public static final int SHIELD_SLOT = 45;
/*  36 */   public static final Identifier EMPTY_ARMOR_SLOT_HELMET = Identifier.withDefaultNamespace("container/slot/helmet");
/*  37 */   public static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE = Identifier.withDefaultNamespace("container/slot/chestplate");
/*  38 */   public static final Identifier EMPTY_ARMOR_SLOT_LEGGINGS = Identifier.withDefaultNamespace("container/slot/leggings");
/*  39 */   public static final Identifier EMPTY_ARMOR_SLOT_BOOTS = Identifier.withDefaultNamespace("container/slot/boots");
/*  40 */   public static final Identifier EMPTY_ARMOR_SLOT_SHIELD = Identifier.withDefaultNamespace("container/slot/shield");
/*     */   
/*  42 */   private static final Map<EquipmentSlot, Identifier> TEXTURE_EMPTY_SLOTS = Map.of(EquipmentSlot.FEET, EMPTY_ARMOR_SLOT_BOOTS, EquipmentSlot.LEGS, EMPTY_ARMOR_SLOT_LEGGINGS, EquipmentSlot.CHEST, EMPTY_ARMOR_SLOT_CHESTPLATE, EquipmentSlot.HEAD, EMPTY_ARMOR_SLOT_HELMET);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static final EquipmentSlot[] SLOT_IDS = { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
/*     */ 
/*     */   
/*     */   public final boolean active;
/*     */ 
/*     */   
/*     */   private final Player owner;
/*     */ 
/*     */ 
/*     */   
/*     */   public InventoryMenu(Inventory inventory, boolean active, final Player owner) {
/*  60 */     super(null, 0, 2, 2);
/*  61 */     this.active = active;
/*  62 */     this.owner = owner;
/*     */     
/*  64 */     addResultSlot(owner, 154, 28);
/*  65 */     addCraftingGridSlots(98, 18);
/*     */     
/*  67 */     for (int i = 0; i < 4; i++) {
/*  68 */       EquipmentSlot slot = SLOT_IDS[i];
/*  69 */       Identifier emptyIcon = (Identifier)TEXTURE_EMPTY_SLOTS.get(slot);
/*  70 */       addSlot(new ArmorSlot(inventory, owner, slot, 39 - i, 8, 8 + i * 18, emptyIcon));
/*     */     } 
/*     */     
/*  73 */     addStandardInventorySlots(inventory, 8, 84);
/*     */     
/*  75 */     addSlot(new Slot(this, inventory, 40, 77, 62)
/*     */         {
/*     */           public void setByPlayer(ItemStack itemStack, ItemStack previous) {
/*  78 */             owner.onEquipItem(EquipmentSlot.OFFHAND, previous, itemStack);
/*  79 */             super.setByPlayer(itemStack, previous);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*  84 */           public Identifier getNoItemIcon() { return InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD; }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static boolean isHotbarSlot(int slot) { return ((slot >= 36 && slot < 45) || slot == 45); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/*  97 */     Level level1 = this.owner.level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/*  98 */       CraftingMenu.slotChangedCraftingGrid(this, level, this.owner, this.craftSlots, this.resultSlots, null); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 104 */     super.removed(player);
/*     */     
/* 106 */     this.resultSlots.clearContent();
/*     */     
/* 108 */     if (player.level().isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 112 */     clearContainer(player, this.craftSlots);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public boolean stillValid(Player player) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 122 */     ItemStack clicked = ItemStack.EMPTY;
/* 123 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 124 */     if (slot.hasItem()) {
/* 125 */       ItemStack stack = slot.getItem();
/* 126 */       clicked = stack.copy();
/*     */       
/* 128 */       EquipmentSlot eqSlot = player.getEquipmentSlotForItem(clicked);
/*     */       
/* 130 */       if (slotIndex == 0) {
/* 131 */         if (!moveItemStackTo(stack, 9, 45, true)) {
/* 132 */           return ItemStack.EMPTY;
/*     */         }
/* 134 */         slot.onQuickCraft(stack, clicked);
/* 135 */       } else if (slotIndex >= 1 && slotIndex < 5) {
/* 136 */         if (!moveItemStackTo(stack, 9, 45, false)) {
/* 137 */           return ItemStack.EMPTY;
/*     */         }
/* 139 */       } else if (slotIndex >= 5 && slotIndex < 9) {
/* 140 */         if (!moveItemStackTo(stack, 9, 45, false)) {
/* 141 */           return ItemStack.EMPTY;
/*     */         }
/* 143 */       } else if (eqSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && !((Slot)this.slots.get(8 - eqSlot.getIndex())).hasItem()) {
/* 144 */         int pos = 8 - eqSlot.getIndex();
/* 145 */         if (!moveItemStackTo(stack, pos, pos + 1, false)) {
/* 146 */           return ItemStack.EMPTY;
/*     */         }
/* 148 */       } else if (eqSlot == EquipmentSlot.OFFHAND && !((Slot)this.slots.get(45)).hasItem()) {
/* 149 */         if (!moveItemStackTo(stack, 45, 46, false)) {
/* 150 */           return ItemStack.EMPTY;
/*     */         }
/* 152 */       } else if (slotIndex >= 9 && slotIndex < 36) {
/* 153 */         if (!moveItemStackTo(stack, 36, 45, false)) {
/* 154 */           return ItemStack.EMPTY;
/*     */         }
/* 156 */       } else if (slotIndex >= 36 && slotIndex < 45) {
/* 157 */         if (!moveItemStackTo(stack, 9, 36, false)) {
/* 158 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/* 161 */       else if (!moveItemStackTo(stack, 9, 45, false)) {
/* 162 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 165 */       if (stack.isEmpty()) {
/* 166 */         slot.setByPlayer(ItemStack.EMPTY, clicked);
/*     */       } else {
/* 168 */         slot.setChanged();
/*     */       } 
/* 170 */       if (stack.getCount() == clicked.getCount())
/*     */       {
/* 172 */         return ItemStack.EMPTY;
/*     */       }
/* 174 */       slot.onTake(player, stack);
/* 175 */       if (slotIndex == 0) {
/* 176 */         player.drop(stack, false);
/*     */       }
/*     */     } 
/*     */     
/* 180 */     return clicked;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 185 */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) { return (target.container != this.resultSlots && super.canTakeItemForPickAll(carried, target)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public Slot getResultSlot() { return (Slot)this.slots.get(0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   public List<Slot> getInputGridSlots() { return this.slots.subList(1, 5); }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public CraftingContainer getCraftSlots() { return this.craftSlots; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   public RecipeBookType getRecipeBookType() { return RecipeBookType.CRAFTING; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   protected Player owner() { return this.owner; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\InventoryMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */