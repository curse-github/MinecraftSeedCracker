/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.level.Level;
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
/*    */ static enum null
/*    */ {
/*    */   public void tick(ServerLevel level, EndDragonFight fight, List<EndCrystal> crystals, int time, BlockPos portal) {
/* 78 */     if (time >= 100) {
/* 79 */       fight.setRespawnStage(END);
/* 80 */       fight.resetSpikeCrystals();
/* 81 */       for (EndCrystal crystal : crystals) {
/* 82 */         crystal.setBeamTarget(null);
/* 83 */         level.explode(crystal, crystal.getX(), crystal.getY(), crystal.getZ(), 6.0F, Level.ExplosionInteraction.NONE);
/* 84 */         crystal.discard();
/*    */       } 
/* 86 */     } else if (time >= 80) {
/* 87 */       level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/* 88 */     } else if (time == 0) {
/* 89 */       for (EndCrystal crystal : crystals) {
/* 90 */         crystal.setBeamTarget(new BlockPos(0, 128, 0));
/*    */       }
/* 92 */     } else if (time < 5) {
/* 93 */       level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\end\DragonRespawnAnimation$4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */