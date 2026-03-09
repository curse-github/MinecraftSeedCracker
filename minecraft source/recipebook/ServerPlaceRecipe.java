/*     */ package net.minecraft.recipebook;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.RecipeBookMenu;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerPlaceRecipe<R extends Recipe<?>>
/*     */   extends Object
/*     */ {
/*     */   private static final int ITEM_NOT_FOUND = -1;
/*     */   private final Inventory inventory;
/*     */   private final CraftingMenuAccess<R> menu;
/*     */   private final boolean useMaxItems;
/*     */   private final int gridWidth;
/*     */   private final int gridHeight;
/*     */   private final List<Slot> inputGridSlots;
/*     */   private final List<Slot> slotsToClear;
/*     */   
/*     */   public static <I extends net.minecraft.world.item.crafting.RecipeInput, R extends Recipe<I>> RecipeBookMenu.PostPlaceAction placeRecipe(CraftingMenuAccess<R> menu, int gridWidth, int gridHeight, List<Slot> inputGridSlots, List<Slot> slotsToClear, Inventory inventory, RecipeHolder<R> recipe, boolean useMaxItems, boolean allowDroppingItemsToClear) {
/*  43 */     ServerPlaceRecipe<R> placer = new ServerPlaceRecipe<R>(menu, inventory, useMaxItems, gridWidth, gridHeight, inputGridSlots, slotsToClear);
/*     */ 
/*     */     
/*  46 */     if (!allowDroppingItemsToClear && !placer.testClearGrid()) {
/*  47 */       return RecipeBookMenu.PostPlaceAction.NOTHING;
/*     */     }
/*     */     
/*  50 */     StackedItemContents availableItems = new StackedItemContents();
/*  51 */     inventory.fillStackedContents(availableItems);
/*  52 */     menu.fillCraftSlotsStackedContents(availableItems);
/*     */     
/*  54 */     return placer.tryPlaceRecipe(recipe, availableItems);
/*     */   }
/*     */   
/*     */   private ServerPlaceRecipe(CraftingMenuAccess<R> menu, Inventory inventory, boolean useMaxItems, int gridWidth, int gridHeight, List<Slot> inputGridSlots, List<Slot> slotsToClear) {
/*  58 */     this.menu = menu;
/*  59 */     this.inventory = inventory;
/*  60 */     this.useMaxItems = useMaxItems;
/*  61 */     this.gridWidth = gridWidth;
/*  62 */     this.gridHeight = gridHeight;
/*  63 */     this.inputGridSlots = inputGridSlots;
/*  64 */     this.slotsToClear = slotsToClear;
/*     */   }
/*     */   
/*     */   private RecipeBookMenu.PostPlaceAction tryPlaceRecipe(RecipeHolder<R> recipe, StackedItemContents availableItems) {
/*  68 */     if (availableItems.canCraft(recipe.value(), null)) {
/*  69 */       placeRecipe(recipe, availableItems);
/*  70 */       this.inventory.setChanged();
/*  71 */       return RecipeBookMenu.PostPlaceAction.NOTHING;
/*     */     } 
/*  73 */     clearGrid();
/*  74 */     this.inventory.setChanged();
/*  75 */     return RecipeBookMenu.PostPlaceAction.PLACE_GHOST_RECIPE;
/*     */   }
/*     */ 
/*     */   
/*     */   private void clearGrid() {
/*  80 */     for (Slot slot : this.slotsToClear) {
/*  81 */       ItemStack itemStackCopy = slot.getItem().copy();
/*  82 */       this.inventory.placeItemBackInInventory(itemStackCopy, false);
/*  83 */       slot.set(itemStackCopy);
/*     */     } 
/*  85 */     this.menu.clearCraftingContent();
/*     */   }
/*     */   
/*     */   private void placeRecipe(RecipeHolder<R> recipe, StackedItemContents availableItems) {
/*  89 */     boolean recipeMatchesPlaced = this.menu.recipeMatches(recipe);
/*  90 */     int biggestCraftableStack = availableItems.getBiggestCraftableStack(recipe.value(), null);
/*     */ 
/*     */     
/*  93 */     if (recipeMatchesPlaced) {
/*  94 */       for (Slot inputSlot : this.inputGridSlots) {
/*  95 */         ItemStack itemStack = inputSlot.getItem();
/*  96 */         if (!itemStack.isEmpty() && Math.min(biggestCraftableStack, itemStack.getMaxStackSize()) < itemStack.getCount() + 1) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 102 */     int amountToCraft = calculateAmountToCraft(biggestCraftableStack, recipeMatchesPlaced);
/* 103 */     List<Holder<Item>> itemsUsedPerIngredient = new ArrayList<Holder<Item>>();
/* 104 */     Objects.requireNonNull(itemsUsedPerIngredient); if (!availableItems.canCraft(recipe.value(), amountToCraft, itemsUsedPerIngredient::add)) {
/*     */       return;
/*     */     }
/*     */     
/* 108 */     int adjustedAmountToCraft = clampToMaxStackSize(amountToCraft, itemsUsedPerIngredient);
/*     */     
/* 110 */     itemsUsedPerIngredient.clear();
/* 111 */     Objects.requireNonNull(itemsUsedPerIngredient); if (adjustedAmountToCraft != amountToCraft && !availableItems.canCraft(recipe.value(), adjustedAmountToCraft, itemsUsedPerIngredient::add)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 116 */     clearGrid();
/* 117 */     PlaceRecipeHelper.placeRecipe(this.gridWidth, this.gridHeight, recipe.value(), recipe.value().placementInfo().slotsToIngredientIndex(), (ingredientIndex, gridIndex, gridXPos, gridYPos) -> {
/*     */           
/* 119 */           if (ingredientIndex.intValue() == -1) {
/*     */             return;
/*     */           }
/* 122 */           Slot targetGridSlot = (Slot)this.inputGridSlots.get(gridIndex);
/* 123 */           Holder<Item> itemUsed = (Holder)itemsUsedPerIngredient.get(ingredientIndex.intValue());
/* 124 */           int remainingCount = adjustedAmountToCraft;
/* 125 */           while (remainingCount > 0) {
/* 126 */             remainingCount = moveItemToGrid(targetGridSlot, itemUsed, remainingCount);
/* 127 */             if (remainingCount == -1) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static int clampToMaxStackSize(int value, List<Holder<Item>> items) {
/* 136 */     for (Holder<Item> item : items) {
/* 137 */       value = Math.min(value, ((Item)item.value()).getDefaultMaxStackSize());
/*     */     }
/* 139 */     return value;
/*     */   }
/*     */   
/*     */   private int calculateAmountToCraft(int biggestCraftableStack, boolean recipeMatchesPlaced) {
/* 143 */     if (this.useMaxItems) {
/* 144 */       return biggestCraftableStack;
/*     */     }
/*     */     
/* 147 */     if (recipeMatchesPlaced) {
/* 148 */       int smallestStackSize = Integer.MAX_VALUE;
/*     */       
/* 150 */       for (Slot inputSlot : this.inputGridSlots) {
/* 151 */         ItemStack itemStack = inputSlot.getItem();
/* 152 */         if (!itemStack.isEmpty() && smallestStackSize > itemStack.getCount()) {
/* 153 */           smallestStackSize = itemStack.getCount();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 158 */       if (smallestStackSize != Integer.MAX_VALUE) {
/* 159 */         smallestStackSize++;
/*     */       }
/* 161 */       return smallestStackSize;
/*     */     } 
/*     */     
/* 164 */     return 1;
/*     */   }
/*     */   
/*     */   private int moveItemToGrid(Slot targetSlot, Holder<Item> itemInInventory, int count) {
/* 168 */     ItemStack takenStack, itemInTargetSlot = targetSlot.getItem();
/* 169 */     int inventorySlotId = this.inventory.findSlotMatchingCraftingIngredient(itemInInventory, itemInTargetSlot);
/* 170 */     if (inventorySlotId == -1) {
/* 171 */       return -1;
/*     */     }
/* 173 */     ItemStack inventoryItem = this.inventory.getItem(inventorySlotId);
/*     */ 
/*     */     
/* 176 */     if (count < inventoryItem.getCount()) {
/* 177 */       takenStack = this.inventory.removeItem(inventorySlotId, count);
/*     */     } else {
/* 179 */       takenStack = this.inventory.removeItemNoUpdate(inventorySlotId);
/*     */     } 
/*     */     
/* 182 */     int takenCount = takenStack.getCount();
/* 183 */     if (itemInTargetSlot.isEmpty()) {
/* 184 */       targetSlot.set(takenStack);
/*     */     }
/*     */     else {
/*     */       
/* 188 */       itemInTargetSlot.grow(takenCount);
/*     */     } 
/*     */     
/* 191 */     return count - takenCount;
/*     */   }
/*     */   
/*     */   private boolean testClearGrid() {
/* 195 */     List<ItemStack> freeSlots = Lists.newArrayList();
/* 196 */     int freeSlotsInInventory = getAmountOfFreeSlotsInInventory();
/*     */     
/* 198 */     for (Slot inputSlot : this.inputGridSlots) {
/* 199 */       ItemStack itemStack = inputSlot.getItem().copy();
/* 200 */       if (itemStack.isEmpty()) {
/*     */         continue;
/*     */       }
/*     */       
/* 204 */       int slotId = this.inventory.getSlotWithRemainingSpace(itemStack);
/* 205 */       if (slotId == -1 && freeSlots.size() <= freeSlotsInInventory) {
/* 206 */         for (ItemStack itemStackInList : freeSlots) {
/* 207 */           if (ItemStack.isSameItem(itemStackInList, itemStack) && itemStackInList.getCount() != itemStackInList.getMaxStackSize() && itemStackInList.getCount() + itemStack.getCount() <= itemStackInList.getMaxStackSize()) {
/* 208 */             itemStackInList.grow(itemStack.getCount());
/* 209 */             itemStack.setCount(0);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 214 */         if (!itemStack.isEmpty()) {
/* 215 */           if (freeSlots.size() < freeSlotsInInventory) {
/* 216 */             freeSlots.add(itemStack); continue;
/*     */           } 
/* 218 */           return false;
/*     */         } 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 225 */       if (slotId == -1) {
/* 226 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 230 */     return true;
/*     */   }
/*     */   
/*     */   private int getAmountOfFreeSlotsInInventory() {
/* 234 */     int freeSlots = 0;
/* 235 */     for (ItemStack item : this.inventory.getNonEquipmentItems()) {
/* 236 */       if (item.isEmpty()) {
/* 237 */         freeSlots++;
/*     */       }
/*     */     } 
/* 240 */     return freeSlots;
/*     */   }
/*     */   
/*     */   public static interface CraftingMenuAccess<T extends Recipe<?>> {
/*     */     void fillCraftSlotsStackedContents(StackedItemContents param1StackedItemContents);
/*     */     
/*     */     void clearCraftingContent();
/*     */     
/*     */     boolean recipeMatches(RecipeHolder<T> param1RecipeHolder);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\recipebook\ServerPlaceRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */