/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConduitBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private static final int BLOCK_REFRESH_RATE = 2;
/*     */   private static final int EFFECT_DURATION = 13;
/*     */   private static final float ROTATION_SPEED = -0.0375F;
/*     */   private static final int MIN_ACTIVE_SIZE = 16;
/*     */   private static final int MIN_KILL_SIZE = 42;
/*     */   private static final int KILL_RANGE = 8;
/*  46 */   private static final Block[] VALID_BLOCKS = { Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE };
/*     */   
/*     */   public int tickCount;
/*     */   
/*     */   private float activeRotation;
/*     */   private boolean isActive;
/*     */   private boolean isHunting;
/*  53 */   private final List<BlockPos> effectBlocks = Lists.newArrayList();
/*     */   
/*     */   private EntityReference<LivingEntity> destroyTarget;
/*     */   
/*     */   private long nextAmbientSoundActivation;
/*     */   
/*  59 */   public ConduitBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.CONDUIT, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  64 */     super.loadAdditional(input);
/*  65 */     this.destroyTarget = EntityReference.read(input, "Target");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  70 */     super.saveAdditional(output);
/*  71 */     EntityReference.store(this.destroyTarget, output, "Target");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */ 
/*     */   
/*     */   public static void clientTick(Level level, BlockPos pos, BlockState state, ConduitBlockEntity entity) {
/*  85 */     entity.tickCount++;
/*     */     
/*  87 */     long gameTime = level.getGameTime();
/*     */     
/*  89 */     List<BlockPos> effectBlocks = entity.effectBlocks;
/*  90 */     if (gameTime % 40L == 0L) {
/*  91 */       entity.isActive = updateShape(level, pos, effectBlocks);
/*  92 */       updateHunting(entity, effectBlocks);
/*     */     } 
/*     */     
/*  95 */     LivingEntity destroyTarget = EntityReference.getLivingEntity(entity.destroyTarget, level);
/*  96 */     animationTick(level, pos, effectBlocks, destroyTarget, entity.tickCount);
/*  97 */     if (entity.isActive()) {
/*  98 */       entity.activeRotation++;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void serverTick(Level level, BlockPos pos, BlockState state, ConduitBlockEntity entity) {
/* 103 */     entity.tickCount++;
/*     */     
/* 105 */     long gameTime = level.getGameTime();
/*     */     
/* 107 */     List<BlockPos> effectBlocks = entity.effectBlocks;
/* 108 */     if (gameTime % 40L == 0L) {
/* 109 */       boolean active = updateShape(level, pos, effectBlocks);
/* 110 */       if (active != entity.isActive) {
/* 111 */         SoundEvent event = active ? SoundEvents.CONDUIT_ACTIVATE : SoundEvents.CONDUIT_DEACTIVATE;
/* 112 */         level.playSound(null, pos, event, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/* 114 */       entity.isActive = active;
/* 115 */       updateHunting(entity, effectBlocks);
/*     */       
/* 117 */       if (active) {
/* 118 */         applyEffects(level, pos, effectBlocks);
/* 119 */         updateAndAttackTarget((ServerLevel)level, pos, state, entity, (effectBlocks.size() >= 42));
/*     */       } 
/*     */     } 
/*     */     
/* 123 */     if (entity.isActive()) {
/* 124 */       if (gameTime % 80L == 0L) {
/* 125 */         level.playSound(null, pos, SoundEvents.CONDUIT_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 128 */       if (gameTime > entity.nextAmbientSoundActivation) {
/* 129 */         entity.nextAmbientSoundActivation = gameTime + 60L + level.getRandom().nextInt(40);
/* 130 */         level.playSound(null, pos, SoundEvents.CONDUIT_AMBIENT_SHORT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 136 */   private static void updateHunting(ConduitBlockEntity entity, List<BlockPos> effectBlocks) { entity.setHunting((effectBlocks.size() >= 42)); }
/*     */ 
/*     */   
/*     */   private static boolean updateShape(Level level, BlockPos worldPosition, List<BlockPos> effectBlocks) {
/* 140 */     effectBlocks.clear();
/*     */ 
/*     */     
/* 143 */     for (int ox = -1; ox <= 1; ox++) {
/* 144 */       for (int oy = -1; oy <= 1; oy++) {
/* 145 */         for (int oz = -1; oz <= 1; oz++) {
/* 146 */           BlockPos testPos = worldPosition.offset(ox, oy, oz);
/* 147 */           if (!level.isWaterAt(testPos)) {
/* 148 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 155 */     for (int ox = -2; ox <= 2; ox++) {
/* 156 */       for (int oy = -2; oy <= 2; oy++) {
/* 157 */         for (int oz = -2; oz <= 2; oz++) {
/* 158 */           int ax = Math.abs(ox);
/* 159 */           int ay = Math.abs(oy);
/* 160 */           int az = Math.abs(oz);
/* 161 */           if (ax > 1 || ay > 1 || az > 1)
/*     */           {
/*     */             
/* 164 */             if ((ox == 0 && (ay == 2 || az == 2)) || (oy == 0 && (ax == 2 || az == 2)) || (oz == 0 && (ax == 2 || ay == 2))) {
/* 165 */               BlockPos testPos = worldPosition.offset(ox, oy, oz);
/* 166 */               BlockState testBlock = level.getBlockState(testPos);
/* 167 */               for (Block type : VALID_BLOCKS) {
/* 168 */                 if (testBlock.is(type)) {
/* 169 */                   effectBlocks.add(testPos);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 177 */     return (effectBlocks.size() >= 16);
/*     */   }
/*     */   
/*     */   private static void applyEffects(Level level, BlockPos worldPosition, List<BlockPos> effectBlocks) {
/* 181 */     int activeSize = effectBlocks.size();
/* 182 */     int effectRange = activeSize / 7 * 16;
/*     */ 
/*     */     
/* 185 */     int x = worldPosition.getX();
/* 186 */     int y = worldPosition.getY();
/* 187 */     int z = worldPosition.getZ();
/* 188 */     AABB bb = (new AABB(x, y, z, (x + 1), (y + 1), (z + 1))).inflate(effectRange).expandTowards(0.0D, level.getHeight(), 0.0D);
/* 189 */     List<Player> players = level.getEntitiesOfClass(Player.class, bb);
/*     */     
/* 191 */     if (players.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 195 */     for (Player player : players) {
/* 196 */       if (worldPosition.closerThan(player.blockPosition(), effectRange) && player.isInWaterOrRain()) {
/* 197 */         player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void updateAndAttackTarget(ServerLevel level, BlockPos worldPosition, BlockState blockState, ConduitBlockEntity entity, boolean isActive) {
/* 203 */     EntityReference<LivingEntity> newDestroyTarget = updateDestroyTarget(entity.destroyTarget, level, worldPosition, isActive);
/*     */     
/* 205 */     LivingEntity targetEntity = EntityReference.getLivingEntity(newDestroyTarget, level);
/* 206 */     if (targetEntity != null) {
/* 207 */       level.playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 208 */       targetEntity.hurtServer(level, level.damageSources().magic(), 4.0F);
/*     */     } 
/*     */     
/* 211 */     if (!Objects.equals(newDestroyTarget, entity.destroyTarget)) {
/* 212 */       entity.destroyTarget = newDestroyTarget;
/* 213 */       level.sendBlockUpdated(worldPosition, blockState, blockState, 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static EntityReference<LivingEntity> updateDestroyTarget(EntityReference<LivingEntity> target, ServerLevel level, BlockPos pos, boolean isActive) {
/* 218 */     if (!isActive) {
/* 219 */       return null;
/*     */     }
/* 221 */     if (target == null) {
/* 222 */       return selectNewTarget(level, pos);
/*     */     }
/* 224 */     LivingEntity targetEntity = EntityReference.getLivingEntity(target, level);
/* 225 */     if (targetEntity == null || !targetEntity.isAlive() || !pos.closerThan(targetEntity.blockPosition(), 8.0D))
/*     */     {
/* 227 */       return null;
/*     */     }
/* 229 */     return target;
/*     */   }
/*     */   
/*     */   private static EntityReference<LivingEntity> selectNewTarget(ServerLevel level, BlockPos pos) {
/* 233 */     List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class, getDestroyRangeAABB(pos), input -> (input instanceof net.minecraft.world.entity.monster.Enemy && input.isInWaterOrRain()));
/* 234 */     if (candidates.isEmpty()) {
/* 235 */       return null;
/*     */     }
/* 237 */     return EntityReference.of((LivingEntity)Util.getRandom(candidates, level.random));
/*     */   }
/*     */ 
/*     */   
/* 241 */   private static AABB getDestroyRangeAABB(BlockPos worldPosition) { return (new AABB(worldPosition)).inflate(8.0D); }
/*     */ 
/*     */   
/*     */   private static void animationTick(Level level, BlockPos worldPosition, List<BlockPos> effectBlocks, Entity destroyTarget, int tickCount) {
/* 245 */     RandomSource random = level.random;
/*     */     
/* 247 */     double hh = (Mth.sin(((tickCount + 35) * 0.1F)) / 2.0F + 0.5F);
/* 248 */     hh = (hh * hh + hh) * 0.30000001192092896D;
/*     */     
/* 250 */     Vec3 particleEnd = new Vec3(worldPosition.getX() + 0.5D, worldPosition.getY() + 1.5D + hh, worldPosition.getZ() + 0.5D);
/* 251 */     for (BlockPos pos : effectBlocks) {
/* 252 */       if (random.nextInt(50) != 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 256 */       BlockPos delta = pos.subtract(worldPosition);
/* 257 */       float dx = -0.5F + random.nextFloat() + delta.getX();
/* 258 */       float dy = -2.0F + random.nextFloat() + delta.getY();
/* 259 */       float dz = -0.5F + random.nextFloat() + delta.getZ();
/* 260 */       level.addParticle(ParticleTypes.NAUTILUS, particleEnd.x, particleEnd.y, particleEnd.z, dx, dy, dz);
/*     */     } 
/*     */     
/* 263 */     if (destroyTarget != null) {
/* 264 */       Vec3 targetPosition = new Vec3(destroyTarget.getX(), destroyTarget.getEyeY(), destroyTarget.getZ());
/* 265 */       float randx = (-0.5F + random.nextFloat()) * (3.0F + destroyTarget.getBbWidth());
/* 266 */       float randy = -1.0F + random.nextFloat() * destroyTarget.getBbHeight();
/* 267 */       float randz = (-0.5F + random.nextFloat()) * (3.0F + destroyTarget.getBbWidth());
/* 268 */       Vec3 velocity = new Vec3(randx, randy, randz);
/* 269 */       level.addParticle(ParticleTypes.NAUTILUS, targetPosition.x, targetPosition.y, targetPosition.z, velocity.x, velocity.y, velocity.z);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 274 */   public boolean isActive() { return this.isActive; }
/*     */ 
/*     */ 
/*     */   
/* 278 */   public boolean isHunting() { return this.isHunting; }
/*     */ 
/*     */ 
/*     */   
/* 282 */   private void setHunting(boolean hunting) { this.isHunting = hunting; }
/*     */ 
/*     */ 
/*     */   
/* 286 */   public float getActiveRotation(float a) { return (this.activeRotation + a) * -0.0375F; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ConduitBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */