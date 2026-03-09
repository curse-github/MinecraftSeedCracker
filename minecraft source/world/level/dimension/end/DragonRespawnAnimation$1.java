/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
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
/*    */ static enum null
/*    */ {
/*    */   public void tick(ServerLevel level, EndDragonFight fight, List<EndCrystal> crystals, int time, BlockPos portal) {
/* 21 */     BlockPos beamPos = new BlockPos(0, 128, 0);
/* 22 */     for (EndCrystal respawnCrystal : crystals) {
/* 23 */       respawnCrystal.setBeamTarget(beamPos);
/*    */     }
/* 25 */     fight.setRespawnStage(PREPARING_TO_SUMMON_PILLARS);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\end\DragonRespawnAnimation$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */