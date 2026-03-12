/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WardenSpawnTracker {
/*  20 */   public static final Codec<WardenSpawnTracker> CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/*  21 */         .fieldOf("ticks_since_last_warning").orElse(Integer.valueOf(0)).forGetter(()), ExtraCodecs.NON_NEGATIVE_INT
/*  22 */         .fieldOf("warning_level").orElse(Integer.valueOf(0)).forGetter(()), ExtraCodecs.NON_NEGATIVE_INT
/*  23 */         .fieldOf("cooldown_ticks").orElse(Integer.valueOf(0)).forGetter(()))
/*  24 */       .apply(i, WardenSpawnTracker::new));
/*     */   
/*     */   public static final int MAX_WARNING_LEVEL = 4;
/*     */   
/*     */   private static final double PLAYER_SEARCH_RADIUS = 16.0D;
/*     */   
/*     */   private static final int WARNING_CHECK_DIAMETER = 48;
/*     */   private static final int DECREASE_WARNING_LEVEL_EVERY_INTERVAL = 12000;
/*     */   private static final int WARNING_LEVEL_INCREASE_COOLDOWN = 200;
/*     */   private int ticksSinceLastWarning;
/*     */   private int warningLevel;
/*     */   private int cooldownTicks;
/*     */   
/*     */   public WardenSpawnTracker(int ticksSinceLastWarning, int warningLevel, int cooldownTicks) {
/*  38 */     this.ticksSinceLastWarning = ticksSinceLastWarning;
/*  39 */     this.warningLevel = warningLevel;
/*  40 */     this.cooldownTicks = cooldownTicks;
/*     */   }
/*     */ 
/*     */   
/*  44 */   public WardenSpawnTracker() { this(0, 0, 0); }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  48 */     if (this.ticksSinceLastWarning >= 12000) {
/*  49 */       decreaseWarningLevel();
/*  50 */       this.ticksSinceLastWarning = 0;
/*     */     } else {
/*  52 */       this.ticksSinceLastWarning++;
/*     */     } 
/*     */     
/*  55 */     if (this.cooldownTicks > 0) {
/*  56 */       this.cooldownTicks--;
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/*  61 */     this.ticksSinceLastWarning = 0;
/*  62 */     this.warningLevel = 0;
/*  63 */     this.cooldownTicks = 0;
/*     */   }
/*     */   
/*     */   public static OptionalInt tryWarn(ServerLevel level, BlockPos pos, ServerPlayer triggerPlayer) {
/*  67 */     if (hasNearbyWarden(level, pos)) {
/*  68 */       return OptionalInt.empty();
/*     */     }
/*     */     
/*  71 */     List<ServerPlayer> players = getNearbyPlayers(level, pos);
/*     */     
/*  73 */     if (!players.contains(triggerPlayer)) {
/*  74 */       players.add(triggerPlayer);
/*     */     }
/*     */ 
/*     */     
/*  78 */     if (players.stream().anyMatch(player -> ((Boolean)player.getWardenSpawnTracker().map(WardenSpawnTracker::onCooldown).orElse(Boolean.valueOf(false))).booleanValue())) {
/*  79 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     Optional<WardenSpawnTracker> highestWarningSpawnTracker = players.stream().flatMap(player -> player.getWardenSpawnTracker().stream()).max(Comparator.comparingInt(WardenSpawnTracker::getWarningLevel));
/*     */     
/*  87 */     if (highestWarningSpawnTracker.isPresent()) {
/*  88 */       WardenSpawnTracker spawnTracker = (WardenSpawnTracker)highestWarningSpawnTracker.get();
/*     */       
/*  90 */       spawnTracker.increaseWarningLevel();
/*     */ 
/*     */       
/*  93 */       players.forEach(player -> player.getWardenSpawnTracker().ifPresent(()));
/*     */       
/*  95 */       return OptionalInt.of(spawnTracker.warningLevel);
/*     */     } 
/*  97 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 102 */   private boolean onCooldown() { return (this.cooldownTicks > 0); }
/*     */ 
/*     */   
/*     */   private static boolean hasNearbyWarden(ServerLevel level, BlockPos pos) {
/* 106 */     AABB areaToCheck = AABB.ofSize(Vec3.atCenterOf(pos), 48.0D, 48.0D, 48.0D);
/* 107 */     return !level.getEntitiesOfClass(Warden.class, areaToCheck).isEmpty();
/*     */   }
/*     */   
/*     */   private static List<ServerPlayer> getNearbyPlayers(ServerLevel level, BlockPos pos) {
/* 111 */     Vec3 origin = Vec3.atCenterOf(pos);
/*     */     
/* 113 */     return level.getPlayers(player -> 
/* 114 */         (!player.isSpectator() && player.position().closerThan(origin, 16.0D) && player.isAlive()));
/*     */   }
/*     */ 
/*     */   
/*     */   private void increaseWarningLevel() {
/* 119 */     if (!onCooldown()) {
/* 120 */       this.ticksSinceLastWarning = 0;
/* 121 */       this.cooldownTicks = 200;
/* 122 */       setWarningLevel(getWarningLevel() + 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 127 */   private void decreaseWarningLevel() { setWarningLevel(getWarningLevel() - 1); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public void setWarningLevel(int warningLevel) { this.warningLevel = Mth.clamp(warningLevel, 0, 4); }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public int getWarningLevel() { return this.warningLevel; }
/*     */ 
/*     */   
/*     */   private void copyData(WardenSpawnTracker copyFrom) {
/* 139 */     this.warningLevel = copyFrom.warningLevel;
/* 140 */     this.cooldownTicks = copyFrom.cooldownTicks;
/* 141 */     this.ticksSinceLastWarning = copyFrom.ticksSinceLastWarning;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\warden\WardenSpawnTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */