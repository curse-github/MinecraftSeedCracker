/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ public static final enum VaultState
/*     */   implements StringRepresentable
/*     */ {
/*     */   private static final int UPDATE_CONNECTED_PLAYERS_TICK_RATE = 20;
/*     */   private static final int DELAY_BETWEEN_EJECTIONS_TICKS = 20;
/*     */   private static final int DELAY_AFTER_LAST_EJECTION_TICKS = 20;
/*     */   private static final int DELAY_BEFORE_FIRST_EJECTION_TICKS = 20;
/*     */   private final String stateName;
/*     */   private final LightLevel lightLevel;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/world/level/block/entity/vault/VaultState$1
/*     */     //   3: dup
/*     */     //   4: ldc 'INACTIVE'
/*     */     //   6: iconst_0
/*     */     //   7: ldc 'inactive'
/*     */     //   9: getstatic net/minecraft/world/level/block/entity/vault/VaultState$LightLevel.HALF_LIT : Lnet/minecraft/world/level/block/entity/vault/VaultState$LightLevel;
/*     */     //   12: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;Lnet/minecraft/world/level/block/entity/vault/VaultState$LightLevel;)V
/*     */     //   15: putstatic net/minecraft/world/level/block/entity/vault/VaultState.INACTIVE : Lnet/minecraft/world/level/block/entity/vault/VaultState;
/*     */     //   18: new net/minecraft/world/level/block/entity/vault/VaultState$2
/*     */     //   21: dup
/*     */     //   22: ldc 'ACTIVE'
/*     */     //   24: iconst_1
/*     */     //   25: ldc 'active'
/*     */     //   27: getstatic net/minecraft/world/level/block/entity/vault/VaultState$LightLevel.LIT : Lnet/minecraft/world/level/block/entity/vault/VaultState$LightLevel;
/*     */     //   30: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;Lnet/minecraft/world/level/block/entity/vault/VaultState$LightLevel;)V
/*     */     //   33: putstatic net/minecraft/world/level/block/entity/vault/VaultState.ACTIVE : Lnet/minecraft/world/level/block/entity/vault/VaultState;
/*     */     //   36: new net/minecraft/world/level/block/entity/vault/VaultState$3
/*     */     //   39: dup
/*     */     //   40: ldc 'UNLOCKING'
/*     */     //   42: iconst_2
/*     */     //   43: ldc 'unlocking'
/*     */     //   45: getstatic net/minecraft/world/level/block/entity/vault/VaultState$LightLevel.LIT : Lnet/minecraft/world/level/block/entity/vault/VaultState$LightLevel;
/*     */     //   48: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;Lnet/minecraft/world/level/block/entity/vault/VaultState$LightLevel;)V
/*     */     //   51: putstatic net/minecraft/world/level/block/entity/vault/VaultState.UNLOCKING : Lnet/minecraft/world/level/block/entity/vault/VaultState;
/*     */     //   54: new net/minecraft/world/level/block/entity/vault/VaultState$4
/*     */     //   57: dup
/*     */     //   58: ldc 'EJECTING'
/*     */     //   60: iconst_3
/*     */     //   61: ldc 'ejecting'
/*     */     //   63: getstatic net/minecraft/world/level/block/entity/vault/VaultState$LightLevel.LIT : Lnet/minecraft/world/level/block/entity/vault/VaultState$LightLevel;
/*     */     //   66: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;Lnet/minecraft/world/level/block/entity/vault/VaultState$LightLevel;)V
/*     */     //   69: putstatic net/minecraft/world/level/block/entity/vault/VaultState.EJECTING : Lnet/minecraft/world/level/block/entity/vault/VaultState;
/*     */     //   72: invokestatic $values : ()[Lnet/minecraft/world/level/block/entity/vault/VaultState;
/*     */     //   75: putstatic net/minecraft/world/level/block/entity/vault/VaultState.$VALUES : [Lnet/minecraft/world/level/block/entity/vault/VaultState;
/*     */     //   78: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #16	-> 0
/*     */     //   #23	-> 18
/*     */     //   #32	-> 36
/*     */     //   #38	-> 54
/*     */     //   #15	-> 72
/*     */   }
/*     */   
/*     */   VaultState(String stateName, LightLevel lightLevel) {
/*     */     this.stateName = stateName;
/*     */     this.lightLevel = lightLevel;
/*     */   }
/*     */   
/*     */   public String getSerializedName() { return this.stateName; }
/*     */   
/*     */   public int lightLevel() { return this.lightLevel.value; }
/*     */   
/*     */   public VaultState tickAndGetNext(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData) {
/*     */     switch (ordinal()) {
/*     */       default:
/*     */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */         serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 20L);
/*     */       case 3:
/*     */         break;
/*     */     } 
/*     */     serverData.markEjectionFinished();
/*     */     float ejectionSoundProgress = serverData.ejectionProgress();
/*     */     ejectResultItem(serverLevel, pos, serverData.popNextItemToEject(), ejectionSoundProgress);
/*     */     sharedData.setDisplayItem(serverData.getNextItemToEject());
/*     */     boolean isLastEjection = serverData.getItemsToEject().isEmpty();
/*     */     int ejectionDelay = isLastEjection ? 20 : 20;
/*     */     serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + ejectionDelay);
/*     */     return serverData.getItemsToEject().isEmpty() ? updateStateForConnectedPlayers(serverLevel, pos, config, serverData, sharedData, config.deactivationRange()) : EJECTING;
/*     */   }
/*     */   
/*     */   private static VaultState updateStateForConnectedPlayers(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, double activationRange) {
/*     */     sharedData.updateConnectedPlayersWithinRange(serverLevel, pos, serverData, config, activationRange);
/*     */     serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 20L);
/*     */     return sharedData.hasConnectedPlayers() ? ACTIVE : INACTIVE;
/*     */   }
/*     */   
/*     */   public void onTransition(ServerLevel serverLevel, BlockPos pos, VaultState to, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {
/*     */     onExit(serverLevel, pos, config, sharedData);
/*     */     to.onEnter(serverLevel, pos, config, sharedData, isOminous);
/*     */   }
/*     */   
/*     */   protected void onEnter(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData, boolean isOminous) {}
/*     */   
/*     */   protected void onExit(ServerLevel serverLevel, BlockPos pos, VaultConfig config, VaultSharedData sharedData) {}
/*     */   
/*     */   private void ejectResultItem(ServerLevel serverLevel, BlockPos pos, ItemStack itemToEject, float ejectionSoundProgress) {
/*     */     DefaultDispenseItemBehavior.spawnItem(serverLevel, itemToEject, 2, Direction.UP, Vec3.atBottomCenterOf(pos).relative(Direction.UP, 1.2D));
/*     */     serverLevel.levelEvent(3017, pos, 0);
/*     */     serverLevel.playSound(null, pos, SoundEvents.VAULT_EJECT_ITEM, SoundSource.BLOCKS, 1.0F, 0.8F + 0.4F * ejectionSoundProgress);
/*     */   }
/*     */   
/* 122 */   INACTIVE(12), ACTIVE(12), UNLOCKING(12), EJECTING(12); private enum LightLevel { HALF_LIT(6), LIT(12);
/*     */ 
/*     */     
/*     */     final int value;
/*     */ 
/*     */     
/* 128 */     LightLevel(int value) { this.value = value; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */