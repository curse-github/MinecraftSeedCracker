/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
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
/* 43 */     int interval = 40;
/* 44 */     boolean startOfBeam = (time % 40 == 0);
/* 45 */     boolean endOfBeam = (time % 40 == 39);
/* 46 */     if (startOfBeam || endOfBeam) {
/* 47 */       List<SpikeFeature.EndSpike> spikes = SpikeFeature.getSpikesForLevel(level);
/* 48 */       int index = time / 40;
/* 49 */       if (index < spikes.size()) {
/* 50 */         SpikeFeature.EndSpike spike = (SpikeFeature.EndSpike)spikes.get(index);
/*    */         
/* 52 */         if (startOfBeam) {
/* 53 */           for (EndCrystal respawnCrystal : crystals) {
/* 54 */             respawnCrystal.setBeamTarget(new BlockPos(spike.getCenterX(), spike.getHeight() + 1, spike.getCenterZ()));
/*    */           }
/*    */         } else {
/* 57 */           int radius = 10;
/* 58 */           for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(spike
/* 59 */                 .getCenterX() - 10, spike.getHeight() - 10, spike.getCenterZ() - 10), new BlockPos(spike
/* 60 */                 .getCenterX() + 10, spike.getHeight() + 10, spike.getCenterZ() + 10)))
/*    */           {
/* 62 */             level.removeBlock(pos, false);
/*    */           }
/* 64 */           level.explode(null, (spike.getCenterX() + 0.5F), spike.getHeight(), (spike.getCenterZ() + 0.5F), 5.0F, Level.ExplosionInteraction.BLOCK);
/*    */           
/* 66 */           SpikeConfiguration configuration = new SpikeConfiguration(true, ImmutableList.of(spike), new BlockPos(0, 128, 0));
/* 67 */           Feature.END_SPIKE.place(configuration, level, level.getChunkSource().getGenerator(), RandomSource.create(), new BlockPos(spike.getCenterX(), 45, spike.getCenterZ()));
/*    */         } 
/* 69 */       } else if (startOfBeam) {
/* 70 */         fight.setRespawnStage(SUMMONING_DRAGON);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\end\DragonRespawnAnimation$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */