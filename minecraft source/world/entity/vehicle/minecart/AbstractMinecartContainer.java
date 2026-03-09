/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.ContainerEntity;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractMinecartContainer
/*     */   extends AbstractMinecart
/*     */   implements ContainerEntity {
/*  27 */   private NonNullList<ItemStack> itemStacks = NonNullList.withSize(36, ItemStack.EMPTY);
/*     */   
/*     */   private ResourceKey<LootTable> lootTable;
/*     */   private long lootTableSeed;
/*     */   
/*  32 */   protected AbstractMinecartContainer(EntityType<?> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy(ServerLevel level, DamageSource source) {
/*  37 */     super.destroy(level, source);
/*  38 */     chestVehicleDestroyed(source, level, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public ItemStack getItem(int slot) { return getChestVehicleItem(slot); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public ItemStack removeItem(int slot, int count) { return removeChestVehicleItem(slot, count); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public ItemStack removeItemNoUpdate(int slot) { return removeChestVehicleItemNoUpdate(slot); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public void setItem(int slot, ItemStack itemStack) { setChestVehicleItem(slot, itemStack); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public SlotAccess getSlot(int slot) { return getChestVehicleSlot(slot); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged() {}
/*     */ 
/*     */ 
/*     */   
/*  72 */   public boolean stillValid(Player player) { return isChestVehicleStillValid(player); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/*  77 */     if (!level().isClientSide() && reason.shouldDestroy()) {
/*  78 */       Containers.dropContents(level(), this, this);
/*     */     }
/*     */     
/*  81 */     super.remove(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  86 */     super.addAdditionalSaveData(output);
/*  87 */     addChestVehicleSaveData(output);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  92 */     super.readAdditionalSaveData(input);
/*  93 */     readChestVehicleSaveData(input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public InteractionResult interact(Player player, InteractionHand hand) { return interactWithContainerVehicle(player); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 applyNaturalSlowdown(Vec3 deltaMovement) {
/* 103 */     float keep = 0.98F;
/*     */     
/* 105 */     if (this.lootTable == null) {
/* 106 */       int emptiness = 15 - AbstractContainerMenu.getRedstoneSignalFromContainer(this);
/* 107 */       keep += emptiness * 0.001F;
/*     */     } 
/*     */     
/* 110 */     if (isInWater()) {
/* 111 */       keep *= 0.95F;
/*     */     }
/*     */     
/* 114 */     return deltaMovement.multiply(keep, 0.0D, keep);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public void clearContent() { clearChestVehicleContent(); }
/*     */ 
/*     */   
/*     */   public void setLootTable(ResourceKey<LootTable> lootTable, long seed) {
/* 127 */     this.lootTable = lootTable;
/* 128 */     this.lootTableSeed = seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 133 */     if (this.lootTable == null || !player.isSpectator()) {
/* 134 */       unpackChestVehicleLootTable(inventory.player);
/* 135 */       return createMenu(containerId, inventory);
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory);
/*     */ 
/*     */   
/* 144 */   public ResourceKey<LootTable> getContainerLootTable() { return this.lootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   public void setContainerLootTable(ResourceKey<LootTable> lootTable) { this.lootTable = lootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public long getContainerLootTableSeed() { return this.lootTableSeed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public void setContainerLootTableSeed(long lootTableSeed) { this.lootTableSeed = lootTableSeed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public NonNullList<ItemStack> getItemStacks() { return this.itemStacks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public void clearItemStacks() { this.itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\AbstractMinecartContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */