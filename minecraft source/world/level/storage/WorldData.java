/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface WorldData
/*     */ {
/*     */   public static final int ANVIL_VERSION_ID = 19133;
/*     */   public static final int MCREGION_VERSION_ID = 19132;
/*     */   
/*     */   default void fillCrashReportCategory(CrashReportCategory category) {
/*  37 */     category.setDetail("Known server brands", () -> String.join(", ", getKnownServerBrands()));
/*  38 */     category.setDetail("Removed feature flags", () -> String.join(", ", getRemovedFeatureFlags()));
/*  39 */     category.setDetail("Level was modded", () -> Boolean.toString(wasModded()));
/*  40 */     category.setDetail("Level storage version", () -> {
/*  41 */           int version = getVersion();
/*  42 */           return String.format(Locale.ROOT, "0x%05X - %s", new Object[] { Integer.valueOf(version), getStorageVersionName(version) });
/*     */         });
/*     */   }
/*     */   
/*     */   default String getStorageVersionName(int version) {
/*  47 */     switch (version) {
/*     */       case 19133:
/*  49 */         return "Anvil";
/*     */       case 19132:
/*  51 */         return "McRegion";
/*     */     } 
/*  53 */     return "Unknown?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   default FeatureFlagSet enabledFeatures() { return getDataConfiguration().enabledFeatures(); }
/*     */   
/*     */   WorldDataConfiguration getDataConfiguration();
/*     */   
/*     */   void setDataConfiguration(WorldDataConfiguration paramWorldDataConfiguration);
/*     */   
/*     */   boolean wasModded();
/*     */   
/*     */   Set<String> getKnownServerBrands();
/*     */   
/*     */   Set<String> getRemovedFeatureFlags();
/*     */   
/*     */   void setModdedInfo(String paramString, boolean paramBoolean);
/*     */   
/*     */   CompoundTag getCustomBossEvents();
/*     */   
/*     */   void setCustomBossEvents(CompoundTag paramCompoundTag);
/*     */   
/*     */   ServerLevelData overworldData();
/*     */   
/*     */   LevelSettings getLevelSettings();
/*     */   
/*     */   CompoundTag createTag(RegistryAccess paramRegistryAccess, CompoundTag paramCompoundTag);
/*     */   
/*     */   boolean isHardcore();
/*     */   
/*     */   int getVersion();
/*     */   
/*     */   String getLevelName();
/*     */   
/*     */   GameType getGameType();
/*     */   
/*     */   void setGameType(GameType paramGameType);
/*     */   
/*     */   boolean isAllowCommands();
/*     */   
/*     */   Difficulty getDifficulty();
/*     */   
/*     */   void setDifficulty(Difficulty paramDifficulty);
/*     */   
/*     */   boolean isDifficultyLocked();
/*     */   
/*     */   void setDifficultyLocked(boolean paramBoolean);
/*     */   
/*     */   GameRules getGameRules();
/*     */   
/*     */   CompoundTag getLoadedPlayerTag();
/*     */   
/*     */   EndDragonFight.Data endDragonFightData();
/*     */   
/*     */   void setEndDragonFightData(EndDragonFight.Data paramData);
/*     */   
/*     */   WorldOptions worldGenOptions();
/*     */   
/*     */   boolean isFlatWorld();
/*     */   
/*     */   boolean isDebugWorld();
/*     */   
/*     */   Lifecycle worldGenSettingsLifecycle();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\WorldData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */