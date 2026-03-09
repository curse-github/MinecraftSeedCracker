/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.WorldVersion;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class LevelSummary
/*     */   extends Object
/*     */   implements Comparable<LevelSummary> {
/*  19 */   public static final Component PLAY_WORLD = Component.translatable("selectWorld.select");
/*     */   
/*     */   private final LevelSettings settings;
/*     */   private final LevelVersion levelVersion;
/*     */   private final String levelId;
/*     */   private final boolean requiresManualConversion;
/*     */   private final boolean locked;
/*     */   private final boolean experimental;
/*     */   private final Path icon;
/*     */   private Component info;
/*     */   
/*     */   public LevelSummary(LevelSettings settings, LevelVersion levelVersion, String levelId, boolean requiresManualConversion, boolean locked, boolean experimental, Path icon) {
/*  31 */     this.settings = settings;
/*  32 */     this.levelVersion = levelVersion;
/*  33 */     this.levelId = levelId;
/*  34 */     this.locked = locked;
/*  35 */     this.experimental = experimental;
/*  36 */     this.icon = icon;
/*  37 */     this.requiresManualConversion = requiresManualConversion;
/*     */   }
/*     */ 
/*     */   
/*  41 */   public String getLevelId() { return this.levelId; }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public String getLevelName() { return StringUtils.isEmpty(this.settings.levelName()) ? this.levelId : this.settings.levelName(); }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public Path getIcon() { return this.icon; }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public boolean requiresManualConversion() { return this.requiresManualConversion; }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public boolean isExperimental() { return this.experimental; }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public long getLastPlayed() { return this.levelVersion.lastPlayed(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(LevelSummary rhs) {
/*  66 */     if (getLastPlayed() < rhs.getLastPlayed()) {
/*  67 */       return 1;
/*     */     }
/*  69 */     if (getLastPlayed() > rhs.getLastPlayed()) {
/*  70 */       return -1;
/*     */     }
/*  72 */     return this.levelId.compareTo(rhs.levelId);
/*     */   }
/*     */ 
/*     */   
/*  76 */   public LevelSettings getSettings() { return this.settings; }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public GameType getGameMode() { return this.settings.gameType(); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean isHardcore() { return this.settings.hardcore(); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public boolean hasCommands() { return this.settings.allowCommands(); }
/*     */ 
/*     */   
/*     */   public MutableComponent getWorldVersionName() {
/*  92 */     if (StringUtil.isNullOrEmpty(this.levelVersion.minecraftVersionName())) {
/*  93 */       return Component.translatable("selectWorld.versionUnknown");
/*     */     }
/*  95 */     return Component.literal(this.levelVersion.minecraftVersionName());
/*     */   }
/*     */ 
/*     */   
/*  99 */   public LevelVersion levelVersion() { return this.levelVersion; }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean shouldBackup() { return backupStatus().shouldBackup(); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public boolean isDowngrade() { return (backupStatus() == BackupStatus.DOWNGRADE); }
/*     */ 
/*     */   
/*     */   public BackupStatus backupStatus() {
/* 111 */     WorldVersion currentVersion = SharedConstants.getCurrentVersion();
/* 112 */     int currentVersionNumber = currentVersion.dataVersion().version();
/* 113 */     int levelVersionNumber = this.levelVersion.minecraftVersion().version();
/* 114 */     if (!currentVersion.stable() && levelVersionNumber < currentVersionNumber)
/* 115 */       return BackupStatus.UPGRADE_TO_SNAPSHOT; 
/* 116 */     if (levelVersionNumber > currentVersionNumber) {
/* 117 */       return BackupStatus.DOWNGRADE;
/*     */     }
/* 119 */     return BackupStatus.NONE;
/*     */   }
/*     */ 
/*     */   
/* 123 */   public boolean isLocked() { return this.locked; }
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/* 127 */     if (isLocked() || requiresManualConversion()) {
/* 128 */       return true;
/*     */     }
/*     */     
/* 131 */     return !isCompatible();
/*     */   }
/*     */ 
/*     */   
/* 135 */   public boolean isCompatible() { return SharedConstants.getCurrentVersion().dataVersion().isCompatible(this.levelVersion.minecraftVersion()); }
/*     */ 
/*     */   
/*     */   public Component getInfo() {
/* 139 */     if (this.info == null) {
/* 140 */       this.info = createInfo();
/*     */     }
/*     */     
/* 143 */     return this.info;
/*     */   }
/*     */   
/*     */   private Component createInfo() {
/* 147 */     if (isLocked()) {
/* 148 */       return Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);
/*     */     }
/* 150 */     if (requiresManualConversion()) {
/* 151 */       return Component.translatable("selectWorld.conversion").withStyle(ChatFormatting.RED);
/*     */     }
/* 153 */     if (!isCompatible()) {
/* 154 */       return Component.translatable("selectWorld.incompatible.info", new Object[] { getWorldVersionName() }).withStyle(ChatFormatting.RED);
/*     */     }
/*     */ 
/*     */     
/* 158 */     MutableComponent result = isHardcore() ? Component.empty().append(Component.translatable("gameMode.hardcore").withColor(-65536)) : Component.translatable("gameMode." + getGameMode().getName());
/*     */     
/* 160 */     if (hasCommands()) {
/* 161 */       result.append(", ").append(Component.translatable("selectWorld.commands"));
/*     */     }
/*     */     
/* 164 */     if (isExperimental()) {
/* 165 */       result.append(", ").append(Component.translatable("selectWorld.experimental").withStyle(ChatFormatting.YELLOW));
/*     */     }
/*     */     
/* 168 */     MutableComponent worldVersionName = getWorldVersionName();
/* 169 */     MutableComponent decoratedVersionName = Component.literal(", ").append(Component.translatable("selectWorld.version")).append(CommonComponents.SPACE);
/* 170 */     if (shouldBackup()) {
/* 171 */       decoratedVersionName.append(worldVersionName.withStyle(isDowngrade() ? ChatFormatting.RED : ChatFormatting.ITALIC));
/*     */     } else {
/* 173 */       decoratedVersionName.append(worldVersionName);
/*     */     } 
/* 175 */     result.append(decoratedVersionName);
/* 176 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 180 */   public Component primaryActionMessage() { return PLAY_WORLD; }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public boolean primaryActionActive() { return !isDisabled(); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public boolean canUpload() { return (!requiresManualConversion() && !isLocked()); }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public boolean canEdit() { return !isDisabled(); }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public boolean canRecreate() { return !isDisabled(); }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public boolean canDelete() { return true; }
/*     */   
/*     */   public enum BackupStatus
/*     */   {
/* 204 */     NONE(false, false, ""),
/* 205 */     DOWNGRADE(true, true, "downgrade"),
/* 206 */     UPGRADE_TO_SNAPSHOT(true, false, "snapshot");
/*     */     
/*     */     private final boolean shouldBackup;
/*     */     private final boolean severe;
/*     */     private final String translationKey;
/*     */     
/*     */     BackupStatus(boolean shouldBackup, boolean severe, String translationKey) {
/* 213 */       this.shouldBackup = shouldBackup;
/* 214 */       this.severe = severe;
/* 215 */       this.translationKey = translationKey;
/*     */     }
/*     */ 
/*     */     
/* 219 */     public boolean shouldBackup() { return this.shouldBackup; }
/*     */ 
/*     */ 
/*     */     
/* 223 */     public boolean isSevere() { return this.severe; }
/*     */ 
/*     */ 
/*     */     
/* 227 */     public String getTranslationKey() { return this.translationKey; }
/*     */   }
/*     */   
/*     */   public static class SymlinkLevelSummary
/*     */     extends LevelSummary {
/* 232 */     private static final Component MORE_INFO_BUTTON = Component.translatable("symlink_warning.more_info");
/* 233 */     private static final Component INFO = Component.translatable("symlink_warning.title").withColor(-65536);
/*     */ 
/*     */     
/* 236 */     public SymlinkLevelSummary(String levelId, Path icon) { super(null, null, levelId, false, false, false, icon); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     public String getLevelName() { return getLevelId(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 246 */     public Component getInfo() { return INFO; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     public long getLastPlayed() { return -1L; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     public boolean isDisabled() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     public Component primaryActionMessage() { return MORE_INFO_BUTTON; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     public boolean primaryActionActive() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 271 */     public boolean canUpload() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     public boolean canEdit() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     public boolean canRecreate() { return false; }
/*     */   }
/*     */   
/*     */   public static class CorruptedLevelSummary
/*     */     extends LevelSummary {
/* 286 */     private static final Component INFO = Component.translatable("recover_world.warning").withStyle(style -> style.withColor(-65536));
/* 287 */     private static final Component RECOVER = Component.translatable("recover_world.button");
/*     */     private final long lastPlayed;
/*     */     
/*     */     public CorruptedLevelSummary(String levelId, Path icon, long lastPlayed) {
/* 291 */       super(null, null, levelId, false, false, false, icon);
/* 292 */       this.lastPlayed = lastPlayed;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 297 */     public String getLevelName() { return getLevelId(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     public Component getInfo() { return INFO; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     public long getLastPlayed() { return this.lastPlayed; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     public boolean isDisabled() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     public Component primaryActionMessage() { return RECOVER; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     public boolean primaryActionActive() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     public boolean canUpload() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     public boolean canEdit() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     public boolean canRecreate() { return false; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\LevelSummary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */