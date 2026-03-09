/*    */ package net.minecraft.world.entity.animal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.pathfinder.PathType;
/*    */ 
/*    */ public abstract class AgeableWaterCreature
/*    */   extends AgeableMob {
/*    */   protected AgeableWaterCreature(EntityType<? extends AgeableWaterCreature> type, Level level) {
/* 19 */     super(type, level);
/*    */     
/* 21 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean checkSpawnObstruction(LevelReader level) { return level.isUnobstructed(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public int getAmbientSoundInterval() { return 120; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public int getBaseExperienceReward(ServerLevel level) { return 1 + this.random.nextInt(3); }
/*    */ 
/*    */   
/*    */   protected void handleAirSupply(int preTickAirSupply) {
/* 40 */     if (isAlive() && !isInWater()) {
/* 41 */       setAirSupply(preTickAirSupply - 1);
/* 42 */       if (shouldTakeDrowningDamage()) {
/* 43 */         setAirSupply(0);
/* 44 */         hurt(damageSources().drown(), 2.0F);
/*    */       } 
/*    */     } else {
/* 47 */       setAirSupply(300);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void baseTick() {
/* 53 */     int airSupply = getAirSupply();
/* 54 */     super.baseTick();
/* 55 */     handleAirSupply(airSupply);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public boolean isPushedByFluid() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public boolean canBeLeashed() { return false; }
/*    */ 
/*    */   
/*    */   public static boolean checkSurfaceAgeableWaterCreatureSpawnRules(EntityType<? extends AgeableWaterCreature> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 70 */     int seaLevel = level.getSeaLevel();
/* 71 */     int minSpawnLevel = seaLevel - 13;
/* 72 */     return (pos.getY() >= minSpawnLevel && pos
/* 73 */       .getY() <= seaLevel && level
/* 74 */       .getFluidState(pos.below()).is(FluidTags.WATER) && level
/* 75 */       .getBlockState(pos.above()).is(Blocks.WATER));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\AgeableWaterCreature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */