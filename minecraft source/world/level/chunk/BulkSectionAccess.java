/*    */ package net.minecraft.world.level.chunk;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ 
/*    */ public class BulkSectionAccess implements AutoCloseable {
/*    */   private final LevelAccessor level;
/*    */   private final Long2ObjectMap<LevelChunkSection> acquiredSections;
/*    */   
/*    */   public BulkSectionAccess(LevelAccessor level) {
/* 14 */     this.acquiredSections = new Long2ObjectOpenHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 19 */     this.level = level;
/*    */   }
/*    */   private LevelChunkSection lastSection; private long lastSectionKey;
/*    */   public LevelChunkSection getSection(BlockPos pos) {
/* 23 */     int sectionIndex = this.level.getSectionIndex(pos.getY());
/* 24 */     if (sectionIndex < 0 || sectionIndex >= this.level.getSectionsCount()) {
/* 25 */       return null;
/*    */     }
/* 27 */     long sectionKey = SectionPos.asLong(pos);
/* 28 */     if (this.lastSection == null || this.lastSectionKey != sectionKey) {
/* 29 */       this.lastSection = (LevelChunkSection)this.acquiredSections.computeIfAbsent(sectionKey, key -> {
/* 30 */             ChunkAccess chunk = this.level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/* 31 */             LevelChunkSection result = chunk.getSection(sectionIndex);
/* 32 */             result.acquire();
/* 33 */             return result;
/*    */           });
/* 35 */       this.lastSectionKey = sectionKey;
/*    */     } 
/* 37 */     return this.lastSection;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 41 */     LevelChunkSection section = getSection(pos);
/*    */     
/* 43 */     if (section == null) {
/* 44 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 46 */     int sectionRelativeX = SectionPos.sectionRelative(pos.getX());
/* 47 */     int sectionRelativeY = SectionPos.sectionRelative(pos.getY());
/* 48 */     int sectionRelativeZ = SectionPos.sectionRelative(pos.getZ());
/* 49 */     return section.getBlockState(sectionRelativeX, sectionRelativeY, sectionRelativeZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 54 */     for (ObjectIterator objectIterator = this.acquiredSections.values().iterator(); objectIterator.hasNext(); ) { LevelChunkSection section = (LevelChunkSection)objectIterator.next();
/* 55 */       section.release(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\BulkSectionAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */