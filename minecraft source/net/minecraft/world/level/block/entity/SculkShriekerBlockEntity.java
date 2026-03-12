/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.SpawnUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.warden.Warden;
/*     */ import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.SculkShriekerBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.BlockPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SculkShriekerBlockEntity
/*     */   extends BlockEntity
/*     */   implements GameEventListener.Provider<VibrationSystem.Listener>, VibrationSystem {
/*     */   private static final int WARNING_SOUND_RADIUS = 10;
/*     */   private static final int WARDEN_SPAWN_ATTEMPTS = 20;
/*     */   private static final int WARDEN_SPAWN_RANGE_XZ = 5;
/*     */   private static final int WARDEN_SPAWN_RANGE_Y = 6;
/*     */   private static final int DARKNESS_RADIUS = 40;
/*     */   private static final int SHRIEKING_TICKS = 90;
/*  50 */   private static final Int2ObjectMap<SoundEvent> SOUND_BY_LEVEL = (Int2ObjectMap)Util.make(new Int2ObjectOpenHashMap(), map -> {
/*  51 */         map.put(1, SoundEvents.WARDEN_NEARBY_CLOSE);
/*  52 */         map.put(2, SoundEvents.WARDEN_NEARBY_CLOSER);
/*  53 */         map.put(3, SoundEvents.WARDEN_NEARBY_CLOSEST);
/*  54 */         map.put(4, SoundEvents.WARDEN_LISTENING_ANGRY);
/*     */       });
/*     */   
/*     */   private static final int DEFAULT_WARNING_LEVEL = 0;
/*  58 */   private int warningLevel = 0;
/*     */   private final VibrationSystem.User vibrationUser;
/*     */   private VibrationSystem.Data vibrationData;
/*     */   private final VibrationSystem.Listener vibrationListener;
/*     */   
/*     */   public SculkShriekerBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  64 */     super(BlockEntityType.SCULK_SHRIEKER, worldPosition, blockState);
/*  65 */     this.vibrationUser = new VibrationUser();
/*  66 */     this.vibrationData = new VibrationSystem.Data();
/*  67 */     this.vibrationListener = new VibrationSystem.Listener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public VibrationSystem.Data getVibrationData() { return this.vibrationData; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public VibrationSystem.User getVibrationUser() { return this.vibrationUser; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  82 */     super.loadAdditional(input);
/*     */     
/*  84 */     this.warningLevel = input.getIntOr("warning_level", 0);
/*     */     
/*  86 */     this.vibrationData = (VibrationSystem.Data)input.read("listener", VibrationSystem.Data.CODEC).orElseGet(net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data::new);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  91 */     super.saveAdditional(output);
/*     */     
/*  93 */     output.putInt("warning_level", this.warningLevel);
/*     */     
/*  95 */     output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ServerPlayer tryGetPlayer(Entity sourceEntity) {
/* 100 */     if (sourceEntity instanceof ServerPlayer) return (ServerPlayer)sourceEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (sourceEntity != null) { LivingEntity livingEntity = sourceEntity.getControllingPassenger(); if (livingEntity instanceof ServerPlayer) return (ServerPlayer)livingEntity;
/*     */        }
/*     */ 
/*     */     
/* 109 */     if (sourceEntity instanceof Projectile) { Projectile projectile = (Projectile)sourceEntity; Entity entity = projectile.getOwner(); if (entity instanceof ServerPlayer) return (ServerPlayer)entity;
/*     */        }
/*     */ 
/*     */     
/* 113 */     if (sourceEntity instanceof ItemEntity) { ItemEntity item = (ItemEntity)sourceEntity; Entity entity = item.getOwner(); if (entity instanceof ServerPlayer) return (ServerPlayer)entity;
/*     */        }
/*     */ 
/*     */     
/* 117 */     return null;
/*     */   }
/*     */   
/*     */   public void tryShriek(ServerLevel level, ServerPlayer player) {
/* 121 */     if (player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     BlockState state = getBlockState();
/* 126 */     if (((Boolean)state.getValue(SculkShriekerBlock.SHRIEKING)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     this.warningLevel = 0;
/* 131 */     if (canRespond(level) && !tryToWarn(level, player)) {
/*     */       return;
/*     */     }
/*     */     
/* 135 */     shriek(level, player);
/*     */   }
/*     */   
/*     */   private boolean tryToWarn(ServerLevel level, ServerPlayer player) {
/* 139 */     OptionalInt maybeWarningLevel = WardenSpawnTracker.tryWarn(level, getBlockPos(), player);
/* 140 */     maybeWarningLevel.ifPresent(warningLevel -> this.warningLevel = warningLevel);
/* 141 */     return maybeWarningLevel.isPresent();
/*     */   }
/*     */   
/*     */   private void shriek(ServerLevel level, Entity sourceEntity) {
/* 145 */     BlockPos pos = getBlockPos();
/* 146 */     BlockState state = getBlockState();
/* 147 */     level.setBlock(pos, (BlockState)state.setValue(SculkShriekerBlock.SHRIEKING, Boolean.valueOf(true)), 2);
/* 148 */     level.scheduleTick(pos, state.getBlock(), 90);
/* 149 */     level.levelEvent(3007, pos, 0);
/* 150 */     level.gameEvent(GameEvent.SHRIEK, pos, GameEvent.Context.of(sourceEntity));
/*     */   }
/*     */   
/*     */   private boolean canRespond(ServerLevel level) {
/* 154 */     return (((Boolean)getBlockState().getValue(SculkShriekerBlock.CAN_SUMMON)).booleanValue() && level
/* 155 */       .getDifficulty() != Difficulty.PEACEFUL && ((Boolean)level
/* 156 */       .getGameRules().get(GameRules.SPAWN_WARDENS)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 161 */     if (((Boolean)state.getValue(SculkShriekerBlock.SHRIEKING)).booleanValue()) { Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*     */         
/* 163 */         tryRespond(serverLevel); }
/*     */        }
/*     */   
/*     */   }
/*     */   public void tryRespond(ServerLevel level) {
/* 168 */     if (canRespond(level) && this.warningLevel > 0) {
/* 169 */       if (!trySummonWarden(level)) {
/* 170 */         playWardenReplySound(level);
/*     */       }
/*     */       
/* 173 */       Warden.applyDarknessAround(level, Vec3.atCenterOf(getBlockPos()), null, 40);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playWardenReplySound(Level level) {
/* 178 */     SoundEvent sound = (SoundEvent)SOUND_BY_LEVEL.get(this.warningLevel);
/* 179 */     if (sound != null) {
/* 180 */       BlockPos pos = getBlockPos();
/* 181 */       int x = pos.getX() + Mth.randomBetweenInclusive(level.random, -10, 10);
/* 182 */       int y = pos.getY() + Mth.randomBetweenInclusive(level.random, -10, 10);
/* 183 */       int z = pos.getZ() + Mth.randomBetweenInclusive(level.random, -10, 10);
/*     */       
/* 185 */       level.playSound(null, x, y, z, sound, SoundSource.HOSTILE, 5.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean trySummonWarden(ServerLevel level) {
/* 190 */     if (this.warningLevel < 4) {
/* 191 */       return false;
/*     */     }
/*     */     
/* 194 */     return SpawnUtil.trySpawnMob(EntityType.WARDEN, EntitySpawnReason.TRIGGERED, level, getBlockPos(), 20, 5, 6, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER, false).isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public VibrationSystem.Listener getListener() { return this.vibrationListener; }
/*     */ 
/*     */   
/*     */   private class VibrationUser
/*     */     implements VibrationSystem.User
/*     */   {
/*     */     private static final int LISTENER_RADIUS = 8;
/*     */     
/* 207 */     private final PositionSource positionSource = new BlockPositionSource(SculkShriekerBlockEntity.this.worldPosition);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     public int getListenerRadius() { return 8; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     public PositionSource getPositionSource() { return this.positionSource; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     public TagKey<GameEvent> getListenableEvents() { return GameEventTags.SHRIEKER_CAN_LISTEN; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 227 */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) { return (!((Boolean)SculkShriekerBlockEntity.this.getBlockState().getValue(SculkShriekerBlock.SHRIEKING)).booleanValue() && SculkShriekerBlockEntity.tryGetPlayer(context.sourceEntity()) != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) { SculkShriekerBlockEntity.this.tryShriek(level, SculkShriekerBlockEntity.tryGetPlayer((projectileOwner != null) ? projectileOwner : sourceEntity)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     public void onDataChanged() { SculkShriekerBlockEntity.this.setChanged(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     public boolean requiresAdjacentChunksToBeTicking() { return true; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\SculkShriekerBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */