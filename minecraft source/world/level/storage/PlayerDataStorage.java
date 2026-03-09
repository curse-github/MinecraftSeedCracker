/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.File;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PlayerDataStorage {
/* 24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private final File playerDir;
/*    */   protected final DataFixer fixerUpper;
/*    */   
/*    */   public PlayerDataStorage(LevelStorageSource.LevelStorageAccess levelAccess, DataFixer fixerUpper) {
/* 29 */     this.fixerUpper = fixerUpper;
/* 30 */     this.playerDir = levelAccess.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
/* 31 */     this.playerDir.mkdirs();
/*    */   }
/*    */   public void save(Player player) {
/*    */     
/* 35 */     try { ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(player.problemPath(), LOGGER); 
/* 36 */       try { TagValueOutput output = TagValueOutput.createWithContext(reporter, player.registryAccess());
/* 37 */         player.saveWithoutId(output);
/* 38 */         Path playerDirPath = this.playerDir.toPath();
/* 39 */         Path tmpFile = Files.createTempFile(playerDirPath, player.getStringUUID() + "-", ".dat", new java.nio.file.attribute.FileAttribute[0]);
/* 40 */         CompoundTag dataToStore = output.buildResult();
/* 41 */         NbtIo.writeCompressed(dataToStore, tmpFile);
/*    */         
/* 43 */         Path realFile = playerDirPath.resolve(player.getStringUUID() + ".dat");
/* 44 */         Path oldFile = playerDirPath.resolve(player.getStringUUID() + ".dat_old");
/* 45 */         Util.safeReplaceFile(realFile, tmpFile, oldFile);
/* 46 */         reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception ignored)
/* 47 */     { LOGGER.warn("Failed to save player data for {}", player.getPlainTextName()); }
/*    */   
/*    */   }
/*    */   
/*    */   private void backup(NameAndId nameAndId, String suffix) {
/* 52 */     Path playerDirPath = this.playerDir.toPath();
/* 53 */     String idString = nameAndId.id().toString();
/* 54 */     Path realPath = playerDirPath.resolve(idString + idString);
/* 55 */     Path backupPath = playerDirPath.resolve(idString + "_corrupted_" + idString + ZonedDateTime.now().format(FileNameDateFormatter.FORMATTER));
/*    */     
/* 57 */     if (!Files.isRegularFile(realPath, new java.nio.file.LinkOption[0])) {
/*    */       return;
/*    */     }
/*    */     
/*    */     try {
/* 62 */       Files.copy(realPath, backupPath, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES });
/* 63 */     } catch (Exception e) {
/* 64 */       LOGGER.warn("Failed to copy the player.dat file for {}", nameAndId.name(), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private Optional<CompoundTag> load(NameAndId nameAndId, String suffix) {
/* 69 */     File realFile = new File(this.playerDir, String.valueOf(nameAndId.id()) + String.valueOf(nameAndId.id()));
/* 70 */     if (realFile.exists() && realFile.isFile()) {
/*    */       try {
/* 72 */         return Optional.of(NbtIo.readCompressed(realFile.toPath(), NbtAccounter.unlimitedHeap()));
/* 73 */       } catch (Exception ignored) {
/* 74 */         LOGGER.warn("Failed to load player data for {}", nameAndId.name());
/*    */       } 
/*    */     }
/* 77 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public Optional<CompoundTag> load(NameAndId nameAndId) {
/* 81 */     Optional<CompoundTag> optTag = load(nameAndId, ".dat");
/* 82 */     if (optTag.isEmpty()) {
/* 83 */       backup(nameAndId, ".dat");
/*    */     }
/*    */     
/* 86 */     return optTag.or(() -> load(nameAndId, ".dat_old"))
/* 87 */       .map(tag -> {
/* 88 */           int version = NbtUtils.getDataVersion(tag);
/* 89 */           return DataFixTypes.PLAYER.updateToCurrentVersion(this.fixerUpper, tag, version);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\PlayerDataStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */