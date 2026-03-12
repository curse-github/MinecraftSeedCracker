/*     */ package net.minecraft.world.entity.vehicle;
/*     */ 
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ContainerEntity
/*     */   extends Container, MenuProvider
/*     */ {
/*     */   Vec3 position();
/*     */   
/*     */   AABB getBoundingBox();
/*     */   
/*     */   ResourceKey<LootTable> getContainerLootTable();
/*     */   
/*     */   void setContainerLootTable(ResourceKey<LootTable> paramResourceKey);
/*     */   
/*     */   long getContainerLootTableSeed();
/*     */   
/*     */   void setContainerLootTableSeed(long paramLong);
/*     */   
/*     */   NonNullList<ItemStack> getItemStacks();
/*     */   
/*     */   void clearItemStacks();
/*     */   
/*     */   Level level();
/*     */   
/*     */   boolean isRemoved();
/*     */   
/*  63 */   default boolean isEmpty() { return isChestVehicleEmpty(); }
/*     */ 
/*     */   
/*     */   default void addChestVehicleSaveData(ValueOutput output) {
/*  67 */     if (getContainerLootTable() != null) {
/*  68 */       output.putString("LootTable", getContainerLootTable().identifier().toString());
/*  69 */       if (getContainerLootTableSeed() != 0L) {
/*  70 */         output.putLong("LootTableSeed", getContainerLootTableSeed());
/*     */       }
/*     */     } else {
/*  73 */       ContainerHelper.saveAllItems(output, getItemStacks());
/*     */     } 
/*     */   }
/*     */   
/*     */   default void readChestVehicleSaveData(ValueInput input) {
/*  78 */     clearItemStacks();
/*     */     
/*  80 */     ResourceKey<LootTable> lootTable = (ResourceKey)input.read("LootTable", LootTable.KEY_CODEC).orElse(null);
/*  81 */     setContainerLootTable(lootTable);
/*  82 */     setContainerLootTableSeed(input.getLongOr("LootTableSeed", 0L));
/*  83 */     if (lootTable == null) {
/*  84 */       ContainerHelper.loadAllItems(input, getItemStacks());
/*     */     }
/*     */   }
/*     */   
/*     */   default void chestVehicleDestroyed(DamageSource source, ServerLevel level, Entity entity) {
/*  89 */     if (!((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     Containers.dropContents(level, entity, this);
/*     */     
/*  95 */     Entity directEntity = source.getDirectEntity();
/*  96 */     if (directEntity != null && directEntity.getType() == EntityType.PLAYER) {
/*  97 */       PiglinAi.angerNearbyPiglins(level, (Player)directEntity, true);
/*     */     }
/*     */   }
/*     */   
/*     */   default InteractionResult interactWithContainerVehicle(Player player) {
/* 102 */     player.openMenu(this);
/* 103 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   default void unpackChestVehicleLootTable(Player player) {
/* 107 */     MinecraftServer server = level().getServer();
/* 108 */     if (getContainerLootTable() != null && server != null) {
/* 109 */       LootTable lootTable = server.reloadableRegistries().getLootTable(getContainerLootTable());
/* 110 */       if (player != null) {
/* 111 */         CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)player, getContainerLootTable());
/*     */       }
/* 113 */       setContainerLootTable(null);
/*     */ 
/*     */       
/* 116 */       LootParams.Builder builder = (new LootParams.Builder((ServerLevel)level())).withParameter(LootContextParams.ORIGIN, position());
/*     */       
/* 118 */       if (player != null) {
/* 119 */         builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
/*     */       }
/* 121 */       lootTable.fill(this, builder.create(LootContextParamSets.CHEST), getContainerLootTableSeed());
/*     */     } 
/*     */   }
/*     */   
/*     */   default void clearChestVehicleContent() {
/* 126 */     unpackChestVehicleLootTable(null);
/* 127 */     getItemStacks().clear();
/*     */   }
/*     */   
/*     */   default boolean isChestVehicleEmpty() {
/* 131 */     for (ItemStack itemStack : getItemStacks()) {
/* 132 */       if (!itemStack.isEmpty()) {
/* 133 */         return false;
/*     */       }
/*     */     } 
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   default ItemStack removeChestVehicleItemNoUpdate(int slot) {
/* 140 */     unpackChestVehicleLootTable(null);
/* 141 */     ItemStack itemStack = (ItemStack)getItemStacks().get(slot);
/* 142 */     if (itemStack.isEmpty()) {
/* 143 */       return ItemStack.EMPTY;
/*     */     }
/* 145 */     getItemStacks().set(slot, ItemStack.EMPTY);
/* 146 */     return itemStack;
/*     */   }
/*     */   
/*     */   default ItemStack getChestVehicleItem(int slot) {
/* 150 */     unpackChestVehicleLootTable(null);
/* 151 */     return (ItemStack)getItemStacks().get(slot);
/*     */   }
/*     */   
/*     */   default ItemStack removeChestVehicleItem(int slot, int count) {
/* 155 */     unpackChestVehicleLootTable(null);
/* 156 */     return ContainerHelper.removeItem(getItemStacks(), slot, count);
/*     */   }
/*     */   
/*     */   default void setChestVehicleItem(int slot, ItemStack itemStack) {
/* 160 */     unpackChestVehicleLootTable(null);
/* 161 */     getItemStacks().set(slot, itemStack);
/* 162 */     itemStack.limitSize(getMaxStackSize(itemStack));
/*     */   }
/*     */   
/*     */   default SlotAccess getChestVehicleSlot(final int slot) {
/* 166 */     if (slot >= 0 && slot < getContainerSize()) {
/* 167 */       return new SlotAccess()
/*     */         {
/*     */           public ItemStack get() {
/* 170 */             return ContainerEntity.this.getChestVehicleItem(slot);
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean set(ItemStack itemStack) {
/* 175 */             ContainerEntity.this.setChestVehicleItem(slot, itemStack);
/* 176 */             return true;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 185 */   default boolean isChestVehicleStillValid(Player player) { return (!isRemoved() && player.isWithinEntityInteractionRange(getBoundingBox(), 4.0D)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\ContainerEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */