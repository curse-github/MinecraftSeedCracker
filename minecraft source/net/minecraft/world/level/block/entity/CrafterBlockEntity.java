/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.CrafterMenu;
/*     */ import net.minecraft.world.inventory.CraftingContainer;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.CrafterBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class CrafterBlockEntity
/*     */   extends RandomizableContainerBlockEntity
/*     */   implements CraftingContainer
/*     */ {
/*     */   public static final int CONTAINER_WIDTH = 3;
/*     */   public static final int CONTAINER_HEIGHT = 3;
/*     */   public static final int CONTAINER_SIZE = 9;
/*     */   public static final int SLOT_DISABLED = 1;
/*     */   public static final int SLOT_ENABLED = 0;
/*     */   public static final int DATA_TRIGGERED = 9;
/*     */   public static final int NUM_DATA = 10;
/*     */   private static final int DEFAULT_CRAFTING_TICKS_REMAINING = 0;
/*     */   private static final int DEFAULT_TRIGGERED = 0;
/*  38 */   private static final Component DEFAULT_NAME = Component.translatable("container.crafter");
/*     */   
/*  40 */   private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
/*     */   
/*  42 */   private int craftingTicksRemaining = 0;
/*     */   
/*  44 */   protected final ContainerData containerData = new ContainerData(this)
/*     */     {
/*  46 */       private final int[] slotStates = new int[9];
/*  47 */       private int triggered = 0;
/*     */ 
/*     */ 
/*     */       
/*  51 */       public int get(int dataId) { return (dataId == 9) ? this.triggered : this.slotStates[dataId]; }
/*     */ 
/*     */ 
/*     */       
/*     */       public void set(int dataId, int value) {
/*  56 */         if (dataId == 9) {
/*  57 */           this.triggered = value;
/*     */         } else {
/*  59 */           this.slotStates[dataId] = value;
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  65 */       public int getCount() { return 10; }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  70 */   public CrafterBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.CRAFTER, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return new CrafterMenu(containerId, inventory, this, this.containerData); }
/*     */ 
/*     */   
/*     */   public void setSlotState(int slotId, boolean enabled) {
/*  84 */     if (!slotCanBeDisabled(slotId)) {
/*     */       return;
/*     */     }
/*  87 */     this.containerData.set(slotId, enabled ? 0 : 1);
/*  88 */     setChanged();
/*     */   }
/*     */   
/*     */   public boolean isSlotDisabled(int slotId) {
/*  92 */     if (slotId >= 0 && slotId < 9) {
/*  93 */       return (this.containerData.get(slotId) == 1);
/*     */     }
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 100 */     if (this.containerData.get(slot) == 1) {
/* 101 */       return false;
/*     */     }
/*     */     
/* 104 */     ItemStack slotStack = (ItemStack)this.items.get(slot);
/* 105 */     int currentStackSize = slotStack.getCount();
/* 106 */     if (currentStackSize >= slotStack.getMaxStackSize()) {
/* 107 */       return false;
/*     */     }
/*     */     
/* 110 */     if (slotStack.isEmpty()) {
/* 111 */       return true;
/*     */     }
/*     */     
/* 114 */     return !smallerStackExist(currentStackSize, slotStack, slot);
/*     */   }
/*     */   
/*     */   private boolean smallerStackExist(int baseSize, ItemStack baseItem, int baseSlot) {
/* 118 */     for (int i = baseSlot + 1; i < 9; i++) {
/* 119 */       if (!isSlotDisabled(i)) {
/* 120 */         ItemStack slotStack = getItem(i);
/* 121 */         if (slotStack.isEmpty() || (slotStack.getCount() < baseSize && ItemStack.isSameItemSameComponents(slotStack, baseItem))) {
/* 122 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 131 */     super.loadAdditional(input);
/*     */     
/* 133 */     this.craftingTicksRemaining = input.getIntOr("crafting_ticks_remaining", 0);
/* 134 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 135 */     if (!tryLoadLootTable(input)) {
/* 136 */       ContainerHelper.loadAllItems(input, this.items);
/*     */     }
/*     */     
/* 139 */     for (int i = 0; i < 9; i++) {
/* 140 */       this.containerData.set(i, 0);
/*     */     }
/* 142 */     input.getIntArray("disabled_slots").ifPresent(disabledSlots -> {
/* 143 */           for (int i : disabledSlots) {
/* 144 */             if (slotCanBeDisabled(i)) {
/* 145 */               this.containerData.set(i, 1);
/*     */             }
/*     */           } 
/*     */         });
/*     */     
/* 150 */     this.containerData.set(9, input.getIntOr("triggered", 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 155 */     super.saveAdditional(output);
/*     */     
/* 157 */     output.putInt("crafting_ticks_remaining", this.craftingTicksRemaining);
/* 158 */     if (!trySaveLootTable(output)) {
/* 159 */       ContainerHelper.saveAllItems(output, this.items);
/*     */     }
/*     */     
/* 162 */     addDisabledSlots(output);
/* 163 */     addTriggered(output);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public int getContainerSize() { return 9; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 173 */     for (ItemStack is : this.items) {
/* 174 */       if (!is.isEmpty()) {
/* 175 */         return false;
/*     */       }
/*     */     } 
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 183 */   public ItemStack getItem(int slot) { return (ItemStack)this.items.get(slot); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 188 */     if (isSlotDisabled(slot))
/*     */     {
/* 190 */       setSlotState(slot, true);
/*     */     }
/* 192 */     super.setItem(slot, itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 197 */   public boolean stillValid(Player player) { return Container.stillValidBlockEntity(this, player); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   protected void setItems(NonNullList<ItemStack> items) { this.items = items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public int getWidth() { return 3; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public int getHeight() { return 3; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedItemContents contents) {
/* 222 */     for (ItemStack itemStack : this.items) {
/* 223 */       contents.accountSimpleStack(itemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addDisabledSlots(ValueOutput output) {
/* 228 */     IntArrayList intArrayList = new IntArrayList();
/* 229 */     for (int i = 0; i < 9; i++) {
/* 230 */       if (isSlotDisabled(i)) {
/* 231 */         intArrayList.add(i);
/*     */       }
/*     */     } 
/* 234 */     output.putIntArray("disabled_slots", intArrayList.toIntArray());
/*     */   }
/*     */ 
/*     */   
/* 238 */   private void addTriggered(ValueOutput output) { output.putInt("triggered", this.containerData.get(9)); }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public void setTriggered(boolean value) { this.containerData.set(9, value ? 1 : 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 247 */   public boolean isTriggered() { return (this.containerData.get(9) == 1); }
/*     */ 
/*     */   
/*     */   public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CrafterBlockEntity entity) {
/* 251 */     int craftingTicksRemaining = entity.craftingTicksRemaining - 1;
/*     */     
/* 253 */     if (craftingTicksRemaining < 0) {
/*     */       return;
/*     */     }
/*     */     
/* 257 */     entity.craftingTicksRemaining = craftingTicksRemaining;
/* 258 */     if (craftingTicksRemaining == 0) {
/* 259 */       level.setBlock(blockPos, (BlockState)blockState.setValue(CrafterBlock.CRAFTING, Boolean.valueOf(false)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 264 */   public void setCraftingTicksRemaining(int maxCraftingTicks) { this.craftingTicksRemaining = maxCraftingTicks; }
/*     */ 
/*     */   
/*     */   public int getRedstoneSignal() {
/* 268 */     int count = 0;
/*     */     
/* 270 */     for (int i = 0; i < getContainerSize(); i++) {
/* 271 */       ItemStack itemStack = getItem(i);
/*     */       
/* 273 */       if (!itemStack.isEmpty() || isSlotDisabled(i)) {
/* 274 */         count++;
/*     */       }
/*     */     } 
/*     */     
/* 278 */     return count;
/*     */   }
/*     */   
/*     */   private boolean slotCanBeDisabled(int slotId) {
/* 282 */     return (slotId > -1 && slotId < 9 && ((ItemStack)this.items
/*     */       
/* 284 */       .get(slotId)).isEmpty());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\CrafterBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */