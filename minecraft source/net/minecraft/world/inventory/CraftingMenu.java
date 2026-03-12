/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CraftingMenu
/*     */   extends AbstractCraftingMenu
/*     */ {
/*     */   private static final int CRAFTING_GRID_WIDTH = 3;
/*     */   private static final int CRAFTING_GRID_HEIGHT = 3;
/*     */   public static final int RESULT_SLOT = 0;
/*     */   private static final int CRAFT_SLOT_START = 1;
/*     */   private static final int CRAFT_SLOT_COUNT = 9;
/*     */   private static final int CRAFT_SLOT_END = 10;
/*     */   private static final int INV_SLOT_START = 10;
/*     */   private static final int INV_SLOT_END = 37;
/*     */   private static final int USE_ROW_SLOT_START = 37;
/*     */   private static final int USE_ROW_SLOT_END = 46;
/*     */   private final ContainerLevelAccess access;
/*     */   private final Player player;
/*     */   private boolean placingRecipe;
/*     */   
/*  39 */   public CraftingMenu(int containerId, Inventory inventory) { this(containerId, inventory, ContainerLevelAccess.NULL); }
/*     */ 
/*     */   
/*     */   public CraftingMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
/*  43 */     super(MenuType.CRAFTING, containerId, 3, 3);
/*  44 */     this.access = access;
/*  45 */     this.player = inventory.player;
/*     */     
/*  47 */     addResultSlot(this.player, 124, 35);
/*  48 */     addCraftingGridSlots(30, 17);
/*     */     
/*  50 */     addStandardInventorySlots(inventory, 8, 84);
/*     */   }
/*     */   
/*     */   protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, ServerLevel level, Player player, CraftingContainer container, ResultContainer resultSlots, RecipeHolder<CraftingRecipe> recipeHint) {
/*  54 */     CraftingInput input = container.asCraftInput();
/*  55 */     ServerPlayer serverPlayer = (ServerPlayer)player;
/*  56 */     ItemStack result = ItemStack.EMPTY;
/*  57 */     Optional<RecipeHolder<CraftingRecipe>> maybeRecipe = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level, recipeHint);
/*  58 */     if (maybeRecipe.isPresent()) {
/*  59 */       RecipeHolder<CraftingRecipe> recipeHolder = (RecipeHolder)maybeRecipe.get();
/*  60 */       CraftingRecipe craftingRecipe = (CraftingRecipe)recipeHolder.value();
/*  61 */       if (resultSlots.setRecipeUsed(serverPlayer, recipeHolder)) {
/*  62 */         ItemStack recipeResult = craftingRecipe.assemble(input, level.registryAccess());
/*  63 */         if (recipeResult.isItemEnabled(level.enabledFeatures())) {
/*  64 */           result = recipeResult;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  69 */     resultSlots.setItem(0, result);
/*  70 */     menu.setRemoteSlot(0, result);
/*  71 */     serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/*  76 */     if (!this.placingRecipe) {
/*  77 */       this.access.execute((level, pos) -> {
/*  78 */             if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  79 */               slotChangedCraftingGrid(this, serverLevel, this.player, this.craftSlots, this.resultSlots, null); }
/*     */           
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public void beginPlacingRecipe() { this.placingRecipe = true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void finishPlacingRecipe(ServerLevel level, RecipeHolder<CraftingRecipe> recipe) {
/*  92 */     this.placingRecipe = false;
/*  93 */     slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots, recipe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/*  98 */     super.removed(player);
/*  99 */     this.access.execute((level, pos) -> clearContainer(player, this.craftSlots));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public boolean stillValid(Player player) { return stillValid(this.access, player, Blocks.CRAFTING_TABLE); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 109 */     ItemStack clicked = ItemStack.EMPTY;
/* 110 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 111 */     if (slot != null && slot.hasItem()) {
/* 112 */       ItemStack stack = slot.getItem();
/* 113 */       clicked = stack.copy();
/*     */       
/* 115 */       if (slotIndex == 0) {
/*     */         
/* 117 */         stack.getItem().onCraftedBy(stack, player);
/* 118 */         if (!moveItemStackTo(stack, 10, 46, true)) {
/* 119 */           return ItemStack.EMPTY;
/*     */         }
/* 121 */         slot.onQuickCraft(stack, clicked);
/* 122 */       } else if (slotIndex >= 10 && slotIndex < 46) {
/* 123 */         if (!moveItemStackTo(stack, 1, 10, false)) {
/* 124 */           if (slotIndex < 37) {
/* 125 */             if (!moveItemStackTo(stack, 37, 46, false)) {
/* 126 */               return ItemStack.EMPTY;
/*     */             }
/*     */           }
/* 129 */           else if (!moveItemStackTo(stack, 10, 37, false)) {
/* 130 */             return ItemStack.EMPTY;
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 135 */       else if (!moveItemStackTo(stack, 10, 46, false)) {
/* 136 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 139 */       if (stack.isEmpty()) {
/* 140 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/* 142 */         slot.setChanged();
/*     */       } 
/* 144 */       if (stack.getCount() == clicked.getCount())
/*     */       {
/* 146 */         return ItemStack.EMPTY;
/*     */       }
/* 148 */       slot.onTake(player, stack);
/* 149 */       if (slotIndex == 0) {
/* 150 */         player.drop(stack, false);
/*     */       }
/*     */     } 
/*     */     
/* 154 */     return clicked;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) { return (target.container != this.resultSlots && super.canTakeItemForPickAll(carried, target)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public Slot getResultSlot() { return (Slot)this.slots.get(0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public List<Slot> getInputGridSlots() { return this.slots.subList(1, 10); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public RecipeBookType getRecipeBookType() { return RecipeBookType.CRAFTING; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   protected Player owner() { return this.player; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\CraftingMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */