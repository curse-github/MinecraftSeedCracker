/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.HashBasedTable;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.HashedStack;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.BundleItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractContainerMenu
/*     */ {
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   public static final int SLOT_CLICKED_OUTSIDE = -999;
/*     */   public static final int QUICKCRAFT_TYPE_CHARITABLE = 0;
/*     */   public static final int QUICKCRAFT_TYPE_GREEDY = 1;
/*     */   public static final int QUICKCRAFT_TYPE_CLONE = 2;
/*     */   public static final int QUICKCRAFT_HEADER_START = 0;
/*     */   public static final int QUICKCRAFT_HEADER_CONTINUE = 1;
/*     */   public static final int QUICKCRAFT_HEADER_END = 2;
/*     */   public static final int CARRIED_SLOT_SIZE = 2147483647;
/*     */   public static final int SLOTS_PER_ROW = 9;
/*     */   public static final int SLOT_SIZE = 18;
/*     */   private final NonNullList<ItemStack> lastSlots;
/*     */   public final NonNullList<Slot> slots;
/*     */   private final List<DataSlot> dataSlots;
/*     */   
/*     */   protected AbstractContainerMenu(MenuType<?> menuType, int containerId) {
/*  57 */     this.lastSlots = NonNullList.create();
/*  58 */     this.slots = NonNullList.create();
/*  59 */     this.dataSlots = Lists.newArrayList();
/*  60 */     this.carried = ItemStack.EMPTY;
/*     */ 
/*     */     
/*  63 */     this.remoteSlots = NonNullList.create();
/*  64 */     this.remoteDataSlots = new IntArrayList();
/*  65 */     this.remoteCarried = RemoteSlot.PLACEHOLDER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     this.quickcraftType = -1;
/*     */     
/*  74 */     this.quickcraftSlots = Sets.newHashSet();
/*     */     
/*  76 */     this.containerListeners = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.menuType = menuType;
/*  82 */     this.containerId = containerId;
/*     */   }
/*     */   private ItemStack carried; private final NonNullList<RemoteSlot> remoteSlots; private final IntList remoteDataSlots; private RemoteSlot remoteCarried; private int stateId; private final MenuType<?> menuType; public final int containerId; private int quickcraftType; private int quickcraftStatus; private final Set<Slot> quickcraftSlots; private final List<ContainerListener> containerListeners; private ContainerSynchronizer synchronizer; private boolean suppressRemoteUpdates;
/*     */   protected void addInventoryHotbarSlots(Container inventory, int left, int top) {
/*  86 */     for (int x = 0; x < 9; x++) {
/*  87 */       addSlot(new Slot(inventory, x, left + x * 18, top));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addInventoryExtendedSlots(Container inventory, int left, int top) {
/*  92 */     for (int y = 0; y < 3; y++) {
/*  93 */       for (int x = 0; x < 9; x++) {
/*  94 */         addSlot(new Slot(inventory, x + (y + 1) * 9, left + x * 18, top + y * 18));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addStandardInventorySlots(Container container, int left, int top) {
/* 100 */     addInventoryExtendedSlots(container, left, top);
/* 101 */     int hotbarSeparator = 4;
/* 102 */     int topToHotbar = 58;
/* 103 */     addInventoryHotbarSlots(container, left, top + 58);
/*     */   }
/*     */ 
/*     */   
/* 107 */   protected static boolean stillValid(ContainerLevelAccess access, Player player, Block block) { return ((Boolean)access.evaluate((level, pos) -> {
/* 108 */           if (!level.getBlockState(pos).is(block)) {
/* 109 */             return Boolean.valueOf(false);
/*     */           }
/* 111 */           return Boolean.valueOf(player.isWithinBlockInteractionRange(pos, 4.0D));
/* 112 */         }Boolean.valueOf(true))).booleanValue(); }
/*     */ 
/*     */   
/*     */   public MenuType<?> getType() {
/* 116 */     if (this.menuType == null) {
/* 117 */       throw new UnsupportedOperationException("Unable to construct this menu by type");
/*     */     }
/* 119 */     return this.menuType;
/*     */   }
/*     */   
/*     */   protected static void checkContainerSize(Container container, int expected) {
/* 123 */     int actual = container.getContainerSize();
/* 124 */     if (actual < expected) {
/* 125 */       throw new IllegalArgumentException("Container size " + actual + " is smaller than expected " + expected);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void checkContainerDataCount(ContainerData data, int expected) {
/* 130 */     int actual = data.getCount();
/* 131 */     if (actual < expected) {
/* 132 */       throw new IllegalArgumentException("Container data count " + actual + " is smaller than expected " + expected);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 137 */   public boolean isValidSlotIndex(int slotIndex) { return (slotIndex == -1 || slotIndex == -999 || slotIndex < this.slots.size()); }
/*     */ 
/*     */   
/*     */   protected Slot addSlot(Slot slot) {
/* 141 */     slot.index = this.slots.size();
/* 142 */     this.slots.add(slot);
/* 143 */     this.lastSlots.add(ItemStack.EMPTY);
/* 144 */     this.remoteSlots.add((this.synchronizer != null) ? this.synchronizer.createSlot() : RemoteSlot.PLACEHOLDER);
/* 145 */     return slot;
/*     */   }
/*     */   
/*     */   protected DataSlot addDataSlot(DataSlot dataSlot) {
/* 149 */     this.dataSlots.add(dataSlot);
/* 150 */     this.remoteDataSlots.add(0);
/* 151 */     return dataSlot;
/*     */   }
/*     */   
/*     */   protected void addDataSlots(ContainerData container) {
/* 155 */     for (int i = 0; i < container.getCount(); i++) {
/* 156 */       addDataSlot(DataSlot.forContainer(container, i));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addSlotListener(ContainerListener listener) {
/* 161 */     if (this.containerListeners.contains(listener)) {
/*     */       return;
/*     */     }
/* 164 */     this.containerListeners.add(listener);
/* 165 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   public void setSynchronizer(ContainerSynchronizer synchronizer) {
/* 169 */     this.synchronizer = synchronizer;
/*     */     
/* 171 */     this.remoteCarried = synchronizer.createSlot();
/* 172 */     this.remoteSlots.replaceAll(ignored -> synchronizer.createSlot());
/* 173 */     sendAllDataToRemote();
/*     */   }
/*     */   
/*     */   public void sendAllDataToRemote() {
/* 177 */     List<ItemStack> itemsToSend = new ArrayList<ItemStack>(this.slots.size());
/* 178 */     for (int i = 0, slotsSize = this.slots.size(); i < slotsSize; i++) {
/* 179 */       ItemStack slotContents = ((Slot)this.slots.get(i)).getItem();
/* 180 */       itemsToSend.add(slotContents.copy());
/* 181 */       ((RemoteSlot)this.remoteSlots.get(i)).force(slotContents);
/*     */     } 
/*     */     
/* 184 */     ItemStack carried = getCarried();
/* 185 */     this.remoteCarried.force(carried);
/*     */     
/* 187 */     for (int i = 0, slotsSize = this.dataSlots.size(); i < slotsSize; i++) {
/* 188 */       this.remoteDataSlots.set(i, ((DataSlot)this.dataSlots.get(i)).get());
/*     */     }
/*     */     
/* 191 */     if (this.synchronizer != null) {
/* 192 */       this.synchronizer.sendInitialData(this, itemsToSend, carried.copy(), this.remoteDataSlots.toIntArray());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 197 */   public void removeSlotListener(ContainerListener listener) { this.containerListeners.remove(listener); }
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 201 */     NonNullList<ItemStack> itemStacks = NonNullList.create();
/* 202 */     for (Slot slot : this.slots) {
/* 203 */       itemStacks.add(slot.getItem());
/*     */     }
/* 205 */     return itemStacks;
/*     */   }
/*     */   
/*     */   public void broadcastChanges() {
/* 209 */     for (int i = 0; i < this.slots.size(); i++) {
/* 210 */       ItemStack current = ((Slot)this.slots.get(i)).getItem();
/* 211 */       Objects.requireNonNull(current); Supplier supplier = Suppliers.memoize(current::copy);
/* 212 */       triggerSlotListeners(i, current, supplier);
/* 213 */       synchronizeSlotToRemote(i, current, supplier);
/*     */     } 
/*     */     
/* 216 */     synchronizeCarriedToRemote();
/*     */     
/* 218 */     for (int i = 0; i < this.dataSlots.size(); i++) {
/* 219 */       DataSlot current = (DataSlot)this.dataSlots.get(i);
/* 220 */       int currentValue = current.get();
/* 221 */       if (current.checkAndClearUpdateFlag()) {
/* 222 */         updateDataSlotListeners(i, currentValue);
/*     */       }
/* 224 */       synchronizeDataSlotToRemote(i, currentValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastFullState() {
/* 229 */     for (int i = 0; i < this.slots.size(); i++) {
/* 230 */       ItemStack current = ((Slot)this.slots.get(i)).getItem();
/* 231 */       Objects.requireNonNull(current); triggerSlotListeners(i, current, current::copy);
/*     */     } 
/*     */     
/* 234 */     for (int i = 0; i < this.dataSlots.size(); i++) {
/* 235 */       DataSlot current = (DataSlot)this.dataSlots.get(i);
/* 236 */       if (current.checkAndClearUpdateFlag()) {
/* 237 */         updateDataSlotListeners(i, current.get());
/*     */       }
/*     */     } 
/* 240 */     sendAllDataToRemote();
/*     */   }
/*     */   
/*     */   private void updateDataSlotListeners(int id, int currentValue) {
/* 244 */     for (ContainerListener containerListener : this.containerListeners) {
/* 245 */       containerListener.dataChanged(this, id, currentValue);
/*     */     }
/*     */   }
/*     */   
/*     */   private void triggerSlotListeners(int i, ItemStack current, Supplier<ItemStack> currentCopy) {
/* 250 */     ItemStack localExpected = (ItemStack)this.lastSlots.get(i);
/* 251 */     if (!ItemStack.matches(localExpected, current)) {
/* 252 */       ItemStack newItem = (ItemStack)currentCopy.get();
/* 253 */       this.lastSlots.set(i, newItem);
/* 254 */       for (ContainerListener containerListener : this.containerListeners) {
/* 255 */         containerListener.slotChanged(this, i, newItem);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void synchronizeSlotToRemote(int i, ItemStack current, Supplier<ItemStack> currentCopy) {
/* 261 */     if (this.suppressRemoteUpdates) {
/*     */       return;
/*     */     }
/*     */     
/* 265 */     RemoteSlot remoteExpected = (RemoteSlot)this.remoteSlots.get(i);
/* 266 */     if (!remoteExpected.matches(current)) {
/* 267 */       remoteExpected.force(current);
/* 268 */       if (this.synchronizer != null) {
/* 269 */         this.synchronizer.sendSlotChange(this, i, (ItemStack)currentCopy.get());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void synchronizeDataSlotToRemote(int i, int current) {
/* 275 */     if (this.suppressRemoteUpdates) {
/*     */       return;
/*     */     }
/*     */     
/* 279 */     int remoteExpected = this.remoteDataSlots.getInt(i);
/* 280 */     if (remoteExpected != current) {
/* 281 */       this.remoteDataSlots.set(i, current);
/* 282 */       if (this.synchronizer != null) {
/* 283 */         this.synchronizer.sendDataChange(this, i, current);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void synchronizeCarriedToRemote() {
/* 289 */     if (this.suppressRemoteUpdates) {
/*     */       return;
/*     */     }
/*     */     
/* 293 */     ItemStack carriedItem = getCarried();
/* 294 */     if (!this.remoteCarried.matches(carriedItem)) {
/* 295 */       this.remoteCarried.force(carriedItem);
/* 296 */       if (this.synchronizer != null) {
/* 297 */         this.synchronizer.sendCarriedChange(this, carriedItem.copy());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 303 */   public void setRemoteSlot(int slot, ItemStack itemStack) { ((RemoteSlot)this.remoteSlots.get(slot)).force(itemStack); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteSlotUnsafe(int slot, HashedStack itemStack) {
/* 310 */     if (slot < 0 || slot >= this.remoteSlots.size()) {
/* 311 */       LOGGER.debug("Incorrect slot index: {} available slots: {}", Integer.valueOf(slot), Integer.valueOf(this.remoteSlots.size()));
/*     */       return;
/*     */     } 
/* 314 */     ((RemoteSlot)this.remoteSlots.get(slot)).receive(itemStack);
/*     */   }
/*     */ 
/*     */   
/* 318 */   public void setRemoteCarried(HashedStack carriedItem) { this.remoteCarried.receive(carriedItem); }
/*     */ 
/*     */ 
/*     */   
/* 322 */   public boolean clickMenuButton(Player player, int buttonId) { return false; }
/*     */ 
/*     */ 
/*     */   
/* 326 */   public Slot getSlot(int index) { return (Slot)this.slots.get(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelectedBundleItemIndex(int slotIndex, int selectedItemIndex) {
/* 332 */     if (slotIndex >= 0 && slotIndex < this.slots.size()) {
/* 333 */       ItemStack itemStack = ((Slot)this.slots.get(slotIndex)).getItem();
/* 334 */       BundleItem.toggleSelectedItem(itemStack, selectedItemIndex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clicked(int slotIndex, int buttonNum, ClickType clickType, Player player) {
/*     */     try {
/* 340 */       doClick(slotIndex, buttonNum, clickType, player);
/* 341 */     } catch (Exception e) {
/* 342 */       CrashReport report = CrashReport.forThrowable(e, "Container click");
/* 343 */       CrashReportCategory category = report.addCategory("Click info");
/* 344 */       category.setDetail("Menu Type", () -> (this.menuType != null) ? BuiltInRegistries.MENU.getKey(this.menuType).toString() : "<no type>");
/* 345 */       category.setDetail("Menu Class", () -> getClass().getCanonicalName());
/* 346 */       category.setDetail("Slot Count", Integer.valueOf(this.slots.size()));
/* 347 */       category.setDetail("Slot", Integer.valueOf(slotIndex));
/* 348 */       category.setDetail("Button", Integer.valueOf(buttonNum));
/* 349 */       category.setDetail("Type", clickType);
/* 350 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doClick(int slotIndex, int buttonNum, ClickType clickType, Player player) {
/* 356 */     Inventory inventory = player.getInventory();
/*     */     
/* 358 */     if (clickType == ClickType.QUICK_CRAFT) {
/* 359 */       int expectedStatus = this.quickcraftStatus;
/* 360 */       this.quickcraftStatus = getQuickcraftHeader(buttonNum);
/*     */       
/* 362 */       if ((expectedStatus != 1 || this.quickcraftStatus != 2) && expectedStatus != this.quickcraftStatus) {
/* 363 */         resetQuickCraft();
/* 364 */       } else if (getCarried().isEmpty()) {
/* 365 */         resetQuickCraft();
/* 366 */       } else if (this.quickcraftStatus == 0) {
/* 367 */         this.quickcraftType = getQuickcraftType(buttonNum);
/*     */         
/* 369 */         if (isValidQuickcraftType(this.quickcraftType, player)) {
/* 370 */           this.quickcraftStatus = 1;
/* 371 */           this.quickcraftSlots.clear();
/*     */         } else {
/* 373 */           resetQuickCraft();
/*     */         } 
/* 375 */       } else if (this.quickcraftStatus == 1) {
/* 376 */         Slot slot = (Slot)this.slots.get(slotIndex);
/*     */         
/* 378 */         ItemStack carriedItemStack = getCarried();
/* 379 */         if (canItemQuickReplace(slot, carriedItemStack, true) && slot.mayPlace(carriedItemStack) && (this.quickcraftType == 2 || carriedItemStack.getCount() > this.quickcraftSlots.size()) && canDragTo(slot)) {
/* 380 */           this.quickcraftSlots.add(slot);
/*     */         }
/* 382 */       } else if (this.quickcraftStatus == 2) {
/* 383 */         if (!this.quickcraftSlots.isEmpty()) {
/* 384 */           if (this.quickcraftSlots.size() == 1) {
/*     */             
/* 386 */             int slot = ((Slot)this.quickcraftSlots.iterator().next()).index;
/* 387 */             resetQuickCraft();
/* 388 */             doClick(slot, this.quickcraftType, ClickType.PICKUP, player);
/*     */             return;
/*     */           } 
/* 391 */           ItemStack source = getCarried().copy();
/* 392 */           if (source.isEmpty()) {
/* 393 */             resetQuickCraft();
/*     */             return;
/*     */           } 
/* 396 */           int remaining = getCarried().getCount();
/*     */           
/* 398 */           for (Slot slot : this.quickcraftSlots) {
/* 399 */             ItemStack carriedItemStack = getCarried();
/* 400 */             if (slot != null && canItemQuickReplace(slot, carriedItemStack, true) && slot.mayPlace(carriedItemStack) && (this.quickcraftType == 2 || carriedItemStack.getCount() >= this.quickcraftSlots.size()) && canDragTo(slot)) {
/* 401 */               int carry = slot.hasItem() ? slot.getItem().getCount() : 0;
/* 402 */               int maxSize = Math.min(source.getMaxStackSize(), slot.getMaxStackSize(source));
/* 403 */               int newCount = Math.min(getQuickCraftPlaceCount(this.quickcraftSlots, this.quickcraftType, source) + carry, maxSize);
/*     */               
/* 405 */               remaining -= newCount - carry;
/* 406 */               slot.setByPlayer(source.copyWithCount(newCount));
/*     */             } 
/*     */           } 
/*     */           
/* 410 */           source.setCount(remaining);
/* 411 */           setCarried(source);
/*     */         } 
/*     */         
/* 414 */         resetQuickCraft();
/*     */       } else {
/* 416 */         resetQuickCraft();
/*     */       } 
/* 418 */     } else if (this.quickcraftStatus != 0) {
/* 419 */       resetQuickCraft();
/* 420 */     } else if ((clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) && (buttonNum == 0 || buttonNum == 1)) {
/* 421 */       ClickAction clickAction = (buttonNum == 0) ? ClickAction.PRIMARY : ClickAction.SECONDARY;
/* 422 */       if (slotIndex == -999) {
/* 423 */         if (!getCarried().isEmpty()) {
/* 424 */           if (clickAction == ClickAction.PRIMARY) {
/* 425 */             player.drop(getCarried(), true);
/* 426 */             setCarried(ItemStack.EMPTY);
/*     */           } else {
/* 428 */             player.drop(getCarried().split(1), true);
/*     */           } 
/*     */         }
/* 431 */       } else if (clickType == ClickType.QUICK_MOVE) {
/* 432 */         if (slotIndex < 0) {
/*     */           return;
/*     */         }
/* 435 */         Slot slot = (Slot)this.slots.get(slotIndex);
/* 436 */         if (!slot.mayPickup(player)) {
/*     */           return;
/*     */         }
/*     */         
/* 440 */         ItemStack clicked = quickMoveStack(player, slotIndex);
/* 441 */         while (!clicked.isEmpty() && ItemStack.isSameItem(slot.getItem(), clicked)) {
/* 442 */           clicked = quickMoveStack(player, slotIndex);
/*     */         }
/*     */       } else {
/* 445 */         if (slotIndex < 0) {
/*     */           return;
/*     */         }
/*     */         
/* 449 */         Slot slot = (Slot)this.slots.get(slotIndex);
/* 450 */         ItemStack clicked = slot.getItem();
/* 451 */         ItemStack carried = getCarried();
/*     */         
/* 453 */         player.updateTutorialInventoryAction(carried, slot.getItem(), clickAction);
/* 454 */         if (!tryItemClickBehaviourOverride(player, clickAction, slot, clicked, carried)) {
/* 455 */           if (clicked.isEmpty()) {
/* 456 */             if (!carried.isEmpty()) {
/* 457 */               int amount = (clickAction == ClickAction.PRIMARY) ? carried.getCount() : 1;
/* 458 */               setCarried(slot.safeInsert(carried, amount));
/*     */             } 
/* 460 */           } else if (slot.mayPickup(player)) {
/*     */             
/* 462 */             if (carried.isEmpty()) {
/* 463 */               int amount = (clickAction == ClickAction.PRIMARY) ? clicked.getCount() : ((clicked.getCount() + 1) / 2);
/* 464 */               Optional<ItemStack> newCarried = slot.tryRemove(amount, 2147483647, player);
/* 465 */               newCarried.ifPresent(itemsTaken -> {
/* 466 */                     setCarried(itemsTaken);
/* 467 */                     slot.onTake(player, itemsTaken);
/*     */                   });
/* 469 */             } else if (slot.mayPlace(carried)) {
/*     */               
/* 471 */               if (ItemStack.isSameItemSameComponents(clicked, carried)) {
/*     */                 
/* 473 */                 int amount = (clickAction == ClickAction.PRIMARY) ? carried.getCount() : 1;
/* 474 */                 setCarried(slot.safeInsert(carried, amount));
/*     */               
/*     */               }
/* 477 */               else if (carried.getCount() <= slot.getMaxStackSize(carried)) {
/* 478 */                 setCarried(clicked);
/* 479 */                 slot.setByPlayer(carried);
/*     */               }
/*     */             
/* 482 */             } else if (ItemStack.isSameItemSameComponents(clicked, carried)) {
/* 483 */               Optional<ItemStack> newCarried = slot.tryRemove(clicked.getCount(), carried.getMaxStackSize() - carried.getCount(), player);
/* 484 */               newCarried.ifPresent(itemsTaken -> {
/* 485 */                     carried.grow(itemsTaken.getCount());
/* 486 */                     slot.onTake(player, itemsTaken);
/*     */                   });
/*     */             } 
/*     */           } 
/*     */         }
/* 491 */         slot.setChanged();
/*     */       } 
/* 493 */     } else if (clickType == ClickType.SWAP && ((buttonNum >= 0 && buttonNum < 9) || buttonNum == 40)) {
/*     */ 
/*     */       
/* 496 */       ItemStack source = inventory.getItem(buttonNum);
/* 497 */       Slot target = (Slot)this.slots.get(slotIndex);
/*     */       
/* 499 */       ItemStack targetItemStack = target.getItem();
/* 500 */       if (!source.isEmpty() || !targetItemStack.isEmpty())
/*     */       {
/* 502 */         if (source.isEmpty()) {
/*     */           
/* 504 */           if (target.mayPickup(player)) {
/* 505 */             inventory.setItem(buttonNum, targetItemStack);
/* 506 */             target.onSwapCraft(targetItemStack.getCount());
/* 507 */             target.setByPlayer(ItemStack.EMPTY);
/* 508 */             target.onTake(player, targetItemStack);
/*     */           } 
/* 510 */         } else if (targetItemStack.isEmpty()) {
/* 511 */           if (target.mayPlace(source)) {
/* 512 */             int maxStackSize = target.getMaxStackSize(source);
/* 513 */             if (source.getCount() > maxStackSize) {
/*     */               
/* 515 */               target.setByPlayer(source.split(maxStackSize));
/*     */             } else {
/*     */               
/* 518 */               inventory.setItem(buttonNum, ItemStack.EMPTY);
/* 519 */               target.setByPlayer(source);
/*     */             } 
/*     */           } 
/* 522 */         } else if (target.mayPickup(player) && target.mayPlace(source)) {
/*     */ 
/*     */           
/* 525 */           int maxStackSize = target.getMaxStackSize(source);
/* 526 */           if (source.getCount() > maxStackSize) {
/* 527 */             target.setByPlayer(source.split(maxStackSize));
/* 528 */             target.onTake(player, targetItemStack);
/* 529 */             if (!inventory.add(targetItemStack)) {
/* 530 */               player.drop(targetItemStack, true);
/*     */             }
/*     */           } else {
/* 533 */             inventory.setItem(buttonNum, targetItemStack);
/* 534 */             target.setByPlayer(source);
/* 535 */             target.onTake(player, targetItemStack);
/*     */           } 
/*     */         }  } 
/* 538 */     } else if (clickType == ClickType.CLONE && player.hasInfiniteMaterials() && getCarried().isEmpty() && slotIndex >= 0) {
/* 539 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 540 */       if (slot.hasItem()) {
/* 541 */         ItemStack item = slot.getItem();
/* 542 */         setCarried(item.copyWithCount(item.getMaxStackSize()));
/*     */       } 
/* 544 */     } else if (clickType == ClickType.THROW && getCarried().isEmpty() && slotIndex >= 0) {
/* 545 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 546 */       int amount = (buttonNum == 0) ? 1 : slot.getItem().getCount();
/* 547 */       if (!player.canDropItems()) {
/*     */         return;
/*     */       }
/* 550 */       ItemStack itemStack = slot.safeTake(amount, 2147483647, player);
/* 551 */       player.drop(itemStack, true);
/* 552 */       player.handleCreativeModeItemDrop(itemStack);
/* 553 */       if (buttonNum == 1) {
/* 554 */         while (!itemStack.isEmpty() && ItemStack.isSameItem(slot.getItem(), itemStack)) {
/* 555 */           if (!player.canDropItems()) {
/*     */             return;
/*     */           }
/* 558 */           itemStack = slot.safeTake(amount, 2147483647, player);
/* 559 */           player.drop(itemStack, true);
/* 560 */           player.handleCreativeModeItemDrop(itemStack);
/*     */         } 
/*     */       }
/* 563 */     } else if (clickType == ClickType.PICKUP_ALL && slotIndex >= 0) {
/* 564 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 565 */       ItemStack carried = getCarried();
/*     */       
/* 567 */       if (!carried.isEmpty() && (!slot.hasItem() || !slot.mayPickup(player))) {
/* 568 */         int start = (buttonNum == 0) ? 0 : (this.slots.size() - 1);
/* 569 */         int step = (buttonNum == 0) ? 1 : -1;
/*     */         
/* 571 */         for (int pass = 0; pass < 2; pass++) {
/*     */           int i;
/* 573 */           for (i = start; i >= 0 && i < this.slots.size() && carried.getCount() < carried.getMaxStackSize(); i += step) {
/* 574 */             Slot target = (Slot)this.slots.get(i);
/*     */             
/* 576 */             if (target.hasItem() && canItemQuickReplace(target, carried, true) && target.mayPickup(player) && canTakeItemForPickAll(carried, target)) {
/* 577 */               ItemStack itemStack = target.getItem();
/* 578 */               if (pass != 0 || itemStack.getCount() != itemStack.getMaxStackSize()) {
/*     */ 
/*     */                 
/* 581 */                 ItemStack removed = target.safeTake(itemStack.getCount(), carried.getMaxStackSize() - carried.getCount(), player);
/* 582 */                 carried.grow(removed.getCount());
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean tryItemClickBehaviourOverride(Player player, ClickAction clickAction, Slot slot, ItemStack clicked, ItemStack carried) {
/* 591 */     FeatureFlagSet enabledFeatures = player.level().enabledFeatures();
/* 592 */     if (carried.isItemEnabled(enabledFeatures) && carried.overrideStackedOnOther(slot, clickAction, player)) {
/* 593 */       return true;
/*     */     }
/* 595 */     return (clicked.isItemEnabled(enabledFeatures) && clicked.overrideOtherStackedOnMe(carried, slot, clickAction, player, createCarriedSlotAccess()));
/*     */   }
/*     */   
/*     */   private SlotAccess createCarriedSlotAccess() {
/* 599 */     return new SlotAccess()
/*     */       {
/*     */         public ItemStack get() {
/* 602 */           return AbstractContainerMenu.this.getCarried();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean set(ItemStack itemStack) {
/* 607 */           AbstractContainerMenu.this.setCarried(itemStack);
/* 608 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 614 */   public boolean canTakeItemForPickAll(ItemStack carried, Slot target) { return true; }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 618 */     if (!(player instanceof net.minecraft.server.level.ServerPlayer)) {
/*     */       return;
/*     */     }
/* 621 */     ItemStack carried = getCarried();
/* 622 */     if (!carried.isEmpty()) {
/* 623 */       dropOrPlaceInInventory(player, carried);
/* 624 */       setCarried(ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void dropOrPlaceInInventory(Player player, ItemStack carried) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual isRemoved : ()Z
/*     */     //   4: ifeq -> 21
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual getRemovalReason : ()Lnet/minecraft/world/entity/Entity$RemovalReason;
/*     */     //   11: getstatic net/minecraft/world/entity/Entity$RemovalReason.CHANGED_DIMENSION : Lnet/minecraft/world/entity/Entity$RemovalReason;
/*     */     //   14: if_acmpeq -> 21
/*     */     //   17: iconst_1
/*     */     //   18: goto -> 22
/*     */     //   21: iconst_0
/*     */     //   22: istore_2
/*     */     //   23: aload_0
/*     */     //   24: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   27: ifeq -> 48
/*     */     //   30: aload_0
/*     */     //   31: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   34: astore #4
/*     */     //   36: aload #4
/*     */     //   38: invokevirtual hasDisconnected : ()Z
/*     */     //   41: ifeq -> 48
/*     */     //   44: iconst_1
/*     */     //   45: goto -> 49
/*     */     //   48: iconst_0
/*     */     //   49: istore_3
/*     */     //   50: iload_2
/*     */     //   51: ifne -> 58
/*     */     //   54: iload_3
/*     */     //   55: ifeq -> 68
/*     */     //   58: aload_0
/*     */     //   59: aload_1
/*     */     //   60: iconst_0
/*     */     //   61: invokevirtual drop : (Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;
/*     */     //   64: pop
/*     */     //   65: goto -> 83
/*     */     //   68: aload_0
/*     */     //   69: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   72: ifeq -> 83
/*     */     //   75: aload_0
/*     */     //   76: invokevirtual getInventory : ()Lnet/minecraft/world/entity/player/Inventory;
/*     */     //   79: aload_1
/*     */     //   80: invokevirtual placeItemBackInInventory : (Lnet/minecraft/world/item/ItemStack;)V
/*     */     //   83: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #629	-> 0
/*     */     //   #630	-> 23
/*     */     //   #631	-> 50
/*     */     //   #632	-> 58
/*     */     //   #633	-> 68
/*     */     //   #634	-> 75
/*     */     //   #636	-> 83
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   36	12	4	serverPlayer	Lnet/minecraft/server/level/ServerPlayer;
/*     */     //   0	84	0	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   0	84	1	carried	Lnet/minecraft/world/item/ItemStack;
/*     */     //   23	61	2	playerRemovedNotChangingDimension	Z
/*     */     //   50	34	3	serverPlayerHasDisconnected	Z }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearContainer(Player player, Container container) {
/* 639 */     for (int i = 0; i < container.getContainerSize(); i++) {
/* 640 */       dropOrPlaceInInventory(player, container.removeItemNoUpdate(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 645 */   public void slotsChanged(Container container) { broadcastChanges(); }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, int stateId, ItemStack itemStack) {
/* 649 */     getSlot(slot).set(itemStack);
/* 650 */     this.stateId = stateId;
/*     */   }
/*     */   
/*     */   public void initializeContents(int stateId, List<ItemStack> items, ItemStack carried) {
/* 654 */     for (int i = 0; i < items.size(); i++) {
/* 655 */       getSlot(i).set((ItemStack)items.get(i));
/*     */     }
/* 657 */     this.carried = carried;
/* 658 */     this.stateId = stateId;
/*     */   }
/*     */ 
/*     */   
/* 662 */   public void setData(int id, int value) { ((DataSlot)this.dataSlots.get(id)).set(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean moveItemStackTo(ItemStack itemStack, int startSlot, int endSlot, boolean backwards) {
/* 668 */     boolean anythingChanged = false;
/*     */     
/* 670 */     int destSlot = startSlot;
/* 671 */     if (backwards) {
/* 672 */       destSlot = endSlot - 1;
/*     */     }
/*     */ 
/*     */     
/* 676 */     if (itemStack.isStackable()) {
/* 677 */       while (!itemStack.isEmpty() && (backwards ? (destSlot >= startSlot) : (destSlot < endSlot))) {
/* 678 */         Slot slot = (Slot)this.slots.get(destSlot);
/* 679 */         ItemStack target = slot.getItem();
/* 680 */         if (!target.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, target)) {
/* 681 */           int totalStack = target.getCount() + itemStack.getCount();
/* 682 */           int maxStackSize = slot.getMaxStackSize(target);
/* 683 */           if (totalStack <= maxStackSize) {
/* 684 */             itemStack.setCount(0);
/* 685 */             target.setCount(totalStack);
/* 686 */             slot.setChanged();
/* 687 */             anythingChanged = true;
/* 688 */           } else if (target.getCount() < maxStackSize) {
/* 689 */             itemStack.shrink(maxStackSize - target.getCount());
/* 690 */             target.setCount(maxStackSize);
/* 691 */             slot.setChanged();
/* 692 */             anythingChanged = true;
/*     */           } 
/*     */         } 
/*     */         
/* 696 */         if (backwards) {
/* 697 */           destSlot--; continue;
/*     */         } 
/* 699 */         destSlot++;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 705 */     if (!itemStack.isEmpty()) {
/* 706 */       if (backwards) {
/* 707 */         destSlot = endSlot - 1;
/*     */       } else {
/* 709 */         destSlot = startSlot;
/*     */       } 
/* 711 */       while (backwards ? (destSlot >= startSlot) : (destSlot < endSlot)) {
/* 712 */         Slot slot = (Slot)this.slots.get(destSlot);
/* 713 */         ItemStack target = slot.getItem();
/*     */         
/* 715 */         if (target.isEmpty() && slot.mayPlace(itemStack)) {
/* 716 */           int maxStackSize = slot.getMaxStackSize(itemStack);
/* 717 */           slot.setByPlayer(itemStack.split(Math.min(itemStack.getCount(), maxStackSize)));
/* 718 */           slot.setChanged();
/* 719 */           anythingChanged = true;
/*     */           
/*     */           break;
/*     */         } 
/* 723 */         if (backwards) {
/* 724 */           destSlot--; continue;
/*     */         } 
/* 726 */         destSlot++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 731 */     return anythingChanged;
/*     */   }
/*     */ 
/*     */   
/* 735 */   public static int getQuickcraftType(int mask) { return mask >> 2 & 0x3; }
/*     */ 
/*     */ 
/*     */   
/* 739 */   public static int getQuickcraftHeader(int mask) { return mask & 0x3; }
/*     */ 
/*     */ 
/*     */   
/* 743 */   public static int getQuickcraftMask(int header, int type) { return header & 0x3 | (type & 0x3) << 2; }
/*     */ 
/*     */   
/*     */   public static boolean isValidQuickcraftType(int type, Player player) {
/* 747 */     if (type == 0) {
/* 748 */       return true;
/*     */     }
/* 750 */     if (type == 1) {
/* 751 */       return true;
/*     */     }
/* 753 */     if (type == 2 && player.hasInfiniteMaterials()) {
/* 754 */       return true;
/*     */     }
/* 756 */     return false;
/*     */   }
/*     */   
/*     */   protected void resetQuickCraft() {
/* 760 */     this.quickcraftStatus = 0;
/* 761 */     this.quickcraftSlots.clear();
/*     */   }
/*     */   
/*     */   public static boolean canItemQuickReplace(Slot slot, ItemStack itemStack, boolean ignoreSize) {
/* 765 */     boolean slotIsEmpty = (slot == null || !slot.hasItem());
/*     */     
/* 767 */     if (!slotIsEmpty && ItemStack.isSameItemSameComponents(itemStack, slot.getItem())) {
/* 768 */       return (slot.getItem().getCount() + (ignoreSize ? 0 : itemStack.getCount()) <= itemStack.getMaxStackSize());
/*     */     }
/*     */     
/* 771 */     return slotIsEmpty;
/*     */   }
/*     */   
/*     */   public static int getQuickCraftPlaceCount(Set<Slot> quickCraftSlots, int quickCraftingType, ItemStack itemStack) {
/* 775 */     switch (quickCraftingType) { case 0: case 1: case 2:  }  return 
/*     */ 
/*     */ 
/*     */       
/* 779 */       itemStack.getCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 784 */   public boolean canDragTo(Slot slot) { return true; }
/*     */ 
/*     */   
/*     */   public static int getRedstoneSignalFromBlockEntity(BlockEntity blockEntity) {
/* 788 */     if (blockEntity instanceof Container) {
/* 789 */       return getRedstoneSignalFromContainer((Container)blockEntity);
/*     */     }
/*     */     
/* 792 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getRedstoneSignalFromContainer(Container container) {
/* 796 */     if (container == null) {
/* 797 */       return 0;
/*     */     }
/* 799 */     float totalPercent = 0.0F;
/* 800 */     for (int i = 0; i < container.getContainerSize(); i++) {
/* 801 */       ItemStack itemStack = container.getItem(i);
/* 802 */       if (!itemStack.isEmpty()) {
/* 803 */         totalPercent += itemStack.getCount() / container.getMaxStackSize(itemStack);
/*     */       }
/*     */     } 
/*     */     
/* 807 */     totalPercent /= container.getContainerSize();
/* 808 */     return Mth.lerpDiscrete(totalPercent, 0, 15);
/*     */   }
/*     */ 
/*     */   
/* 812 */   public void setCarried(ItemStack carried) { this.carried = carried; }
/*     */ 
/*     */ 
/*     */   
/* 816 */   public ItemStack getCarried() { return this.carried; }
/*     */ 
/*     */ 
/*     */   
/* 820 */   public void suppressRemoteUpdates() { this.suppressRemoteUpdates = true; }
/*     */ 
/*     */ 
/*     */   
/* 824 */   public void resumeRemoteUpdates() { this.suppressRemoteUpdates = false; }
/*     */ 
/*     */   
/*     */   public void transferState(AbstractContainerMenu otherContainer) {
/* 828 */     HashBasedTable hashBasedTable = HashBasedTable.create();
/* 829 */     for (int slotIndex = 0; slotIndex < otherContainer.slots.size(); slotIndex++) {
/* 830 */       Slot slot = (Slot)otherContainer.slots.get(slotIndex);
/* 831 */       hashBasedTable.put(slot.container, Integer.valueOf(slot.getContainerSlot()), Integer.valueOf(slotIndex));
/*     */     } 
/*     */     
/* 834 */     for (int slotIndex = 0; slotIndex < this.slots.size(); slotIndex++) {
/* 835 */       Slot slot = (Slot)this.slots.get(slotIndex);
/* 836 */       Integer otherSlotIndex = (Integer)hashBasedTable.get(slot.container, Integer.valueOf(slot.getContainerSlot()));
/* 837 */       if (otherSlotIndex != null) {
/* 838 */         this.lastSlots.set(slotIndex, (ItemStack)otherContainer.lastSlots.get(otherSlotIndex.intValue()));
/* 839 */         RemoteSlot sourceRemoteSlot = (RemoteSlot)otherContainer.remoteSlots.get(otherSlotIndex.intValue());
/* 840 */         RemoteSlot targetRemoteSlot = (RemoteSlot)this.remoteSlots.get(slotIndex);
/* 841 */         if (sourceRemoteSlot instanceof RemoteSlot.Synchronized) { RemoteSlot.Synchronized synchronizedSource = (RemoteSlot.Synchronized)sourceRemoteSlot; if (targetRemoteSlot instanceof RemoteSlot.Synchronized) { RemoteSlot.Synchronized synchronizedTarget = (RemoteSlot.Synchronized)targetRemoteSlot;
/* 842 */             synchronizedTarget.copyFrom(synchronizedSource); }
/*     */            }
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public OptionalInt findSlot(Container inventory, int slotIndex) {
/* 849 */     for (int i = 0; i < this.slots.size(); i++) {
/* 850 */       Slot slot = (Slot)this.slots.get(i);
/* 851 */       if (slot.container == inventory && slotIndex == slot.getContainerSlot()) {
/* 852 */         return OptionalInt.of(i);
/*     */       }
/*     */     } 
/*     */     
/* 856 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/* 860 */   public int getStateId() { return this.stateId; }
/*     */ 
/*     */ 
/*     */   
/*     */   public int incrementStateId() {
/* 865 */     this.stateId = this.stateId + 1 & 0x7FFF;
/* 866 */     return this.stateId;
/*     */   }
/*     */   
/*     */   public abstract ItemStack quickMoveStack(Player paramPlayer, int paramInt);
/*     */   
/*     */   public abstract boolean stillValid(Player paramPlayer);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\AbstractContainerMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */