/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ public class StructureGridSpawner
/*    */   implements GameTestRunner.StructureSpawner
/*    */ {
/*    */   private static final int SPACE_BETWEEN_COLUMNS = 5;
/*    */   private static final int SPACE_BETWEEN_ROWS = 6;
/*    */   private final int testsPerRow;
/*    */   private int currentRowCount;
/*    */   private AABB rowBounds;
/*    */   private final BlockPos.MutableBlockPos nextTestNorthWestCorner;
/*    */   private final BlockPos firstTestNorthWestCorner;
/*    */   private final boolean clearOnBatch;
/*    */   private float maxX;
/*    */   private final Collection<GameTestInfo> testInLastBatch;
/*    */   
/*    */   public StructureGridSpawner(BlockPos firstTestNorthWestCorner, int testsPerRow, boolean clearOnBatch) {
/* 27 */     this.maxX = -1.0F;
/* 28 */     this.testInLastBatch = new ArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     this.testsPerRow = testsPerRow;
/* 35 */     this.nextTestNorthWestCorner = firstTestNorthWestCorner.mutable();
/* 36 */     this.rowBounds = new AABB(this.nextTestNorthWestCorner);
/* 37 */     this.firstTestNorthWestCorner = firstTestNorthWestCorner;
/* 38 */     this.clearOnBatch = clearOnBatch;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onBatchStart(ServerLevel level) {
/* 43 */     if (this.clearOnBatch) {
/* 44 */       this.testInLastBatch.forEach(info -> {
/* 45 */             BoundingBox boundingBox = info.getTestInstanceBlockEntity().getStructureBoundingBox();
/* 46 */             StructureUtils.clearSpaceForStructure(boundingBox, level);
/*    */           });
/* 48 */       this.testInLastBatch.clear();
/* 49 */       this.rowBounds = new AABB(this.firstTestNorthWestCorner);
/* 50 */       this.nextTestNorthWestCorner.set(this.firstTestNorthWestCorner);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<GameTestInfo> spawnStructure(GameTestInfo testInfo) {
/* 56 */     BlockPos northWestCorner = new BlockPos(this.nextTestNorthWestCorner);
/* 57 */     testInfo.setTestBlockPos(northWestCorner);
/* 58 */     GameTestInfo infoWithStructure = testInfo.prepareTestStructure();
/* 59 */     if (infoWithStructure == null) {
/* 60 */       return Optional.empty();
/*    */     }
/* 62 */     infoWithStructure.startExecution(1);
/*    */     
/* 64 */     AABB structureBounds = testInfo.getTestInstanceBlockEntity().getStructureBounds();
/* 65 */     this.rowBounds = this.rowBounds.minmax(structureBounds);
/*    */     
/* 67 */     this.nextTestNorthWestCorner.move((int)structureBounds.getXsize() + 5, 0, 0);
/* 68 */     if (this.nextTestNorthWestCorner.getX() > this.maxX) {
/* 69 */       this.maxX = this.nextTestNorthWestCorner.getX();
/*    */     }
/*    */     
/* 72 */     if (++this.currentRowCount >= this.testsPerRow) {
/*    */       
/* 74 */       this.currentRowCount = 0;
/* 75 */       this.nextTestNorthWestCorner.move(0, 0, (int)this.rowBounds.getZsize() + 6);
/* 76 */       this.nextTestNorthWestCorner.setX(this.firstTestNorthWestCorner.getX());
/* 77 */       this.rowBounds = new AABB(this.nextTestNorthWestCorner);
/*    */     } 
/*    */     
/* 80 */     this.testInLastBatch.add(testInfo);
/* 81 */     return Optional.of(testInfo);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\StructureGridSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */