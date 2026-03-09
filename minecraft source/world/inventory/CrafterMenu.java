/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.level.block.CrafterBlock;
/*     */ 
/*     */ public class CrafterMenu
/*     */   extends AbstractContainerMenu
/*     */   implements ContainerListener {
/*     */   protected static final int SLOT_COUNT = 9;
/*     */   private static final int INV_SLOT_START = 9;
/*  19 */   private final ResultContainer resultContainer = new ResultContainer(); private static final int INV_SLOT_END = 36; private static final int USE_ROW_SLOT_START = 36; private static final int USE_ROW_SLOT_END = 45;
/*     */   private final ContainerData containerData;
/*     */   private final Player player;
/*     */   private final CraftingContainer container;
/*     */   
/*     */   public CrafterMenu(int containerId, Inventory inventory) {
/*  25 */     super(MenuType.CRAFTER_3x3, containerId);
/*  26 */     this.player = inventory.player;
/*  27 */     this.containerData = new SimpleContainerData(10);
/*  28 */     this.container = new TransientCraftingContainer(this, 3, 3);
/*  29 */     addSlots(inventory);
/*     */   }
/*     */   
/*     */   public CrafterMenu(int containerId, Inventory inventory, CraftingContainer container, ContainerData containerData) {
/*  33 */     super(MenuType.CRAFTER_3x3, containerId);
/*  34 */     this.player = inventory.player;
/*  35 */     this.containerData = containerData;
/*  36 */     this.container = container;
/*  37 */     checkContainerSize(container, 9);
/*  38 */     container.startOpen(inventory.player);
/*  39 */     addSlots(inventory);
/*  40 */     addSlotListener(this);
/*     */   }
/*     */   
/*     */   private void addSlots(Inventory inventory) {
/*  44 */     for (int y = 0; y < 3; y++) {
/*  45 */       for (int x = 0; x < 3; x++) {
/*  46 */         int slot = x + y * 3;
/*  47 */         addSlot(new CrafterSlot(this.container, slot, 26 + x * 18, 17 + y * 18, this));
/*     */       } 
/*     */     } 
/*     */     
/*  51 */     addStandardInventorySlots(inventory, 8, 84);
/*     */     
/*  53 */     addSlot(new NonInteractiveResultSlot(this.resultContainer, 0, 134, 35));
/*  54 */     addDataSlots(this.containerData);
/*  55 */     refreshRecipeResult();
/*     */   }
/*     */   
/*     */   public void setSlotState(int slotId, boolean isEnabled) {
/*  59 */     CrafterSlot slot = (CrafterSlot)getSlot(slotId);
/*  60 */     this.containerData.set(slot.index, isEnabled ? 0 : 1);
/*  61 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   public boolean isSlotDisabled(int slotId) {
/*  65 */     if (slotId > -1 && slotId < 9) {
/*  66 */       return (this.containerData.get(slotId) == 1);
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   
/*  72 */   public boolean isPowered() { return (this.containerData.get(9) == 1); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  77 */     ItemStack clicked = ItemStack.EMPTY;
/*  78 */     Slot slot = (Slot)this.slots.get(slotIndex);
/*  79 */     if (slot != null && slot.hasItem()) {
/*  80 */       ItemStack stack = slot.getItem();
/*  81 */       clicked = stack.copy();
/*     */       
/*  83 */       if (slotIndex < 9) {
/*  84 */         if (!moveItemStackTo(stack, 9, 45, true)) {
/*  85 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/*  88 */       else if (!moveItemStackTo(stack, 0, 9, false)) {
/*  89 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/*  92 */       if (stack.isEmpty()) {
/*  93 */         slot.set(ItemStack.EMPTY);
/*     */       } else {
/*  95 */         slot.setChanged();
/*     */       } 
/*  97 */       if (stack.getCount() == clicked.getCount())
/*     */       {
/*  99 */         return ItemStack.EMPTY;
/*     */       }
/* 101 */       slot.onTake(player, stack);
/*     */     } 
/*     */     
/* 104 */     return clicked;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public boolean stillValid(Player player) { return this.container.stillValid(player); }
/*     */ 
/*     */   
/*     */   private void refreshRecipeResult() {
/* 113 */     Player player1 = this.player; if (player1 instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player1;
/* 114 */       ServerLevel level = serverPlayer.level();
/* 115 */       CraftingInput craftInput = this.container.asCraftInput();
/*     */ 
/*     */       
/* 118 */       ItemStack result = (ItemStack)CrafterBlock.getPotentialResults(level, craftInput).map(recipe -> ((CraftingRecipe)recipe.value()).assemble(craftInput, level.registryAccess())).orElse(ItemStack.EMPTY);
/* 119 */       this.resultContainer.setItem(0, result); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 124 */   public Container getContainer() { return this.container; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) { refreshRecipeResult(); }
/*     */   
/*     */   public void dataChanged(AbstractContainerMenu container, int id, int value) {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\CrafterMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */