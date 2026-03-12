/*     */ package net.minecraft.world.entity.vehicle.boat;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.ContainerEntity;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public abstract class AbstractChestBoat
/*     */   extends AbstractBoat
/*     */   implements HasCustomInventoryScreen, ContainerEntity {
/*     */   private static final int CONTAINER_SIZE = 27;
/*  34 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
/*     */   
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*  39 */   public AbstractChestBoat(EntityType<? extends AbstractChestBoat> type, Level level, Supplier<Item> dropItem) { super(type, level, dropItem); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   protected float getSinglePassengerXOffset() { return 0.15F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected int getMaxPassengers() { return 1; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  55 */     super.addAdditionalSaveData(output);
/*  56 */     addChestVehicleSaveData(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  61 */     super.readAdditionalSaveData(input);
/*  62 */     readChestVehicleSaveData(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy(ServerLevel level, DamageSource source) {
/*  67 */     destroy(level, getDropItem());
/*  68 */     chestVehicleDestroyed(source, level, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/*  73 */     if (!level().isClientSide() && reason.shouldDestroy()) {
/*  74 */       Containers.dropContents(level(), this, this);
/*     */     }
/*  76 */     super.remove(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/*  81 */     InteractionResult superInteraction = super.interact(player, hand);
/*  82 */     if (superInteraction != InteractionResult.PASS) {
/*  83 */       return superInteraction;
/*     */     }
/*  85 */     if (!canAddPassenger(player) || player.isSecondaryUseActive()) {
/*  86 */       InteractionResult result = interactWithContainerVehicle(player);
/*  87 */       if (result.consumesAction()) { Level level = player.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  88 */           gameEvent(GameEvent.CONTAINER_OPEN, player);
/*  89 */           PiglinAi.angerNearbyPiglins(serverLevel, player, true); }
/*     */          }
/*  91 */        return result;
/*     */     } 
/*  93 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void openCustomInventoryScreen(Player player) {
/*  98 */     player.openMenu(this);
/*  99 */     Level level1 = player.level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1;
/* 100 */       gameEvent(GameEvent.CONTAINER_OPEN, player);
/* 101 */       PiglinAi.angerNearbyPiglins(level, player, true); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public void clearContent() { clearChestVehicleContent(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public int getContainerSize() { return 27; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public ItemStack getItem(int slot) { return getChestVehicleItem(slot); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public ItemStack removeItem(int slot, int count) { return removeChestVehicleItem(slot, count); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public ItemStack removeItemNoUpdate(int slot) { return removeChestVehicleItemNoUpdate(slot); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public void setItem(int slot, ItemStack itemStack) { setChestVehicleItem(slot, itemStack); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public SlotAccess getSlot(int slot) { return getChestVehicleSlot(slot); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */ 
/*     */   
/* 146 */   public boolean stillValid(Player player) { return isChestVehicleStillValid(player); }
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 151 */     if (this.lootTable == null || !player.isSpectator()) {
/* 152 */       unpackLootTable(inventory.player);
/* 153 */       return ChestMenu.threeRows(containerId, inventory, this);
/*     */     } 
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 159 */   public void unpackLootTable(Player player) { unpackChestVehicleLootTable(player); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public ResourceKey<LootTable> getContainerLootTable() { return this.lootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public void setContainerLootTable(ResourceKey<LootTable> lootTable) { this.lootTable = lootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public long getContainerLootTableSeed() { return this.lootTableSeed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public void setContainerLootTableSeed(long lootTableSeed) { this.lootTableSeed = lootTableSeed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public NonNullList<ItemStack> getItemStacks() { return this.itemStacks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   public void clearItemStacks() { this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public void stopOpen(ContainerUser containerUser) { level().gameEvent(GameEvent.CONTAINER_CLOSE, position(), GameEvent.Context.of(containerUser.getLivingEntity())); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\boat\AbstractChestBoat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */