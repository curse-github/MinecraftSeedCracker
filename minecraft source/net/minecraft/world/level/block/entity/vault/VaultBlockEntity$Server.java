/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Server
/*     */ {
/*     */   private static final int UNLOCKING_DELAY_TICKS = 14;
/*     */   private static final int DISPLAY_CYCLE_TICK_RATE = 20;
/*     */   private static final int INSERT_FAIL_SOUND_BUFFER_TICKS = 15;
/*     */   
/*     */   public static void tick(ServerLevel serverLevel, BlockPos pos, BlockState blockState, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData) {
/* 113 */     VaultState currentState = (VaultState)blockState.getValue(VaultBlock.STATE);
/*     */     
/* 115 */     if (shouldCycleDisplayItem(serverLevel.getGameTime(), currentState)) {
/* 116 */       cycleDisplayItemFromLootTable(serverLevel, currentState, config, sharedData, pos);
/*     */     }
/*     */     
/* 119 */     BlockState nextBlockState = blockState;
/* 120 */     if (serverLevel.getGameTime() >= serverData.stateUpdatingResumesAt()) {
/* 121 */       nextBlockState = (BlockState)nextBlockState.setValue(VaultBlock.STATE, currentState.tickAndGetNext(serverLevel, pos, config, serverData, sharedData));
/*     */       
/* 123 */       if (blockState != nextBlockState) {
/* 124 */         setVaultState(serverLevel, pos, blockState, nextBlockState, config, sharedData);
/*     */       }
/*     */     } 
/*     */     
/* 128 */     if (serverData.isDirty || sharedData.isDirty) {
/*     */       
/* 130 */       VaultBlockEntity.access$000(serverLevel, pos, blockState);
/*     */ 
/*     */       
/* 133 */       if (sharedData.isDirty) {
/* 134 */         serverLevel.sendBlockUpdated(pos, blockState, nextBlockState, 2);
/*     */       }
/* 136 */       serverData.isDirty = false;
/* 137 */       sharedData.isDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void tryInsertKey(ServerLevel serverLevel, BlockPos pos, BlockState blockState, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, Player player, ItemStack stackToInsert) {
/* 142 */     VaultState vaultState = (VaultState)blockState.getValue(VaultBlock.STATE);
/*     */     
/* 144 */     if (!canEjectReward(config, vaultState)) {
/*     */       return;
/*     */     }
/*     */     
/* 148 */     if (!isValidToInsert(config, stackToInsert)) {
/* 149 */       playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL);
/*     */       
/*     */       return;
/*     */     } 
/* 153 */     if (serverData.hasRewardedPlayer(player)) {
/* 154 */       playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
/*     */       
/*     */       return;
/*     */     } 
/* 158 */     List<ItemStack> itemsToEject = resolveItemsToEject(serverLevel, config, pos, player, stackToInsert);
/* 159 */     if (itemsToEject.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 163 */     player.awardStat(Stats.ITEM_USED.get(stackToInsert.getItem()));
/* 164 */     stackToInsert.consume(config.keyItem().getCount(), player);
/*     */     
/* 166 */     unlock(serverLevel, blockState, pos, config, serverData, sharedData, itemsToEject);
/* 167 */     serverData.addToRewardedPlayers(player);
/* 168 */     sharedData.updateConnectedPlayersWithinRange(serverLevel, pos, serverData, config, config.deactivationRange());
/*     */   }
/*     */   
/*     */   static void setVaultState(ServerLevel serverLevel, BlockPos pos, BlockState currentBlockState, BlockState newBlockState, VaultConfig config, VaultSharedData sharedData) {
/* 172 */     VaultState currentVaultState = (VaultState)currentBlockState.getValue(VaultBlock.STATE);
/* 173 */     VaultState newVaultState = (VaultState)newBlockState.getValue(VaultBlock.STATE);
/*     */     
/* 175 */     serverLevel.setBlock(pos, newBlockState, 3);
/* 176 */     currentVaultState.onTransition(serverLevel, pos, newVaultState, config, sharedData, ((Boolean)newBlockState.getValue(VaultBlock.OMINOUS)).booleanValue());
/*     */   }
/*     */   
/*     */   static void cycleDisplayItemFromLootTable(ServerLevel serverLevel, VaultState vaultState, VaultConfig config, VaultSharedData sharedData, BlockPos pos) {
/* 180 */     if (!canEjectReward(config, vaultState)) {
/* 181 */       sharedData.setDisplayItem(ItemStack.EMPTY);
/*     */       
/*     */       return;
/*     */     } 
/* 185 */     ItemStack displayItem = getRandomDisplayItemFromLootTable(serverLevel, pos, (ResourceKey)config.overrideLootTableToDisplay().orElse(config.lootTable()));
/* 186 */     sharedData.setDisplayItem(displayItem);
/*     */   }
/*     */   
/*     */   private static ItemStack getRandomDisplayItemFromLootTable(ServerLevel serverLevel, BlockPos pos, ResourceKey<LootTable> lootTableId) {
/* 190 */     LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableId);
/*     */ 
/*     */     
/* 193 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).create(LootContextParamSets.VAULT);
/* 194 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(params, serverLevel.getRandom());
/*     */     
/* 196 */     if (objectArrayList.isEmpty()) {
/* 197 */       return ItemStack.EMPTY;
/*     */     }
/*     */     
/* 200 */     return (ItemStack)Util.getRandom(objectArrayList, serverLevel.getRandom());
/*     */   }
/*     */   
/*     */   private static void unlock(ServerLevel serverLevel, BlockState blockState, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, List<ItemStack> itemsToEject) {
/* 204 */     serverData.setItemsToEject(itemsToEject);
/* 205 */     sharedData.setDisplayItem(serverData.getNextItemToEject());
/* 206 */     serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 14L);
/* 207 */     setVaultState(serverLevel, pos, blockState, (BlockState)blockState.setValue(VaultBlock.STATE, VaultState.UNLOCKING), config, sharedData);
/*     */   }
/*     */   
/*     */   private static List<ItemStack> resolveItemsToEject(ServerLevel serverLevel, VaultConfig config, BlockPos pos, Player player, ItemStack insertedStack) {
/* 211 */     LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(config.lootTable());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.TOOL, insertedStack).create(LootContextParamSets.VAULT);
/*     */     
/* 219 */     return lootTable.getRandomItems(params);
/*     */   }
/*     */ 
/*     */   
/* 223 */   private static boolean canEjectReward(VaultConfig config, VaultState vaultState) { return (!config.keyItem().isEmpty() && vaultState != VaultState.INACTIVE); }
/*     */ 
/*     */ 
/*     */   
/* 227 */   private static boolean isValidToInsert(VaultConfig config, ItemStack stackToInsert) { return (ItemStack.isSameItemSameComponents(stackToInsert, config.keyItem()) && stackToInsert.getCount() >= config.keyItem().getCount()); }
/*     */ 
/*     */ 
/*     */   
/* 231 */   private static boolean shouldCycleDisplayItem(long gameTime, VaultState vaultState) { return (gameTime % 20L == 0L && vaultState == VaultState.ACTIVE); }
/*     */ 
/*     */   
/*     */   private static void playInsertFailSound(ServerLevel serverLevel, VaultServerData serverData, BlockPos pos, SoundEvent sound) {
/* 235 */     if (serverLevel.getGameTime() >= serverData.getLastInsertFailTimestamp() + 15L) {
/* 236 */       serverLevel.playSound(null, pos, sound, SoundSource.BLOCKS);
/* 237 */       serverData.setLastInsertFailTimestamp(serverLevel.getGameTime());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity$Server.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */