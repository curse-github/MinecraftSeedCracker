/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.SeededContainerLoot;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public abstract class RandomizableContainerBlockEntity
/*     */   extends BaseContainerBlockEntity implements RandomizableContainer {
/*     */   protected ResourceKey<LootTable> lootTable;
/*  21 */   protected long lootTableSeed = 0L;
/*     */ 
/*     */   
/*  24 */   protected RandomizableContainerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) { super(type, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  29 */   public ResourceKey<LootTable> getLootTable() { return this.lootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   public void setLootTable(ResourceKey<LootTable> lootTable) { this.lootTable = lootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   public long getLootTableSeed() { return this.lootTableSeed; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public void setLootTableSeed(long lootTableSeed) { this.lootTableSeed = lootTableSeed; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  49 */     unpackLootTable(null);
/*  50 */     return super.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int slot) {
/*  55 */     unpackLootTable(null);
/*  56 */     return super.getItem(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int slot, int count) {
/*  61 */     unpackLootTable(null);
/*  62 */     return super.removeItem(slot, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int slot) {
/*  67 */     unpackLootTable(null);
/*  68 */     return super.removeItemNoUpdate(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/*  73 */     unpackLootTable(null);
/*  74 */     super.setItem(slot, itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public boolean canOpen(Player player) { return (super.canOpen(player) && (this.lootTable == null || !player.isSpectator())); }
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/*  84 */     if (canOpen(player)) {
/*  85 */       unpackLootTable(inventory.player);
/*  86 */       return createMenu(containerId, inventory);
/*     */     } 
/*  88 */     BaseContainerBlockEntity.sendChestLockedNotifications(getBlockPos().getCenter(), player, getDisplayName());
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/*  94 */     super.applyImplicitComponents(components);
/*  95 */     SeededContainerLoot loot = (SeededContainerLoot)components.get(DataComponents.CONTAINER_LOOT);
/*  96 */     if (loot != null) {
/*  97 */       this.lootTable = loot.lootTable();
/*  98 */       this.lootTableSeed = loot.seed();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 104 */     super.collectImplicitComponents(components);
/* 105 */     if (this.lootTable != null) {
/* 106 */       components.set(DataComponents.CONTAINER_LOOT, new SeededContainerLoot(this.lootTable, this.lootTableSeed));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 112 */     super.removeComponentsFromTag(output);
/* 113 */     output.discard("LootTable");
/* 114 */     output.discard("LootTableSeed");
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\RandomizableContainerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */