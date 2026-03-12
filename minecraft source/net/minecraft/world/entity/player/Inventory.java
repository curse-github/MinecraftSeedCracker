/*     */ package net.minecraft.world.entity.player;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Map;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.ItemStackWithSlot;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.entity.EntityEquipment;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public class Inventory
/*     */   implements Container, Nameable
/*     */ {
/*     */   public static final int POP_TIME_DURATION = 5;
/*     */   public static final int INVENTORY_SIZE = 36;
/*     */   public static final int SELECTION_SIZE = 9;
/*     */   public static final int SLOT_OFFHAND = 40;
/*     */   public static final int SLOT_BODY_ARMOR = 41;
/*     */   public static final int SLOT_SADDLE = 42;
/*     */   public static final int NOT_FOUND_INDEX = -1;
/*  40 */   public static final Int2ObjectMap<EquipmentSlot> EQUIPMENT_SLOT_MAPPING = new Int2ObjectArrayMap(Map.of(
/*  41 */         Integer.valueOf(EquipmentSlot.FEET.getIndex(36)), EquipmentSlot.FEET, 
/*  42 */         Integer.valueOf(EquipmentSlot.LEGS.getIndex(36)), EquipmentSlot.LEGS, 
/*  43 */         Integer.valueOf(EquipmentSlot.CHEST.getIndex(36)), EquipmentSlot.CHEST, 
/*  44 */         Integer.valueOf(EquipmentSlot.HEAD.getIndex(36)), EquipmentSlot.HEAD, 
/*  45 */         Integer.valueOf(40), EquipmentSlot.OFFHAND, 
/*  46 */         Integer.valueOf(41), EquipmentSlot.BODY, 
/*  47 */         Integer.valueOf(42), EquipmentSlot.SADDLE));
/*     */ 
/*     */   
/*  50 */   private static final Component DEFAULT_NAME = Component.translatable("container.inventory"); private final NonNullList<ItemStack> items; private int selected;
/*     */   public Inventory(Player player, EntityEquipment equipment) {
/*  52 */     this.items = NonNullList.withSize(36, ItemStack.EMPTY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     this.player = player;
/*  61 */     this.equipment = equipment;
/*     */   }
/*     */   public final Player player; private final EntityEquipment equipment; private int timesChanged;
/*     */   
/*  65 */   public int getSelectedSlot() { return this.selected; }
/*     */ 
/*     */   
/*     */   public void setSelectedSlot(int selected) {
/*  69 */     if (!isHotbarSlot(selected)) {
/*  70 */       throw new IllegalArgumentException("Invalid selected slot");
/*     */     }
/*  72 */     this.selected = selected;
/*     */   }
/*     */ 
/*     */   
/*  76 */   public ItemStack getSelectedItem() { return (ItemStack)this.items.get(this.selected); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public ItemStack setSelectedItem(ItemStack itemStack) { return (ItemStack)this.items.set(this.selected, itemStack); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static int getSelectionSize() { return 9; }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public NonNullList<ItemStack> getNonEquipmentItems() { return this.items; }
/*     */ 
/*     */   
/*     */   private boolean hasRemainingSpaceForItem(ItemStack slotItemStack, ItemStack newItemStack) {
/*  92 */     return (!slotItemStack.isEmpty() && 
/*  93 */       ItemStack.isSameItemSameComponents(slotItemStack, newItemStack) && slotItemStack
/*  94 */       .isStackable() && slotItemStack
/*  95 */       .getCount() < getMaxStackSize(slotItemStack));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFreeSlot() {
/* 100 */     for (int i = 0; i < this.items.size(); i++) {
/* 101 */       if (((ItemStack)this.items.get(i)).isEmpty()) {
/* 102 */         return i;
/*     */       }
/*     */     } 
/* 105 */     return -1;
/*     */   }
/*     */   
/*     */   public void addAndPickItem(ItemStack itemStack) {
/* 109 */     setSelectedSlot(getSuitableHotbarSlot());
/*     */ 
/*     */     
/* 112 */     if (!((ItemStack)this.items.get(this.selected)).isEmpty()) {
/* 113 */       int freeSlot = getFreeSlot();
/* 114 */       if (freeSlot != -1) {
/* 115 */         this.items.set(freeSlot, (ItemStack)this.items.get(this.selected));
/*     */       }
/*     */     } 
/*     */     
/* 119 */     this.items.set(this.selected, itemStack);
/*     */   }
/*     */   
/*     */   public void pickSlot(int slot) {
/* 123 */     setSelectedSlot(getSuitableHotbarSlot());
/*     */ 
/*     */     
/* 126 */     ItemStack tmp = (ItemStack)this.items.get(this.selected);
/* 127 */     this.items.set(this.selected, (ItemStack)this.items.get(slot));
/* 128 */     this.items.set(slot, tmp);
/*     */   }
/*     */ 
/*     */   
/* 132 */   public static boolean isHotbarSlot(int slot) { return (slot >= 0 && slot < 9); }
/*     */ 
/*     */   
/*     */   public int findSlotMatchingItem(ItemStack itemStack) {
/* 136 */     for (int i = 0; i < this.items.size(); i++) {
/* 137 */       if (!((ItemStack)this.items.get(i)).isEmpty() && ItemStack.isSameItemSameComponents(itemStack, (ItemStack)this.items.get(i))) {
/* 138 */         return i;
/*     */       }
/*     */     } 
/* 141 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean isUsableForCrafting(ItemStack item) {
/* 145 */     return (!item.isDamaged() && 
/* 146 */       !item.isEnchanted() && 
/* 147 */       !item.has(DataComponents.CUSTOM_NAME));
/*     */   }
/*     */   
/*     */   public int findSlotMatchingCraftingIngredient(Holder<Item> item, ItemStack existingItem) {
/* 151 */     for (int i = 0; i < this.items.size(); i++) {
/* 152 */       ItemStack inventoryItemStack = (ItemStack)this.items.get(i);
/* 153 */       if (!inventoryItemStack.isEmpty() && inventoryItemStack
/* 154 */         .is(item) && 
/* 155 */         isUsableForCrafting(inventoryItemStack) && (existingItem
/* 156 */         .isEmpty() || ItemStack.isSameItemSameComponents(existingItem, inventoryItemStack)))
/*     */       {
/* 158 */         return i;
/*     */       }
/*     */     } 
/* 161 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSuitableHotbarSlot() {
/* 166 */     for (int slot = 0; slot < 9; slot++) {
/* 167 */       int index = (this.selected + slot) % 9;
/*     */       
/* 169 */       if (((ItemStack)this.items.get(index)).isEmpty()) {
/* 170 */         return index;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 175 */     for (int slot = 0; slot < 9; slot++) {
/* 176 */       int index = (this.selected + slot) % 9;
/*     */       
/* 178 */       if (!((ItemStack)this.items.get(index)).isEnchanted()) {
/* 179 */         return index;
/*     */       }
/*     */     } 
/*     */     
/* 183 */     return this.selected;
/*     */   }
/*     */   
/*     */   public int clearOrCountMatchingItems(Predicate<ItemStack> predicate, int amountToRemove, Container craftSlots) {
/* 187 */     int count = 0;
/* 188 */     boolean countingOnly = (amountToRemove == 0);
/*     */     
/* 190 */     count += ContainerHelper.clearOrCountMatchingItems(this, predicate, amountToRemove - count, countingOnly);
/* 191 */     count += ContainerHelper.clearOrCountMatchingItems(craftSlots, predicate, amountToRemove - count, countingOnly);
/*     */     
/* 193 */     ItemStack carried = this.player.containerMenu.getCarried();
/* 194 */     count += ContainerHelper.clearOrCountMatchingItems(carried, predicate, amountToRemove - count, countingOnly);
/* 195 */     if (carried.isEmpty()) {
/* 196 */       this.player.containerMenu.setCarried(ItemStack.EMPTY);
/*     */     }
/* 198 */     return count;
/*     */   }
/*     */   
/*     */   private int addResource(ItemStack itemStack) {
/* 202 */     int slot = getSlotWithRemainingSpace(itemStack);
/* 203 */     if (slot == -1) {
/* 204 */       slot = getFreeSlot();
/*     */     }
/* 206 */     if (slot == -1) {
/* 207 */       return itemStack.getCount();
/*     */     }
/* 209 */     return addResource(slot, itemStack);
/*     */   }
/*     */   
/*     */   private int addResource(int slot, ItemStack itemStack) {
/* 213 */     int count = itemStack.getCount();
/*     */     
/* 215 */     ItemStack itemStackInSlot = getItem(slot);
/* 216 */     if (itemStackInSlot.isEmpty()) {
/* 217 */       itemStackInSlot = itemStack.copyWithCount(0);
/* 218 */       setItem(slot, itemStackInSlot);
/*     */     } 
/*     */     
/* 221 */     int maxToAdd = getMaxStackSize(itemStackInSlot) - itemStackInSlot.getCount();
/* 222 */     int toAdd = Math.min(count, maxToAdd);
/* 223 */     if (toAdd == 0) {
/* 224 */       return count;
/*     */     }
/*     */     
/* 227 */     count -= toAdd;
/* 228 */     itemStackInSlot.grow(toAdd);
/* 229 */     itemStackInSlot.setPopTime(5);
/*     */     
/* 231 */     return count;
/*     */   }
/*     */   
/*     */   public int getSlotWithRemainingSpace(ItemStack newItemStack) {
/* 235 */     if (hasRemainingSpaceForItem(getItem(this.selected), newItemStack)) {
/* 236 */       return this.selected;
/*     */     }
/* 238 */     if (hasRemainingSpaceForItem(getItem(40), newItemStack)) {
/* 239 */       return 40;
/*     */     }
/* 241 */     for (int i = 0; i < this.items.size(); i++) {
/* 242 */       if (hasRemainingSpaceForItem((ItemStack)this.items.get(i), newItemStack)) {
/* 243 */         return i;
/*     */       }
/*     */     } 
/* 246 */     return -1;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 250 */     for (int i = 0; i < this.items.size(); i++) {
/* 251 */       ItemStack itemStack = getItem(i);
/* 252 */       if (!itemStack.isEmpty()) {
/* 253 */         itemStack.inventoryTick(this.player.level(), this.player, (i == this.selected) ? EquipmentSlot.MAINHAND : null);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 259 */   public boolean add(ItemStack itemStack) { return add(-1, itemStack); }
/*     */ 
/*     */   
/*     */   public boolean add(int slot, ItemStack itemStack) {
/* 263 */     if (itemStack.isEmpty()) {
/* 264 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 268 */       if (!itemStack.isDamaged()) {
/*     */         int lastSize;
/*     */         do {
/* 271 */           lastSize = itemStack.getCount();
/* 272 */           if (slot == -1) {
/* 273 */             itemStack.setCount(addResource(itemStack));
/*     */           } else {
/* 275 */             itemStack.setCount(addResource(slot, itemStack));
/*     */           } 
/* 277 */         } while (!itemStack.isEmpty() && itemStack.getCount() < lastSize);
/* 278 */         if (itemStack.getCount() == lastSize && this.player.hasInfiniteMaterials()) {
/*     */           
/* 280 */           itemStack.setCount(0);
/* 281 */           return true;
/*     */         } 
/* 283 */         return (itemStack.getCount() < lastSize);
/*     */       } 
/*     */       
/* 286 */       if (slot == -1) {
/* 287 */         slot = getFreeSlot();
/*     */       }
/* 289 */       if (slot >= 0) {
/* 290 */         this.items.set(slot, itemStack.copyAndClear());
/* 291 */         ((ItemStack)this.items.get(slot)).setPopTime(5);
/* 292 */         return true;
/* 293 */       }  if (this.player.hasInfiniteMaterials()) {
/*     */         
/* 295 */         itemStack.setCount(0);
/* 296 */         return true;
/*     */       } 
/* 298 */       return false;
/* 299 */     } catch (Throwable t) {
/* 300 */       CrashReport report = CrashReport.forThrowable(t, "Adding item to inventory");
/* 301 */       CrashReportCategory category = report.addCategory("Item being added");
/*     */       
/* 303 */       category.setDetail("Item ID", Integer.valueOf(Item.getId(itemStack.getItem())));
/* 304 */       category.setDetail("Item data", Integer.valueOf(itemStack.getDamageValue()));
/* 305 */       category.setDetail("Item name", () -> itemStack.getHoverName().getString());
/*     */       
/* 307 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 312 */   public void placeItemBackInInventory(ItemStack itemStack) { placeItemBackInInventory(itemStack, true); }
/*     */ 
/*     */   
/*     */   public void placeItemBackInInventory(ItemStack itemStack, boolean shouldSendSetSlotPacket) {
/* 316 */     while (!itemStack.isEmpty()) {
/* 317 */       int slot = getSlotWithRemainingSpace(itemStack);
/* 318 */       if (slot == -1) {
/* 319 */         slot = getFreeSlot();
/*     */       }
/*     */       
/* 322 */       if (slot == -1) {
/* 323 */         this.player.drop(itemStack, false);
/*     */         
/*     */         break;
/*     */       } 
/* 327 */       int slotHasSpaceFor = itemStack.getMaxStackSize() - getItem(slot).getCount();
/*     */       
/* 329 */       if (add(slot, itemStack.split(slotHasSpaceFor)) && shouldSendSetSlotPacket) { Player player1 = this.player; if (player1 instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player1;
/* 330 */           serverPlayer.connection.send(createInventoryUpdatePacket(slot)); }
/*     */          }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/* 336 */   public ClientboundSetPlayerInventoryPacket createInventoryUpdatePacket(int slot) { return new ClientboundSetPlayerInventoryPacket(slot, getItem(slot).copy()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/* 341 */     if (slot < this.items.size()) {
/* 342 */       return ContainerHelper.removeItem(this.items, slot, count);
/*     */     }
/* 344 */     EquipmentSlot equipmentSlot = (EquipmentSlot)EQUIPMENT_SLOT_MAPPING.get(slot);
/* 345 */     if (equipmentSlot != null) {
/* 346 */       ItemStack itemStack = this.equipment.get(equipmentSlot);
/* 347 */       if (!itemStack.isEmpty()) {
/* 348 */         return itemStack.split(count);
/*     */       }
/*     */     } 
/* 351 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeItem(ItemStack itemStack) {
/* 358 */     for (int slot = 0; slot < this.items.size(); slot++) {
/* 359 */       if (this.items.get(slot) == itemStack) {
/* 360 */         this.items.set(slot, ItemStack.EMPTY);
/*     */         return;
/*     */       } 
/*     */     } 
/* 364 */     for (ObjectIterator objectIterator = EQUIPMENT_SLOT_MAPPING.values().iterator(); objectIterator.hasNext(); ) { EquipmentSlot equipmentSlot = (EquipmentSlot)objectIterator.next();
/* 365 */       ItemStack stackInSlot = this.equipment.get(equipmentSlot);
/* 366 */       if (stackInSlot == itemStack) {
/* 367 */         this.equipment.set(equipmentSlot, ItemStack.EMPTY);
/*     */         return;
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/* 375 */     if (slot < this.items.size()) {
/* 376 */       ItemStack itemStack = (ItemStack)this.items.get(slot);
/* 377 */       this.items.set(slot, ItemStack.EMPTY);
/* 378 */       return itemStack;
/*     */     } 
/* 380 */     EquipmentSlot equipmentSlot = (EquipmentSlot)EQUIPMENT_SLOT_MAPPING.get(slot);
/* 381 */     if (equipmentSlot != null) {
/* 382 */       return this.equipment.set(equipmentSlot, ItemStack.EMPTY);
/*     */     }
/* 384 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 389 */     if (slot < this.items.size()) {
/* 390 */       this.items.set(slot, itemStack);
/*     */     }
/* 392 */     EquipmentSlot equipmentSlot = (EquipmentSlot)EQUIPMENT_SLOT_MAPPING.get(slot);
/* 393 */     if (equipmentSlot != null) {
/* 394 */       this.equipment.set(equipmentSlot, itemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void save(ValueOutput.TypedOutputList<ItemStackWithSlot> output) {
/* 399 */     for (int i = 0; i < this.items.size(); i++) {
/* 400 */       ItemStack item = (ItemStack)this.items.get(i);
/* 401 */       if (!item.isEmpty()) {
/* 402 */         output.add(new ItemStackWithSlot(i, item));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void load(ValueInput.TypedInputList<ItemStackWithSlot> input) {
/* 408 */     this.items.clear();
/* 409 */     for (ItemStackWithSlot item : input) {
/* 410 */       if (item.isValidInContainer(this.items.size())) {
/* 411 */         setItem(item.slot(), item.stack());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 418 */   public int getContainerSize() { return this.items.size() + EQUIPMENT_SLOT_MAPPING.size(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 423 */     for (ItemStack itemStack : this.items) {
/* 424 */       if (!itemStack.isEmpty()) {
/* 425 */         return false;
/*     */       }
/*     */     } 
/* 428 */     for (ObjectIterator objectIterator = EQUIPMENT_SLOT_MAPPING.values().iterator(); objectIterator.hasNext(); ) { EquipmentSlot slot = (EquipmentSlot)objectIterator.next();
/* 429 */       if (!this.equipment.get(slot).isEmpty()) {
/* 430 */         return false;
/*     */       } }
/*     */     
/* 433 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/* 438 */     if (slot < this.items.size()) {
/* 439 */       return (ItemStack)this.items.get(slot);
/*     */     }
/* 441 */     EquipmentSlot equipmentSlot = (EquipmentSlot)EQUIPMENT_SLOT_MAPPING.get(slot);
/* 442 */     if (equipmentSlot != null) {
/* 443 */       return this.equipment.get(equipmentSlot);
/*     */     }
/* 445 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 450 */   public Component getName() { return DEFAULT_NAME; }
/*     */ 
/*     */   
/*     */   public void dropAll() {
/* 454 */     for (int i = 0; i < this.items.size(); i++) {
/* 455 */       ItemStack itemStack = (ItemStack)this.items.get(i);
/* 456 */       if (!itemStack.isEmpty()) {
/* 457 */         this.player.drop(itemStack, true, false);
/* 458 */         this.items.set(i, ItemStack.EMPTY);
/*     */       } 
/*     */     } 
/* 461 */     this.equipment.dropAll(this.player);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 466 */   public void setChanged() { this.timesChanged++; }
/*     */ 
/*     */ 
/*     */   
/* 470 */   public int getTimesChanged() { return this.timesChanged; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 475 */   public boolean stillValid(Player player) { return true; }
/*     */ 
/*     */   
/*     */   public boolean contains(ItemStack searchStack) {
/* 479 */     for (ItemStack itemStack : this) {
/* 480 */       if (!itemStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, searchStack)) {
/* 481 */         return true;
/*     */       }
/*     */     } 
/* 484 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(TagKey<Item> tag) {
/* 488 */     for (ItemStack itemStack : this) {
/* 489 */       if (!itemStack.isEmpty() && itemStack.is(tag)) {
/* 490 */         return true;
/*     */       }
/*     */     } 
/* 493 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Predicate<ItemStack> predicate) {
/* 497 */     for (ItemStack stack : this) {
/* 498 */       if (predicate.test(stack)) {
/* 499 */         return true;
/*     */       }
/*     */     } 
/* 502 */     return false;
/*     */   }
/*     */   
/*     */   public void replaceWith(Inventory other) {
/* 506 */     for (int i = 0; i < getContainerSize(); i++) {
/* 507 */       setItem(i, other.getItem(i));
/*     */     }
/* 509 */     setSelectedSlot(other.getSelectedSlot());
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 514 */     this.items.clear();
/* 515 */     this.equipment.clear();
/*     */   }
/*     */   
/*     */   public void fillStackedContents(StackedItemContents contents) {
/* 519 */     for (ItemStack itemStack : this.items) {
/* 520 */       contents.accountSimpleStack(itemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   public ItemStack removeFromSelected(boolean all) {
/* 525 */     ItemStack selectedItem = getSelectedItem();
/* 526 */     if (selectedItem.isEmpty()) {
/* 527 */       return ItemStack.EMPTY;
/*     */     }
/* 529 */     return removeItem(this.selected, all ? selectedItem.getCount() : 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\Inventory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */