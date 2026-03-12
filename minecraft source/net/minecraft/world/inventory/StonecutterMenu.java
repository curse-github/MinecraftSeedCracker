/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.SelectableRecipe;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.item.crafting.StonecutterRecipe;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class StonecutterMenu
/*     */   extends AbstractContainerMenu {
/*     */   public static final int INPUT_SLOT = 0;
/*     */   public static final int RESULT_SLOT = 1;
/*     */   private static final int INV_SLOT_START = 2;
/*     */   private static final int INV_SLOT_END = 29;
/*     */   private static final int USE_ROW_SLOT_START = 29;
/*     */   private static final int USE_ROW_SLOT_END = 38;
/*     */   private final ContainerLevelAccess access;
/*  30 */   private final DataSlot selectedRecipeIndex = DataSlot.standalone();
/*     */   
/*     */   private final Level level;
/*  33 */   private SelectableRecipe.SingleInputSet<StonecutterRecipe> recipesForInput = SelectableRecipe.SingleInputSet.empty();
/*  34 */   private ItemStack input = ItemStack.EMPTY;
/*     */   private long lastSoundTime;
/*     */   final Slot inputSlot;
/*     */   final Slot resultSlot;
/*     */   private Runnable slotUpdateListener = () -> {
/*     */     
/*     */     };
/*     */   
/*  42 */   public final Container container = new SimpleContainer(1)
/*     */     {
/*     */       public void setChanged() {
/*  45 */         super.setChanged();
/*  46 */         StonecutterMenu.this.slotsChanged(this);
/*  47 */         StonecutterMenu.this.slotUpdateListener.run();
/*     */       }
/*     */     };
/*  50 */   private final ResultContainer resultContainer = new ResultContainer();
/*     */ 
/*     */   
/*  53 */   public StonecutterMenu(int containerId, Inventory inventory) { this(containerId, inventory, ContainerLevelAccess.NULL); }
/*     */ 
/*     */   
/*     */   public StonecutterMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
/*  57 */     super(MenuType.STONECUTTER, containerId);
/*     */     
/*  59 */     this.access = access;
/*  60 */     this.level = inventory.player.level();
/*     */     
/*  62 */     this.inputSlot = addSlot(new Slot(this.container, 0, 20, 33));
/*     */     
/*  64 */     this.resultSlot = addSlot(new Slot(this.resultContainer, 1, 143, 33)
/*     */         {
/*     */           public boolean mayPlace(ItemStack itemStack) {
/*  67 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public void onTake(Player player, ItemStack carried) {
/*  72 */             carried.onCraftedBy(player, carried.getCount());
/*  73 */             StonecutterMenu.this.resultContainer.awardUsedRecipes(player, getRelevantItems());
/*     */ 
/*     */             
/*  76 */             ItemStack remaining = StonecutterMenu.this.inputSlot.remove(1);
/*  77 */             if (!remaining.isEmpty()) {
/*  78 */               StonecutterMenu.this.setupResultSlot(StonecutterMenu.this.selectedRecipeIndex.get());
/*     */             }
/*     */             
/*  81 */             access.execute((level, pos) -> {
/*     */                   
/*  83 */                   long gameTime = level.getGameTime();
/*  84 */                   if (StonecutterMenu.this.lastSoundTime != gameTime) {
/*  85 */                     level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*  86 */                     StonecutterMenu.this.lastSoundTime = gameTime;
/*     */                   } 
/*     */                 });
/*     */             
/*  90 */             super.onTake(player, carried);
/*     */           }
/*     */           
/*     */           private List<ItemStack> getRelevantItems() {
/*  94 */             return List.of(StonecutterMenu.this.inputSlot
/*  95 */                 .getItem());
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 100 */     addStandardInventorySlots(inventory, 8, 84);
/*     */     
/* 102 */     addDataSlot(this.selectedRecipeIndex);
/*     */   }
/*     */ 
/*     */   
/* 106 */   public int getSelectedRecipeIndex() { return this.selectedRecipeIndex.get(); }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public SelectableRecipe.SingleInputSet<StonecutterRecipe> getVisibleRecipes() { return this.recipesForInput; }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public int getNumberOfVisibleRecipes() { return this.recipesForInput.size(); }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public boolean hasInputItem() { return (this.inputSlot.hasItem() && !this.recipesForInput.isEmpty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public boolean stillValid(Player player) { return stillValid(this.access, player, Blocks.STONECUTTER); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean clickMenuButton(Player player, int buttonId) {
/* 128 */     if (this.selectedRecipeIndex.get() == buttonId) {
/* 129 */       return false;
/*     */     }
/*     */     
/* 132 */     if (isValidRecipeIndex(buttonId)) {
/* 133 */       this.selectedRecipeIndex.set(buttonId);
/* 134 */       setupResultSlot(buttonId);
/*     */     } 
/*     */     
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 141 */   private boolean isValidRecipeIndex(int buttonId) { return (buttonId >= 0 && buttonId < this.recipesForInput.size()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container container) {
/* 146 */     ItemStack input = this.inputSlot.getItem();
/* 147 */     if (!input.is(this.input.getItem())) {
/* 148 */       this.input = input.copy();
/* 149 */       setupRecipeList(input);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupRecipeList(ItemStack item) {
/* 154 */     this.selectedRecipeIndex.set(-1);
/* 155 */     this.resultSlot.set(ItemStack.EMPTY);
/*     */     
/* 157 */     if (!item.isEmpty()) {
/* 158 */       this.recipesForInput = this.level.recipeAccess().stonecutterRecipes().selectByInput(item);
/*     */     } else {
/* 160 */       this.recipesForInput = SelectableRecipe.SingleInputSet.empty();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupResultSlot(int index) {
/*     */     Optional<RecipeHolder<StonecutterRecipe>> usedRecipe;
/* 169 */     if (!this.recipesForInput.isEmpty() && isValidRecipeIndex(index)) {
/* 170 */       SelectableRecipe.SingleInputEntry<StonecutterRecipe> entry = (SelectableRecipe.SingleInputEntry)this.recipesForInput.entries().get(index);
/* 171 */       usedRecipe = entry.recipe().recipe();
/*     */     } else {
/* 173 */       usedRecipe = Optional.empty();
/*     */     } 
/*     */     
/* 176 */     usedRecipe.ifPresentOrElse(recipe -> {
/*     */           
/* 178 */           this.resultContainer.setRecipeUsed(recipe);
/* 179 */           this.resultSlot.set(((StonecutterRecipe)recipe.value()).assemble(new SingleRecipeInput(this.container.getItem(0)), this.level.registryAccess()));
/*     */         }() -> {
/*     */           
/* 182 */           this.resultSlot.set(ItemStack.EMPTY);
/* 183 */           this.resultContainer.setRecipeUsed(null);
/*     */         });
/*     */     
/* 186 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 191 */   public MenuType<?> getType() { return MenuType.STONECUTTER; }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public void registerUpdateListener(Runnable slotUpdateListener) { this.slotUpdateListener = slotUpdateListener; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) { return (target.container != this.resultContainer && super.canTakeItemForPickAll(carried, target)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 205 */     ItemStack clicked = ItemStack.EMPTY;
/* 206 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 207 */     if (slot != null && slot.hasItem()) {
/* 208 */       ItemStack stack = slot.getItem();
/* 209 */       Item item = stack.getItem();
/* 210 */       clicked = stack.copy();
/*     */       
/* 212 */       if (slotIndex == 1) {
/* 213 */         item.onCraftedBy(stack, player);
/* 214 */         if (!moveItemStackTo(stack, 2, 38, true)) {
/* 215 */           return ItemStack.EMPTY;
/*     */         }
/* 217 */         slot.onQuickCraft(stack, clicked);
/* 218 */       } else if (slotIndex == 0) {
/* 219 */         if (!moveItemStackTo(stack, 2, 38, false)) {
/* 220 */           return ItemStack.EMPTY;
/*     */         }
/* 222 */       } else if (this.level.recipeAccess().stonecutterRecipes().acceptsInput(stack)) {
/* 223 */         if (!moveItemStackTo(stack, 0, 1, false)) {
/* 224 */           return ItemStack.EMPTY;
/*     */         }
/* 226 */       } else if (slotIndex >= 2 && slotIndex < 29) {
/* 227 */         if (!moveItemStackTo(stack, 29, 38, false)) {
/* 228 */           return ItemStack.EMPTY;
/*     */         }
/* 230 */       } else if (slotIndex >= 29 && slotIndex < 38 && 
/* 231 */         !moveItemStackTo(stack, 2, 29, false)) {
/* 232 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 236 */       if (stack.isEmpty()) {
/* 237 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       }
/*     */       
/* 240 */       slot.setChanged();
/*     */       
/* 242 */       if (stack.getCount() == clicked.getCount()) {
/* 243 */         return ItemStack.EMPTY;
/*     */       }
/* 245 */       slot.onTake(player, stack);
/* 246 */       if (slotIndex == 1) {
/* 247 */         player.drop(stack, false);
/*     */       }
/* 249 */       broadcastChanges();
/*     */     } 
/*     */     
/* 252 */     return clicked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 257 */     super.removed(player);
/*     */     
/* 259 */     this.resultContainer.removeItemNoUpdate(1);
/* 260 */     this.access.execute((level, pos) -> clearContainer(player, this.container));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\StonecutterMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */