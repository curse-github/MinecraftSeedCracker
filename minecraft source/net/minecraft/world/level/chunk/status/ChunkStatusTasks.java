/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.server.level.GenerationChunkHolder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ThreadedLevelLightEngine;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkStatusTasks {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  31 */   private static boolean isLighted(ChunkAccess chunk) { return (chunk.getPersistedStatus().isOrAfter(ChunkStatus.LIGHT) && chunk.isLightCorrect()); }
/*     */ 
/*     */ 
/*     */   
/*  35 */   static CompletableFuture<ChunkAccess> passThrough(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) { return CompletableFuture.completedFuture(chunk); }
/*     */ 
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateStructureStarts(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  39 */     ServerLevel level = context.level();
/*  40 */     if (level.getServer().getWorldData().worldGenOptions().generateStructures()) {
/*  41 */       context.generator().createStructures(level.registryAccess(), level.getChunkSource().getGeneratorState(), level.structureManager(), chunk, context.structureManager(), level.dimension());
/*     */     }
/*  43 */     level.onStructureStartsAvailable(chunk);
/*  44 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> loadStructureStarts(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk) {
/*  48 */     context.level().onStructureStartsAvailable(chunk);
/*  49 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateStructureReferences(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  53 */     ServerLevel level = context.level();
/*  54 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  55 */     context.generator().createReferences(region, level.structureManager().forWorldGenRegion(region), chunk);
/*  56 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateBiomes(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  60 */     ServerLevel level = context.level();
/*  61 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  62 */     return context.generator().createBiomes(level.getChunkSource().randomState(), Blender.of(region), level.structureManager().forWorldGenRegion(region), chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateNoise(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  66 */     ServerLevel level = context.level();
/*  67 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  68 */     return context.generator().fillFromNoise(Blender.of(region), level.getChunkSource().randomState(), level.structureManager().forWorldGenRegion(region), chunk).thenApply(generatedChunk -> {
/*  69 */           if (generatedChunk instanceof ProtoChunk) { ProtoChunk protoChunk = (ProtoChunk)generatedChunk;
/*  70 */             BelowZeroRetrogen belowZeroRetrogen = protoChunk.getBelowZeroRetrogen();
/*  71 */             if (belowZeroRetrogen != null) {
/*  72 */               BelowZeroRetrogen.replaceOldBedrock(protoChunk);
/*     */               
/*  74 */               if (belowZeroRetrogen.hasBedrockHoles()) {
/*  75 */                 belowZeroRetrogen.applyBedrockMask(protoChunk);
/*     */               }
/*     */             }  }
/*     */ 
/*     */           
/*  80 */           return generatedChunk;
/*     */         });
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateSurface(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  85 */     ServerLevel level = context.level();
/*  86 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  87 */     context.generator().buildSurface(region, level.structureManager().forWorldGenRegion(region), level.getChunkSource().randomState(), chunk);
/*  88 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateCarvers(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  92 */     ServerLevel level = context.level();
/*  93 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  94 */     if (chunk instanceof ProtoChunk) { ProtoChunk protoChunk = (ProtoChunk)chunk;
/*  95 */       Blender.addAroundOldChunksCarvingMaskFilter(region, protoChunk); }
/*     */     
/*  97 */     context.generator().applyCarvers(region, level.getSeed(), level.getChunkSource().randomState(), level.getBiomeManager(), level.structureManager().forWorldGenRegion(region), chunk);
/*  98 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateFeatures(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 102 */     ServerLevel level = context.level();
/* 103 */     Heightmap.primeHeightmaps(chunk, EnumSet.of(Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE));
/* 104 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/* 105 */     if (!SharedConstants.DEBUG_DISABLE_FEATURES) {
/* 106 */       context.generator().applyBiomeDecoration(region, chunk, level.structureManager().forWorldGenRegion(region));
/*     */     }
/*     */     
/* 109 */     Blender.generateBorderTicks(region, chunk);
/* 110 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> initializeLight(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 114 */     ThreadedLevelLightEngine lightEngine = context.lightEngine();
/* 115 */     chunk.initializeLightSources();
/* 116 */     ((ProtoChunk)chunk).setLightEngine(lightEngine);
/* 117 */     boolean lighted = isLighted(chunk);
/*     */     
/* 119 */     return lightEngine.initializeLight(chunk, lighted);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> light(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 123 */     boolean lighted = isLighted(chunk);
/* 124 */     return context.lightEngine().lightChunk(chunk, lighted);
/*     */   }
/*     */ 
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateSpawn(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 129 */     if (!chunk.isUpgrading()) {
/* 130 */       context.generator().spawnOriginalMobs(new WorldGenRegion(context.level(), chunks, step, chunk));
/*     */     }
/* 132 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> full(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 136 */     ChunkPos pos = chunk.getPos();
/* 137 */     GenerationChunkHolder holder = (GenerationChunkHolder)chunks.get(pos.x, pos.z);
/* 138 */     return CompletableFuture.supplyAsync(() -> {
/* 139 */           LevelChunk levelChunk; ProtoChunk protoChunk = (ProtoChunk)chunk;
/*     */           
/* 141 */           ServerLevel level = context.level();
/* 142 */           if (protoChunk instanceof ImposterProtoChunk) { ImposterProtoChunk imposter = (ImposterProtoChunk)protoChunk;
/* 143 */             levelChunk = imposter.getWrapped(); }
/*     */           else
/* 145 */           { levelChunk = new LevelChunk(level, protoChunk, ());
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 150 */             holder.replaceProtoChunk(new ImposterProtoChunk(levelChunk, false)); }
/*     */ 
/*     */           
/* 153 */           Objects.requireNonNull(holder); levelChunk.setFullStatus(holder::getFullStatus);
/* 154 */           levelChunk.runPostLoad();
/* 155 */           levelChunk.setLoaded(true);
/* 156 */           levelChunk.registerAllBlockEntitiesAfterLevelLoad();
/* 157 */           levelChunk.registerTickContainerInLevel(level);
/* 158 */           levelChunk.setUnsavedListener(context.unsavedListener());
/* 159 */           return levelChunk;
/* 160 */         }context.mainThreadExecutor());
/*     */   }
/*     */   
/*     */   private static void postLoadProtoChunk(ServerLevel level, ValueInput.ValueInputList entities) {
/* 164 */     if (!entities.isEmpty())
/* 165 */       level.addWorldGenChunkEntities(EntityType.loadEntitiesRecursive(entities, level, EntitySpawnReason.LOAD)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\status\ChunkStatusTasks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */