/*    */ package net.minecraft.world.level.lighting;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ public abstract class DataLayerStorageMap<M extends DataLayerStorageMap<M>> extends Object {
/*    */   private static final int CACHE_SIZE = 2;
/*    */   private final long[] lastSectionKeys;
/*    */   
/*    */   protected DataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> map) {
/*  9 */     this.lastSectionKeys = new long[2];
/* 10 */     this.lastSections = new DataLayer[2];
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 15 */     this.map = map;
/* 16 */     clearCache();
/* 17 */     this.cacheEnabled = true;
/*    */   }
/*    */   private final DataLayer[] lastSections; private boolean cacheEnabled; protected final Long2ObjectOpenHashMap<DataLayer> map;
/*    */   public abstract M copy();
/*    */   
/*    */   public DataLayer copyDataLayer(long sectionNode) {
/* 23 */     DataLayer newDataLayer = ((DataLayer)this.map.get(sectionNode)).copy();
/* 24 */     this.map.put(sectionNode, newDataLayer);
/* 25 */     clearCache();
/* 26 */     return newDataLayer;
/*    */   }
/*    */ 
/*    */   
/* 30 */   public boolean hasLayer(long sectionNode) { return this.map.containsKey(sectionNode); }
/*    */ 
/*    */   
/*    */   public DataLayer getLayer(long sectionNode) {
/* 34 */     if (this.cacheEnabled) {
/* 35 */       for (int i = 0; i < 2; i++) {
/* 36 */         if (sectionNode == this.lastSectionKeys[i]) {
/* 37 */           return this.lastSections[i];
/*    */         }
/*    */       } 
/*    */     }
/* 41 */     DataLayer data = (DataLayer)this.map.get(sectionNode);
/* 42 */     if (data != null) {
/* 43 */       if (this.cacheEnabled) {
/* 44 */         for (int i = 1; i > 0; i--) {
/* 45 */           this.lastSectionKeys[i] = this.lastSectionKeys[i - 1];
/* 46 */           this.lastSections[i] = this.lastSections[i - 1];
/*    */         } 
/* 48 */         this.lastSectionKeys[0] = sectionNode;
/* 49 */         this.lastSections[0] = data;
/*    */       } 
/* 51 */       return data;
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public DataLayer removeLayer(long sectionNode) { return (DataLayer)this.map.remove(sectionNode); }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public void setLayer(long sectionNode, DataLayer layer) { this.map.put(sectionNode, layer); }
/*    */ 
/*    */   
/*    */   public void clearCache() {
/* 66 */     for (int i = 0; i < 2; i++) {
/* 67 */       this.lastSectionKeys[i] = Float.MAX_VALUE;
/* 68 */       this.lastSections[i] = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 73 */   public void disableCache() { this.cacheEnabled = false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\DataLayerStorageMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */