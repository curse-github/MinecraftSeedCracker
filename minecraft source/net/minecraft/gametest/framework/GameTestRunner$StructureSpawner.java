/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface StructureSpawner
/*    */ {
/*    */   default void onBatchStart(ServerLevel level) {}
/*    */   
/* 47 */   public static final StructureSpawner IN_PLACE = testInfo -> Optional.ofNullable(testInfo.prepareTestStructure())
/* 48 */     .map(());
/* 49 */   public static final StructureSpawner NOT_SET = testInfo -> Optional.empty();
/*    */   
/*    */   Optional<GameTestInfo> spawnStructure(GameTestInfo paramGameTestInfo);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestRunner$StructureSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */