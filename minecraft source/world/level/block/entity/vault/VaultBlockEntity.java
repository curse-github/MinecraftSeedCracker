/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class VaultBlockEntity
/*     */   extends BlockEntity {
/*     */   private final VaultServerData serverData;
/*     */   private final VaultSharedData sharedData;
/*     */   private final VaultClientData clientData;
/*     */   private VaultConfig config;
/*     */   
/*     */   public VaultBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  51 */     super(BlockEntityType.VAULT, worldPosition, blockState);
/*  52 */     this.serverData = new VaultServerData();
/*  53 */     this.sharedData = new VaultSharedData();
/*  54 */     this.clientData = new VaultClientData();
/*  55 */     this.config = VaultConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return (CompoundTag)Util.make(new CompoundTag(), tag -> tag.store("shared_data", VaultSharedData.CODEC, registries.createSerializationContext(NbtOps.INSTANCE), this.sharedData)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  70 */     super.saveAdditional(output);
/*     */     
/*  72 */     output.store("config", VaultConfig.CODEC, this.config);
/*  73 */     output.store("shared_data", VaultSharedData.CODEC, this.sharedData);
/*  74 */     output.store("server_data", VaultServerData.CODEC, this.serverData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  79 */     super.loadAdditional(input);
/*     */     
/*  81 */     Objects.requireNonNull(this.serverData); input.read("server_data", VaultServerData.CODEC).ifPresent(this.serverData::set);
/*  82 */     this.config = (VaultConfig)input.read("config", VaultConfig.CODEC).orElse(VaultConfig.DEFAULT);
/*  83 */     Objects.requireNonNull(this.sharedData); input.read("shared_data", VaultSharedData.CODEC).ifPresent(this.sharedData::set);
/*     */   }
/*     */ 
/*     */   
/*  87 */   public VaultServerData getServerData() { return (this.level == null || this.level.isClientSide()) ? null : this.serverData; }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public VaultSharedData getSharedData() { return this.sharedData; }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public VaultClientData getClientData() { return this.clientData; }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public VaultConfig getConfig() { return this.config; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 104 */   public void setConfig(VaultConfig config) { this.config = config; }
/*     */   
/*     */   public static final class Server
/*     */   {
/*     */     private static final int UNLOCKING_DELAY_TICKS = 14;
/*     */     private static final int DISPLAY_CYCLE_TICK_RATE = 20;
/*     */     private static final int INSERT_FAIL_SOUND_BUFFER_TICKS = 15;
/*     */     
/*     */     public static void tick(ServerLevel serverLevel, BlockPos pos, BlockState blockState, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData) {
/* 113 */       VaultState currentState = (VaultState)blockState.getValue(VaultBlock.STATE);
/*     */       
/* 115 */       if (shouldCycleDisplayItem(serverLevel.getGameTime(), currentState)) {
/* 116 */         cycleDisplayItemFromLootTable(serverLevel, currentState, config, sharedData, pos);
/*     */       }
/*     */       
/* 119 */       BlockState nextBlockState = blockState;
/* 120 */       if (serverLevel.getGameTime() >= serverData.stateUpdatingResumesAt()) {
/* 121 */         nextBlockState = (BlockState)nextBlockState.setValue(VaultBlock.STATE, currentState.tickAndGetNext(serverLevel, pos, config, serverData, sharedData));
/*     */         
/* 123 */         if (blockState != nextBlockState) {
/* 124 */           setVaultState(serverLevel, pos, blockState, nextBlockState, config, sharedData);
/*     */         }
/*     */       } 
/*     */       
/* 128 */       if (serverData.isDirty || sharedData.isDirty) {
/*     */         
/* 130 */         VaultBlockEntity.setChanged(serverLevel, pos, blockState);
/*     */ 
/*     */         
/* 133 */         if (sharedData.isDirty) {
/* 134 */           serverLevel.sendBlockUpdated(pos, blockState, nextBlockState, 2);
/*     */         }
/* 136 */         serverData.isDirty = false;
/* 137 */         sharedData.isDirty = false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public static void tryInsertKey(ServerLevel serverLevel, BlockPos pos, BlockState blockState, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, Player player, ItemStack stackToInsert) {
/* 142 */       VaultState vaultState = (VaultState)blockState.getValue(VaultBlock.STATE);
/*     */       
/* 144 */       if (!canEjectReward(config, vaultState)) {
/*     */         return;
/*     */       }
/*     */       
/* 148 */       if (!isValidToInsert(config, stackToInsert)) {
/* 149 */         playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL);
/*     */         
/*     */         return;
/*     */       } 
/* 153 */       if (serverData.hasRewardedPlayer(player)) {
/* 154 */         playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
/*     */         
/*     */         return;
/*     */       } 
/* 158 */       List<ItemStack> itemsToEject = resolveItemsToEject(serverLevel, config, pos, player, stackToInsert);
/* 159 */       if (itemsToEject.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 163 */       player.awardStat(Stats.ITEM_USED.get(stackToInsert.getItem()));
/* 164 */       stackToInsert.consume(config.keyItem().getCount(), player);
/*     */       
/* 166 */       unlock(serverLevel, blockState, pos, config, serverData, sharedData, itemsToEject);
/* 167 */       serverData.addToRewardedPlayers(player);
/* 168 */       sharedData.updateConnectedPlayersWithinRange(serverLevel, pos, serverData, config, config.deactivationRange());
/*     */     }
/*     */     
/*     */     static void setVaultState(ServerLevel serverLevel, BlockPos pos, BlockState currentBlockState, BlockState newBlockState, VaultConfig config, VaultSharedData sharedData) {
/* 172 */       VaultState currentVaultState = (VaultState)currentBlockState.getValue(VaultBlock.STATE);
/* 173 */       VaultState newVaultState = (VaultState)newBlockState.getValue(VaultBlock.STATE);
/*     */       
/* 175 */       serverLevel.setBlock(pos, newBlockState, 3);
/* 176 */       currentVaultState.onTransition(serverLevel, pos, newVaultState, config, sharedData, ((Boolean)newBlockState.getValue(VaultBlock.OMINOUS)).booleanValue());
/*     */     }
/*     */     
/*     */     static void cycleDisplayItemFromLootTable(ServerLevel serverLevel, VaultState vaultState, VaultConfig config, VaultSharedData sharedData, BlockPos pos) {
/* 180 */       if (!canEjectReward(config, vaultState)) {
/* 181 */         sharedData.setDisplayItem(ItemStack.EMPTY);
/*     */         
/*     */         return;
/*     */       } 
/* 185 */       ItemStack displayItem = getRandomDisplayItemFromLootTable(serverLevel, pos, (ResourceKey)config.overrideLootTableToDisplay().orElse(config.lootTable()));
/* 186 */       sharedData.setDisplayItem(displayItem);
/*     */     }
/*     */     
/*     */     private static ItemStack getRandomDisplayItemFromLootTable(ServerLevel serverLevel, BlockPos pos, ResourceKey<LootTable> lootTableId) {
/* 190 */       LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableId);
/*     */ 
/*     */       
/* 193 */       LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).create(LootContextParamSets.VAULT);
/* 194 */       ObjectArrayList objectArrayList = lootTable.getRandomItems(params, serverLevel.getRandom());
/*     */       
/* 196 */       if (objectArrayList.isEmpty()) {
/* 197 */         return ItemStack.EMPTY;
/*     */       }
/*     */       
/* 200 */       return (ItemStack)Util.getRandom(objectArrayList, serverLevel.getRandom());
/*     */     }
/*     */     
/*     */     private static void unlock(ServerLevel serverLevel, BlockState blockState, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, List<ItemStack> itemsToEject) {
/* 204 */       serverData.setItemsToEject(itemsToEject);
/* 205 */       sharedData.setDisplayItem(serverData.getNextItemToEject());
/* 206 */       serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 14L);
/* 207 */       setVaultState(serverLevel, pos, blockState, (BlockState)blockState.setValue(VaultBlock.STATE, VaultState.UNLOCKING), config, sharedData);
/*     */     }
/*     */     
/*     */     private static List<ItemStack> resolveItemsToEject(ServerLevel serverLevel, VaultConfig config, BlockPos pos, Player player, ItemStack insertedStack) {
/* 211 */       LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(config.lootTable());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.TOOL, insertedStack).create(LootContextParamSets.VAULT);
/*     */       
/* 219 */       return lootTable.getRandomItems(params);
/*     */     }
/*     */ 
/*     */     
/* 223 */     private static boolean canEjectReward(VaultConfig config, VaultState vaultState) { return (!config.keyItem().isEmpty() && vaultState != VaultState.INACTIVE); }
/*     */ 
/*     */ 
/*     */     
/* 227 */     private static boolean isValidToInsert(VaultConfig config, ItemStack stackToInsert) { return (ItemStack.isSameItemSameComponents(stackToInsert, config.keyItem()) && stackToInsert.getCount() >= config.keyItem().getCount()); }
/*     */ 
/*     */ 
/*     */     
/* 231 */     private static boolean shouldCycleDisplayItem(long gameTime, VaultState vaultState) { return (gameTime % 20L == 0L && vaultState == VaultState.ACTIVE); }
/*     */ 
/*     */     
/*     */     private static void playInsertFailSound(ServerLevel serverLevel, VaultServerData serverData, BlockPos pos, SoundEvent sound) {
/* 235 */       if (serverLevel.getGameTime() >= serverData.getLastInsertFailTimestamp() + 15L) {
/* 236 */         serverLevel.playSound(null, pos, sound, SoundSource.BLOCKS);
/* 237 */         serverData.setLastInsertFailTimestamp(serverLevel.getGameTime());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Client {
/*     */     private static final int PARTICLE_TICK_RATE = 20;
/*     */     private static final float IDLE_PARTICLE_CHANCE = 0.5F;
/*     */     private static final float AMBIENT_SOUND_CHANCE = 0.02F;
/*     */     private static final int ACTIVATION_PARTICLE_COUNT = 20;
/*     */     private static final int DEACTIVATION_PARTICLE_COUNT = 20;
/*     */     
/*     */     public static void tick(Level clientLevel, BlockPos pos, BlockState blockState, VaultClientData clientData, VaultSharedData sharedData) {
/* 250 */       clientData.updateDisplayItemSpin();
/*     */       
/* 252 */       if (clientLevel.getGameTime() % 20L == 0L) {
/* 253 */         emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData);
/*     */       }
/*     */       
/* 256 */       emitIdleParticles(clientLevel, pos, sharedData, ((Boolean)blockState.getValue(VaultBlock.OMINOUS)).booleanValue() ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME);
/* 257 */       playIdleSounds(clientLevel, pos, sharedData);
/*     */     }
/*     */     
/*     */     public static void emitActivationParticles(Level clientLevel, BlockPos pos, BlockState blockState, VaultSharedData sharedData, ParticleOptions flameParticle) {
/* 261 */       emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData);
/* 262 */       RandomSource random = clientLevel.random;
/* 263 */       for (int i = 0; i < 20; i++) {
/* 264 */         Vec3 particlePos = randomPosInsideCage(pos, random);
/* 265 */         clientLevel.addParticle(ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/* 266 */         clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     }
/*     */     
/*     */     public static void emitDeactivationParticles(Level clientLevel, BlockPos pos, ParticleOptions flameParticle) {
/* 271 */       RandomSource random = clientLevel.random;
/* 272 */       for (int i = 0; i < 20; i++) {
/* 273 */         Vec3 particlePos = randomPosCenterOfCage(pos, random);
/* 274 */         Vec3 dir = new Vec3(random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D);
/* 275 */         clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), dir.x(), dir.y(), dir.z());
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitIdleParticles(Level clientLevel, BlockPos pos, VaultSharedData sharedData, ParticleOptions flameParticle) {
/* 280 */       RandomSource random = clientLevel.getRandom();
/* 281 */       if (random.nextFloat() <= 0.5F) {
/* 282 */         Vec3 particlePos = randomPosInsideCage(pos, random);
/* 283 */         clientLevel.addParticle(ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/* 284 */         if (shouldDisplayActiveEffects(sharedData)) {
/* 285 */           clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitConnectionParticlesForPlayer(Level level, Vec3 flyTowards, Player player) {
/* 291 */       RandomSource random = level.random;
/* 292 */       Vec3 direction = flyTowards.vectorTo(player.position().add(0.0D, (player.getBbHeight() / 2.0F), 0.0D));
/* 293 */       int particleCount = Mth.nextInt(random, 2, 5);
/* 294 */       for (int i = 0; i < particleCount; i++) {
/* 295 */         Vec3 randomDirection = direction.offsetRandom(random, 1.0F);
/* 296 */         level.addParticle(ParticleTypes.VAULT_CONNECTION, flyTowards.x(), flyTowards.y(), flyTowards.z(), randomDirection.x(), randomDirection.y(), randomDirection.z());
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitConnectionParticlesForNearbyPlayers(Level level, BlockPos pos, BlockState blockState, VaultSharedData sharedData) {
/* 301 */       Set<UUID> connectedPlayers = sharedData.getConnectedPlayers();
/*     */       
/* 303 */       if (connectedPlayers.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 307 */       Vec3 keyholePos = keyholePos(pos, (Direction)blockState.getValue(VaultBlock.FACING));
/*     */       
/* 309 */       for (UUID uuid : connectedPlayers) {
/* 310 */         Player player = level.getPlayerByUUID(uuid);
/* 311 */         if (player == null || !isWithinConnectionRange(pos, sharedData, player)) {
/*     */           continue;
/*     */         }
/*     */         
/* 315 */         emitConnectionParticlesForPlayer(level, keyholePos, player);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 320 */     private static boolean isWithinConnectionRange(BlockPos vaultPos, VaultSharedData sharedData, Player player) { return (player.blockPosition().distSqr(vaultPos) <= Mth.square(sharedData.connectedParticlesRange())); }
/*     */ 
/*     */     
/*     */     private static void playIdleSounds(Level clientLevel, BlockPos pos, VaultSharedData sharedData) {
/* 324 */       if (!shouldDisplayActiveEffects(sharedData)) {
/*     */         return;
/*     */       }
/*     */       
/* 328 */       RandomSource random = clientLevel.getRandom();
/* 329 */       if (random.nextFloat() <= 0.02F) {
/* 330 */         clientLevel.playLocalSound(pos, SoundEvents.VAULT_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 335 */     public static boolean shouldDisplayActiveEffects(VaultSharedData sharedData) { return sharedData.hasDisplayItem(); }
/*     */ 
/*     */ 
/*     */     
/* 339 */     private static Vec3 randomPosCenterOfCage(BlockPos blockPos, RandomSource random) { return Vec3.atLowerCornerOf(blockPos).add(Mth.nextDouble(random, 0.4D, 0.6D), Mth.nextDouble(random, 0.4D, 0.6D), Mth.nextDouble(random, 0.4D, 0.6D)); }
/*     */ 
/*     */ 
/*     */     
/* 343 */     private static Vec3 randomPosInsideCage(BlockPos blockPos, RandomSource random) { return Vec3.atLowerCornerOf(blockPos).add(Mth.nextDouble(random, 0.1D, 0.9D), Mth.nextDouble(random, 0.25D, 0.75D), Mth.nextDouble(random, 0.1D, 0.9D)); }
/*     */ 
/*     */ 
/*     */     
/* 347 */     private static Vec3 keyholePos(BlockPos blockPos, Direction blockFacing) { return Vec3.atBottomCenterOf(blockPos).add(blockFacing.getStepX() * 0.5D, 1.75D, blockFacing.getStepZ() * 0.5D); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */