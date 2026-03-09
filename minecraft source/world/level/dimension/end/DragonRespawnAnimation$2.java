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
/* 31 */     if (time < 100) {
/* 32 */       if (time == 0 || time == 50 || time == 51 || time == 52 || time >= 95) {
/* 33 */         level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*    */       }
/*    */     } else {
/* 36 */       fight.setRespawnStage(SUMMONING_PILLARS);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\end\DragonRespawnAnimation$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */