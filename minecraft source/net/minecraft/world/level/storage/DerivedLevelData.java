/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DerivedLevelData
/*     */   implements ServerLevelData
/*     */ {
/*     */   private final WorldData worldData;
/*     */   private final ServerLevelData wrapped;
/*     */   
/*     */   public DerivedLevelData(WorldData worldData, ServerLevelData wrapped) {
/*  26 */     this.worldData = worldData;
/*  27 */     this.wrapped = wrapped;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public LevelData.RespawnData getRespawnData() { return this.wrapped.getRespawnData(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public long getGameTime() { return this.wrapped.getGameTime(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public long getDayTime() { return this.wrapped.getDayTime(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public String getLevelName() { return this.worldData.getLevelName(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public int getClearWeatherTime() { return this.wrapped.getClearWeatherTime(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClearWeatherTime(int clearWeatherTime) {}
/*     */ 
/*     */ 
/*     */   
/*  61 */   public boolean isThundering() { return this.wrapped.isThundering(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public int getThunderTime() { return this.wrapped.getThunderTime(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public boolean isRaining() { return this.wrapped.isRaining(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public int getRainTime() { return this.wrapped.getRainTime(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public GameType getGameType() { return this.worldData.getGameType(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGameTime(long time) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDayTime(long time) {}
/*     */ 
/*     */ 
/*     */   
/*  94 */   public void setSpawn(LevelData.RespawnData respawnData) { this.wrapped.setSpawn(respawnData); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThundering(boolean thundering) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThunderTime(int thunderTime) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRaining(boolean raining) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRainTime(int rainTime) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGameType(GameType gameType) {}
/*     */ 
/*     */ 
/*     */   
/* 119 */   public boolean isHardcore() { return this.worldData.isHardcore(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public boolean isAllowCommands() { return this.worldData.isAllowCommands(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public boolean isInitialized() { return this.wrapped.isInitialized(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialized(boolean initialized) {}
/*     */ 
/*     */ 
/*     */   
/* 138 */   public GameRules getGameRules() { return this.worldData.getGameRules(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public Optional<WorldBorder.Settings> getLegacyWorldBorderSettings() { return this.wrapped.getLegacyWorldBorderSettings(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLegacyWorldBorderSettings(Optional<WorldBorder.Settings> settings) {}
/*     */ 
/*     */ 
/*     */   
/* 152 */   public Difficulty getDifficulty() { return this.worldData.getDifficulty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   public boolean isDifficultyLocked() { return this.worldData.isDifficultyLocked(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public TimerQueue<MinecraftServer> getScheduledEvents() { return this.wrapped.getScheduledEvents(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public int getWanderingTraderSpawnDelay() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnDelay(int wanderingTraderSpawnDelay) {}
/*     */ 
/*     */ 
/*     */   
/* 176 */   public int getWanderingTraderSpawnChance() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderSpawnChance(int wanderingTraderSpawnChance) {}
/*     */ 
/*     */ 
/*     */   
/* 185 */   public UUID getWanderingTraderId() { return null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWanderingTraderId(UUID wanderingTraderId) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 194 */     category.setDetail("Derived", Boolean.valueOf(true));
/* 195 */     this.wrapped.fillCrashReportCategory(category, levelHeightAccessor);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\DerivedLevelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */