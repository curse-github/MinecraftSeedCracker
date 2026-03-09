/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
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
/*     */ public final class Client
/*     */ {
/*     */   private static final int PARTICLE_TICK_RATE = 20;
/*     */   private static final float IDLE_PARTICLE_CHANCE = 0.5F;
/*     */   private static final float AMBIENT_SOUND_CHANCE = 0.02F;
/*     */   private static final int ACTIVATION_PARTICLE_COUNT = 20;
/*     */   private static final int DEACTIVATION_PARTICLE_COUNT = 20;
/*     */   
/*     */   public static void tick(Level clientLevel, BlockPos pos, BlockState blockState, VaultClientData clientData, VaultSharedData sharedData) {
/* 250 */     clientData.updateDisplayItemSpin();
/*     */     
/* 252 */     if (clientLevel.getGameTime() % 20L == 0L) {
/* 253 */       emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData);
/*     */     }
/*     */     
/* 256 */     emitIdleParticles(clientLevel, pos, sharedData, ((Boolean)blockState.getValue(VaultBlock.OMINOUS)).booleanValue() ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME);
/* 257 */     playIdleSounds(clientLevel, pos, sharedData);
/*     */   }
/*     */   
/*     */   public static void emitActivationParticles(Level clientLevel, BlockPos pos, BlockState blockState, VaultSharedData sharedData, ParticleOptions flameParticle) {
/* 261 */     emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData);
/* 262 */     RandomSource random = clientLevel.random;
/* 263 */     for (int i = 0; i < 20; i++) {
/* 264 */       Vec3 particlePos = randomPosInsideCage(pos, random);
/* 265 */       clientLevel.addParticle(ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/* 266 */       clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void emitDeactivationParticles(Level clientLevel, BlockPos pos, ParticleOptions flameParticle) {
/* 271 */     RandomSource random = clientLevel.random;
/* 272 */     for (int i = 0; i < 20; i++) {
/* 273 */       Vec3 particlePos = randomPosCenterOfCage(pos, random);
/* 274 */       Vec3 dir = new Vec3(random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D);
/* 275 */       clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), dir.x(), dir.y(), dir.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitIdleParticles(Level clientLevel, BlockPos pos, VaultSharedData sharedData, ParticleOptions flameParticle) {
/* 280 */     RandomSource random = clientLevel.getRandom();
/* 281 */     if (random.nextFloat() <= 0.5F) {
/* 282 */       Vec3 particlePos = randomPosInsideCage(pos, random);
/* 283 */       clientLevel.addParticle(ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/* 284 */       if (shouldDisplayActiveEffects(sharedData)) {
/* 285 */         clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitConnectionParticlesForPlayer(Level level, Vec3 flyTowards, Player player) {
/* 291 */     RandomSource random = level.random;
/* 292 */     Vec3 direction = flyTowards.vectorTo(player.position().add(0.0D, (player.getBbHeight() / 2.0F), 0.0D));
/* 293 */     int particleCount = Mth.nextInt(random, 2, 5);
/* 294 */     for (int i = 0; i < particleCount; i++) {
/* 295 */       Vec3 randomDirection = direction.offsetRandom(random, 1.0F);
/* 296 */       level.addParticle(ParticleTypes.VAULT_CONNECTION, flyTowards.x(), flyTowards.y(), flyTowards.z(), randomDirection.x(), randomDirection.y(), randomDirection.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitConnectionParticlesForNearbyPlayers(Level level, BlockPos pos, BlockState blockState, VaultSharedData sharedData) {
/* 301 */     Set<UUID> connectedPlayers = sharedData.getConnectedPlayers();
/*     */     
/* 303 */     if (connectedPlayers.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 307 */     Vec3 keyholePos = keyholePos(pos, (Direction)blockState.getValue(VaultBlock.FACING));
/*     */     
/* 309 */     for (UUID uuid : connectedPlayers) {
/* 310 */       Player player = level.getPlayerByUUID(uuid);
/* 311 */       if (player == null || !isWithinConnectionRange(pos, sharedData, player)) {
/*     */         continue;
/*     */       }
/*     */       
/* 315 */       emitConnectionParticlesForPlayer(level, keyholePos, player);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 320 */   private static boolean isWithinConnectionRange(BlockPos vaultPos, VaultSharedData sharedData, Player player) { return (player.blockPosition().distSqr(vaultPos) <= Mth.square(sharedData.connectedParticlesRange())); }
/*     */ 
/*     */   
/*     */   private static void playIdleSounds(Level clientLevel, BlockPos pos, VaultSharedData sharedData) {
/* 324 */     if (!shouldDisplayActiveEffects(sharedData)) {
/*     */       return;
/*     */     }
/*     */     
/* 328 */     RandomSource random = clientLevel.getRandom();
/* 329 */     if (random.nextFloat() <= 0.02F) {
/* 330 */       clientLevel.playLocalSound(pos, SoundEvents.VAULT_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 335 */   public static boolean shouldDisplayActiveEffects(VaultSharedData sharedData) { return sharedData.hasDisplayItem(); }
/*     */ 
/*     */ 
/*     */   
/* 339 */   private static Vec3 randomPosCenterOfCage(BlockPos blockPos, RandomSource random) { return Vec3.atLowerCornerOf(blockPos).add(Mth.nextDouble(random, 0.4D, 0.6D), Mth.nextDouble(random, 0.4D, 0.6D), Mth.nextDouble(random, 0.4D, 0.6D)); }
/*     */ 
/*     */ 
/*     */   
/* 343 */   private static Vec3 randomPosInsideCage(BlockPos blockPos, RandomSource random) { return Vec3.atLowerCornerOf(blockPos).add(Mth.nextDouble(random, 0.1D, 0.9D), Mth.nextDouble(random, 0.25D, 0.75D), Mth.nextDouble(random, 0.1D, 0.9D)); }
/*     */ 
/*     */ 
/*     */   
/* 347 */   private static Vec3 keyholePos(BlockPos blockPos, Direction blockFacing) { return Vec3.atBottomCenterOf(blockPos).add(blockFacing.getStepX() * 0.5D, 1.75D, blockFacing.getStepZ() * 0.5D); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity$Client.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */