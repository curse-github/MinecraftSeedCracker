/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.WorldlyContainerHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.HopperMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.HopperBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class HopperBlockEntity
/*     */   extends RandomizableContainerBlockEntity
/*     */   implements Hopper {
/*     */   public static final int MOVE_ITEM_SPEED = 8;
/*     */   public static final int HOPPER_CONTAINER_SIZE = 5;
/*  35 */   private static final int[][] CACHED_SLOTS = new int[54][];
/*     */   private static final int NO_COOLDOWN_TIME = -1;
/*  37 */   private static final Component DEFAULT_NAME = Component.translatable("container.hopper");
/*     */   
/*  39 */   private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
/*  40 */   private int cooldownTime = -1;
/*     */   private long tickedGameTime;
/*     */   private Direction facing;
/*     */   
/*     */   public HopperBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  45 */     super(BlockEntityType.HOPPER, worldPosition, blockState);
/*  46 */     this.facing = (Direction)blockState.getValue(HopperBlock.FACING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  51 */     super.loadAdditional(input);
/*     */     
/*  53 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  54 */     if (!tryLoadLootTable(input)) {
/*  55 */       ContainerHelper.loadAllItems(input, this.items);
/*     */     }
/*  57 */     this.cooldownTime = input.getIntOr("TransferCooldown", -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  62 */     super.saveAdditional(output);
/*     */     
/*  64 */     if (!trySaveLootTable(output)) {
/*  65 */       ContainerHelper.saveAllItems(output, this.items);
/*     */     }
/*     */     
/*  68 */     output.putInt("TransferCooldown", this.cooldownTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public int getContainerSize() { return this.items.size(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  78 */     unpackLootTable(null);
/*     */ 
/*     */     
/*  81 */     return ContainerHelper.removeItem(getItems(), slot, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/*  86 */     unpackLootTable(null);
/*  87 */     getItems().set(slot, itemStack);
/*  88 */     itemStack.limitSize(getMaxStackSize(itemStack));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockState(BlockState blockState) {
/*  94 */     super.setBlockState(blockState);
/*  95 */     this.facing = (Direction)blockState.getValue(HopperBlock.FACING);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*     */ 
/*     */   
/*     */   public static void pushItemsTick(Level level, BlockPos pos, BlockState state, HopperBlockEntity entity) {
/* 104 */     entity.cooldownTime--;
/* 105 */     entity.tickedGameTime = level.getGameTime();
/*     */     
/* 107 */     if (!entity.isOnCooldown()) {
/* 108 */       entity.setCooldown(0);
/* 109 */       tryMoveItems(level, pos, state, entity, () -> suckInItems(level, entity));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean tryMoveItems(Level level, BlockPos pos, BlockState state, HopperBlockEntity entity, BooleanSupplier action) {
/* 114 */     if (level.isClientSide()) {
/* 115 */       return false;
/*     */     }
/*     */     
/* 118 */     if (!entity.isOnCooldown() && ((Boolean)state.getValue(HopperBlock.ENABLED)).booleanValue()) {
/* 119 */       boolean changed = false;
/*     */       
/* 121 */       if (!entity.isEmpty()) {
/* 122 */         changed = ejectItems(level, pos, entity);
/*     */       }
/* 124 */       if (!entity.inventoryFull()) {
/* 125 */         changed |= action.getAsBoolean();
/*     */       }
/*     */       
/* 128 */       if (changed) {
/* 129 */         entity.setCooldown(8);
/* 130 */         setChanged(level, pos, state);
/* 131 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   private boolean inventoryFull() {
/* 139 */     for (ItemStack itemStack : this.items) {
/* 140 */       if (itemStack.isEmpty() || itemStack.getCount() != itemStack.getMaxStackSize()) {
/* 141 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean ejectItems(Level level, BlockPos blockPos, HopperBlockEntity self) {
/* 149 */     Container container = getAttachedContainer(level, blockPos, self);
/* 150 */     if (container == null) {
/* 151 */       return false;
/*     */     }
/*     */     
/* 154 */     Direction direction = self.facing.getOpposite();
/* 155 */     if (isFullContainer(container, direction)) {
/* 156 */       return false;
/*     */     }
/*     */     
/* 159 */     for (int slot = 0; slot < self.getContainerSize(); slot++) {
/* 160 */       ItemStack itemStack = self.getItem(slot);
/* 161 */       if (!itemStack.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/* 165 */         int originalCount = itemStack.getCount();
/* 166 */         ItemStack result = addItem(self, container, self.removeItem(slot, 1), direction);
/*     */         
/* 168 */         if (result.isEmpty()) {
/* 169 */           container.setChanged();
/* 170 */           return true;
/*     */         } 
/* 172 */         itemStack.setCount(originalCount);
/* 173 */         if (originalCount == 1) {
/* 174 */           self.setItem(slot, itemStack);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 179 */     return false;
/*     */   }
/*     */   
/*     */   private static int[] getSlots(Container container, Direction direction) {
/* 183 */     if (container instanceof WorldlyContainer) { WorldlyContainer worldlyContainer = (WorldlyContainer)container;
/* 184 */       return worldlyContainer.getSlotsForFace(direction); }
/*     */     
/* 186 */     int containerSize = container.getContainerSize();
/* 187 */     if (containerSize < CACHED_SLOTS.length) {
/* 188 */       int[] cachedSlots = CACHED_SLOTS[containerSize];
/* 189 */       if (cachedSlots != null) {
/* 190 */         return cachedSlots;
/*     */       }
/* 192 */       int[] slots = createFlatSlots(containerSize);
/* 193 */       CACHED_SLOTS[containerSize] = slots;
/* 194 */       return slots;
/*     */     } 
/* 196 */     return createFlatSlots(containerSize);
/*     */   }
/*     */   
/*     */   private static int[] createFlatSlots(int containerSize) {
/* 200 */     int[] slots = new int[containerSize];
/* 201 */     for (int i = 0; i < slots.length; i++) {
/* 202 */       slots[i] = i;
/*     */     }
/* 204 */     return slots;
/*     */   }
/*     */   
/*     */   private static boolean isFullContainer(Container container, Direction direction) {
/* 208 */     int[] slots = getSlots(container, direction);
/* 209 */     for (int slot : slots) {
/* 210 */       ItemStack itemStack = container.getItem(slot);
/* 211 */       if (itemStack.getCount() < itemStack.getMaxStackSize()) {
/* 212 */         return false;
/*     */       }
/*     */     } 
/* 215 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean suckInItems(Level level, Hopper hopper) {
/* 219 */     BlockPos blockPos = BlockPos.containing(hopper.getLevelX(), hopper.getLevelY() + 1.0D, hopper.getLevelZ());
/* 220 */     BlockState blockState = level.getBlockState(blockPos);
/* 221 */     Container container = getSourceContainer(level, hopper, blockPos, blockState);
/*     */     
/* 223 */     if (container != null) {
/* 224 */       Direction direction = Direction.DOWN;
/* 225 */       for (int slot : getSlots(container, direction)) {
/* 226 */         if (tryTakeInItemFromSlot(hopper, container, slot, direction)) {
/* 227 */           return true;
/*     */         }
/*     */       } 
/* 230 */       return false;
/*     */     } 
/* 232 */     boolean isBlocked = (hopper.isGridAligned() && blockState.isCollisionShapeFullBlock(level, blockPos) && !blockState.is(BlockTags.DOES_NOT_BLOCK_HOPPERS));
/* 233 */     if (!isBlocked) {
/* 234 */       for (ItemEntity entity : getItemsAtAndAbove(level, hopper)) {
/* 235 */         if (addItem(hopper, entity)) {
/* 236 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 242 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean tryTakeInItemFromSlot(Hopper hopper, Container container, int slot, Direction direction) {
/* 246 */     ItemStack itemStack = container.getItem(slot);
/*     */     
/* 248 */     if (!itemStack.isEmpty() && canTakeItemFromContainer(hopper, container, itemStack, slot, direction)) {
/* 249 */       int originalCount = itemStack.getCount();
/* 250 */       ItemStack result = addItem(container, hopper, container.removeItem(slot, 1), null);
/*     */       
/* 252 */       if (result.isEmpty()) {
/* 253 */         container.setChanged();
/* 254 */         return true;
/*     */       } 
/* 256 */       itemStack.setCount(originalCount);
/* 257 */       if (originalCount == 1) {
/* 258 */         container.setItem(slot, itemStack);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 263 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean addItem(Container container, ItemEntity entity) {
/* 267 */     boolean changed = false;
/*     */     
/* 269 */     ItemStack copy = entity.getItem().copy();
/* 270 */     ItemStack result = addItem(null, container, copy, null);
/*     */     
/* 272 */     if (result.isEmpty()) {
/* 273 */       changed = true;
/*     */       
/* 275 */       entity.setItem(ItemStack.EMPTY);
/* 276 */       entity.discard();
/*     */     } else {
/* 278 */       entity.setItem(result);
/*     */     } 
/*     */     
/* 281 */     return changed;
/*     */   }
/*     */   
/*     */   public static ItemStack addItem(Container from, Container container, ItemStack itemStack, Direction direction) {
/* 285 */     if (container instanceof WorldlyContainer) { WorldlyContainer worldly = (WorldlyContainer)container; if (direction != null)
/* 286 */       { int[] slots = worldly.getSlotsForFace(direction);
/*     */         
/* 288 */         for (int i = 0; i < slots.length && !itemStack.isEmpty(); i++) {
/* 289 */           itemStack = tryMoveInItem(from, container, itemStack, slots[i], direction);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 298 */         return itemStack; }  }  int size = container.getContainerSize(); for (int i = 0; i < size && !itemStack.isEmpty(); i++) itemStack = tryMoveInItem(from, container, itemStack, i, direction);  return itemStack;
/*     */   }
/*     */   
/*     */   private static boolean canPlaceItemInContainer(Container container, ItemStack itemStack, int slot, Direction direction) {
/* 302 */     if (!container.canPlaceItem(slot, itemStack)) {
/* 303 */       return false;
/*     */     }
/* 305 */     if (container instanceof WorldlyContainer) { WorldlyContainer worldly = (WorldlyContainer)container; if (worldly.canPlaceItemThroughFace(slot, itemStack, direction)); return false; }
/*     */   
/*     */   }
/*     */   private static boolean canTakeItemFromContainer(Container into, Container from, ItemStack itemStack, int slot, Direction direction) {
/* 309 */     if (!from.canTakeItem(into, slot, itemStack)) {
/* 310 */       return false;
/*     */     }
/* 312 */     if (from instanceof WorldlyContainer) { WorldlyContainer worldly = (WorldlyContainer)from; if (worldly.canTakeItemThroughFace(slot, itemStack, direction)); return false; }
/*     */   
/*     */   }
/*     */   private static ItemStack tryMoveInItem(Container from, Container container, ItemStack itemStack, int slot, Direction direction) {
/* 316 */     ItemStack current = container.getItem(slot);
/*     */     
/* 318 */     if (canPlaceItemInContainer(container, itemStack, slot, direction)) {
/* 319 */       boolean success = false;
/* 320 */       boolean wasEmpty = container.isEmpty();
/* 321 */       if (current.isEmpty()) {
/* 322 */         container.setItem(slot, itemStack);
/* 323 */         itemStack = ItemStack.EMPTY;
/* 324 */         success = true;
/* 325 */       } else if (canMergeItems(current, itemStack)) {
/* 326 */         int space = itemStack.getMaxStackSize() - current.getCount();
/* 327 */         int count = Math.min(itemStack.getCount(), space);
/*     */         
/* 329 */         itemStack.shrink(count);
/* 330 */         current.grow(count);
/* 331 */         success = (count > 0);
/*     */       } 
/* 333 */       if (success) {
/* 334 */         if (wasEmpty && container instanceof HopperBlockEntity) { HopperBlockEntity hopperBlockEntity = (HopperBlockEntity)container;
/* 335 */           if (!hopperBlockEntity.isOnCustomCooldown()) {
/* 336 */             int skipTickCount = 0;
/* 337 */             if (from instanceof HopperBlockEntity) { HopperBlockEntity fromHopper = (HopperBlockEntity)from;
/* 338 */               if (hopperBlockEntity.tickedGameTime >= fromHopper.tickedGameTime)
/*     */               {
/* 340 */                 skipTickCount = 1;
/*     */               } }
/*     */             
/* 343 */             hopperBlockEntity.setCooldown(8 - skipTickCount);
/*     */           }  }
/*     */         
/* 346 */         container.setChanged();
/*     */       } 
/*     */     } 
/* 349 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/* 353 */   private static Container getAttachedContainer(Level level, BlockPos blockPos, HopperBlockEntity self) { return getContainerAt(level, blockPos.relative(self.facing)); }
/*     */ 
/*     */ 
/*     */   
/* 357 */   private static Container getSourceContainer(Level level, Hopper hopper, BlockPos pos, BlockState state) { return getContainerAt(level, pos, state, hopper.getLevelX(), hopper.getLevelY() + 1.0D, hopper.getLevelZ()); }
/*     */ 
/*     */   
/*     */   public static List<ItemEntity> getItemsAtAndAbove(Level level, Hopper hopper) {
/* 361 */     AABB aabb = hopper.getSuckAabb().move(hopper.getLevelX() - 0.5D, hopper.getLevelY() - 0.5D, hopper.getLevelZ() - 0.5D);
/* 362 */     return level.getEntitiesOfClass(ItemEntity.class, aabb, EntitySelector.ENTITY_STILL_ALIVE);
/*     */   }
/*     */ 
/*     */   
/* 366 */   public static Container getContainerAt(Level level, BlockPos pos) { return getContainerAt(level, pos, level.getBlockState(pos), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D); }
/*     */ 
/*     */   
/*     */   private static Container getContainerAt(Level level, BlockPos pos, BlockState state, double x, double y, double z) {
/* 370 */     Container result = getBlockContainer(level, pos, state);
/* 371 */     if (result == null) {
/* 372 */       result = getEntityContainer(level, x, y, z);
/*     */     }
/* 374 */     return result;
/*     */   }
/*     */   
/*     */   private static Container getBlockContainer(Level level, BlockPos pos, BlockState state) {
/* 378 */     Block block = state.getBlock();
/* 379 */     if (block instanceof WorldlyContainerHolder)
/* 380 */       return ((WorldlyContainerHolder)block).getContainer(state, level, pos); 
/* 381 */     if (state.hasBlockEntity()) {
/* 382 */       BlockEntity entity = level.getBlockEntity(pos);
/* 383 */       if (entity instanceof Container) { Container container = (Container)entity;
/*     */ 
/*     */ 
/*     */         
/* 387 */         if (container instanceof ChestBlockEntity && 
/* 388 */           block instanceof ChestBlock) {
/* 389 */           container = ChestBlock.getContainer((ChestBlock)block, state, level, pos, true);
/*     */         }
/*     */         
/* 392 */         return container; }
/*     */     
/*     */     } 
/* 395 */     return null;
/*     */   }
/*     */   
/*     */   private static Container getEntityContainer(Level level, double x, double y, double z) {
/* 399 */     List<Entity> entities = level.getEntities((Entity)null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelector.CONTAINER_ENTITY_SELECTOR);
/*     */     
/* 401 */     if (!entities.isEmpty()) {
/* 402 */       return (Container)entities.get(level.random.nextInt(entities.size()));
/*     */     }
/* 404 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 408 */   private static boolean canMergeItems(ItemStack a, ItemStack b) { return (a.getCount() <= a.getMaxStackSize() && ItemStack.isSameItemSameComponents(a, b)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 413 */   public double getLevelX() { return this.worldPosition.getX() + 0.5D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 418 */   public double getLevelY() { return this.worldPosition.getY() + 0.5D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 423 */   public double getLevelZ() { return this.worldPosition.getZ() + 0.5D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 428 */   public boolean isGridAligned() { return true; }
/*     */ 
/*     */ 
/*     */   
/* 432 */   private void setCooldown(int time) { this.cooldownTime = time; }
/*     */ 
/*     */ 
/*     */   
/* 436 */   private boolean isOnCooldown() { return (this.cooldownTime > 0); }
/*     */ 
/*     */ 
/*     */   
/* 440 */   private boolean isOnCustomCooldown() { return (this.cooldownTime > 8); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 445 */   protected NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 450 */   protected void setItems(NonNullList<ItemStack> items) { this.items = items; }
/*     */ 
/*     */   
/*     */   public static void entityInside(Level level, BlockPos pos, BlockState blockState, Entity entity, HopperBlockEntity hopper) {
/* 454 */     if (entity instanceof ItemEntity) { ItemEntity itemEntity = (ItemEntity)entity; if (!itemEntity.getItem().isEmpty() && 
/* 455 */         entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()).intersects(hopper.getSuckAabb())) {
/* 456 */         tryMoveItems(level, pos, blockState, hopper, () -> addItem(hopper, itemEntity));
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 463 */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return new HopperMenu(containerId, inventory, this); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\HopperBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */