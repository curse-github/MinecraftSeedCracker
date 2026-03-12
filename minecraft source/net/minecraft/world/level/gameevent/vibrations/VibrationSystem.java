/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ClipBlockStateContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.HitResult;
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
/*     */ public interface VibrationSystem
/*     */ {
/*  51 */   public static final List<ResourceKey<GameEvent>> RESONANCE_EVENTS = List.of(new ResourceKey[] { GameEvent.RESONATE_1
/*  52 */         .key(), GameEvent.RESONATE_2.key(), GameEvent.RESONATE_3.key(), GameEvent.RESONATE_4.key(), GameEvent.RESONATE_5.key(), GameEvent.RESONATE_6
/*  53 */         .key(), GameEvent.RESONATE_7.key(), GameEvent.RESONATE_8.key(), GameEvent.RESONATE_9.key(), GameEvent.RESONATE_10.key(), GameEvent.RESONATE_11
/*  54 */         .key(), GameEvent.RESONATE_12.key(), GameEvent.RESONATE_13.key(), GameEvent.RESONATE_14.key(), GameEvent.RESONATE_15.key() });
/*     */ 
/*     */   
/*     */   public static final int NO_VIBRATION_FREQUENCY = 0;
/*     */   
/*  59 */   public static final ToIntFunction<ResourceKey<GameEvent>> VIBRATION_FREQUENCY_FOR_EVENT = (ToIntFunction)Util.make(new Reference2IntOpenHashMap(), map -> {
/*  60 */         map.defaultReturnValue(0);
/*     */ 
/*     */         
/*  63 */         map.put(GameEvent.STEP.key(), 1);
/*  64 */         map.put(GameEvent.SWIM.key(), 1);
/*  65 */         map.put(GameEvent.FLAP.key(), 1);
/*     */ 
/*     */         
/*  68 */         map.put(GameEvent.PROJECTILE_LAND.key(), 2);
/*  69 */         map.put(GameEvent.HIT_GROUND.key(), 2);
/*  70 */         map.put(GameEvent.SPLASH.key(), 2);
/*     */ 
/*     */         
/*  73 */         map.put(GameEvent.ITEM_INTERACT_FINISH.key(), 3);
/*  74 */         map.put(GameEvent.PROJECTILE_SHOOT.key(), 3);
/*  75 */         map.put(GameEvent.INSTRUMENT_PLAY.key(), 3);
/*     */ 
/*     */         
/*  78 */         map.put(GameEvent.ENTITY_ACTION.key(), 4);
/*  79 */         map.put(GameEvent.ELYTRA_GLIDE.key(), 4);
/*  80 */         map.put(GameEvent.UNEQUIP.key(), 4);
/*     */ 
/*     */         
/*  83 */         map.put(GameEvent.ENTITY_DISMOUNT.key(), 5);
/*  84 */         map.put(GameEvent.EQUIP.key(), 5);
/*     */ 
/*     */         
/*  87 */         map.put(GameEvent.ENTITY_INTERACT.key(), 6);
/*  88 */         map.put(GameEvent.SHEAR.key(), 6);
/*  89 */         map.put(GameEvent.ENTITY_MOUNT.key(), 6);
/*     */ 
/*     */         
/*  92 */         map.put(GameEvent.ENTITY_DAMAGE.key(), 7);
/*     */ 
/*     */         
/*  95 */         map.put(GameEvent.DRINK.key(), 8);
/*  96 */         map.put(GameEvent.EAT.key(), 8);
/*     */ 
/*     */         
/*  99 */         map.put(GameEvent.CONTAINER_CLOSE.key(), 9);
/* 100 */         map.put(GameEvent.BLOCK_CLOSE.key(), 9);
/* 101 */         map.put(GameEvent.BLOCK_DEACTIVATE.key(), 9);
/* 102 */         map.put(GameEvent.BLOCK_DETACH.key(), 9);
/*     */ 
/*     */         
/* 105 */         map.put(GameEvent.CONTAINER_OPEN.key(), 10);
/* 106 */         map.put(GameEvent.BLOCK_OPEN.key(), 10);
/* 107 */         map.put(GameEvent.BLOCK_ACTIVATE.key(), 10);
/* 108 */         map.put(GameEvent.BLOCK_ATTACH.key(), 10);
/* 109 */         map.put(GameEvent.PRIME_FUSE.key(), 10);
/* 110 */         map.put(GameEvent.NOTE_BLOCK_PLAY.key(), 10);
/*     */ 
/*     */         
/* 113 */         map.put(GameEvent.BLOCK_CHANGE.key(), 11);
/*     */ 
/*     */         
/* 116 */         map.put(GameEvent.BLOCK_DESTROY.key(), 12);
/* 117 */         map.put(GameEvent.FLUID_PICKUP.key(), 12);
/*     */ 
/*     */         
/* 120 */         map.put(GameEvent.BLOCK_PLACE.key(), 13);
/* 121 */         map.put(GameEvent.FLUID_PLACE.key(), 13);
/*     */ 
/*     */         
/* 124 */         map.put(GameEvent.ENTITY_PLACE.key(), 14);
/* 125 */         map.put(GameEvent.LIGHTNING_STRIKE.key(), 14);
/* 126 */         map.put(GameEvent.TELEPORT.key(), 14);
/*     */ 
/*     */         
/* 129 */         map.put(GameEvent.ENTITY_DIE.key(), 15);
/* 130 */         map.put(GameEvent.EXPLODE.key(), 15);
/*     */         
/* 132 */         for (int i = 1; i <= 15; i++) {
/* 133 */           map.put(getResonanceEventByFrequency(i), i);
/*     */         }
/*     */       });
/*     */ 
/*     */   
/* 138 */   static int getGameEventFrequency(Holder<GameEvent> event) { return ((Integer)event.unwrapKey().map(VibrationSystem::getGameEventFrequency).orElse(Integer.valueOf(0))).intValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   static int getGameEventFrequency(ResourceKey<GameEvent> event) { return VIBRATION_FREQUENCY_FOR_EVENT.applyAsInt(event); }
/*     */ 
/*     */ 
/*     */   
/* 147 */   static ResourceKey<GameEvent> getResonanceEventByFrequency(int vibrationFrequency) { return (ResourceKey)RESONANCE_EVENTS.get(vibrationFrequency - 1); }
/*     */ 
/*     */   
/*     */   static int getRedstoneStrengthForDistance(float distance, int listenerRadius) {
/* 151 */     double powerScale = 15.0D / listenerRadius;
/* 152 */     return Math.max(1, 15 - Mth.floor(powerScale * distance));
/*     */   }
/*     */   
/*     */   Data getVibrationData();
/*     */   
/*     */   User getVibrationUser();
/*     */   
/*     */   public static final class Data
/*     */   {
/* 161 */     public static Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(VibrationInfo.CODEC
/* 162 */           .lenientOptionalFieldOf("event").forGetter(()), VibrationSelector.CODEC
/* 163 */           .fieldOf("selector").forGetter(Data::getSelectionStrategy), ExtraCodecs.NON_NEGATIVE_INT
/* 164 */           .fieldOf("event_delay").orElse(Integer.valueOf(0)).forGetter(Data::getTravelTimeInTicks))
/* 165 */         .apply(i, ()));
/*     */     
/*     */     public static final String NBT_TAG_KEY = "listener";
/*     */     
/*     */     private VibrationInfo currentVibration;
/*     */     private int travelTimeInTicks;
/*     */     private final VibrationSelector selectionStrategy;
/*     */     private boolean reloadVibrationParticle;
/*     */     
/*     */     private Data(VibrationInfo currentVibration, VibrationSelector selectionStrategy, int travelTimeInTicks, boolean reloadVibrationParticle) {
/* 175 */       this.currentVibration = currentVibration;
/* 176 */       this.travelTimeInTicks = travelTimeInTicks;
/* 177 */       this.selectionStrategy = selectionStrategy;
/* 178 */       this.reloadVibrationParticle = reloadVibrationParticle;
/*     */     }
/*     */ 
/*     */     
/* 182 */     public Data() { this(null, new VibrationSelector(), 0, false); }
/*     */ 
/*     */ 
/*     */     
/* 186 */     public VibrationSelector getSelectionStrategy() { return this.selectionStrategy; }
/*     */ 
/*     */ 
/*     */     
/* 190 */     public VibrationInfo getCurrentVibration() { return this.currentVibration; }
/*     */ 
/*     */ 
/*     */     
/* 194 */     public void setCurrentVibration(VibrationInfo currentVibration) { this.currentVibration = currentVibration; }
/*     */ 
/*     */ 
/*     */     
/* 198 */     public int getTravelTimeInTicks() { return this.travelTimeInTicks; }
/*     */ 
/*     */ 
/*     */     
/* 202 */     public void setTravelTimeInTicks(int travelTimeInTicks) { this.travelTimeInTicks = travelTimeInTicks; }
/*     */ 
/*     */ 
/*     */     
/* 206 */     public void decrementTravelTime() { this.travelTimeInTicks = Math.max(0, this.travelTimeInTicks - 1); }
/*     */ 
/*     */ 
/*     */     
/* 210 */     public boolean shouldReloadVibrationParticle() { return this.reloadVibrationParticle; }
/*     */ 
/*     */ 
/*     */     
/* 214 */     public void setReloadVibrationParticle(boolean reloadVibrationParticle) { this.reloadVibrationParticle = reloadVibrationParticle; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Listener
/*     */     implements GameEventListener
/*     */   {
/*     */     private final VibrationSystem system;
/*     */ 
/*     */ 
/*     */     
/* 227 */     public Listener(VibrationSystem system) { this.system = system; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     public PositionSource getListenerSource() { return this.system.getVibrationUser().getPositionSource(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     public int getListenerRadius() { return this.system.getVibrationUser().getListenerRadius(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/* 242 */       VibrationSystem.Data data = this.system.getVibrationData();
/* 243 */       VibrationSystem.User user = this.system.getVibrationUser();
/*     */ 
/*     */       
/* 246 */       if (data.getCurrentVibration() != null) {
/* 247 */         return false;
/*     */       }
/*     */       
/* 250 */       if (!user.isValidVibration(event, context)) {
/* 251 */         return false;
/*     */       }
/*     */       
/* 254 */       Optional<Vec3> listenerSourcePos = user.getPositionSource().getPosition(level);
/*     */       
/* 256 */       if (listenerSourcePos.isEmpty()) {
/* 257 */         return false;
/*     */       }
/*     */       
/* 260 */       Vec3 destination = (Vec3)listenerSourcePos.get();
/*     */ 
/*     */       
/* 263 */       if (!user.canReceiveVibration(level, BlockPos.containing(sourcePosition), event, context)) {
/* 264 */         return false;
/*     */       }
/*     */       
/* 267 */       if (isOccluded(level, sourcePosition, destination)) {
/* 268 */         return false;
/*     */       }
/*     */       
/* 271 */       scheduleVibration(level, data, event, context, sourcePosition, destination);
/*     */       
/* 273 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 277 */     public void forceScheduleVibration(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 origin) { this.system.getVibrationUser().getPositionSource().getPosition(level).ifPresent(p -> scheduleVibration(level, this.system.getVibrationData(), event, context, origin, p)); }
/*     */ 
/*     */ 
/*     */     
/* 281 */     private void scheduleVibration(ServerLevel level, VibrationSystem.Data data, Holder<GameEvent> event, GameEvent.Context context, Vec3 origin, Vec3 dest) { data.selectionStrategy.addCandidate(new VibrationInfo(event, (float)origin.distanceTo(dest), origin, context.sourceEntity()), level.getGameTime()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     public static float distanceBetweenInBlocks(BlockPos origin, BlockPos dest) { return (float)Math.sqrt(origin.distSqr(dest)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static boolean isOccluded(Level level, Vec3 origin, Vec3 dest) {
/* 306 */       Vec3 from = new Vec3(Mth.floor(origin.x) + 0.5D, Mth.floor(origin.y) + 0.5D, Mth.floor(origin.z) + 0.5D);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 311 */       Vec3 to = new Vec3(Mth.floor(dest.x) + 0.5D, Mth.floor(dest.y) + 0.5D, Mth.floor(dest.z) + 0.5D);
/*     */ 
/*     */       
/* 314 */       for (Direction direction : Direction.values()) {
/* 315 */         Vec3 nudgedSource = from.relative(direction, 9.999999747378752E-6D);
/* 316 */         if (level.isBlockInLine(new ClipBlockStateContext(nudgedSource, to, state -> state.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK) {
/* 317 */           return false;
/*     */         }
/*     */       } 
/* 320 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Ticker
/*     */   {
/*     */     static void tick(Level level, VibrationSystem.Data data, VibrationSystem.User user) {
/*     */       ServerLevel serverLevel;
/* 333 */       if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */       else
/*     */       { return; }
/*     */       
/* 337 */       if (data.currentVibration == null) {
/* 338 */         trySelectAndScheduleVibration(serverLevel, data, user);
/*     */       }
/*     */       
/* 341 */       if (data.currentVibration == null) {
/*     */         return;
/*     */       }
/*     */       
/* 345 */       boolean hasChanged = (data.getTravelTimeInTicks() > 0);
/* 346 */       tryReloadVibrationParticle(serverLevel, data, user);
/* 347 */       data.decrementTravelTime();
/*     */       
/* 349 */       if (data.getTravelTimeInTicks() <= 0) {
/* 350 */         hasChanged = receiveVibration(serverLevel, data, user, data.currentVibration);
/*     */       }
/*     */       
/* 353 */       if (hasChanged) {
/* 354 */         user.onDataChanged();
/*     */       }
/*     */     }
/*     */     
/*     */     private static void trySelectAndScheduleVibration(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user) {
/* 359 */       data.getSelectionStrategy().chosenCandidate(serverLevel.getGameTime()).ifPresent(context -> {
/* 360 */             data.setCurrentVibration(context);
/* 361 */             Vec3 origin = context.pos();
/* 362 */             data.setTravelTimeInTicks(user.calculateTravelTimeInTicks(context.distance()));
/* 363 */             serverLevel.sendParticles(new VibrationParticleOption(user.getPositionSource(), data.getTravelTimeInTicks()), origin.x, origin.y, origin.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/* 364 */             user.onDataChanged();
/* 365 */             data.getSelectionStrategy().startOver();
/*     */           });
/*     */     }
/*     */     
/*     */     private static void tryReloadVibrationParticle(ServerLevel level, VibrationSystem.Data data, VibrationSystem.User user) {
/* 370 */       if (!data.shouldReloadVibrationParticle()) {
/*     */         return;
/*     */       }
/*     */       
/* 374 */       if (data.currentVibration == null) {
/* 375 */         data.setReloadVibrationParticle(false);
/*     */         
/*     */         return;
/*     */       } 
/* 379 */       Vec3 origin = data.currentVibration.pos();
/* 380 */       PositionSource positionSource = user.getPositionSource();
/* 381 */       Vec3 destination = (Vec3)positionSource.getPosition(level).orElse(origin);
/* 382 */       int travelTimeInTicks = data.getTravelTimeInTicks();
/*     */       
/* 384 */       int initialTravelTime = user.calculateTravelTimeInTicks(data.currentVibration.distance());
/* 385 */       double alpha = 1.0D - travelTimeInTicks / initialTravelTime;
/*     */       
/* 387 */       double newInitialX = Mth.lerp(alpha, origin.x, destination.x);
/* 388 */       double newInitialY = Mth.lerp(alpha, origin.y, destination.y);
/* 389 */       double newInitialZ = Mth.lerp(alpha, origin.z, destination.z);
/*     */       
/* 391 */       boolean particleWasSent = (level.sendParticles(new VibrationParticleOption(positionSource, travelTimeInTicks), newInitialX, newInitialY, newInitialZ, 1, 0.0D, 0.0D, 0.0D, 0.0D) > 0);
/*     */       
/* 393 */       if (particleWasSent) {
/* 394 */         data.setReloadVibrationParticle(false);
/*     */       }
/*     */     }
/*     */     
/*     */     private static boolean receiveVibration(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user, VibrationInfo currentVibration) {
/* 399 */       BlockPos origin = BlockPos.containing(currentVibration.pos());
/* 400 */       BlockPos destination = (BlockPos)user.getPositionSource().getPosition(serverLevel).map(BlockPos::containing).orElse(origin);
/*     */ 
/*     */ 
/*     */       
/* 404 */       if (user.requiresAdjacentChunksToBeTicking() && !areAdjacentChunksTicking(serverLevel, destination)) {
/* 405 */         return false;
/*     */       }
/*     */       
/* 408 */       user.onReceiveVibration(serverLevel, origin, currentVibration
/*     */ 
/*     */           
/* 411 */           .gameEvent(), (Entity)currentVibration
/* 412 */           .getEntity(serverLevel).orElse(null), (Entity)currentVibration
/* 413 */           .getProjectileOwner(serverLevel).orElse(null), 
/* 414 */           VibrationSystem.Listener.distanceBetweenInBlocks(origin, destination));
/*     */ 
/*     */ 
/*     */       
/* 418 */       data.setCurrentVibration(null);
/* 419 */       return true;
/*     */     }
/*     */     
/*     */     private static boolean areAdjacentChunksTicking(Level level, BlockPos listenerPos) {
/* 423 */       ChunkPos listenerChunkPos = new ChunkPos(listenerPos);
/*     */       
/* 425 */       for (int x = listenerChunkPos.x - 1; x <= listenerChunkPos.x + 1; x++) {
/* 426 */         for (int z = listenerChunkPos.z - 1; z <= listenerChunkPos.z + 1; z++) {
/* 427 */           if (!level.shouldTickBlocksAt(ChunkPos.asLong(x, z)) || level.getChunkSource().getChunkNow(x, z) == null) {
/* 428 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 433 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface User
/*     */   {
/*     */     int getListenerRadius();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PositionSource getPositionSource();
/*     */ 
/*     */ 
/*     */     
/*     */     boolean canReceiveVibration(ServerLevel param1ServerLevel, BlockPos param1BlockPos, Holder<GameEvent> param1Holder, GameEvent.Context param1Context);
/*     */ 
/*     */ 
/*     */     
/*     */     void onReceiveVibration(ServerLevel param1ServerLevel, BlockPos param1BlockPos, Holder<GameEvent> param1Holder, Entity param1Entity1, Entity param1Entity2, float param1Float);
/*     */ 
/*     */ 
/*     */     
/* 459 */     default TagKey<GameEvent> getListenableEvents() { return GameEventTags.VIBRATIONS; }
/*     */ 
/*     */ 
/*     */     
/* 463 */     default boolean canTriggerAvoidVibration() { return false; }
/*     */ 
/*     */ 
/*     */     
/* 467 */     default boolean requiresAdjacentChunksToBeTicking() { return false; }
/*     */ 
/*     */ 
/*     */     
/* 471 */     default int calculateTravelTimeInTicks(float distanceToDestination) { return Mth.floor(distanceToDestination); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default boolean isValidVibration(Holder<GameEvent> event, GameEvent.Context context) {
/* 487 */       if (!event.is(getListenableEvents())) {
/* 488 */         return false;
/*     */       }
/*     */       
/* 491 */       Entity sourceEntity = context.sourceEntity();
/*     */       
/* 493 */       if (sourceEntity != null) {
/* 494 */         if (sourceEntity.isSpectator()) {
/* 495 */           return false;
/*     */         }
/*     */         
/* 498 */         if (sourceEntity.isSteppingCarefully() && event.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
/* 499 */           if (canTriggerAvoidVibration() && sourceEntity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)sourceEntity;
/* 500 */             CriteriaTriggers.AVOID_VIBRATION.trigger(player); }
/*     */ 
/*     */           
/* 503 */           return false;
/*     */         } 
/*     */         
/* 506 */         if (sourceEntity.dampensVibrations()) {
/* 507 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 511 */       if (context.affectedState() != null) {
/* 512 */         return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
/*     */       }
/*     */       
/* 515 */       return true;
/*     */     }
/*     */     
/*     */     default void onDataChanged() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */