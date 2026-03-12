/*     */ package net.minecraft.util.worldupdate;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.storage.LegacyTagFixer;
/*     */ import net.minecraft.world.level.chunk.storage.RecreatingSimpleRegionStorage;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.structure.LegacyStructureDataHandler;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ChunkUpgrader
/*     */   extends WorldUpgrader.AbstractUpgrader
/*     */ {
/* 429 */   private ChunkUpgrader() { super(paramWorldUpgrader, DataFixTypes.CHUNK, "chunk", "region", WorldUpgrader.STATUS_UPGRADING_CHUNKS, WorldUpgrader.STATUS_FINISHED_CHUNKS); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean tryProcessOnePosition(SimpleRegionStorage storage, ChunkPos pos, ResourceKey<Level> dimension) {
/* 434 */     CompoundTag chunkTag = (CompoundTag)((Optional)storage.read(pos).join()).orElse(null);
/* 435 */     if (chunkTag != null) {
/* 436 */       int version = NbtUtils.getDataVersion(chunkTag);
/*     */       
/* 438 */       ChunkGenerator generator = ((LevelStem)WorldUpgrader.this.dimensions.getValueOrThrow(Registries.levelToLevelStem(dimension))).generator();
/* 439 */       CompoundTag upgradedTag = storage.upgradeChunkTag(chunkTag, -1, ChunkMap.getChunkDataFixContextTag(dimension, generator.getTypeNameForDataFixer()));
/*     */       
/* 441 */       ChunkPos storedPos = new ChunkPos(upgradedTag.getIntOr("xPos", 0), upgradedTag.getIntOr("zPos", 0));
/* 442 */       if (!storedPos.equals(pos)) {
/* 443 */         WorldUpgrader.LOGGER.warn("Chunk {} has invalid position {}", pos, storedPos);
/*     */       }
/*     */       
/* 446 */       boolean changed = (version < SharedConstants.getCurrentVersion().dataVersion().version());
/* 447 */       if (WorldUpgrader.this.eraseCache) {
/* 448 */         changed = (changed || upgradedTag.contains("Heightmaps"));
/* 449 */         upgradedTag.remove("Heightmaps");
/* 450 */         changed = (changed || upgradedTag.contains("isLightOn"));
/* 451 */         upgradedTag.remove("isLightOn");
/*     */         
/* 453 */         ListTag sections = upgradedTag.getListOrEmpty("sections");
/* 454 */         for (int i = 0; i < sections.size(); i++) {
/* 455 */           Optional<CompoundTag> maybeSection = sections.getCompound(i);
/* 456 */           if (!maybeSection.isEmpty()) {
/*     */ 
/*     */             
/* 459 */             CompoundTag section = (CompoundTag)maybeSection.get();
/* 460 */             changed = (changed || section.contains("BlockLight"));
/* 461 */             section.remove("BlockLight");
/* 462 */             changed = (changed || section.contains("SkyLight"));
/* 463 */             section.remove("SkyLight");
/*     */           } 
/*     */         } 
/*     */       } 
/* 467 */       if (changed || WorldUpgrader.this.recreateRegionFiles) {
/* 468 */         if (this.previousWriteFuture != null) {
/* 469 */           this.previousWriteFuture.join();
/*     */         }
/* 471 */         this.previousWriteFuture = storage.write(pos, upgradedTag);
/* 472 */         return true;
/*     */       } 
/*     */     } 
/* 475 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SimpleRegionStorage createStorage(RegionStorageInfo info, Path regionFolder) {
/* 480 */     Supplier<LegacyTagFixer> legacyFixer = LegacyStructureDataHandler.getLegacyTagFixer(info.dimension(), () -> WorldUpgrader.this.overworldDataStorage, WorldUpgrader.this.dataFixer);
/* 481 */     return WorldUpgrader.this.recreateRegionFiles ? 
/* 482 */       new RecreatingSimpleRegionStorage(info
/* 483 */         .withTypeSuffix("source"), regionFolder, info
/* 484 */         .withTypeSuffix("target"), WorldUpgrader.resolveRecreateDirectory(regionFolder), WorldUpgrader.this.dataFixer, true, DataFixTypes.CHUNK, legacyFixer) : 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 490 */       new SimpleRegionStorage(info, regionFolder, WorldUpgrader.this.dataFixer, true, DataFixTypes.CHUNK, legacyFixer);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\worldupdate\WorldUpgrader$ChunkUpgrader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */